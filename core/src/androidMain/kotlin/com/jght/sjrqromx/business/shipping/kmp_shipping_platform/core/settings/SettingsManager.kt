package com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings

import android.content.Context
import android.content.SharedPreferences
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.settings.UiEngine

class SettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("shipping_prefs", Context.MODE_PRIVATE)

    var engine: UiEngine
        get() = UiEngine.valueOf(prefs.getString("engine", UiEngine.COMPOSE.name) ?: UiEngine.COMPOSE.name)
        set(value) = prefs.edit().putString("engine", value.name).apply()

    var simulateNetworkError: Boolean
        get() = prefs.getBoolean("network_error", false)
        set(value) = prefs.edit().putBoolean("network_error", value).apply()

    var useRemoteServer: Boolean
        get() = prefs.getBoolean("use_remote", false)
        set(value) = prefs.edit().putBoolean("use_remote", value).apply()
}
