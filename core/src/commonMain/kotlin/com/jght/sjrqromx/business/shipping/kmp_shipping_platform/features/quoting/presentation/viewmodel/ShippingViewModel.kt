package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.AppSettings
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.SettingsRepository
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.UiEngine
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteRequest
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteResult
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.ShippingType
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.usecase.CalculateQuoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Orquestador central de la plataforma (Shared ViewModel).
 * Maneja tanto la persistencia de configuración como la lógica de cotización.
 */
class ShippingViewModel(
    private val calculateQuoteUseCase: CalculateQuoteUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // Configuración persistente (DataStore)
    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    // Estado de la cotización nativa
    private val _quoteResult = MutableStateFlow<QuoteResult?>(null)
    val quoteResult: StateFlow<QuoteResult?> = _quoteResult.asStateFlow()

    private val _showNativeResult = MutableStateFlow(false)
    val showNativeResult: StateFlow<Boolean> = _showNativeResult.asStateFlow()

    /**
     * Ejecuta el cálculo de cotización (KMP logic).
     */
    fun calculateQuote(
        weight: Double,
        distance: Double,
        type: ShippingType,
        zipCode: String
    ) {
        viewModelScope.launch {
            val request = QuoteRequest(
                weightKg = weight,
                distanceKm = distance,
                shippingType = type,
                destinationZipCode = zipCode
            )
            val result = calculateQuoteUseCase(request)
            _quoteResult.value = result
            
            // SOLO navegamos si es Éxito o Error de Red (Regla 7)
            // Si es Error de Validación, NO activamos _showNativeResult
            if (result is QuoteResult.Success || 
                (result is QuoteResult.Error && result.error.type != com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteErrorType.VALIDATION_ERROR)) {
                _showNativeResult.value = true
            }
        }
    }

    /**
     * Resetea el flujo de cotización.
     */
    fun resetQuote() {
        _quoteResult.value = null
        _showNativeResult.value = false
    }

    // Acciones de configuración
    fun updateEngine(engine: UiEngine) {
        viewModelScope.launch { settingsRepository.updateEngine(engine) }
    }

    fun updateNetworkError(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateNetworkError(enabled) }
    }

    fun updateDataSource(useRemote: Boolean) {
        viewModelScope.launch { settingsRepository.updateDataSource(useRemote) }
    }
}
