@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
}

kotlin {
    androidLibrary {
        compileSdk = 36
        minSdk = 26
        namespace = "org.vocaminder.core"
        androidResources.enable = true

    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()


    jvm()

    js {
        browser()
        binaries.executable()
    }

    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.decompose)
            implementation(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
    }
}




//compose.desktop {
//    application {
//        mainClass = "org.vocaminder.MainKt"
//
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "org.vocaminder"
//            packageVersion = "1.0.0"
//        }
//    }
//}

