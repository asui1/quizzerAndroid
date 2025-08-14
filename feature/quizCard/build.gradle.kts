plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.asu1.quizcard"
}

dependencies {
    api(project(":core:quizCardModel"))
    api(project(":core:resource"))
    implementation(project(":core:quizModel"))

    implementation(project(":feature:customComposable"))
    implementation(project(":feature:activityNavigation"))
    implementation(project(":feature:quiz"))

    implementation(libs.collections.immutable)
    api(libs.runtime.android)
    implementation(libs.foundation.layout)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.foundation.android)
    api(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.landscapist.glide)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.runtime.livedata)
    implementation(libs.navigation.compose)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.ui.tooling)
}