import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
}

// Dynamic resolution of the Flutter engine to avoid evaluation friction
val flutterEngineHash: String by lazy {
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    val flutterSdk = localProperties.getProperty("flutter.sdk") 
        ?: "/opt/homebrew/share/flutter" // Common fallback on macOS
    
    val engineVersionFile = file("$flutterSdk/bin/internal/engine.version")
    if (engineVersionFile.exists()) {
        engineVersionFile.readText().trim()
    } else {
        "83675ed27633283e7fc296c8bca22e841224c096" // Last known hash
    }
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "SharedLogic"
            isStatic = true
        }
    }
    
    androidLibrary {
       namespace = "com.jght.sjrqromx.business.shipping.kmp_shipping_platform.sharedLogic"
       compileSdk = libs.versions.android.compileSdk.get().toInt()
       minSdk = libs.versions.android.minSdk.get().toInt()
    
       compilerOptions {
           jvmTarget = JvmTarget.JVM_11
       }
       androidResources {
           enable = true
       }
       withHostTest {
           isIncludeAndroidResources = true
       }
    }
    
    sourceSets {
        commonMain.dependencies {
            api(projects.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
        androidMain.dependencies {
            // Dynamic binary linking of the Flutter engine
            implementation("io.flutter:flutter_embedding_debug:1.0.0-$flutterEngineHash")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
