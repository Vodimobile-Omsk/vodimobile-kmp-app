package com.vodimobile.presentation.components.pickers

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vodimobile.android.R
import com.vodimobile.presentation.theme.ExtendedTheme
import com.vodimobile.presentation.theme.VodimobileTheme

@Composable
fun DateRentField(
    date: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    showTrailingIcon: Boolean = false,
    onFieldClick: () -> Unit
) {
    ExtendedTheme {
        OutlinedTextField(
            value = date,
            onValueChange = { },
            placeholder = {
                Text(
                    text = placeholder,
                    color = ExtendedTheme.colorScheme.hintText,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            readOnly = true,
            enabled = false,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = if (showTrailingIcon) ExtendedTheme.colorScheme.containerBack
                else ExtendedTheme.colorScheme.onSecondaryBackground,
                focusedContainerColor = if (showTrailingIcon) ExtendedTheme.colorScheme.containerBack
                else ExtendedTheme.colorScheme.onSecondaryBackground,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                errorCursorColor = Color.Transparent,
                focusedTextColor = if (showTrailingIcon) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = if (showTrailingIcon) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground
            ),
            modifier = modifier
                .fillMaxWidth()
                .then(
                    if (!showTrailingIcon) {
                        Modifier.border(
                            BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.tertiary
                            ),
                            shape = MaterialTheme.shapes.small
                        )
                    } else {
                        Modifier
                    }
                ),
            shape = MaterialTheme.shapes.small,
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = { onFieldClick.invoke() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar),
                        contentDescription = stringResource(id = R.string.icon_calendar),
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            trailingIcon = {
                if (showTrailingIcon) {
                    Icon(
                        painter = painterResource(id = R.drawable.edit_grey),
                        contentDescription = stringResource(id = R.string.edit_data),
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateRentFieldWithoutValueLightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "",
            placeholder = "Когда?",
            onFieldClick = {},
            showTrailingIcon = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateRentFieldWithValueLightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "5-17 августа 2024",
            placeholder = "Когда?",
            onFieldClick = {},
            showTrailingIcon = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateRentFieldWithoutValueShowTrailingLightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "",
            placeholder = "Выберите дату",
            onFieldClick = {},
            showTrailingIcon = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun DateRentFieldWithValueShowTrailingLightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "5-17 августа 2024",
            placeholder = "Выберите дату",
            onFieldClick = {},
            showTrailingIcon = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRentFieldWithoutValueNightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "",
            placeholder = "Когда?",
            onFieldClick = {},
            showTrailingIcon = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRentFieldWithValueNightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "5-17 августа 2024",
            placeholder = "Когда?",
            onFieldClick = {},
            showTrailingIcon = false
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRentFieldWithoutValueShowTrailingNightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "",
            placeholder = "Выберите дату",
            onFieldClick = {},
            showTrailingIcon = true
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DateRentFieldWithValueShowTrailingNightPreview() {
    VodimobileTheme(dynamicColor = false) {
        DateRentField(
            date = "5-17 августа 2024",
            placeholder = "Выберите дату",
            onFieldClick = {},
            showTrailingIcon = true
        )
    }
}
