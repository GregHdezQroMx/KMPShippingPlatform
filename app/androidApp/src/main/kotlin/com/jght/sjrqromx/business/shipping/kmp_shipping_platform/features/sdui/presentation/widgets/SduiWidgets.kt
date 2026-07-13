package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIComponent

@Composable
fun SduiTextComponent(component: SDUIComponent) {
    val style = when (component.style) {
        "headline" -> MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        "subtitle" -> MaterialTheme.typography.titleMedium.copy(color = Color.Gray)
        "price" -> MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold)
        "error" -> MaterialTheme.typography.bodySmall.copy(color = Color.Red)
        "caption" -> MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
        else -> MaterialTheme.typography.bodyLarge
    }
    Text(
        text = component.label,
        style = style,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
fun SduiTextInputComponent(
    component: SDUIComponent,
    formValues: MutableMap<String, String>,
    formErrors: Map<String, String?>
) {
    val text = formValues[component.id] ?: ""
    val error = formErrors[component.id]

    OutlinedTextField(
        value = text,
        onValueChange = {
            formValues[component.id] = it
        },
        label = { Text(component.label) },
        isError = error != null,
        supportingText = { if (error != null) Text(error!!) },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SduiSelectComponent(
    component: SDUIComponent,
    formValues: MutableMap<String, String>
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { 
        mutableStateOf(component.options?.find { it.value == formValues[component.id] } ?: component.options?.firstOrNull()) 
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
            component.options?.forEach { option ->
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

@Composable
fun SduiButtonComponent(component: SDUIComponent, onClick: () -> Unit) {
    val isPrimary = component.style == "primary" || component.id == "submit"
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = if (isPrimary) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = component.label, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SduiImageComponent(component: SDUIComponent) {
    if (component.imageUrl.isNullOrEmpty()) return
    AsyncImage(
        model = component.imageUrl,
        contentDescription = component.label,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(vertical = 8.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SduiCardComponent(component: SDUIComponent, childBuilder: @Composable (SDUIComponent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            component.children?.forEach { child ->
                childBuilder(child)
            }
        }
    }
}

@Composable
fun SduiIconComponent(component: SDUIComponent) {
    val icon = when (component.iconName) {
        "check_circle" -> Icons.Default.CheckCircle
        "local_shipping" -> Icons.Default.LocalShipping
        "error_outline" -> Icons.Default.ErrorOutline
        "cloud_off" -> Icons.Default.CloudOff
        "error" -> Icons.Default.Error
        "info" -> Icons.Default.Info
        else -> Icons.Default.Help
    }
    
    val color = when (component.style) {
        "success" -> Color(0xFF4CAF50)
        "error" -> Color.Red
        else -> Color.Gray
    }

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(64.dp).padding(vertical = 16.dp)
        )
    }
}
