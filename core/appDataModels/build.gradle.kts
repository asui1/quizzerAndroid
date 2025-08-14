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


    // If you mean Compose Material (M2):
    // If you mean XML Material Components instead, use this instead of the line above:
    // implementation(libs.material.components)


    // REMOVE: implementation(libs.androidx.ui.graphics.android)  // not in your TOML; ui-graphics is enough
    implementation(platform(libs.compose.bom))
    implementation(libs.material3)
    implementation(libs.compose.ui)

    // These two need TOML entries (see below)
    implementation(libs.material.icons.core)
    api(libs.kotlinx.serialization.json)
    implementation(libs.androidx.foundation.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core) // was libs.androidx.espresso.core
}
