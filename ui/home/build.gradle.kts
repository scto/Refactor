plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
    //alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.scto.refactor.ui.home"
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
    
    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
	// KORRIGIERT: Implementierung des zentralen :core:gemini-Moduls
    implementation(project(":core:gemini"))
	
    // KORRIGIERT: Hilt-Abhängigkeiten hinzugefügt
    implementation(libs.hilt.android)
    //implementation("com.google.dagger:hilt-android:2.51.1")
	ksp(libs.hilt.compiler)
    //kapt("com.google.dagger:hilt-compiler:2.51.1")
	implementation(libs.androidx.hilt.navigation.compose)
    //implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.timber)
}