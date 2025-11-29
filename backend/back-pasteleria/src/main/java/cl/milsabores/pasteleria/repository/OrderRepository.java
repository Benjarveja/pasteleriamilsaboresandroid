package cl.milsabores.pasteleria.repository;

import cl.milsabores.pasteleria.entity.Order;
import cl.milsabores.pasteleria.entity.OrderStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @EntityGraph(attributePaths = {"items", "statusHistory"})
    List<Order> findByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Order> findByCode(String code);

    @EntityGraph(attributePaths = {"items", "statusHistory"})
    Optional<Order> findById(UUID id);

    List<Order> findByStatusInAndStatusUpdatedAtBefore(Collection<OrderStatus> statuses, Instant threshold);
}
