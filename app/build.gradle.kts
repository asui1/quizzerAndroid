import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.13"
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

 //    val localVersionCode = versionProps.getProperty("VERSION_CODE")?.toIntOrNull() ?: 1
//    val versionMain = versionProps.getProperty("VERSION_MAIN")?.toIntOrNull() ?: 1
//    val versionSub = versionProps.getProperty("VERSION_SUB")?.toIntOrNull() ?: 0
//    val versionChild = versionProps.getProperty("VERSION_CHILD")?.toIntOrNull() ?: 0

    defaultConfig {
        applicationId = "com.asu1.quizzer"
        minSdk = 29
        targetSdk = 35
        versionCode = 77
        versionName = "1.1.2"

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
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("Boolean", "isDebug", "false")
            buildConfigField("String", "PASSWORD", "\"${System.getenv("PASSWORD") ?: throw IllegalArgumentException("PASSWORD environment variable not set")}\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${System.getenv("GOOGLE_CLIENTID_WEB") ?: throw IllegalArgumentException("GOOGLE_CLIENT_ID environment variable not set")}\"")
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("Boolean", "isDebug", "true")
            buildConfigField("String", "PASSWORD", "\"${System.getenv("PASSWORD") ?: throw IllegalArgumentException("PASSWORD environment variable not set")}\"")
            buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${System.getenv("GOOGLE_CLIENTID_WEB") ?: throw IllegalArgumentException("GOOGLE_CLIENT_ID environment variable not set")}\"")
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            matchingFallbacks += listOf("release")
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "benchmark-rules.pro"
            )
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

//    composeOptions {
//        kotlinCompilerExtensionVersion = "1.5.1"
//    }

    composeCompiler {
        reportsDestination = layout.buildDirectory.dir("compose_compiler")
        featureFlags = setOf(

        )
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.register<JacocoReport>("jacocoMergedReport") {
    dependsOn("testDebugUnitTest", "connectedDebugAndroidTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R\$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val javaClasses = fileTree("${layout.buildDirectory}/intermediates/javac/debug") {
        exclude(fileFilter)
    }
    val kotlinClasses = fileTree("${layout.buildDirectory}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }

    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    executionData.setFrom(
        files(
            "${layout.buildDirectory}/jacoco/testDebugUnitTest.exec",
            "${layout.buildDirectory}/outputs/code_coverage/debugAndroidTest/connected/*coverage.ec"
        )
    )
}

dependencies {
    implementation(project(":core:resource"))
    testImplementation(project(":core:imageColor"))
    implementation(project(":core:quizModel"))
    implementation(project(":core:util"))

    implementation(project(":domain:userDataUseCase"))
    implementation(project(":domain:appDataUseCase"))

    implementation(project(":repository:appData"))
    implementation(project(":repository:network"))

    implementation(project(":feature:quizCard"))
    implementation(project(":feature:quiz"))
    androidTestImplementation(project(":feature:customComposable"))
    implementation(project(":feature:activityNavigation"))
    implementation(libs.androidx.lifecycle.process)
    implementation(project(":feature:mainPage"))
    implementation(project(":feature:splashPage"))
    implementation(project(":feature:search"))

    // ---- Compose (via BOM) ----
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose.core)

    // ---- Room ----
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)

    // ---- Retrofit / Moshi / JSON ----
    implementation(libs.bundles.retrofit.full)

    // ---- AndroidX & lifecycle/work ----
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.work.runtime.ktx)

    // Optional Compose extras
    implementation(libs.accompanist.flowlayout)
    implementation(libs.google.fonts)
    // If you use foundation-layout specifically:
    implementation(libs.foundation.layout)

    // ---- Google sign-in / credentials ----
    runtimeOnly(libs.credentials.play.services.auth)
    implementation(libs.play.services.auth)

    // ---- Data / Security ----
    implementation(libs.datastore.core)

    // ---- Media / Imaging ----
    implementation(libs.bundles.media3)            // effect, ui, exoplayer-dash, session
                 // core exoplayer
    runtimeOnly(libs.dotlottie.android)

    // ---- UI extras ----
    implementation(libs.material3)                 // Compose M3
       // Google Material (XML) if needed
    runtimeOnly(libs.places)
    // Optional: animation graphics module
    // implementation(libs.androidx.animation.graphics.android)

    // ---- Navigation ----
    implementation(libs.navigation.compose)

    // ---- Serialization / Coroutines / Rx ----

    // ---- Firebase (via BOM) ----
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    // ---- App Update ----
    implementation(libs.google.play.update)

    // ---- Hilt ----
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // ---- Tests ----
    testImplementation(libs.bundles.test.core)
    testImplementation(libs.bundles.mocking)
    testRuntimeOnly(libs.jetbrain.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)

    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.test.core)
    androidTestImplementation(libs.bundles.mocking)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(libs.collections.immutable)

    // ---- Debug-only ----
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}