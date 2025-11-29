package cl.milsabores.pasteleria.controller;

import cl.milsabores.pasteleria.dto.CheckoutRequest;
import cl.milsabores.pasteleria.dto.OrderResponse;
import cl.milsabores.pasteleria.service.CheckoutService;
import cl.milsabores.pasteleria.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final UserService userService;

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody CheckoutRequest request) {
        var user = Optional.ofNullable(principal)
                .map(UserDetails::getUsername)
                .map(userService::findByEmailOrNull)
                .orElse(null);
        return ResponseEntity.ok(checkoutService.checkout(user, request));
    }
}
