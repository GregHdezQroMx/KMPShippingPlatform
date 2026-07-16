package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.bridge

interface SduiBridge {
    fun renderUI(json: String)
    fun resetForm()
    fun showValidationError(code: String, message: String)
    fun setOnEventListener(callback: (event: String, data: Map<String, Any?>) -> Unit)
    fun finishFlow()
}
