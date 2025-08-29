import java.util.Properties
import java.io.FileInputStream

plugins {
	alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
	//alias(libs.plugins.kotlin.kapt)
	//alias(libs.plugins.kotlin.serialization)
	//id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.github.scto.refactor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.github.scto.refactor"
        minSdk = 26
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GEMINI_API_KEY", "\"${getApiKey()}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
		debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
 
    buildFeatures {
        //compose = true
        buildConfig = true
    }
	
    /*
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    */
	
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core:gemini"))
    implementation(project(":data:local"))
    implementation(project(":features:git"))
	implementation(project(":self-update"))
	//implementation(project(":svg-converter"))
    implementation(project(":ui:home"))
    implementation(project(":ui:onboarding"))
    implementation(project(":ui:settings"))
    
    // Hilt
	implementation(libs.hilt.android)
    //implementation("com.google.dagger:hilt-android:2.51.1")
    ksp(libs.hilt.compiler)
	//kapt(libs.hilt.compiler)
    //kapt("com.google.dagger:hilt-compiler:2.51.1")
	implementation(libs.androidx.hilt.navigation.compose)
    //implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    implementation(libs.androidx.lifecycle.viewModelCompose)
	
	implementation(libs.coil.kt.compose)
	implementation(libs.coil.kt.svg)
	
	implementation(libs.inapp.update.compose)
}

fun getApiKey(): String {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(FileInputStream(localPropertiesFile))
    }
    return properties.getProperty("GEMINI_API_KEY", "DEFAULT_API_KEY_IF_NOT_FOUND")
}