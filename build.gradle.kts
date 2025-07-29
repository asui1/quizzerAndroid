// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.android.build.gradle.LibraryExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android)  apply false
    alias(libs.plugins.kotlin.serialization)  apply false
    alias(libs.plugins.kotlin.compose)  apply false
    alias(libs.plugins.kotlin.ksp)  apply false
    alias(libs.plugins.google.services)  apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.benchmark) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

subprojects {
    // 🔹 Apply only to modules that use Android Library plugin
    plugins.withId("com.android.library") {
        extensions.configure<LibraryExtension> {
            compileSdk = 35

            defaultConfig {
                minSdk = 29
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                getByName("release") {
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
        }
    }

    // 🔹 Apply Kotlin JVM target for all Kotlin modules
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
}