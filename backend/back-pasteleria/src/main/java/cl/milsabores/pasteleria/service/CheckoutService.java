package cl.milsabores.pasteleria.service;

import cl.milsabores.pasteleria.dto.CartItemRequest;
import cl.milsabores.pasteleria.dto.CheckoutRequest;
import cl.milsabores.pasteleria.dto.OrderResponse;
import cl.milsabores.pasteleria.entity.Order;
import cl.milsabores.pasteleria.entity.OrderItem;
import cl.milsabores.pasteleria.entity.OrderStatus;
import cl.milsabores.pasteleria.entity.Product;
import cl.milsabores.pasteleria.entity.User;
import cl.milsabores.pasteleria.repository.OrderRepository;
import cl.milsabores.pasteleria.repository.ProductRepository;
import cl.milsabores.pasteleria.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DiscountService discountService;
    private final MapperService mapperService;
    private final ReferenceDataService referenceDataService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional
    public OrderResponse checkout(User user, CheckoutRequest request) {
        User resolvedUser = user != null ? user : resolveGuestUser(request);
        List<OrderItem> items = buildOrderItems(request.getItems());
        int subtotal = items.stream().mapToInt(item -> item.getPrecio() * item.getCantidad()).sum();
        DiscountService.DiscountResult discounts = discountService.computeDiscounts(subtotal, request.getCouponCode(), request.getBirthDate());

        Order order = new Order();
        order.setUser(resolvedUser);
        order.setCode(generateOrderCode());
        order.setSubtotal(subtotal);
        order.setTotal(discounts.total());
        order.setCouponCode(discounts.normalizedCoupon());
        order.setCouponDiscount(discounts.couponDiscount());
        order.setSeniorDiscount(discounts.seniorDiscount());
        order.setTotalSavings(discounts.totalSavings());
        order.setDeliveryOption(request.getDeliveryOption());
        order.setAddress(request.getAddress());
        order.setRegion(request.getRegion());
        order.setComuna(request.getComuna());
        order.setBranchId(request.getBranch());
        order.setBranchLabel(referenceDataService.findBranchLabel(request.getBranch()));
        order.setPickupDate(request.getPickupDate());
        order.setPickupTimeSlot(request.getPickupTimeSlot());
        order.setPickupTimeSlotLabel(referenceDataService.findPickupSlotLabel(request.getPickupTimeSlot()));
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentMethodLabel(referenceDataService.findPaymentLabel(request.getPaymentMethod()));
        order.setNotes(request.getNotes());
        order.setContactEmail(request.getEmail());
        order.setContactPhone(request.getPhone());
        order.setContactName(request.getFirstName() + " " + request.getLastName());
        order.setStatus(OrderStatus.CONFIRMADO);
        order.setStatusUpdatedAt(Instant.now());
        order.addStatusHistoryEntry(order.getStatus(), order.getStatusUpdatedAt());
        order.setCreatedAt(Instant.now());
        for (OrderItem item : items) {
            item.setOrder(order);
        }
        order.setItems(new LinkedHashSet<>(items));
        orderRepository.save(order);
        var logUser = resolvedUser != null ? resolvedUser.getEmail() : "guest";
        log.info("Nuevo pedido {} creado para usuario {} con {} ítems", order.getCode(), logUser, items.size());
        return mapperService.toOrder(order);
    }

    private List<OrderItem> buildOrderItems(List<CartItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }
        return items.stream().map(item -> {
            Product product = productRepository.findById(item.getCodigo())
                    .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + item.getCodigo()));
            OrderItem orderItem = new OrderItem();
            orderItem.setCodigo(product.getCodigo());
            orderItem.setNombre(product.getNombre());
            orderItem.setPrecio(product.getPrecio());
            orderItem.setCantidad(item.getCantidad());
            orderItem.setImagenUrl(product.getImagenUrl());
            return orderItem;
        }).toList();
    }

    private String generateOrderCode() {
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return "MS-" + random;
    }

    private User resolveGuestUser(CheckoutRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase();
        return userRepository.findByEmail(normalizedEmail)
                .orElseGet(() -> {
                    User guest = new User();
                    guest.setEmail(normalizedEmail);
                    guest.setFirstName(request.getFirstName().trim());
                    guest.setLastName(request.getLastName().trim());
                    guest.setPhone(request.getPhone());
                    guest.setRun(request.getRun());
                    guest.setBirthDate(request.getBirthDate());
                    guest.setStreet(request.getStreet());
                    guest.setRegion(request.getRegion());
                    guest.setComuna(request.getComuna());
                    guest.setAddress(request.getAddress());
                    guest.setPasswordHash(userService.generateGuestPassword());
                    return userRepository.save(guest);
                });
    }
}
