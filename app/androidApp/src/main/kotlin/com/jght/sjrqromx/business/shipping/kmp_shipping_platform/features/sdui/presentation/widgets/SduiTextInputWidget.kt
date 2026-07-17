package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUITextInput

@Composable
fun SduiTextInputComponent(
    component: SDUITextInput,
    formValues: MutableMap<String, String>,
    formErrors: Map<String, String?>
) {
    var localText by remember { mutableStateOf(formValues[component.id] ?: "") }
    val error = formErrors[component.id]

    LaunchedEffect(formValues[component.id]) {
        val externalValue = formValues[component.id] ?: ""
        if (localText != externalValue) {
            localText = externalValue
        }
    }

    OutlinedTextField(
        value = localText,
        onValueChange = { newValue ->
            val filtered = when (component.inputType) {
                "decimal" -> {
                    var hasDecimal = false
                    newValue.filter { char ->
                        if (char.isDigit()) true
                        else if ((char == '.' || char == ',') && !hasDecimal) {
                            hasDecimal = true
                            true
                        } else false
                    }
                }
                "number" -> {
                    val digits = newValue.filter { it.isDigit() }
                    if (component.id == "codigoPostal") digits.take(5) else digits
                }
                else -> newValue
            }
            localText = filtered
            formValues[component.id] = filtered
        },
        label = { Text(component.label) },
        isError = error != null,
        supportingText = { if (error != null) Text(error!!) },
        keyboardOptions = KeyboardOptions(
            keyboardType = when (component.inputType) {
                "decimal" -> KeyboardType.Decimal
                "number" -> KeyboardType.Number
                else -> KeyboardType.Text
            }
        ),
        modifier = Modifier.fillMaxWidth()
    )
}
