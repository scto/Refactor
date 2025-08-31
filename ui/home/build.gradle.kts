/*
 * Copyright 2025, S.C.T.O
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
	
    /*
    kotlinOptions {
        jvmTarget = "17"
    }
	
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
	*/
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
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