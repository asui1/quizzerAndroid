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
    implementation(project(":core:imageColor"))
    implementation(project(":core:resource"))
    implementation(libs.androidx.material3.android)
    implementation(project(":core:colorModel"))
    testImplementation(project(":core:util"))
    androidTestImplementation(project(":core:util"))
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.compose.ui)
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.material.icons.extended)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}