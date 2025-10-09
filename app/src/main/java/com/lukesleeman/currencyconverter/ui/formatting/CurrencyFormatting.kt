package com.lukesleeman.currencyconverter.ui.formatting

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Shared formatting logic for currency values with locale support.
 * Handles both complete values and partial input (e.g., "123.").
 *
 * @param input Raw currency value as string
 * @param locale Locale for formatting (determines separators)
 * @param addDecimalPlaces Whether to force 2 decimal places (true for display, false for typing)
 * @return Formatted string with thousand separators and optional decimal places
 */
private fun formatCurrencyValue(input: String, locale: Locale, addDecimalPlaces: Boolean = true): String {
    val symbols = DecimalFormatSymbols.getInstance(locale)
    val groupingSeparator = symbols.groupingSeparator
    val decimalSeparator = symbols.decimalSeparator

    if (input.isEmpty()) {
        return if (addDecimalPlaces) "0${decimalSeparator}00" else ""
    }

    // Remove any existing grouping separators (handles both locale separator and common ones)
    val cleanValue = input.replace(groupingSeparator.toString(), "")
        .replace(",", "")  // Also remove commas in case input has them
        .replace(" ", "")   // Also remove spaces

    // Handle decimal separator alone (e.g., "." or ",")
    if (cleanValue == "." || cleanValue == decimalSeparator.toString()) {
        return if (addDecimalPlaces) "0${decimalSeparator}00" else "0${decimalSeparator}"
    }

    // Special case: preserve trailing decimal separator while typing (e.g., "123.")
    if (cleanValue.endsWith(".") && cleanValue.length > 1) {
        val integerFormatter = DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(locale))
        val integerPart = cleanValue.dropLast(1)
        val number = integerPart.toDoubleOrNull() ?: 0.0
        return integerFormatter.format(number) + decimalSeparator
    }

    // Format with or without forcing 2 decimal places
    val pattern = if (addDecimalPlaces) "#,##0.00" else "#,##0.##"
    val formatter = DecimalFormat(pattern, DecimalFormatSymbols.getInstance(locale))
    val number = cleanValue.toDoubleOrNull() ?: 0.0
    return formatter.format(number)
}

/**
 * Formats a raw currency value string for display with thousand separators and decimal places.
 *
 * @param value Raw currency value as string (e.g., "1234.5", "1234567")
 * @param locale Locale for formatting (defaults to system locale)
 * @return Formatted string with locale-appropriate separators and 2 decimal places
 */
fun formatCurrencyDisplay(value: String, locale: Locale = Locale.getDefault()): String {
    return formatCurrencyValue(value, locale, addDecimalPlaces = true)
}

/**
 * Visual transformation that formats currency input with thousand separators while typing.
 * The underlying value remains unformatted for parsing.
 *
 * @param locale Locale for formatting (defaults to system locale)
 */
class CurrencyVisualTransformation(
    private val locale: Locale = Locale.getDefault()
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Use shared formatting logic without forcing decimal places
        val formattedText = formatCurrencyValue(originalText, locale, addDecimalPlaces = false)

        // Create offset mapping to handle cursor position
        val offsetMapping = CurrencyOffsetMapping(originalText, formattedText, locale)

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }
}

/**
 * Offset mapping for currency visual transformation.
 * Maps positions between original (raw) text and transformed (formatted) text.
 * Handles locale-specific separators (commas, periods, spaces, etc.)
 */
private class CurrencyOffsetMapping(
    private val original: String,
    private val transformed: String,
    locale: Locale
) : OffsetMapping {

    private val groupingSeparator = DecimalFormatSymbols.getInstance(locale).groupingSeparator
    private val decimalSeparator = DecimalFormatSymbols.getInstance(locale).decimalSeparator

    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 0) return 0
        if (offset >= original.length) return transformed.length

        // Count `offset` characters from original in the transformed string
        var originalCount = 0
        var transformedIndex = 0

        // Process `offset` characters from original
        while (originalCount < offset && transformedIndex < transformed.length) {
            if (transformed[transformedIndex] != groupingSeparator) {
                originalCount++
            }
            transformedIndex++
        }

        // After processing offset characters, skip any following separators
        while (transformedIndex < transformed.length && transformed[transformedIndex] == groupingSeparator) {
            transformedIndex++
        }

        return transformedIndex
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 0) return 0
        if (offset >= transformed.length) return original.length

        // Count grouping separators before this offset in transformed text
        val separatorsBefore = transformed.substring(0, offset).count { it == groupingSeparator }

        // Original offset is transformed offset minus the separators
        return (offset - separatorsBefore).coerceIn(0, original.length)
    }
}
