// Refactor/data/local/build.gradle.kts

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
	alias(libs.plugins.hilt)
	//alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.scto.refactor.data.local"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
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
    // DataStore for preferences
    implementation(libs.androidx.datastore.preferences)
	
    // Hilt for Dependency Injection
	implementation(libs.hilt.android)
    // Ersetzen Sie 'kapt' durch 'ksp', falls Sie KSP verwenden
    ksp(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
}