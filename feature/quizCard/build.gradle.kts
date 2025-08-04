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
    implementation(project(":core:quizCardModel"))
    implementation(project(":core:util"))
    implementation(project(":core:resource"))
    implementation(project(":core:quizModel"))
    implementation(project(":repository:network"))

    implementation(project(":feature:customComposable"))
    implementation(project(":feature:activityNavigation"))
    implementation(project(":feature:quiz"))
    implementation(project(":feature:mainPage"))

    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.runtime.android)
    implementation(libs.foundation.layout)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.foundation.android)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.landscapist.glide)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation (libs.retrofit)
    implementation(libs.retrofit2.kotlinx.serialization.converter)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.androidx.compose.ui.ui.tooling)
}