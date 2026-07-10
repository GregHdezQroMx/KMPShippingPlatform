package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return sayHello(platform.name)
    }
}