package com.lukesleeman.currencyconverter.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukesleeman.currencyconverter.data.Currency
import com.lukesleeman.currencyconverter.ui.theme.CurrencyConverterTheme

/**
 * Composable for displaying a currency item with flag, code, and converted amount
 */
@Composable
fun CurrencyItem(
    currency: Currency,
    amount: TextFieldValue,
    modifier: Modifier = Modifier,

    // TODO - These probably shouldn't be nullable.
    onFocusRequest: (() -> Unit)? = null,
    onValueChange: ((TextFieldValue) -> Unit)? = null,
    onCurrencyChangeRequest: (() -> Unit)? = null,
    isActive: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable {
                onFocusRequest?.invoke()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.background
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = currency.flag,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )

                IconButton(
                    onClick = { onCurrencyChangeRequest?.invoke() },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change currency",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Text(
                text = currency.code,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .width(50.dp)
                    .padding(start = 8.dp, end = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f)) // This spacer will take up the flexible space to the left

            if (isActive) {
                TextField(
                    value = amount,
                    onValueChange = { newValue ->
                        onValueChange?.invoke(newValue)
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Medium
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        unfocusedContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.defaultMinSize(minWidth = 120.dp)
                )
            } else {
                Text(
                    text = amount.text,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier.defaultMinSize(minWidth = 120.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Default (Large Amount)")
@Preview(showBackground = true, name = "Wide Screen (Large Amount)", widthDp = 800, heightDp = 200)
@Preview(showBackground = true, name = "Large Font (Large Amount)", fontScale = 1.5f)
@Preview(showBackground = true, name = "Dark Mode (Large Amount)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CurrencyItemVariationsPreview() {
    var amount by remember { mutableStateOf(TextFieldValue("1234567890.12")) }
    val eur = Currency("EUR", "Euro", "€") // Using EUR as a sample currency
    CurrencyConverterTheme {
        // For the wide screen preview, CurrencyItem will naturally fill the width.
        // If specific narrower behavior on wide screens is needed for the item itself,
        // the parent composable calling CurrencyItem would constrain its width.
        CurrencyItem(
            currency = eur,
            amount = amount,
            onFocusRequest = { },
            onCurrencyChangeRequest = { }
        )
    }
}
