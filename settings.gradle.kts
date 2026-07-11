rootProject.name = "KMPShippingPlatform"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        
        // Flutter AAR Repository
        maven { 
            url = uri("/Users/ghetz/Documents/PORTAFOLIO/ANDROID/BUSSINESS/SHIPPING/LIVERPOOL CODE CHALLENGING/KMPShippingPlatform/flutter_sdui_wrapper_4_kmp/build/host/outputs/repo")
        }
        maven { 
            url = uri("https://storage.googleapis.com/download.flutter.io")
        }
    }
}

include(":app:androidApp")
include(":app:sharedLogic")
include(":core")
include(":server")
