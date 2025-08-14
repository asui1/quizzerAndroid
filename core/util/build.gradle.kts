plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asu1.utils"
}

dependencies {
    implementation(project(":core:resource"))

    implementation(libs.core.ktx)
    api(libs.material3)
    implementation(libs.navigation.runtime.ktx)
    implementation(libs.material.kolor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.datastore.core)
    api(libs.mlkit.segmentation.selfie)
    implementation(libs.collections.immutable)
    implementation(libs.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}