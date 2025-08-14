plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.mainpage"
}

dependencies {
    implementation(project(":core:resource"))
    implementation(project(":core:util"))
    api(project(":core:appDataModels"))
    api(project(":core:userDataModels"))
    api(project(":core:quizCardModel"))

    implementation(project(":repository:network"))
    api(project(":repository:appData"))

    api(project(":domain:appDataUseCase"))
    api(project(":domain:userDataUseCase"))

    implementation(project(":feature:customComposable"))
    api(project(":feature:activityNavigation"))
    implementation(project(":feature:quizCard"))
    api(project(":feature:quiz"))

    implementation(libs.core.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    api(libs.credentials)
    implementation(libs.play.services.auth)
    runtimeOnly(libs.credentials.play.services.auth)
    implementation(libs.googleid)
    api(libs.foundation.android)
    implementation(libs.material.icons.core)
    implementation(libs.material.icons.extended)
    implementation(libs.material3)
    implementation(libs.compose.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.ui.tooling)
    implementation(libs.runtime.livedata)
    implementation(libs.collections.immutable)
    implementation(libs.constraint.layout)
    implementation(libs.coil.compose)
    implementation(libs.landscapist.glide)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    testImplementation(kotlin("test"))
}