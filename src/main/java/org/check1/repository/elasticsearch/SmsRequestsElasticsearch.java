package org.check1.repository.elasticsearch;

import org.check1.entities.elasticsearchentities.SmsRequests;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SmsRequestsJPA extends ElasticsearchRepository<SmsRequests,Long> {
}
