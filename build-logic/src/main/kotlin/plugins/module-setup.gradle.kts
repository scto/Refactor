import config.ConfigData
import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("detekt-setup")
}

android {
    namespace = config.ConfingData.applicationBundle
    compileSdk = config.ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = config.ConfigData.minSdkVersion
    }

    kotlin {
        sourceSets.all {
            languageSettings.enableLanguageFeature("ExplicitBackingFields")
        }
        compilerOptions {
			jvmTarget.set(config.ConfigData.jvmTarget)
        }
    }

    compileOptions {
        sourceCompatibility = config.ConfigData.javaVersion
        targetCompatibility = config.ConfigData.javaVersion
    }
}

val libs = the<LibrariesForLibs>()
dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
}
