package org.check1.services;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.check1.entities.sqldatabaseentities.SmsRequestData;
import org.check1.entities.elasticsearchentities.SmsRequestsDataEntity;
import org.check1.entities.sendsmsentities.ThirdPartyAPICall;
import org.check1.entities.sendsmsentities.ThirdPartyResponse;
import org.check1.repository.elasticsearch.SmsRequestsElasticsearch;
import org.check1.repository.jpa.SmsRequestDataJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
public class KafkaConsumerService {
    @Autowired
    private SmsRequestDataJPA smsRequestDataJPA;
    @Autowired
    private BlackListService blackListService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SmsRequestsElasticsearch smsRequestsElasticsearch;

    private final Logger logger = Logger.getLogger(KafkaConsumerService.class.getName());


    @KafkaListener(topics = "sendingSmsUsingThirdparty", groupId = "sendSmsGroup")
    public void Listen(ConsumerRecord<String, Long> record) throws Exception{
        Long requestId = record.value();
        Optional<SmsRequestData> smsRequestData = smsRequestDataJPA.findById(requestId);
        if(smsRequestData.isPresent())
        {
            if(blackListService.check(smsRequestData.get().getPhoneNumber()))
            {
                smsRequestData.get().setStatus("Done");
                smsRequestData.get().setFailureCode((long)403);
                smsRequestData.get().setFailureComment("BlackListed Number");
                smsRequestDataJPA.save(smsRequestData.get());
                logger.log(Level.INFO,"Hello Bhanu, Consumer Got Started, BlackListed requestId is : "+requestId);
            }
            else
            {
                List<String> phoneNumbers = new ArrayList<>();
                phoneNumbers.add(smsRequestData.get().getPhoneNumber());
                ThirdPartyAPICall thirdPartyAPICall = new ThirdPartyAPICall(smsRequestData.get().getMessage(),phoneNumbers);
                String url = "https://api.imiconnect.in/resources/v1/messaging";
                String key = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";
                try{
                    ThirdPartyResponse thirdPartyResponse = thirdPartyAPICall(url, thirdPartyAPICall,key);
                    if(thirdPartyResponse.getResponse().get(0).getCode().equals("1001"))
                    {
                        smsRequestData.get().setStatus("Successfully Sent");
                        smsRequestDataJPA.save(smsRequestData.get());
                        SmsRequestsDataEntity smsRequestsDataEntity = new SmsRequestsDataEntity(smsRequestData.get());
                        smsRequestsElasticsearch.save(smsRequestsDataEntity);
                        logger.log(Level.INFO, "Successfully Sent");
                    }
                    else
                    {
                        smsRequestData.get().setStatus("Done");
                        smsRequestData.get().setFailureCode((long)500);
                        smsRequestData.get().setFailureComment("Internal Server Error");
                        smsRequestDataJPA.save(smsRequestData.get());
                        logger.log(Level.INFO, "Internal server error "+thirdPartyResponse.getResponse().get(0).getCode());
                    }
                }
                catch (Exception e) {
                    smsRequestData.get().setStatus("Done");
                    smsRequestData.get().setFailureCode((long)500);
                    smsRequestData.get().setFailureComment("Internal Server Error");
                    smsRequestDataJPA.save(smsRequestData.get());
                    logger.log(Level.INFO,"Exception on third party api call ");
                }
            }
        }
        else
        {
            logger.log(Level.INFO,"Some internal server error occured on kafka consumer end in sending sms");
        }
    }

    private ThirdPartyResponse thirdPartyAPICall(String url, ThirdPartyAPICall thirdPartyAPICall, String key) throws Exception
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("key", key);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<ThirdPartyAPICall> httpEntity = new HttpEntity<>(thirdPartyAPICall,httpHeaders);
        return restTemplate.postForObject(url, httpEntity, ThirdPartyResponse.class);
    }
}
