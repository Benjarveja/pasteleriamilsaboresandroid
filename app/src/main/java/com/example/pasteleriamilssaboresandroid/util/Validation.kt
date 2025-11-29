package com.example.pasteleriamilssaboresandroid.util

import java.text.Normalizer
import java.util.Calendar

fun normalizeString(value: String?): String = Normalizer.normalize(value ?: "", Normalizer.Form.NFD)
    .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")
    .trim()

fun normalizeRun(value: String?): String = (value ?: "")
    .replace(".", "")
    .replace("-", "")
    .uppercase()
    .trim()

fun formatRun(value: String?): String {
    val cleaned = normalizeRun(value)
    if (cleaned.length < 2) return (value ?: "").trim()
    val body = cleaned.dropLast(1)
    val dv = cleaned.takeLast(1)
    val reversed = body.reversed()
    val grouped = reversed.chunked(3).joinToString(".").reversed().trimStart('.')
    return "$grouped-$dv"
}

fun isValidRun(value: String?): Boolean {
    val cleaned = normalizeRun(value)
    if (cleaned.length < 2) return false
    val body = cleaned.dropLast(1)
    val dv = cleaned.takeLast(1)
    if (!body.all { it.isDigit() }) return false
    var sum = 0
    var multiplier = 2
    for (i in body.length - 1 downTo 0) {
        sum += (body[i].code - '0'.code) * multiplier
        multiplier = if (multiplier == 7) 2 else multiplier + 1
    }
    var expected = 11 - (sum % 11)
    val expectedChar = when (expected) {
        11 -> '0'
        10 -> 'K'
        else -> ('0'.code + expected).toChar()
    }
    return expectedChar.toString() == dv
}

private val EMAIL_REGEX = Regex("^[^ ]+@[^ ]+\\.[a-z]{2,3}$", RegexOption.IGNORE_CASE)
fun isValidEmail(value: String?): Boolean = EMAIL_REGEX.matches((value ?: "").trim())

fun normalizePhone(value: String?): String = (value ?: "").replace("[^0-9]".toRegex(), "")
fun isValidChileanPhone(value: String?): Boolean = Regex("^9\\d{8}$").matches(normalizePhone(value))

fun isValidBirthDate(value: String?, allowFuture: Boolean = false): Boolean {
    val v = value ?: return false
    return try {
        val parts = v.split("-" )
        if (parts.size != 3) return false
        val year = parts[0].toInt()
        val month = parts[1].toInt() - 1
        val day = parts[2].toInt()
        val cal = Calendar.getInstance().apply { set(Calendar.MILLISECOND, 0) }
        val today = cal.clone() as Calendar
        cal.set(year, month, day, 0, 0, 0)
        if (!allowFuture) today.set(Calendar.HOUR_OF_DAY, 0); today.set(Calendar.MINUTE, 0); today.set(Calendar.SECOND, 0); today.set(Calendar.MILLISECOND, 0)
        if (!allowFuture && cal.after(today)) return false
        true
    } catch (_: Exception) { false }
}

fun hasMinLength(value: String?, length: Int = 1): Boolean = (value ?: "").trim().length >= length
fun isNonEmpty(value: Any?): Boolean = when (value) {
    null -> false
    is String -> value.trim().isNotEmpty()
    is Collection<*> -> value.isNotEmpty()
    else -> true
}



