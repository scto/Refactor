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
import config.ConfigData

plugins {
    //alias(libs.plugins.android.library)
    //alias(libs.plugins.kotlin.android)
	alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kfmt)
	id("module-setup")
}

android {
	namespace = ConfigData.applicationBundle + ".data.local"
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
		debug {
            isMinifyEnabled = false
            proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
        }
    }
	
    compileOptions {
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
}

kotlin {
    compilerOptions {
        jvmTarget = ConfigData.javaVersion.toString()
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
