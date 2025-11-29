package cl.milsabores.pasteleria.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CartItemRequest {

    @NotBlank
    private String codigo;

    @Min(1)
    private int cantidad;
}

