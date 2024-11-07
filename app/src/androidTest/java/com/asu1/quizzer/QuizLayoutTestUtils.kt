package com.asu1.quizzer

import android.content.Context
import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.test.espresso.Espresso.onIdle
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.Quiz2
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.Quiz4
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.uriToByteArray
import okhttp3.internal.wait
import kotlin.random.Random

class QuizLayoutTestUtils(private val composeTestRule: ComposeTestRule) {

    fun addQuizInit(quiz: Quiz, quizType: Int, clickAddAnswerNTimes: Int = 0){
        clickOnTag("QuizBuilderScreenAddQuizIconButton")
        clickOnTag("QuizBuilderScreenNewQuizDialogImage${quizType}", true)
        waitFor(200)
        for(i in 0 until clickAddAnswerNTimes){
            clickOnTag("QuizCreatorAddAnswerButton", checkFocus = true)
        }
        inputTextOnTag("QuizQuestionTextField", quiz.question, checkFocus = true)
        replaceTextOnTag("QuizPointTextField", quiz.point.toString(), checkFocus = true)
    }

    // NEED CODE TO ADD BODY, ADD OR REMOVE ANSWERS
    fun addQuiz1(quiz: Quiz1){
        addQuizInit(quiz, 0)
        for(i in 0 until quiz.answers.size){
            if(quiz.ans[i]){
                clickOnTag("QuizAnswerTextField${i}Checkbox", checkFocus = true)
            }
            inputTextOnTag("QuizAnswerTextField${i}TextField", quiz.answers[i], checkFocus = true, withIme = true)
        }
        waitFor(500)
        replaceTextOnTag("QuizMaxAnswerSelectionTextField", quiz.maxAnswerSelection.toString(), checkFocus = true, withIme = true)
        waitFor(500)
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    // CURRENT CODE NEEDS answerDate to be on screen. No swiping around.
    fun addQuiz2(quiz: Quiz2){
        addQuizInit(quiz, 1)
        replaceTextOnTag("YearMonthDropDownYearTextField", quiz.centerDate.year.toString(), checkFocus = true, withIme = true)
        clickOnTag("YearMonthDropDownMonthText")
        waitFor(500)
//        YearMonthDropDownMonth1
        clickOnTag("YearMonthDropDownMonth${quiz.centerDate.monthValue}", useUnmergedTree = true)
        for(i in quiz.answerDate){
            clickOnTag(i.toString())
        }
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    fun addQuiz3(quiz: Quiz3){
        val counts = quiz.answers.size - 5
        addQuizInit(quiz, 2, counts)
        for(i in 0 until quiz.answers.size){
            inputTextOnTag("QuizAnswerTextField${i}", quiz.answers[i], checkFocus = true, withIme = true)
        }
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    fun addQuiz4(quiz: Quiz4){
        val counts = quiz.answers.size - 4
        addQuizInit(quiz, 3, counts)
        for(i in 0 until quiz.answers.size){
            inputTextOnTag("QuizCreatorAnswerLeftTextField${i}", quiz.answers[i], checkFocus = true)
            if(i == quiz.answers.size - 1){
                inputTextOnTag("QuizCreatorAnswerRightTextField${i}", quiz.connectionAnswers[i], checkFocus = true, withIme = true)
            }
            else{
                inputTextOnTag("QuizCreatorAnswerRightTextField${i}", quiz.connectionAnswers[i], checkFocus = true)
            }
        }
        for(i in 0 until quiz.answers.size){
            composeTestRule.onNodeWithTag("QuizCreatorLeftDot$i").performTouchInput {
                val startPosition = this.center
                val startPositionRoot = composeTestRule.onNodeWithTag("QuizCreatorLeftDot$i")
                    .fetchSemanticsNode()
                    .positionInRoot
                val endPosition = composeTestRule.onNodeWithTag("QuizCreatorRightDot${quiz.connectionAnswerIndex[i]}")
                    .fetchSemanticsNode()
                    .positionInRoot
                swipe(startPosition, endPosition-startPositionRoot,
                    durationMillis = 1000
                )
            }
            onIdle()
        }
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    fun setImage(context: Context, image: Int, onImagePicked: (ByteArray) -> Unit = {}) {
        val mockRegistry = object : ActivityResultRegistry() {
            override fun <I : Any?, O : Any?> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                val result = Uri.parse("android.resource://com.asu1.quizzer/${image}")
                dispatchResult(requestCode, result as O)
            }
        }

        val launcher = mockRegistry.register("key", ActivityResultContracts.PickVisualMedia()) { result: Uri? ->
            if (result != null) {
                val byteArray = uriToByteArray(
                    context = context,
                    uri = result,
                    maxWidth = 200.dp,
                    maxHeight = 200.dp,
                )
                if(byteArray != null){
                    onImagePicked(byteArray)
                }
            }
        }
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        onIdle()
    }

    fun setTextStyle(textStyle: List<Int>, targetTag: String){
        val randomFont = Random.nextInt(0, 5)
        val randomColor = Random.nextInt(0, 10)
        for(i in 0 until randomFont){
            clickOnTag(targetTag + "FontFlipperNext", checkFocus = true)
        }
        for(i in 0 until randomColor){
            clickOnTag(targetTag + "ColorFlipperNext", checkFocus = true)
        }
        for(i in 0 until textStyle[2]){
            clickOnTag(targetTag + "BorderFlipperNext", checkFocus = true)
        }
    }

    fun waitUntilTag(tag: String) {
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty()
        }
    }

    fun waitFor(millis: Long) {
        Thread.sleep(millis)
    }

    fun clickOnTag(tag: String, checkFocus: Boolean = false, useUnmergedTree: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag, useUnmergedTree = useUnmergedTree).performScrollTo()
            waitFor(200)
            onIdle()
        }
        composeTestRule.onNodeWithTag(tag, useUnmergedTree = useUnmergedTree).performClick()
        onIdle()
    }

    fun inputTextOnTag(tag: String, text: String, withIme: Boolean = false, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
        }
        composeTestRule.onNodeWithTag(tag).performTextInput(text)
        onIdle()
        if(withIme) {
            onIdle()
            waitFor(200)
            composeTestRule.onNodeWithTag(tag).performImeAction()
            onIdle()
        }
    }

    fun replaceTextOnTag(tag: String, newText: String, withIme: Boolean = false, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
        }
        onIdle()
        composeTestRule.onNodeWithTag(tag).performTextClearance()
        onIdle()
        composeTestRule.onNodeWithTag(tag).performTextInput(newText)
        onIdle()
        if(withIme) {
            onIdle()
            composeTestRule.onNodeWithTag(tag).performImeAction()
            onIdle()
        }
    }

    fun enterTextsOnTag(tag: String, texts: List<String>, withIme: Boolean, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
        }
        texts.forEach {
            onIdle()
            composeTestRule.onNodeWithTag(tag).performTextInput(it)
            onIdle()
            if(withIme) {
                onIdle()
                composeTestRule.onNodeWithTag(tag).performImeAction()
                onIdle()
            }
            onIdle()
        }
    }
}