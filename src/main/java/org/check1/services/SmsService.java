package org.check1.services.smsservice;

import org.apache.kafka.common.protocol.types.Field;
import org.check1.entities.databaseentities.SmsRequestData;
import org.check1.entities.sendsmsentities.FailSmsResponse;
import org.check1.entities.sendsmsentities.FailSmsResponseException;
import org.check1.entities.sendsmsentities.SmsRequest;
import org.check1.entities.sendsmsentities.SmsResponse;
import org.check1.jparepository.SmsRequestDataJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    @Autowired
    private SmsRequestDataJPA smsRequestDataJPA;
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    public SmsService(){}
    public SmsService(SmsRequestDataJPA smsRequestDataJPA, KafkaProducerService kafkaProducerService, RedisTemplate<String,String> redisTemplate)
    {
        this.smsRequestDataJPA=smsRequestDataJPA;
        this.kafkaProducerService=kafkaProducerService;
        this.redisTemplate=redisTemplate;
    }

    private Boolean checkPhoneNumberValidity(String phoneNumber)
    {
        if(phoneNumber.length()!=13)
        {
            return false;
        }
        if(phoneNumber.charAt(0)!='+')
        {
            return false;
        }
        if(phoneNumber.charAt(1)!='9' || phoneNumber.charAt(2)!='1')
            return false;
        if(phoneNumber.charAt(3)>='6' && phoneNumber.charAt(3)<='9')
        {
            for(int i=4;i<13;i++)
            {
                if(phoneNumber.charAt(i)>='0' && phoneNumber.charAt(i)<='9')
                    continue;
                else
                    return false;
            }
            return true;
        }
        else
            return false;
    }

    public SmsResponse send(SmsRequest smsRequest) throws FailSmsResponseException
    {
        SmsRequestData smsRequestData = new SmsRequestData(smsRequest.getPhoneNo(), smsRequest.getMessage(), "Recieved", null, null);
        smsRequestDataJPA.save(smsRequestData);

        if(checkPhoneNumberValidity(smsRequest.getPhoneNo()))
        {
            smsRequestData.setStatus("Failed");
            smsRequestData.setFailureCode((long)400);
            smsRequestData.setFailureComment("Invalid Number");
            throw new FailSmsResponseException(smsRequestData.getId(),"phone_number is Invalid");
        }
        else
        {

            kafkaProducerService.sendMessage(smsRequestData);
        }
    }

}
