package com.earthlyz9.stepin.exceptions;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

  private Integer code;
  private String message;
  private LocalDateTime timestamp;
}
