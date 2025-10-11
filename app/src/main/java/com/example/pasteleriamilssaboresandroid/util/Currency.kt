package com.example.pasteleriamilssaboresandroid.util

import java.text.NumberFormat
import java.util.Locale

fun formatCLP(amount: Int): String {
    val locale = Locale.forLanguageTag("es-CL")
    val nf = NumberFormat.getNumberInstance(locale)
    nf.maximumFractionDigits = 0
    nf.minimumFractionDigits = 0
    return "CLP ${nf.format(amount)}"
}
