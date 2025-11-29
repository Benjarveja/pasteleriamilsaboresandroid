package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.OrderResponse;
import cl.milsabores.pasteleria.entity.Order;
import cl.milsabores.pasteleria.entity.OrderStatus;
import cl.milsabores.pasteleria.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderTransitionService {

    private final OrderRepository orderRepository;

    @Value("${milsabores.orders.status-interval-seconds:20}")
    private long statusIntervalSeconds;

    private static final Map<OrderStatus, OrderStatus> PICKUP_TRANSITIONS = new EnumMap<>(OrderStatus.class);
    private static final Map<OrderStatus, OrderStatus> DELIVERY_TRANSITIONS = new EnumMap<>(OrderStatus.class);

    static {
        PICKUP_TRANSITIONS.put(OrderStatus.CONFIRMADO, OrderStatus.EN_PREPARACION);
        PICKUP_TRANSITIONS.put(OrderStatus.EN_PREPARACION, OrderStatus.PEDIDO_PREPARADO);
        PICKUP_TRANSITIONS.put(OrderStatus.PEDIDO_PREPARADO, OrderStatus.LISTO_PARA_RETIRAR);
        PICKUP_TRANSITIONS.put(OrderStatus.LISTO_PARA_RETIRAR, OrderStatus.ENTREGADO);

        DELIVERY_TRANSITIONS.put(OrderStatus.CONFIRMADO, OrderStatus.EN_PREPARACION);
        DELIVERY_TRANSITIONS.put(OrderStatus.EN_PREPARACION, OrderStatus.PEDIDO_PREPARADO);
        DELIVERY_TRANSITIONS.put(OrderStatus.PEDIDO_PREPARADO, OrderStatus.EN_ESPERA_DESPACHO);
        DELIVERY_TRANSITIONS.put(OrderStatus.EN_ESPERA_DESPACHO, OrderStatus.ENTREGADO);
    }

    @Scheduled(fixedDelayString = "${milsabores.orders.status-interval-ms:20000}")
    @Transactional
    public void advanceOrders() {
        Instant threshold = Instant.now().minus(Duration.ofSeconds(statusIntervalSeconds));
        List<Order> pendingOrders = orderRepository.findByStatusInAndStatusUpdatedAtBefore(
                List.of(
                        OrderStatus.CONFIRMADO,
                        OrderStatus.EN_PREPARACION,
                        OrderStatus.PEDIDO_PREPARADO,
                        OrderStatus.LISTO_PARA_RETIRAR,
                        OrderStatus.EN_ESPERA_DESPACHO
                ),
                threshold
        );

        if (pendingOrders.isEmpty()) {
            log.trace("No hay pedidos para transición automática");
            return;
        }

        for (Order order : pendingOrders) {
            OrderStatus nextStatus = determineNextStatus(order);
            if (nextStatus == null) {
                continue;
            }
            order.setStatus(nextStatus);
            order.setStatusUpdatedAt(Instant.now());
            if (nextStatus == OrderStatus.ENTREGADO) {
                order.setDeliveredAt(Instant.now());
            }
            order.addStatusHistoryEntry(nextStatus, order.getStatusUpdatedAt());
            log.info("Pedido {} avanzó a {}", order.getCode(), nextStatus);
        }
        log.debug("Se procesaron {} pedidos en la transición automática", pendingOrders.size());
    }

    private OrderStatus determineNextStatus(Order order) {
        boolean isDelivery = "delivery".equalsIgnoreCase(order.getDeliveryOption());
        Map<OrderStatus, OrderStatus> transitions = isDelivery ? DELIVERY_TRANSITIONS : PICKUP_TRANSITIONS;
        return transitions.get(order.getStatus());
    }
}
