package com.earthlyz9.stepin.exceptions;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationExceptionReponse {
    private Integer code;
    private Map<String, String> messages;
    private LocalDateTime timestamp;
}
