package cl.milsabores.pasteleria.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CouponValidationRequest {
    @NotBlank
    private String couponCode;
}

