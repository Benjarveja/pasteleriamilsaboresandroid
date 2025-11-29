package cl.milsabores.pasteleria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutRequest {

    @NotEmpty
    private List<@Valid CartItemRequest> items;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phone;

    private String run;
    private String birthDate;

    @NotBlank
    private String deliveryOption;

    private String street;
    private String region;
    private String comuna;
    private String address;

    private String branch;
    private String pickupDate;
    private String pickupTimeSlot;

    @NotBlank
    private String paymentMethod;

    private String notes;
    private String couponCode;
}

