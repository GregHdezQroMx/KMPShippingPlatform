import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

android {
    namespace = "com.jght.sjrqromx.business.shipping.kmp_shipping_platform"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.jght.sjrqromx.business.shipping.kmp_shipping_platform"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        create("profile") {
            initWith(getByName("debug"))
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(projects.app.sharedLogic)
    implementation(libs.androidx.activity.compose)
    implementation(libs.compose.material3)
    implementation(libs.compose.components.resources)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil.compose)
    implementation("androidx.compose.material:material-icons-extended:1.7.5")

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.viewmodel)
    
    // Flutter SDUI Wrapper (AAR) y Engine
    // Usamos implementation para asegurar visibilidad en el IDE
    implementation("com.jght.sjrqromx.business.shipping.kmp_shipping_platform.sdui:flutter_debug:1.0")
    implementation("io.flutter:flutter_embedding_debug:1.0.0-83675ed27633283e7fc296c8bca22e841224c096")
    implementation("io.flutter:arm64_v8a_debug:1.0.0-83675ed27633283e7fc296c8bca22e841224c096")

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}
