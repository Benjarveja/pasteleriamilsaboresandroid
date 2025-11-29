package cl.milsabores.pasteleria.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    private String codigo;

    private String categoria;
    private String nombre;
    private Integer precio;

    @Column(length = 2000)
    private String descripcion;

    private Boolean popular;
    private String historia;
    private String imagenUrl;
}

