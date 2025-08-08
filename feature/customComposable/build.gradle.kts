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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.foundation.android)
    implementation(libs.material3)
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
    implementation(project(":core:util"))
    implementation(project(":core:imageColor"))
    implementation(project(":feature:activityNavigation"))
    implementation(libs.activity.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
    debugImplementation(libs.ui.tooling)
}