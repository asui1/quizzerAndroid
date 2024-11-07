package com.asu1.quizzer.network

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class CustomConverterFactory(
    private val gsonConverterFactory: Converter.Factory,
    private val kotlinxSerializationConverterFactory: Converter.Factory
) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<okhttp3.ResponseBody, *>? {
        return if (shouldUseKotlinxSerialization(annotations)) {
            kotlinxSerializationConverterFactory.responseBodyConverter(type, annotations, retrofit)
        } else {
            gsonConverterFactory.responseBodyConverter(type, annotations, retrofit)
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, okhttp3.RequestBody>? {
        return if (shouldUseKotlinxSerialization(methodAnnotations)) {
            kotlinxSerializationConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
        } else {
            gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
        }
    }

    private fun shouldUseKotlinxSerialization(annotations: Array<Annotation>): Boolean {
        // Add logic to determine if KotlinxSerialization should be used based on annotations
        return annotations.any { it.annotationClass == UseKotlinxSerialization::class }
    }
}

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class UseKotlinxSerialization