//import com.diffplug.gradle.spotless.SpotlessTask
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
	alias(libs.plugins.room) apply false
	alias(libs.plugins.detekt) apply false
    alias(libs.plugins.protobuf) apply false
}

buildscript {
  dependencies { classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.5") }
}