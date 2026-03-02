plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "org.vocaminder.androidapp"
    compileSdk = 36

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures{
        compose = true
    }

}

dependencies {
    implementation(projects.composeApp)
    implementation(projects.features)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.material3.windowsize)

    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3.window.size.class1)
    debugImplementation(libs.androidx.compose.ui.tooling)
}