import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {

    androidLibrary {
        compileSdk = 36
        minSdk = 24
        namespace = "org.vocaminder.app"
        androidResources.enable = true

    }

    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    js {
        browser()
        binaries.executable()
    }
    
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.ui.tooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.compose.ui)
            implementation(libs.androidx.compose.material3)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.bundles.preview)

        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            api(libs.material3.windowsize)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            api(libs.bundles.mvikotlin)
            api(libs.decompose)
            api(libs.decompose.compose)
            api(libs.kotlinx.collections.immutable)

            implementation(projects.features)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)

        }
    }
}
compose.resources {
    publicResClass = true
    packageOfResClass = "org.vocaminder.generated.resources"
    generateResClass = auto
}

compose.desktop {
    application {
        mainClass = "org.vocaminder.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.vocaminder"
            packageVersion = "1.0.0"
        }
    }
}
