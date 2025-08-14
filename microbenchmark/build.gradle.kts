import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.benchmark)
    alias(libs.plugins.kotlin.android)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Change to the desired Java version
    }
}

android {
    namespace = "com.asu1.microbenchmark"

    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.benchmark.junit4.AndroidBenchmarkRunner"
    }

    testBuildType = "release"
    buildTypes {
        debug {
            // Since isDebuggable can"t be modified by gradle for library modules,
            // it must be done in a manifest - see src/androidTest/AndroidManifest.xml
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "benchmark-proguard-rules.pro"
            )
        }
        release {
            isDefault = true
        }
    }
    signingConfigs {
        getByName("debug") {
            keyAlias = System.getenv("KEY_ALIAS")
                ?: throw IllegalArgumentException("KEY_ALIAS environment variable not set")
            keyPassword = System.getenv("KEY_PASSWORD")
                ?: throw IllegalArgumentException("KEY_PASSWORD environment variable not set")
            storeFile = file(
                System.getenv("KEY_STORE")
                    ?: throw IllegalArgumentException("KEY_STORE environment variable not set")
            )
            storePassword = System.getenv("STORE_PASSWORD")
                ?: throw IllegalArgumentException("STORE_PASSWORD environment variable not set")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}

dependencies {
    implementation(libs.ui.graphics)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.benchmark.junit4)
    // Add your dependencies here. Note that you cannot benchmark code
    // in an app module this way - you will need to move any code you
    // want to benchmark to a library module:
    // https://developer.android.com/studio/projects/android-library#Convert
}