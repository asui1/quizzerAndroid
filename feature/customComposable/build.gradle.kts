plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.asu1.custombuttons"
}

dependencies {

    implementation(libs.bundles.compose.core)
    implementation(libs.core.ktx)
    implementation(libs.foundation.android)
    api(libs.material3)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.lottie.compose)
    implementation(libs.dotlottie.android)
    implementation(libs.colorpicker.compose)
    implementation(libs.constraint.layout)

    implementation(project(":core:resource"))
    api(project(":core:util"))
    api(project(":core:imageColor"))
    implementation(project(":feature:activityNavigation"))
    api(libs.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.ui.tooling)
}