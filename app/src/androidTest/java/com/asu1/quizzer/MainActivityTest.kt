package com.asu1.quizzer

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.asu1.quizzer.viewModels.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.reflect.Field
import java.lang.reflect.Method

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun internetAvailable_showsUpdateDialog() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
            val isInternetAvailableField: Field = MainViewModel::class.java.getDeclaredField("_isInternetAvailable")
            isInternetAvailableField.isAccessible = true
            val isInternetAvailable = isInternetAvailableField.get(viewModel) as MutableLiveData<Boolean>
            isInternetAvailable.postValue(true)

            val isUpdateAvailableField: Field = MainViewModel::class.java.getDeclaredField("_isUpdateAvailable")
            isUpdateAvailableField.isAccessible = true
            val isUpdateAvailable = isUpdateAvailableField.get(viewModel) as MutableLiveData<Boolean>
            isUpdateAvailable.postValue(true)
        }

        composeTestRule.onNodeWithText("Update available").assertIsDisplayed()
    }

    @Test
    fun noInternet_showsNoInternetDialog() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity).get(MainViewModel::class.java)
            val isInternetAvailableField: Field = MainViewModel::class.java.getDeclaredField("_isInternetAvailable")
            isInternetAvailableField.isAccessible = true
            val isInternetAvailable = isInternetAvailableField.get(viewModel) as MutableLiveData<Boolean>
            isInternetAvailable.postValue(false)
        }

        composeTestRule.onNodeWithText("No internet connection").assertIsDisplayed()
    }

    @Test
    fun redirectToPlayStore_opensPlayStore() {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        scenario.onActivity { activity ->
            val redirectToPlayStoreMethod: Method = MainActivity::class.java.getDeclaredMethod("redirectToPlayStore")
            redirectToPlayStoreMethod.isAccessible = true
            redirectToPlayStoreMethod.invoke(activity)

            val expectedIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=com.asu1.quizzer")
                setPackage("com.android.vending")
            }

            val actualIntent = InstrumentationRegistry.getInstrumentation().targetContext.packageManager
                .getLaunchIntentForPackage("com.android.vending")

            assertEquals(expectedIntent.data, actualIntent?.data)
            assertEquals(expectedIntent.action, actualIntent?.action)
        }
    }
}