import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties
import config.ConfigData

plugins {
	alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
	alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    //alias(libs.plugins.kfmt)
	id("detekt-setup")
}

android {
	namespace = ConfigData.applicationBundle
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        applicationId = ConfigData.applicationBundle
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        versionCode = ConfigData.versionCode
        versionName = ConfigData.versionName

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

    kotlin {
        sourceSets.all {
            languageSettings.enableLanguageFeature("ExplicitBackingFields")
        }
        compilerOptions {
            jvmTarget.set(JvmTarget.from(ConfigData.javaVersion.toString()))
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
    implementation(project(":ui:home"))
    implementation(project(":ui:onboarding"))
    implementation(project(":ui:settings"))
    
	implementation(libs.androidx.hilt.navigation.compose)

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
    ksp(libs.hilt.compiler)
	
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
