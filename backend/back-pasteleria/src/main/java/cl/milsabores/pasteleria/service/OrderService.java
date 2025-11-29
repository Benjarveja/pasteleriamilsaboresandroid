package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.OrderResponse;
import cl.milsabores.pasteleria.entity.Order;
import cl.milsabores.pasteleria.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final MapperService mapper;

    public List<OrderResponse> findOrdersForUser(UUID userId) {
        List<OrderResponse> responses = orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toOrder)
                .toList();
        log.debug("Usuario {} recuperó {} pedidos", userId, responses.size());
        return responses;
    }

    public OrderResponse findById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        log.debug("Pedido {} consultado", order.getCode());
        return mapper.toOrder(order);
    }
}
