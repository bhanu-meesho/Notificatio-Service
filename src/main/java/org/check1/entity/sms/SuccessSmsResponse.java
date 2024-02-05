package org.check1.entity.sendsmsentities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuccessSmsResponse {
    private Long requestId;
    private String comments;
}
