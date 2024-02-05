package org.check1.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PaginationService {

    private final Logger logger = Logger.getLogger(KafkaConsumerService.class.getName());
    public <O> Page<O> getPagination(List<O> list, PageRequest pageRequest)
    {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), list.size());
        List<O> pageContent = new ArrayList<>();
        if(start>= list.size() || start<0 || end<=start)
        {
            return new PageImpl<>(pageContent, pageRequest, list.size());
        }
        else
        {
            pageContent = list.subList(start, end);
            return new PageImpl<>(pageContent, pageRequest, list.size());
        }

    }
}
