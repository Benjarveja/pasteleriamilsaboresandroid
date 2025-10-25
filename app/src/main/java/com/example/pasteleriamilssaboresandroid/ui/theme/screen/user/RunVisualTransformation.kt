package com.example.pasteleriamilssaboresandroid.ui.theme.screen.user

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.pasteleriamilssaboresandroid.util.formatRun
import com.example.pasteleriamilssaboresandroid.util.normalizeRun

class RunVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = formatRun(originalText)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (originalText.isEmpty()) return 0
                val normalizedOriginal = normalizeRun(originalText)
                if (offset >= normalizedOriginal.length) {
                    return formattedText.length
                }

                var consumedOriginal = 0
                var consumedFormatted = 0
                while (consumedOriginal < offset && consumedFormatted < formattedText.length) {
                    if (formattedText[consumedFormatted] == '.' || formattedText[consumedFormatted] == '-') {
                        consumedFormatted++
                    } else {
                        consumedFormatted++
                        consumedOriginal++
                    }
                }
                return consumedFormatted
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (formattedText.isEmpty()) return 0
                if (offset >= formattedText.length) {
                    return normalizeRun(formattedText).length
                }

                var consumedOriginal = 0
                var consumedFormatted = 0
                while (consumedFormatted < offset) {
                    if (formattedText[consumedFormatted] != '.' && formattedText[consumedFormatted] != '-') {
                        consumedOriginal++
                    }
                    consumedFormatted++
                }
                return consumedOriginal
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}

