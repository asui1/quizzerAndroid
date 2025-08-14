plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.asu1.models"
}

dependencies {
    implementation(project(":core:util"))
    api(project(":core:imageColor"))
    implementation(project(":core:resource"))
    api(libs.material3)
    implementation(project(":core:colorModel"))
    implementation(libs.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.compose.ui)
    implementation(libs.core.ktx)
    implementation(libs.material.icons.extended)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}