package org.check1.entity.elasticsearchdatabase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetByTimeRequestBody {
    @NotEmpty(message = "Phone Number is compulsary")
    private String phoneNo;
    @NotEmpty(message = "Start time is compulsary")
    private String startTime;
    @NotEmpty(message = "End time is compulsary")
    private String endTime;

}
