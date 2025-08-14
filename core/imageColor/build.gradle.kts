plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asu1.imagecolor"
}

dependencies {

    implementation(project(":core:resource"))
    implementation(project(":core:colorModel"))
    api(project(":core:util"))

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ui.graphics)
    implementation(libs.material3)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}