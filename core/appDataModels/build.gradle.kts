plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asu1.appdatamodels"
}

dependencies {
    implementation(project(":core:resource"))

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)

    // If you mean Compose Material (M2):
    implementation(libs.material)
    // If you mean XML Material Components instead, use this instead of the line above:
    // implementation(libs.material.components)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.converter.gson)

    implementation(libs.ui.graphics)
    // REMOVE: implementation(libs.androidx.ui.graphics.android)  // not in your TOML; ui-graphics is enough

    implementation(libs.compose.ui)

    // These two need TOML entries (see below)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core) // was libs.androidx.espresso.core

}