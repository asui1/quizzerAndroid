import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.hilt.android)
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

//    val localVersionCode = versionProps.getProperty("VERSION_CODE")?.toIntOrNull() ?: 1
//    val versionMain = versionProps.getProperty("VERSION_MAIN")?.toIntOrNull() ?: 1
//    val versionSub = versionProps.getProperty("VERSION_SUB")?.toIntOrNull() ?: 0
//    val versionChild = versionProps.getProperty("VERSION_CHILD")?.toIntOrNull() ?: 0

    defaultConfig {
        applicationId = "com.asu1.quizzer"
        minSdk = 29
        targetSdk = 35
        versionCode = 74
        versionName = "1.0.15"

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


dependencies {
    implementation(project(":core:quizCardModel"))
    implementation(project(":core:resource"))
    implementation(project(":core:imageColor"))
    implementation(project(":core:userDataModels"))
    implementation(project(":core:appDataModels"))
    implementation(project(":core:quizModel"))
    implementation(project(":core:util"))

    implementation(project(":domain:userDataUseCase"))
    implementation(project(":domain:appDataUseCase"))

    implementation(project(":repository:appData"))
    implementation(project(":repository:network"))

    implementation(project(":feature:quizCard"))
    implementation(project(":feature:quiz"))
    implementation(project(":feature:customComposable"))
    implementation(project(":feature:activityNavigation"))
    implementation(project(":feature:permissionRequest"))
    testImplementation(project(":feature:quizCard"))
    androidTestImplementation(project(":feature:quizCard"))
    implementation(project(":feature:musicUi"))
    testImplementation(project(":feature:musicUi"))
    androidTestImplementation(project(":feature:musicUi"))
    implementation(project(":feature:mainPage"))
    implementation(project(":feature:splashPage"))
    implementation(project(":feature:search"))

    testImplementation(project(":core:quizModel"))
    androidTestImplementation(project(":core:quizModel"))
    testImplementation(project(":core:util"))
    androidTestImplementation(project(":core:util"))
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.animation.graphics.android)
    implementation(libs.androidx.junit.ktx)
    testImplementation(libs.testng)
    ksp(libs.room.compiler)

    implementation(libs.core.ktx)
    implementation(libs.foundation.layout)
    implementation(libs.navigation.compose)
    implementation (libs.credentials)
    implementation(libs.play.services.auth)
    implementation (libs.credentials.play.services.auth)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.accompanist.flowlayout)
    implementation(libs.uuid.creator)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.androidx.material3.android)
    implementation(libs.material3)
    implementation(libs.lifecycle.viewmodel.compose)
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
    implementation (libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.youtubeplayer.core)
    implementation(libs.calendar.compose)
    implementation(libs.reorderable)
    implementation(libs.lottie.compose)
    implementation(libs.dotlottie.android)
    implementation(libs.places)
    implementation(libs.material)
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)
    implementation(libs.landscapist.glide)
//    ksp(libs.landscapist.glide) -> This line adds error in build
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.constraint.layout)
    implementation(libs.androidx.tracing)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.kotlinx.coroutines.rx3)
    implementation(libs.androidx.lifecycle.process)

    //EXOPLAYER
    implementation(libs.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.legacy.support.v4)

    //firebase login
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)

    androidTestImplementation(project(":app"))
    testImplementation(libs.junit)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.kotlin.test)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.moshi.kotlin)
    testImplementation(libs.converter.moshi)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.exoplayer)
    testImplementation(libs.androidx.media3.exoplayer.dash)
    testImplementation(libs.androidx.media3.ui)
    testImplementation(libs.androidx.media3.session)
    testImplementation(libs.exoplayer.test)
    testImplementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.mockk)

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
    androidTestImplementation(libs.exoplayer)
    androidTestImplementation(libs.androidx.media3.exoplayer.dash)
    androidTestImplementation(libs.androidx.media3.ui)
    androidTestImplementation(libs.androidx.media3.session)
    androidTestImplementation(libs.exoplayer.test)
    androidTestImplementation(libs.hilt.android.test)
    kspAndroidTest(libs.hilt.compiler)

    androidTestImplementation(project(":microbenchmark"))

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}