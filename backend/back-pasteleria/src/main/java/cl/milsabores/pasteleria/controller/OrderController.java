package cl.milsabores.pasteleria.controller;

import cl.milsabores.pasteleria.dto.OrderResponse;
import cl.milsabores.pasteleria.service.OrderService;
import cl.milsabores.pasteleria.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> myOrders(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        var user = userService.getByEmail(principal.getUsername());
        return ResponseEntity.ok(orderService.findOrdersForUser(user.getId()));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> findById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.findById(orderId));
    }
}
