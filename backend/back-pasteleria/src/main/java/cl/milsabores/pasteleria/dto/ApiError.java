package cl.milsabores.pasteleria.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiError {
    String message;
    String detail;
}

