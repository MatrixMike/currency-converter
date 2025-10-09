package com.lukesleeman.currencyconverter.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lukesleeman.currencyconverter.data.Currency
import com.lukesleeman.currencyconverter.ui.FoldablePreview
import com.lukesleeman.currencyconverter.ui.formatting.CurrencyVisualTransformation
import com.lukesleeman.currencyconverter.ui.formatting.filterNumericInput
import com.lukesleeman.currencyconverter.ui.formatting.formatCurrencyDisplay
import com.lukesleeman.currencyconverter.ui.theme.CurrencyConverterTheme

/**
 * Composable for displaying a currency item with flag, code, and converted amount
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyItem(
    currency: Currency,
    amount: TextFieldValue,
    modifier: Modifier = Modifier,
    onFocusRequest: () -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    onCurrencyChangeRequest: () -> Unit,
    onRemove: () -> Unit,
    canRemove: Boolean,
    isActive: Boolean = false
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { dismissValue ->
            if (dismissValue == SwipeToDismissBoxValue.StartToEnd) {
                onRemove()
                true
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = canRemove,
        enableDismissFromEndToStart = false,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        }
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    onFocusRequest()
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onCurrencyChangeRequest()
                    }
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            ) {
                Text(
                    text = currency.flag,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(end = 4.dp)
                )

                Text(
                    text = currency.code,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Change currency",
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // This spacer will take up the flexible space to the left

            if (isActive) {
                TextField(
                    value = amount,
                    onValueChange = { newValue: TextFieldValue ->
                        onValueChange(filterNumericInput(newValue))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    visualTransformation = CurrencyVisualTransformation(),
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
                    text = formatCurrencyDisplay(amount.text),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Medium
                    ),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 120.dp)
                        .padding(vertical = 16.dp)
                )
            }
        }
    }
    }
}

@Preview(showBackground = true, name = "Default (Large Amount)")
@Preview(showBackground = true, name = "Wide Screen (Large Amount)", widthDp = 800, heightDp = 200)
@Preview(showBackground = true, name = "Large Font (Large Amount)", fontScale = 1.5f)
@Preview(showBackground = true, name = "Dark Mode (Large Amount)", uiMode = Configuration.UI_MODE_NIGHT_YES)
@FoldablePreview
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
            onValueChange = { },
            onCurrencyChangeRequest = { },
            onRemove = { },
            canRemove = true
        )
    }
}
