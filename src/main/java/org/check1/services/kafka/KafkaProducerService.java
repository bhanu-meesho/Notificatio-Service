package org.check1.services;

import org.check1.entities.sqldatabaseentities.SmsRequestData;
import org.check1.entities.sendsmsentities.SuccessSmsResponse;
import org.check1.repository.jpa.SmsRequestDataJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Long> kafkaTemplate;
    @Autowired
    private SmsRequestDataJPA smsRequestDataJPA;


    public SuccessSmsResponse sendMessage(String topic, Long id) throws Exception{
        Optional<SmsRequestData> smsRequestData = smsRequestDataJPA.findById(id);
        if(smsRequestData.isPresent())
        {
            smsRequestData.get().setStatus("Processing");
            smsRequestDataJPA.save(smsRequestData.get());
            kafkaTemplate.send(topic, id);
            return new SuccessSmsResponse(id,"Request Processed");
        }
        else
        {
            throw new RuntimeException("Internal Server Error");
        }

    }
}