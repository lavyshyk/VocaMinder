---
name: gradle
description: Gradle build configuration, dependency management, build variants, and build optimization. Triggers when user mentions build, gradle, dependencies, versions, build.gradle, settings.gradle, or compilation issues.
---

# Gradle Build Skill

Build configuration and dependency management for Android projects.

## Common Tasks

```kotlin
// build.gradle.kts (module)
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.app"
    compileSdk = 34
    
    defaultConfig {
        applicationId = "com.example.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext)
}
