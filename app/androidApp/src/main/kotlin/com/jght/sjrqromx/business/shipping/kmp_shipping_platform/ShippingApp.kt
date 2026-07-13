package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

import android.app.Application
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di.commonModule
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di.platformModule
import com.jght.sjrqromx.business.shipping.kmp_shipping_platform.core.di.sharedLogicModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShippingApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ShippingApp)
            modules(
                commonModule,
                platformModule,
                sharedLogicModule
            )
        }
    }
}
