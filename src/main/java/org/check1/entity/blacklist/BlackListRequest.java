package org.check1.entity.blacklistentities;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlackListRequest {
    @NotEmpty(message = "List of numbers cannot be empty")
    private List<String> phoneNumbers = new ArrayList<>();
}
