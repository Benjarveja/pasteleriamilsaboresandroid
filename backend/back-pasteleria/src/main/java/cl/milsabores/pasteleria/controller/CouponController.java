package cl.milsabores.pasteleria.controller;

import cl.milsabores.pasteleria.dto.CouponValidationRequest;
import cl.milsabores.pasteleria.service.DiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final DiscountService discountService;

    @PostMapping("/validate")
    public ResponseEntity<?> validateCoupon(@Valid @RequestBody CouponValidationRequest request) {
        return ResponseEntity.ok(discountService.describeCoupon(request.getCouponCode()));
    }
}

