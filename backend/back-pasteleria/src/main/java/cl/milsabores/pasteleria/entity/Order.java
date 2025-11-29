package cl.milsabores.pasteleria.entity;

import cl.milsabores.pasteleria.dto.OrderResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, unique = true)
    private String code;

    private Integer subtotal;
    private Integer total;
    private String couponCode;
    private Integer couponDiscount;
    private Integer seniorDiscount;
    private Integer totalSavings;

    private String deliveryOption;
    private String address;
    private String region;
    private String comuna;
    private String branchId;
    private String branchLabel;
    private String pickupDate;
    private String pickupTimeSlot;
    private String pickupTimeSlotLabel;
    private String paymentMethod;
    private String paymentMethodLabel;
    private String notes;
    private String contactEmail;
    private String contactPhone;
    private String contactName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.CONFIRMADO;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    @Column(nullable = false)
    private Instant statusUpdatedAt = Instant.now();

    private Instant deliveredAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private Set<OrderItem> items = new LinkedHashSet<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 50)
    private Set<OrderStatusHistory> statusHistory = new LinkedHashSet<>();

    public String getCreatedAtIso() {
        return createdAt == null ? null : DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneOffset.UTC).format(createdAt);
    }

    public OrderResponse toResponse() {
        return OrderResponse.builder()
                .id(id.toString())
                .code(code)
                .subtotal(subtotal)
                .total(total)
                .couponCode(couponCode)
                .couponDiscount(couponDiscount)
                .seniorDiscount(seniorDiscount)
                .totalSavings(totalSavings)
                .deliveryOption(deliveryOption)
                .address(address)
                .region(region)
                .comuna(comuna)
                .branchId(branchId)
                .branchLabel(branchLabel)
                .pickupDate(pickupDate)
                .pickupTimeSlot(pickupTimeSlot)
                .pickupTimeSlotLabel(pickupTimeSlotLabel)
                .paymentMethod(paymentMethod)
                .paymentMethodLabel(paymentMethodLabel)
                .notes(notes)
                .contactEmail(contactEmail)
                .contactPhone(contactPhone)
                .contactName(contactName)
                .status(status.name())
                .statusHistory(statusHistory.stream()
                        .map(entry -> OrderResponse.StatusHistoryEntry.builder()
                                .status(entry.getStatus().name())
                                .changedAt(entry.getChangedAt().toString())
                                .build())
                        .collect(Collectors.toList()))
                .createdAt(getCreatedAtIso())
                .updatedAt(updatedAt == null ? null : DateTimeFormatter.ISO_INSTANT.format(updatedAt))
                .statusUpdatedAt(statusUpdatedAt == null ? null : DateTimeFormatter.ISO_INSTANT.format(statusUpdatedAt))
                .deliveredAt(deliveredAt == null ? null : DateTimeFormatter.ISO_INSTANT.format(deliveredAt))
                .items(items.stream().map(OrderItem::toResponse).collect(Collectors.toList()))
                .discounts(OrderResponse.DiscountSummary.builder()
                        .couponCode(couponCode)
                        .couponDiscount(couponDiscount)
                        .seniorDiscount(seniorDiscount)
                        .totalSavings(totalSavings)
                        .build())
                .build();
    }

    public void addStatusHistoryEntry(OrderStatus newStatus, Instant changeTime) {
        OrderStatusHistory history = new OrderStatusHistory();
        history.setOrder(this);
        history.setStatus(newStatus);
        history.setChangedAt(changeTime);
        statusHistory.add(history);
    }

    @PrePersist
    public void prePersist() {
        if (statusHistory.isEmpty()) {
            addStatusHistoryEntry(status, createdAt != null ? createdAt : Instant.now());
        }
    }

    @PreUpdate
    public void preUpdate() {
        if (statusHistory.stream().noneMatch(entry -> entry.getStatus() == this.status)) {
            addStatusHistoryEntry(status, statusUpdatedAt);
        }
    }
}
