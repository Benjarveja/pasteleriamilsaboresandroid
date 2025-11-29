package cl.milsabores.pasteleria.entity;

import cl.milsabores.pasteleria.dto.OrderItemResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String codigo;
    private String nombre;
    private Integer precio;
    private Integer cantidad;
    private String imagenUrl;

    public OrderItemResponse toResponse() {
        return OrderItemResponse.builder()
                .codigo(codigo)
                .nombre(nombre)
                .precio(precio)
                .cantidad(cantidad)
                .imagenUrl(imagenUrl)
                .build();
    }
}
