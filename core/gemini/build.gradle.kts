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
import java.io.FileInputStream
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = libs.versions.android.core.gemini.name.get().toString()
	
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        
        buildConfigField("String", "GEMINI_API_KEY", "\"${getApiKey()}\"")
        buildConfigField("String", "DEBUG", "\"${getDebug()}\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
	
	/*
    kotlinOptions {
        jvmTarget = "17"
    }
	*/
	
    // ENTFERNT: Veralteter Block für Compose
	/*
    buildFeatures {
        compose = true
        buildConfig = true
    }
	*/
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    implementation(libs.jgit)
    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    // KORRIGIERT: Explizite Abhängigkeit hinzugefügt, um das Problem zu beheben
    implementation(libs.androidx.compose.material3)
    implementation(libs.google.generativeai)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
	
    // Ktor-Abhängigkeiten
    implementation("io.ktor:ktor-client-core:2.3.13")
    implementation("io.ktor:ktor-client-cio:2.3.13")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.13")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.13")
    implementation("io.ktor:ktor-client-logging:2.3.13")
    
    testImplementation(libs.junit4)
	
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
	
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}

fun getApiKey(): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    return properties.getProperty("GEMINI_API_KEY", "DEFAULT_API_KEY_IF_NOT_FOUND")
}

fun getDebug(): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    return properties.getProperty("DEBUG", "DEFAULT_DEBUG_VALUE_IF_NOT_FOUND")
}