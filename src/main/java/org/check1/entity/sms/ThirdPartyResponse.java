package org.check1.entity.sendsmsentities;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class ThirdPartyResponse {
    private List<Response> response = new ArrayList<>();

    @Data
    public static class Response{
        private String code;
        private String transid;
        private String description;
    }
}
