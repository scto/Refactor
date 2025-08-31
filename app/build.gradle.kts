import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
	alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
	//alias(libs.plugins.buildkonfig)
}

android {
	namespace = libs.versions.android.applicationId.get().toString()
    //namespace = "com.github.scto.refactor"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
		applicationId = libs.versions.android.applicationId.get().toString()
        //applicationId = "com.github.scto.refactor"
		minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.android.versionCode.get().toInt()
        versionName = libs.versions.android.versionName.get().toString()
		/*
        minSdk = 26
        targetSdk = 34
        versionCode = 100
        versionName = "1.0.0"
		*/

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GEMINI_API_KEY", "\"${getApiKey()}\"")
        buildConfigField("String", "DEBUG", "\"${getDebug()}\"")
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
    
    /*
    kotlinOptions {
        jvmTarget = "17"
    }
    */
    
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

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
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
    implementation(libs.androidx.lifecycle.viewModelCompose)
	implementation(libs.coil.kt.compose)
	implementation(libs.coil.kt.svg)
	implementation(libs.inapp.update.compose)
	
    // Hilt
	implementation(libs.hilt.android)
    //implementation("com.google.dagger:hilt-android:2.51.1")
    ksp(libs.hilt.compiler)
	//kapt(libs.hilt.compiler)
    //kapt("com.google.dagger:hilt-compiler:2.51.1")
	
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