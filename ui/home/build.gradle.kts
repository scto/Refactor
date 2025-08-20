plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "com.github.scto.refactor.ui.home"
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
	
    buildFeatures {
        compose = true
    }
	
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}
dependencies {
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.timber)
}
