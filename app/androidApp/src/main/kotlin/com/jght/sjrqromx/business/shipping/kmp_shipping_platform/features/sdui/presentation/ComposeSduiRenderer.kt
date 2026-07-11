package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIComponent
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIScreen
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets.*
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeSduiRenderer(
    jsonString: String,
    onAction: (String, Map<String, String>) -> Unit,
    onReset: () -> Unit
) {
    val sduiScreen = remember(jsonString) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            // The JSON from the requirements is wrapped in a "screen" object or is the screen itself?
            // Usually it's {"screen": {...}}
            // Let's handle both or assume container based on our model
            val container = json.decodeFromString<com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIScreenContainer>(jsonString)
            container.screen
        } catch (e: Exception) {
            null
        }
    }

    if (sduiScreen == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text("Error parsing SDUI JSON")
        }
        return
    }

    val formValues = remember { mutableStateMapOf<String, String>() }
    val formErrors = remember { mutableStateMapOf<String, String?>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(sduiScreen.title) },
                navigationIcon = {
                    if (sduiScreen.id != "cotizador_envios") {
                        IconButton(onClick = onReset) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            sduiScreen.components.forEach { component ->
                SduiComponentResolver(
                    component = component,
                    formValues = formValues,
                    formErrors = formErrors,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun SduiComponentResolver(
    component: SDUIComponent,
    formValues: MutableMap<String, String>,
    formErrors: Map<String, String?>,
    onAction: (String, Map<String, String>) -> Unit
) {
    when (component.type) {
        "text" -> SduiTextComponent(component)
        "text_input" -> SduiTextInputComponent(component, formValues, formErrors)
        "select" -> SduiSelectComponent(component, formValues)
        "button" -> SduiButtonComponent(component) {
            val action = component.action
            if (action?.type == "submit") {
                val data = mutableMapOf<String, String>()
                action.fields?.forEach { fieldId ->
                    data[fieldId] = formValues[fieldId] ?: ""
                }
                onAction(action.event ?: "SUBMIT", data)
            }
        }
        "image" -> SduiImageComponent(component)
        "card" -> SduiCardComponent(component) { child ->
            SduiComponentResolver(child, formValues, formErrors, onAction)
        }
        "icon" -> SduiIconComponent(component)
    }
}
