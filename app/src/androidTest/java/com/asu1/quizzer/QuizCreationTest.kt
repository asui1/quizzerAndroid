package com.asu1.quizzer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onIdle
import com.asu1.quizzer.viewModels.QuizData
import org.junit.Rule
import org.junit.Test
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.GestureScope
import androidx.compose.ui.test.TouchInjectionScope
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.action.ViewActions.click
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.getPhotoPickerLauncher
import com.asu1.quizzer.util.uriToByteArray
import com.asu1.quizzer.viewModels.QuizLayoutViewModel

val quizData = QuizData(
    title = "Quiz Test Title",
    image = null,
    description = "This is description for Testing Quiz",
    tags= setOf("Test", "Quiz", "아슈"),
    shuffleQuestions = false,
    creator = "GUEST",
    uuid = null
)
val titleImage: Int = R.drawable.question1

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    fun waitUntilTag(tag: String) {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun waitFor(millis: Long) {
        Thread.sleep(millis)
    }

    fun clickOnTag(tag: String) {
        composeTestRule.onNodeWithTag(tag).performClick()
    }

    fun inputTextOnTag(tag: String, text: String) {
        composeTestRule.onNodeWithTag(tag).performTextInput(text)
    }

    fun enterTextsOnTag(tag: String, texts: List<String>, withIme: Boolean) {
        texts.forEach {
            composeTestRule.onNodeWithTag(tag).performTextInput(it)
            if(withIme) {
                composeTestRule.onNodeWithTag(tag).performImeAction()
            }
            onIdle()
        }
    }

    @Test
    fun testAppLaunchAndStabilize() {
        composeTestRule.waitForIdle()

        waitUntilTag("MainScreenCreateQuiz")
        waitFor(2000)
        clickOnTag("MainScreenCreateQuiz")
        waitUntilTag("QuizLayoutBuilderAgreePolicyButton")
        clickOnTag("QuizLayoutBuilderAgreePolicyButton")
        waitUntilTag("QuizLayoutTitleTextField")
        inputTextOnTag("QuizLayoutTitleTextField", quizData.title)
        onIdle()
        waitFor(1000)
        clickOnTag("QuizLayoutBuilderProceedButton")
        onIdle()
        inputTextOnTag("QuizLayoutBuilderDescriptionTextField", quizData.description)
        onIdle()
        waitFor(1000)
        clickOnTag("QuizLayoutBuilderProceedButton")
        onIdle()

        //SET TAGS
        enterTextsOnTag("TagSetterTextField", quizData.tags.toList(), true)
        onIdle()
        waitFor(1000)
        clickOnTag("QuizLayoutBuilderProceedButton")
        onIdle()

        // Mock ActivityResultRegistry
        val mockRegistry = object : ActivityResultRegistry() {
            override fun <I : Any?, O : Any?> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                // Simulate the result of the image picker
                val result = Uri.parse("android.resource://com.asu1.quizzer/${R.drawable.question1}")
                dispatchResult(requestCode, result as O)
            }
        }

        val activity = composeTestRule.activity
        val quizLayoutViewModel = ViewModelProvider(activity).get(QuizLayoutViewModel::class.java)
        val context = activity.applicationContext


        // Register the image picker with the mock registry
        val launcher = mockRegistry.register("key", ActivityResultContracts.PickVisualMedia()) { result: Uri? ->
            // Handle the result here
            if (result != null) {
                val byteArray = uriToByteArray(
                    context = context,
                    uri = result,
                    maxWidth = 200.dp,
                    maxHeight = 200.dp,
                )
                if(byteArray != null){
                    Logger().debug("Image Picked")
                    quizLayoutViewModel.setQuizImage(byteArray)
                }
            }
        }
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        onIdle()

        onIdle()
        waitFor(3000)
        onIdle()
        waitFor(3000)
        //SET COLOR
        //SET TEXTSTYLE

        //QUIZ BUILDER
        //ADD QUIZ1
        //ADD QUIZ2
        //ADD QUIZ3
        //ADD QUIZ4

        //DESIGN SCORECARD
        //UPLOAD

        waitFor(5000)


    }
}