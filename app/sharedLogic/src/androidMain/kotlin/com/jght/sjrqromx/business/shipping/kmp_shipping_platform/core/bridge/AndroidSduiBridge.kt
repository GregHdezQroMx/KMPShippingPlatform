package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.bridge

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel

class AndroidSduiBridge(messenger: BinaryMessenger) : SduiBridge {
    private val channel = MethodChannel(messenger, "com.jght.shipping/ui_engine")
    private var onEvent: ((String, Map<String, Any?>) -> Unit)? = null
    private var onFinish: (() -> Unit)? = null

    init {
        channel.setMethodCallHandler { call, result ->
            if (call.method == "onUIEvent") {
                val args = call.arguments as? Map<String, Any?>
                val event = args?.get("event") as? String
                val data = (args?.get("data") as? Map<String, Any?>) ?: emptyMap()
                
                if (event != null) {
                    if (event == "CLOSE") {
                        onFinish?.invoke()
                    } else {
                        onEvent?.invoke(event, data)
                    }
                }
                result.success(null)
            } else {
                result.notImplemented()
            }
        }
    }

    override fun renderUI(json: String) {
        channel.invokeMethod("renderUI", json)
    }

    override fun resetForm() {
        channel.invokeMethod("resetForm", null)
    }

    override fun showValidationError(code: String, message: String) {
        channel.invokeMethod("showValidationError", mapOf("code" to code, "message" to message))
    }

    override fun setOnEventListener(callback: (event: String, data: Map<String, Any?>) -> Unit) {
        this.onEvent = callback
    }

    override fun finishFlow() {
        onFinish?.invoke()
    }

    fun setOnFinishListener(callback: () -> Unit) {
        this.onFinish = callback
    }
}
