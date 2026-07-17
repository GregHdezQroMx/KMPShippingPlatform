package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIIcon

@Composable
fun SduiIconComponent(component: SDUIIcon) {
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
