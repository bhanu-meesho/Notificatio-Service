package org.check1.services;

import lombok.extern.slf4j.Slf4j;
import org.check1.entity.exceptions.BadRequestException;
import org.check1.entity.search.ElasticsearchResponse;
import org.check1.entity.search.GetByTimeRequestBody;
import org.check1.entity.elasticsearchdatabase.SmsRequestsDataEntity;
import org.check1.repository.elasticsearch.SmsRequestsElasticsearch;
import org.check1.services.helper.PaginationService;
import org.check1.services.kafka.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@Slf4j
public class ElasticsearchService {

    @Autowired
    private SmsRequestsElasticsearch smsRequestsElasticsearch;

    @Autowired
    private PaginationService paginationService;

    private final Logger logger = Logger.getLogger(KafkaConsumerService.class.getName());

    public Page<ElasticsearchResponse> byTime(GetByTimeRequestBody getByTimeRequestBody, PageRequest pageRequest) throws Exception
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try{
            LocalDateTime startTime = LocalDateTime.parse(getByTimeRequestBody.getStartTime(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(getByTimeRequestBody.getEndTime(), formatter);
            List<SmsRequestsDataEntity> queryResult = smsRequestsElasticsearch.findByPhoneNoAndTimeBtw(getByTimeRequestBody.getPhoneNo(),startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),endTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
            List<ElasticsearchResponse> elasticsearchResponses = new ArrayList<>();
            for(int i=0;i<queryResult.size();i++)
            {
                elasticsearchResponses.add(new ElasticsearchResponse(queryResult.get(i)));
            }
            return paginationService.getPagination(elasticsearchResponses,pageRequest);

        } catch(Exception e){
            throw new BadRequestException("Time Format Mismatched ('yyyy-MM-dd HH:mm:ss')");
        }
    }

    public Page<ElasticsearchResponse> byText(String text, PageRequest pageRequest) throws Exception
    {
        List<SmsRequestsDataEntity> queryResult =  smsRequestsElasticsearch.findByMessageContaining(text);
        List<ElasticsearchResponse> elasticsearchResponses = new ArrayList<>();
        for(int i=0;i<queryResult.size();i++)
        {
            elasticsearchResponses.add(new ElasticsearchResponse(queryResult.get(i)));
        }
        return paginationService.getPagination(elasticsearchResponses,pageRequest);
    }
}
