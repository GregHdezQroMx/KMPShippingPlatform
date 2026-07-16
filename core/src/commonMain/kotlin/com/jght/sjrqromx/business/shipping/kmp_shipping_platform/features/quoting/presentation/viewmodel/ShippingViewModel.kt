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
 * Central orchestrator of the platform (Shared ViewModel).
 * Manages both settings persistence and quoting logic.
 */
class ShippingViewModel(
    private val calculateQuoteUseCase: CalculateQuoteUseCase,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    // Persistent settings (DataStore)
    val settings: StateFlow<AppSettings> = settingsRepository.settings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    // Native quote state
    private val _quoteResult = MutableStateFlow<QuoteResult?>(null)
    val quoteResult: StateFlow<QuoteResult?> = _quoteResult.asStateFlow()

    private val _showNativeResult = MutableStateFlow(false)
    val showNativeResult: StateFlow<Boolean> = _showNativeResult.asStateFlow()

    init {
        try {
            // Tu código actual de inicialización...
        } catch (e: Exception) {
            println("ERROR CRÍTICO EN KOTLIN: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Executes the quote calculation (KMP logic).
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
            
            // ONLY navigate if it is Success or Network Error (Rule 7)
            // If it is a Validation Error, we DO NOT activate _showNativeResult
            if (result is QuoteResult.Success || 
                (result is QuoteResult.Error && result.error.type != com.jght.sjrqromx.business.shipping.kmp_shipping_platform.features.quoting.domain.model.QuoteErrorType.VALIDATION_ERROR)) {
                _showNativeResult.value = true
            }
        }
    }

    /**
     * Resets the quote flow.
     */
    fun resetQuote() {
        _quoteResult.value = null
        _showNativeResult.value = false
    }

    // Configuration actions
    fun updateEngine(engine: UiEngine) {
        viewModelScope.launch { settingsRepository.updateEngine(engine) }
    }

    fun updateNetworkError(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.updateNetworkError(enabled) }
    }

    fun updateDataSource(useRemote: Boolean) {
        viewModelScope.launch { settingsRepository.updateDataSource(useRemote) }
    }

    /**
     * iOS Observation Helpers
     */
    fun watchSettings(onUpdate: (AppSettings) -> Unit) {
        viewModelScope.launch {
            settings.collect { onUpdate(it) }
        }
    }

    fun watchQuoteResult(onUpdate: (QuoteResult?) -> Unit) {
        viewModelScope.launch {
            quoteResult.collect { onUpdate(it) }
        }
    }

    fun watchShowNativeResult(onUpdate: (Boolean) -> Unit) {
        viewModelScope.launch {
            showNativeResult.collect { onUpdate(it) }
        }
    }
}
