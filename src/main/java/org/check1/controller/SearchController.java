package org.check1.controller;

import org.check1.entity.exceptions.BadRequestException;
import org.check1.entity.search.ElasticsearchResponse;
import org.check1.entity.search.GetByTimeRequestBody;
import org.check1.services.ElasticsearchService;
import org.check1.services.kafka.KafkaConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;

@RestController
@RequestMapping("/v1")
public class ElasticsearchController {
    @Autowired
    private ElasticsearchService elasticsearchService;

    private final Logger logger = Logger.getLogger(KafkaConsumerService.class.getName());

    @PostMapping("/getbytime")
    public ResponseEntity<Page<ElasticsearchResponse>> getByTime(@Valid @RequestBody GetByTimeRequestBody getByTimeRequestBody, @RequestParam(name = "page", defaultValue = "0") String pageString, @RequestParam(name = "size", defaultValue = "10") String sizeString) throws Exception
    {
        int page;
        int size;
        try{
            page=Integer.parseInt(pageString);
            size=Integer.parseInt(sizeString);
        } catch (Exception e) {
            throw new BadRequestException("Enter Valid Parameters");
        }

        if(size<=0 || page<0)
        {
            throw new BadRequestException("Page size should be greater than 0");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(elasticsearchService.byTime(getByTimeRequestBody,pageRequest), HttpStatus.OK);
    }

    @GetMapping("/getbytext/{text}")
    public ResponseEntity<Page<ElasticsearchResponse>> getByText(@PathVariable String text, @RequestParam(name = "page", defaultValue = "0") String pageString, @RequestParam(name = "size", defaultValue = "10") String sizeString) throws Exception
    {
        int page;
        int size;

        try{
            page=Integer.parseInt(pageString);
            size=Integer.parseInt(sizeString);
        } catch (Exception e) {
            throw new BadRequestException("Enter Valid Parameters");
        }

        if(size<=0 || page<0)
        {
            throw new BadRequestException("Page size should be greater than 0");
        }
        PageRequest pageRequest = PageRequest.of(page, size);
        return new ResponseEntity<>(elasticsearchService.byText(text,pageRequest), HttpStatus.OK);
    }
}
