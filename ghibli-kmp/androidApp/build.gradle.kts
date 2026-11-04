plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.puciec.ghiblikmp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.puciec.ghiblikmp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // Carregamento de imagens (pôsteres). Coil 2.x é Android-only e estável; ver ADR.
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation(compose.material3)
    implementation(compose.foundation)
    implementation(compose.runtime)
    implementation(compose.ui)
}
