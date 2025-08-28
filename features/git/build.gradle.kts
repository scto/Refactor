plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.protobuf)
}

// HINZUGEFÜGT: Der ksp-Block wurde für eine saubere Konfiguration auf die oberste Ebene verschoben.
// Dies ist die Standardmethode, um KSP-Argumente für Room zu definieren.
ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

android {
    namespace = "com.github.scto.refactor.features.git"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        // WICHTIG: Test-Runner für Instrumented Tests hinzufügen
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ENTFERNT: Der ksp-Block wurde von hier entfernt, da er jetzt global definiert ist.
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
}

dependencies {
    // Git Client
    implementation(libs.jgit)
    implementation(libs.timber)

    // ViewModel and Compose
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.1")

    // DataStore (Proto)
    implementation("androidx.datastore:datastore:1.1.1")
    
    // https://mvnrepository.com/artifact/com.google.protobuf/protoc
    //implementation("com.google.protobuf:protoc:3.25.3")

    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-bom
    //implementation("com.google.protobuf:protobuf-bom:3.25.5")
    //implementation("com.google.protobuf:protobuf-bom:3.25.3")
    
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-parent
    //implementation("com.google.protobuf:protobuf-parent:3.25.3")

    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java
    //implementation("com.google.protobuf:protobuf-java:3.25.5")
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-javalite
    //implementation("com.google.protobuf:protobuf-javalite:3.25.5")
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-java-util
    //implementation("com.google.protobuf:protobuf-java-util:3.25.5")
    //implementation("com.google.protobuf:protobuf-java-util:3.25.3")
    //implementation("com.google.protobuf:protobuf-java:3.25.3")
    implementation("com.google.protobuf:protobuf-javalite:3.25.3")
    //implementation(libs.protobuf.javalite)
    
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-kotlin
    //implementation("com.google.protobuf:protobuf-kotlin:3.25.5")
    // https://mvnrepository.com/artifact/com.google.protobuf/protobuf-kotlin-lite
    //implementation("com.google.protobuf:protobuf-kotlin-lite:3.25.5")
    //implementation("com.google.protobuf:protobuf-kotlin:3.25.5")
    implementation("com.google.protobuf:protobuf-kotlin-lite:3.25.3")
    //implementation(libs.protobuf.kotlin.lite)
    
    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
	
	// Jetpack Security
    implementation("androidx.security:security-crypto-ktx:1.1.0-alpha06")

    // --- NEUE TEST-ABHÄNGIGKEITEN ---
    // Standard Unit-Tests (laufen auf der JVM)
    testImplementation(libs.junit4)
    // Instrumented Tests (laufen auf einem Android-Gerät/Emulator)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
	
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
}

protobuf {
    protoc {
        // BITTE WÄHLE DIE PASSENDE ZEILE FÜR DEINEN COMPUTER AUS UND ENTFERNE DIE KOMMENTARZEICHEN
        
        // --- Für Apple Silicon (M1/M2/M3) ---
        // artifact = "com.google.protobuf:protoc:3.25.5:osx-aarch_64"

        // --- Für Apple Mac (Intel) ---
        // artifact = "com.google.protobuf:protoc:3.25.5:osx-x86_64"

        // --- Für Windows ---
        // artifact = "com.google.protobuf:protoc:3.25.5:windows-x86_64"
        
        // --- Für Linux ---
        // artifact = "com.google.protobuf:protoc:3.25.5:linux-x86_64"
        
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}
