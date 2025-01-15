package com.asu1.macrobenchmark

import android.content.Intent
import android.net.Uri
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until


/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun benchmark_startup() = benchmarkRule.measureRepeated(
        packageName = "com.asu1.quizzer",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
        device.wait(Until.hasObject(By.text("롤")), 20_000)
    }

    @Test
    fun benchmark_getquiz() = benchmarkRule.measureRepeated(
        packageName = "com.asu1.quizzer",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ){
        pressHome()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://quizzer.co.kr?resultId=b0867d48334c84b00834226d93b87ebfeec5daac4443539567078e268752c832"))
        startActivityAndWait(intent)
        device.wait(Until.hasObject(By.text("메인으로")), 20_000)
    }

    @Test
    fun benchmark_getresult() = benchmarkRule.measureRepeated(
        packageName = "com.asu1.quizzer",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD
    ){
        pressHome()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://quizzer.co.kr?quizId=688c0185-1b4b-5064-8431-bfc00414f208"))
        startActivityAndWait(intent)
        device.wait(Until.hasObject(By.text("선택된")), 20_000)
    }
}