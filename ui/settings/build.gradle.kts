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
import config.ConfigData

plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
	id("module-setup")
}

android {
	namespace = ConfigData.applicationBundle + ".ui.settings"
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
	
    buildFeatures {
        compose = true
    }
    
	/*
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
    }
	
	compileOptions {
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
	*/
}

dependencies {
    implementation(project(":data:local"))

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.timber)
    implementation(libs.androidx.navigation.compose)
}