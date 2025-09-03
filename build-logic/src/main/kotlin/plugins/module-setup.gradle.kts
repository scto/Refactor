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
    namespace = ConfingData.applicationBundle
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
    }

    kotlin {
        sourceSets.all {
            languageSettings.enableLanguageFeature("ExplicitBackingFields")
        }
        compilerOptions {
			jvmTarget.set(ConfigData.jvmTarget)
        }
    }

    compileOptions {
        sourceCompatibility = ConfigData.javaVersion
        targetCompatibility = ConfigData.javaVersion
    }
}

val libs = the<LibrariesForLibs>()
dependencies {
    implementation(libs.koin.core)
    implementation(libs.koin.compose)
}
