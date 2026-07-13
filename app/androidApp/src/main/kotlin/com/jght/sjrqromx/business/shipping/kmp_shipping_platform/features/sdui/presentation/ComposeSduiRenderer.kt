package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIComponent
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.domain.model.SDUIScreenContainer
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.sdui.presentation.widgets.*
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeSduiRenderer(
    jsonString: String,
    externalErrors: Map<String, String?> = emptyMap(),
    onAction: (String, Map<String, String>) -> Unit,
    onReset: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val sduiScreen = remember(jsonString) {
        try {
            val json = Json { ignoreUnknownKeys = true }
            val container = json.decodeFromString<SDUIScreenContainer>(jsonString)
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

    LaunchedEffect(jsonString) {
        if (jsonString.contains("cotizador_envios")) {
            formValues.clear()
        }
    }

    val formErrors = externalErrors

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
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Settings"
                        )
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
