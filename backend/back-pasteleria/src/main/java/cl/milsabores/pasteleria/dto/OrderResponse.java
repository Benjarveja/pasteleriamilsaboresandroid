package cl.milsabores.pasteleria.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderResponse {
    String id;
    String code;
    Integer subtotal;
    Integer total;
    String couponCode;
    Integer couponDiscount;
    Integer seniorDiscount;
    Integer totalSavings;
    String deliveryOption;
    String address;
    String region;
    String comuna;
    String branchId;
    String branchLabel;
    String pickupDate;
    String pickupTimeSlot;
    String pickupTimeSlotLabel;
    String paymentMethod;
    String paymentMethodLabel;
    String notes;
    String contactEmail;
    String contactPhone;
    String contactName;
    String status;
    String createdAt;
    String updatedAt;
    String statusUpdatedAt;
    String deliveredAt;
    List<OrderItemResponse> items;
    List<StatusHistoryEntry> statusHistory;
    DiscountSummary discounts;

    @Value
    @Builder
    public static class DiscountSummary {
        String couponCode;
        Integer couponDiscount;
        Integer seniorDiscount;
        Integer totalSavings;
    }

    @Value
    @Builder
    public static class StatusHistoryEntry {
        String status;
        String changedAt;
    }
}
