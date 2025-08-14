plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asu1.resources"
}

dependencies {

    implementation(libs.androidx.appcompat)
    implementation(libs.compose.ui)
    implementation(libs.foundation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.google.fonts)
    implementation(libs.compose.ui)
    implementation(platform(libs.compose.bom))
    implementation(libs.material3)
    implementation(libs.kotlinx.serialization.json)
    api(libs.google.fonts)
    api(libs.media3.session)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}
