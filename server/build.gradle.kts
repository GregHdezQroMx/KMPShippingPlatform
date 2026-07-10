plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
}

group = "com.jght.sjrqromx.business.shipping.kmp_shipping_platform"
version = "1.0.0"
application {
    mainClass = "com.jght.sjrqromx.business.shipping.kmp_shipping_platform.ApplicationKt"
}

dependencies {
    api(projects.core)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serializationKotlinxJson)
    implementation(libs.kotlinx.serialization.json)
    
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
}