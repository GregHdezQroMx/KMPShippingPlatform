package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIText

@Composable
fun SduiTextComponent(component: SDUIText) {
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
