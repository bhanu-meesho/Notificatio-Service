package org.check1.entity.sendsmsentities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequestBody {
    @NotNull(message = "phoneNo can not be a null value")
    private String phoneNo;
    private String message;
}
