plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.github.scto.refactor.selfupdate"
    compileSdk = 34
	
    defaultConfig {
        minSdk = 26
    }
	
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
	
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.timber)
}
