package org.check1.entity.sendsmsentities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailSmsResponseException extends RuntimeException {
    private Long requestId;
    private String message;
    public FailSmsResponseException(Long requestId,String message)
    {
        super(message);
        this.requestId=requestId;
        this.message=message;
    }
    public FailSmsResponseException()
    {
        super();
    }
}
