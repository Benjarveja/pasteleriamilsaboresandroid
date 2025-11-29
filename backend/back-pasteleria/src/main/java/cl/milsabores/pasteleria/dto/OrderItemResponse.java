package cl.milsabores.pasteleria.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderItemResponse {
    String codigo;
    String nombre;
    Integer precio;
    Integer cantidad;
    String imagenUrl;
}

