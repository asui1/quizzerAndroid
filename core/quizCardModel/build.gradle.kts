plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asu1.quizcardmodel"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    api(libs.retrofit2.kotlinx.serialization.converter)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}
