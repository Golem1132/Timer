package com.example.timer.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun DurationPickerField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    keyboardActions: KeyboardActions,
    imeAction: ImeAction

) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        isError = isError,
        maxLines = 1,
        keyboardActions = keyboardActions,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            errorTextColor = Color.Red,
            errorIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(
            textAlign = TextAlign.Center,
        fontSize = 48.sp,
        fontWeight = FontWeight.Bold)

    )
}