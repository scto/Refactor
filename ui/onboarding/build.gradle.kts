plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
    //alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.scto.refactor.ui.onboarding"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // KORRIGIERT: Fehlende Modul-Abhängigkeit hinzugefügt
    implementation(project(":data:local"))

    // Hilt für Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Standard-Abhängigkeiten für Compose und Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.lifecycle.viewModelCompose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Accompanist für Pager-Layout
    implementation(libs.accompanist.pager)
    implementation(libs.accompanist.pager.indicators)
}
