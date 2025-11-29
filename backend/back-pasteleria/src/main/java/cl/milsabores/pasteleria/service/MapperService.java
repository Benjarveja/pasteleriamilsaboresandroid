package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.OrderItemResponse;
import cl.milsabores.pasteleria.dto.OrderResponse;
import cl.milsabores.pasteleria.dto.ProductResponse;
import cl.milsabores.pasteleria.dto.UserProfileResponse;
import cl.milsabores.pasteleria.entity.Order;
import cl.milsabores.pasteleria.entity.OrderItem;
import cl.milsabores.pasteleria.entity.Product;
import cl.milsabores.pasteleria.entity.User;
import cl.milsabores.pasteleria.dto.OrderResponse.StatusHistoryEntry;
import cl.milsabores.pasteleria.entity.OrderStatusHistory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
public class MapperService {

    private static final DateTimeFormatter ORDER_DATE_FORMATTER =
            DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("America/Santiago"));

    public UserProfileResponse toProfile(User user) {
        return UserProfileResponse.builder()
                .id(user.getId().toString())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .run(user.getRun())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .region(user.getRegion())
                .comuna(user.getComuna())
                .street(user.getStreet())
                .address(user.getAddress())
                .build();
    }

    public ProductResponse toProduct(Product product) {
        return ProductResponse.builder()
                .codigo(product.getCodigo())
                .categoria(product.getCategoria())
                .nombre(product.getNombre())
                .precio(product.getPrecio())
                .descripcion(product.getDescripcion())
                .popular(product.getPopular())
                .historia(product.getHistoria())
                .imagenUrl(product.getImagenUrl())
                .build();
    }

    public OrderResponse toOrder(Order order) {
        List<OrderItemResponse> items = order.getItems().stream().map(this::toOrderItem).toList();
        return OrderResponse.builder()
                .id(order.getId().toString())
                .code(order.getCode())
                .subtotal(order.getSubtotal())
                .total(order.getTotal())
                .couponCode(order.getCouponCode())
                .couponDiscount(order.getCouponDiscount())
                .seniorDiscount(order.getSeniorDiscount())
                .totalSavings(order.getTotalSavings())
                .deliveryOption(order.getDeliveryOption())
                .address(order.getAddress())
                .region(order.getRegion())
                .comuna(order.getComuna())
                .branchId(order.getBranchId())
                .branchLabel(order.getBranchLabel())
                .pickupDate(order.getPickupDate())
                .pickupTimeSlot(order.getPickupTimeSlot())
                .paymentMethod(order.getPaymentMethod())
                .paymentMethodLabel(order.getPaymentMethodLabel())
                .notes(order.getNotes())
                .contactEmail(order.getContactEmail())
                .contactPhone(order.getContactPhone())
                .contactName(order.getContactName())
                .status(order.getStatus().name())
                .createdAt(formatInstant(order.getCreatedAt()))
                .updatedAt(formatInstant(order.getUpdatedAt()))
                .statusUpdatedAt(formatInstant(order.getStatusUpdatedAt()))
                .deliveredAt(formatInstant(order.getDeliveredAt()))
                .items(items)
                .statusHistory(mapStatusHistory(order.getStatusHistory()))
                .discounts(OrderResponse.DiscountSummary.builder()
                        .couponCode(order.getCouponCode())
                        .couponDiscount(order.getCouponDiscount())
                        .seniorDiscount(order.getSeniorDiscount())
                        .totalSavings(order.getTotalSavings())
                        .build())
                .build();
    }

    public OrderItemResponse toOrderItem(OrderItem item) {
        return OrderItemResponse.builder()
                .codigo(item.getCodigo())
                .nombre(item.getNombre())
                .precio(item.getPrecio())
                .cantidad(item.getCantidad())
                .imagenUrl(item.getImagenUrl())
                .build();
    }

    private List<StatusHistoryEntry> mapStatusHistory(Collection<OrderStatusHistory> history) {
        return history == null ? List.of() : history.stream()
                .sorted(Comparator.comparing(OrderStatusHistory::getChangedAt))
                .map(entry -> StatusHistoryEntry.builder()
                        .status(entry.getStatus().name())
                        .changedAt(formatInstant(entry.getChangedAt()))
                        .build())
                .toList();
    }

    private String formatInstant(Instant instant) {
        return instant == null ? null : ORDER_DATE_FORMATTER.format(instant);
    }
}
