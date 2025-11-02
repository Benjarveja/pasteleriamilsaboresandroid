package com.example.pasteleriamilssaboresandroid.util

const val VALID_COUPON_CODE = "50MILSABORES"
const val COUPON_DISCOUNT_PERCENTAGE = 0.25

data class PricingResult(
    val subtotal: Int,
    val couponDiscount: Int = 0,
    val total: Int
)

fun computeDiscounts(subtotal: Int, couponCode: String?): PricingResult {
    var total = subtotal
    var couponDiscount = 0

    if (couponCode == VALID_COUPON_CODE) {
        couponDiscount = (subtotal * COUPON_DISCOUNT_PERCENTAGE).toInt()
        total -= couponDiscount
    }

    return PricingResult(
        subtotal = subtotal,
        couponDiscount = couponDiscount,
        total = total
    )
}
