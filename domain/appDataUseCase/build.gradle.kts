import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.asu1.appdatausecase"
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        freeCompilerArgs.add("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {
    implementation(project(":core:resource"))
    implementation(project(":core:quizCardModel"))
    implementation(project(":core:appDataModels"))
    implementation(project(":core:userDataModels"))
    implementation(project(":core:util"))
    implementation(project(":core:quizModel"))

    implementation(project(":repository:network"))
    implementation(project(":repository:quizData"))
    implementation(project(":repository:appData"))

    implementation(libs.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.hilt.android)
    implementation (libs.retrofit)
    implementation(libs.datastore.core)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}