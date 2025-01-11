import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt.android)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.ksp)
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Change to the desired Java version
    }
}

android {
    namespace = "com.asu1.quizzer"
    compileSdk = 35

    val versionFile = file("version.properties")
    val versionProps = Properties()

    if (versionFile.exists()) {
        versionProps.load(versionFile.inputStream())
    }

    val localVersionCode = versionProps.getProperty("VERSION_CODE")?.toIntOrNull() ?: 1
    val versionMain = versionProps.getProperty("VERSION_MAIN")?.toIntOrNull() ?: 1
    val versionSub = versionProps.getProperty("VERSION_SUB")?.toIntOrNull() ?: 0
    val versionChild = versionProps.getProperty("VERSION_CHILD")?.toIntOrNull() ?: 0

    defaultConfig {
        applicationId = "com.asu1.quizzer"
        minSdk = 29
        targetSdk = 35
        versionCode = project.findProperty("versionCode")?.toString()?.toInt() ?: localVersionCode
        versionName = "$versionMain.$versionSub.$versionChild"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = System.getenv("KEY_ALIAS") ?: throw IllegalArgumentException("KEY_ALIAS environment variable not set")
            keyPassword = System.getenv("KEY_PASSWORD") ?: throw IllegalArgumentException("KEY_PASSWORD environment variable not set")
            storeFile = file(System.getenv("KEY_STORE") ?: throw IllegalArgumentException("KEY_STORE environment variable not set"))
            storePassword = System.getenv("STORE_PASSWORD") ?: throw IllegalArgumentException("STORE_PASSWORD environment variable not set")
        }
        create("release") {
            keyAlias = System.getenv("KEY_ALIAS") ?: throw IllegalArgumentException("KEY_ALIAS environment variable not set")
            keyPassword = System.getenv("KEY_PASSWORD") ?: throw IllegalArgumentException("KEY_PASSWORD environment variable not set")
            storeFile = file(System.getenv("KEY_STORE") ?: throw IllegalArgumentException("KEY_STORE environment variable not set"))
            storePassword = System.getenv("STORE_PASSWORD") ?: throw IllegalArgumentException("STORE_PASSWORD environment variable not set")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("Boolean", "isDebug", "false")
            buildConfigField("String", "PASSWORD", "\"${System.getenv("PASSWORD") ?: throw IllegalArgumentException("PASSWORD environment variable not set")}\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${System.getenv("GOOGLE_CLIENT_ID") ?: throw IllegalArgumentException("GOOGLE_CLIENT_ID environment variable not set")}\"")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("Boolean", "isDebug", "true")
            buildConfigField("String", "PASSWORD", "\"${System.getenv("PASSWORD") ?: throw IllegalArgumentException("PASSWORD environment variable not set")}\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${System.getenv("GOOGLE_CLIENT_ID") ?: throw IllegalArgumentException("GOOGLE_CLIENT_ID environment variable not set")}\"")
        }
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
        buildConfig = true
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }

    composeCompiler {
        featureFlags = setOf(

        )
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.core.ktx)
    implementation(libs.foundation.layout)
    implementation(libs.navigation.compose)
    implementation (libs.credentials)
    implementation(libs.play.services.auth)
    implementation (libs.credentials.play.services.auth)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.coil.compose)
    implementation(libs.uuid.creator)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.material3)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.java.dotenv)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.converter.moshi)
    implementation(libs.androidx.security.crypto)
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.googleid)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.coil.compose)
    implementation(libs.colorpicker.compose)
    implementation(libs.androidx.media3.effect)
    implementation(libs.material.kolor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.core)
    implementation(libs.compose)
    implementation(libs.reorderable)
//    implementation("androidx.wear.compose:compose-material-core:1.4.0")
    implementation(libs.lottie.compose)
    implementation(libs.dotlottie.android)
    implementation(libs.places)
    implementation(libs.material)
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation(libs.landscapist.glide)
    implementation(libs.hilt.android)
    testImplementation(libs.androidx.core.testing)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.constraint.layout)

    //EXOPLAYER
    implementation(libs.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)

    //firebase login
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    androidTestImplementation(project(":app"))
    testImplementation(libs.junit)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.moshi.kotlin)
    testImplementation(libs.converter.moshi)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.inline)

    androidTestImplementation(libs.androidx.fragment.ktx)
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.mockito.mockito.core)
    androidTestImplementation(libs.mockito.mockito.inline)
    androidTestImplementation(libs.mockito.kotlin)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}