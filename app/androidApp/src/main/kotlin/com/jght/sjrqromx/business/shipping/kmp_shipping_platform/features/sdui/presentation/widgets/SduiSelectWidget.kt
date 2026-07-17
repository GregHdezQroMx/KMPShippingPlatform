package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUISelect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SduiSelectComponent(
    component: SDUISelect,
    formValues: MutableMap<String, String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { 
        mutableStateOf(component.options.find { it.value == formValues[component.id] } ?: component.options.firstOrNull()) 
    }

    LaunchedEffect(selectedOption) {
        if (selectedOption != null) {
            formValues[component.id] = selectedOption!!.value
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedOption?.label ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(component.label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            component.options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.label) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                    }
                )
            }
        }
    }
}
