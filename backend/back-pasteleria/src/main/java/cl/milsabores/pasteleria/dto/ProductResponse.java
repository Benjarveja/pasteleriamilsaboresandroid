package cl.milsabores.pasteleria.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductResponse {
    String codigo;
    String categoria;
    String nombre;
    Integer precio;
    String descripcion;
    Boolean popular;
    String historia;
    String imagenUrl;
}

