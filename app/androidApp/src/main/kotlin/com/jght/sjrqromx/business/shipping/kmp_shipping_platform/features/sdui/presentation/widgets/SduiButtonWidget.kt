package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIButton

@Composable
fun SduiButtonComponent(component: SDUIButton, onClick: () -> Unit) {
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
