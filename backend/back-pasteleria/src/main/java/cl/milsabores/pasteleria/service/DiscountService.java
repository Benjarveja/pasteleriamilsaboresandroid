package cl.milsabores.pasteleria.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Service
public class DiscountService {

    private static final String VALID_COUPON = "50MILSABORES";
    private static final double COUPON_RATE = 0.25;
    private static final double SENIOR_RATE = 0.5;
    private static final int SENIOR_AGE_THRESHOLD = 50;

    public DiscountResult computeDiscounts(int subtotal, String couponCode, String birthDate) {
        String normalizedCoupon = normalizeCoupon(couponCode);
        boolean couponValid = VALID_COUPON.equals(normalizedCoupon);
        int couponDiscount = couponValid ? (int) Math.round(subtotal * COUPON_RATE) : 0;

        Integer age = calculateAge(birthDate);
        boolean seniorEligible = age != null && age >= SENIOR_AGE_THRESHOLD;
        int seniorBase = Math.max(subtotal - couponDiscount, 0);
        int seniorDiscount = seniorEligible ? (int) Math.round(seniorBase * SENIOR_RATE) : 0;

        int total = Math.max(subtotal - couponDiscount - seniorDiscount, 0);
        int totalSavings = couponDiscount + seniorDiscount;

        return new DiscountResult(subtotal, normalizedCoupon, couponValid, couponDiscount,
                age, seniorEligible, seniorDiscount, total, totalSavings);
    }

    public boolean isCouponValid(String couponCode) {
        return VALID_COUPON.equals(normalizeCoupon(couponCode));
    }

    public Map<String, Object> describeCoupon(String couponCode) {
        boolean valid = isCouponValid(couponCode);
        return Map.of(
                "couponCode", normalizeCoupon(couponCode),
                "valid", valid,
                "message", valid ? "Cupón válido" : "Cupón inválido"
        );
    }

    private Integer calculateAge(String birthDate) {
        if (birthDate == null || birthDate.isBlank()) {
            return null;
        }
        try {
            LocalDate birth = LocalDate.parse(birthDate);
            return Period.between(birth, LocalDate.now()).getYears();
        } catch (Exception ignored) {
            return null;
        }
    }

    private String normalizeCoupon(String couponCode) {
        return couponCode == null ? "" : couponCode.trim().toUpperCase();
    }

    public record DiscountResult(int subtotal,
                                 String normalizedCoupon,
                                 boolean couponValid,
                                 int couponDiscount,
                                 Integer age,
                                 boolean seniorEligible,
                                 int seniorDiscount,
                                 int total,
                                 int totalSavings) {
    }
}

