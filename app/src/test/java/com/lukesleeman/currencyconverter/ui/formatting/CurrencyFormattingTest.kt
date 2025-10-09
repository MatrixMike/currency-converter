package com.lukesleeman.currencyconverter.ui.formatting

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import org.junit.Test
import java.util.Locale
import kotlin.test.assertEquals

/**
 * Tests for currency formatting utilities
 */
class CurrencyFormattingTest {

    @Test
    fun `formatCurrencyDisplay should format whole numbers with commas`() {
        assertEquals("1,234,567,890.00", formatCurrencyDisplay("1234567890", Locale.US))
    }

    @Test
    fun `formatCurrencyDisplay should format numbers with decimals`() {
        assertEquals("1,234.50", formatCurrencyDisplay("1234.5", Locale.US))
        assertEquals("1,234.56", formatCurrencyDisplay("1234.56", Locale.US))
    }

    @Test
    fun `formatCurrencyDisplay should handle small numbers`() {
        assertEquals("0.50", formatCurrencyDisplay("0.5", Locale.US))
        assertEquals("0.12", formatCurrencyDisplay("0.12", Locale.US))
        assertEquals("5.00", formatCurrencyDisplay("5", Locale.US))
    }

    @Test
    fun `formatCurrencyDisplay should handle numbers with two decimal places`() {
        assertEquals("100.00", formatCurrencyDisplay("100", Locale.US))
        assertEquals("100.50", formatCurrencyDisplay("100.5", Locale.US))
        assertEquals("100.99", formatCurrencyDisplay("100.99", Locale.US))
    }

    @Test
    fun `formatCurrencyDisplay should handle empty or invalid input`() {
        assertEquals("0.00", formatCurrencyDisplay("", Locale.US))
        assertEquals("0.00", formatCurrencyDisplay(".", Locale.US))
    }

    @Test
    fun `formatCurrencyDisplay should handle numbers without decimal point`() {
        assertEquals("1,000.00", formatCurrencyDisplay("1000", Locale.US))
        assertEquals("999.00", formatCurrencyDisplay("999", Locale.US))
    }

    @Test
    fun `formatCurrencyDisplay should preserve existing commas in input`() {
        // Input might already have commas - should handle gracefully
        assertEquals("1,234.00", formatCurrencyDisplay("1,234", Locale.US))
        assertEquals("1,234.50", formatCurrencyDisplay("1,234.5", Locale.US))
    }

    @Test
    fun `CurrencyVisualTransformation should transform text with commas`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("1234567")
        val result = transformation.filter(input)

        assertEquals("1,234,567", result.text.text)
    }

    @Test
    fun `CurrencyVisualTransformation should handle text with decimal`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("1234.56")
        val result = transformation.filter(input)

        assertEquals("1,234.56", result.text.text)
    }

    @Test
    fun `CurrencyVisualTransformation offset mapping should map original to transformed indices`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("1234567") // Will become "1,234,567"
        val result = transformation.filter(input)

        // Original index 0 (before '1') -> Transformed index 0
        assertEquals(0, result.offsetMapping.originalToTransformed(0))

        // Original index 1 (before '2') -> Transformed index 2 (after comma)
        assertEquals(2, result.offsetMapping.originalToTransformed(1))

        // Original index 4 (before '5') -> Transformed index 6 (after second comma)
        assertEquals(6, result.offsetMapping.originalToTransformed(4))
    }

    @Test
    fun `CurrencyVisualTransformation offset mapping should map transformed to original indices`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("1234567") // Will become "1,234,567"
        val result = transformation.filter(input)

        // Transformed index 0 (at '1') -> Original index 0
        assertEquals(0, result.offsetMapping.transformedToOriginal(0))

        // Transformed index 1 (at comma) -> Original index 1
        assertEquals(1, result.offsetMapping.transformedToOriginal(1))

        // Transformed index 2 (at '2') -> Original index 1
        assertEquals(1, result.offsetMapping.transformedToOriginal(2))
    }

    @Test
    fun `CurrencyVisualTransformation should handle empty input`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("")
        val result = transformation.filter(input)

        assertEquals("", result.text.text)
    }

    @Test
    fun `CurrencyVisualTransformation should preserve decimal point while typing`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("12.")
        val result = transformation.filter(input)

        assertEquals("12.", result.text.text)
    }

    @Test
    fun `CurrencyVisualTransformation should format partial decimal input`() {
        val transformation = CurrencyVisualTransformation(Locale.US)
        val input = AnnotatedString("12.5")
        val result = transformation.filter(input)

        assertEquals("12.5", result.text.text)
    }

    // Note: Locale support architecture is in place (functions accept locale parameter)
    // Additional locale testing would require more comprehensive DecimalFormat handling
}
