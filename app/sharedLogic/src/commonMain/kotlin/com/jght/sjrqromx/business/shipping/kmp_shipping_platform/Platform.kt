package com.jght.sjrqromx.business.shipping.kmp_shipping_platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform