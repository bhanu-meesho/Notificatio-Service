package org.check1.entities.elasticsearchentities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.check1.entities.sqldatabaseentities.SmsRequestData;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;
import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "elasticsearch")
public class SmsRequestsDataEntity {

    public SmsRequestsDataEntity(SmsRequestData smsRequestData)
    {
        this.id=smsRequestData.getId();
        this.phoneNumber=smsRequestData.getPhoneNumber();
        this.message=smsRequestData.getMessage();
        this.updatedAtInMilis=smsRequestData.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    @Id
    private Long id;

    private String phoneNumber;
    private String message;
    private Long updatedAtInMilis;
}
