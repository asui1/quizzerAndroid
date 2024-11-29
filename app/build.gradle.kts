plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.asu1.quizzer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.asu1.quizzer"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = project.findProperty("KEY_ALIAS") as String
            keyPassword = project.findProperty("KEY_PASSWORD") as String
            storeFile = file(project.findProperty("STORE_FILE") as String)
            storePassword = project.findProperty("STORE_PASSWORD") as String
        }
        create("release") {
            keyAlias = project.findProperty("KEY_ALIAS") as String
            keyPassword = project.findProperty("KEY_PASSWORD") as String
            storeFile = file(project.findProperty("STORE_FILE") as String)
            storePassword = project.findProperty("STORE_PASSWORD") as String
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
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

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.compose.foundation:foundation-layout:1.7.4")
    implementation("androidx.navigation:navigation-compose:2.8.3")
    implementation ("androidx.credentials:credentials:1.3.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation ("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.github.f4b6a3:uuid-creator:6.0.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    implementation("androidx.compose.runtime:runtime-livedata:1.7.4") // Added dependency
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("io.github.cdimascio:java-dotenv:5.2.2")
    implementation("com.squareup.moshi:moshi:1.13.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.compose.material:material:1.7.4")
    implementation("androidx.compose.material:material-icons-core:1.7.4")
    implementation("androidx.compose.material:material-icons-extended:1.7.4")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation("androidx.fragment:fragment-ktx:1.8.4")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("com.github.skydoves:colorpicker-compose:1.1.2")
    implementation("androidx.media3:media3-effect:1.4.1")
    implementation("com.google.ar.sceneform:rendering:1.17.1")
    implementation("com.materialkolor:material-kolor:1.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.1")
    implementation("com.kizitonwose.calendar:compose:2.6.0")
    implementation("sh.calvin.reorderable:reorderable:2.3.3")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.wear.compose:compose-material-core:1.4.0")
    implementation("com.airbnb.android:lottie-compose:6.0.0")
    implementation("com.github.LottieFiles:dotlottie-android:0.4.1")

    androidTestImplementation(project(":app"))
    androidTestImplementation(project(":app"))
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.8.22")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.8.22")
    testImplementation("com.squareup.moshi:moshi-kotlin:1.13.0")
    testImplementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    testImplementation("org.robolectric:robolectric:4.9")
    androidTestImplementation("androidx.fragment:fragment-ktx:1.8.4")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("org.mockito:mockito-core:4.5.1")
    androidTestImplementation("org.mockito:mockito-inline:3.12.4")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.9.3")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
