import java.util.Properties

plugins {
    id("kotlin-kapt")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.compose.compiler)
    id("kotlin-parcelize")
}

android {
    namespace = "com.mvpsales.github"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }

    defaultConfig {
        applicationId = "com.mvpsales.github"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //load the values from .properties file
        val keystoreFile = project.rootProject.file("secrets.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //return empty key in case something goes wrong
        val apiKey = properties.getProperty("API_KEY") ?: ""
        buildConfigField("String", "API_KEY", apiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.kotlin.serialization)
    implementation(libs.retrofit)
    implementation(libs.ktor.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.contentnegotiation)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.cio)
    implementation(libs.coil.compose)
    implementation(libs.coil.network)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    val composeBom = platform(libs.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.compose.material3)
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)
    androidTestImplementation(libs.compose.ui.test)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.compose.activity)
    implementation(libs.compose.viewmodels)
    implementation(libs.compose.navigation)
    implementation(libs.hilt.navigation.compose)
    testImplementation(libs.room.testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.navigation.testing)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

kapt {
    correctErrorTypes = true
}
