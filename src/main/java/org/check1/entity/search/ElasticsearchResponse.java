package org.check1.entity.elasticsearchdatabase;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "elasticsearch")
public class ElasticsearchResponse {

    public ElasticsearchResponse(SmsRequestsDataEntity smsRequestsDataEntity)
    {
        this.id= smsRequestsDataEntity.getId();
        this.phoneNumber= smsRequestsDataEntity.getPhoneNumber();
        this.message= smsRequestsDataEntity.getMessage();
        this.updatedAt=LocalDateTime.ofInstant(Instant.ofEpochMilli(smsRequestsDataEntity.getUpdatedAtInMilis()), ZoneId.of("UTC"));
    }

    private Long id;

    private String phoneNumber;
    private String message;
    private LocalDateTime updatedAt;
}
