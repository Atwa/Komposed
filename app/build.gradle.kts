import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.serialization)
}

kotlin {
    androidTarget {
        compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    }
    listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "app"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":app:core:navigation"))
            implementation(project(":app:core:middleware"))
            implementation(libs.komposed)
            implementation(project(":app:checkout"))
            implementation(project(":app:orderDetails"))
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.navigation.compose.kmp)
            implementation(libs.serialization)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
        }
    }
}

android {
    namespace = "io.github.atwa.komposed.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "io.github.atwa.komposed.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures { compose = true }

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
}
