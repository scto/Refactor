package config

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object ConfigData {
    const val applicationBundle = "com.github.scto.refactor"
	
    const val compileSdkVersion = 35
    const val minSdkVersion = 26
    const val targetSdkVersion = 35
	const val versionCode = 100
	const val versionName = "1.0.0"
	
    val javaVersion = JavaVersion.VERSION_17
}
