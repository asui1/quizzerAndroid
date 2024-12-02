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
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.Dp
import androidx.core.app.ActivityOptionsCompat
import androidx.test.espresso.Espresso.onIdle
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.uriToByteArray
import kotlin.random.Random

class QuizLayoutTestUtils(private val composeTestRule: ComposeTestRule) {

    fun addQuizInit(quiz: TestQuiz, quizType: Int, clickAddAnswerNTimes: Int = 0){
        clickOnTag("QuizBuilderScreenAddQuizIconButton")
        clickOnTag("QuizBuilderScreenNewQuizDialogImage${quizType}", true)
        waitFor(200)
        for(i in 0 until clickAddAnswerNTimes){
            clickOnTag("QuizCreatorAddAnswerButton", checkFocus = true)
        }
        inputTextOnTag("QuizQuestionTextField", quiz.question, checkFocus = true)
        replaceTextOnTag("QuizPointTextField", quiz.point.toString(), checkFocus = true)
    }

    fun addQuizBody(quiz: TestQuiz, youtubeLink: String = ""){
        if(quiz.bodyType is BodyType.NONE) return
        if(quiz.bodyType is BodyType.IMAGE) return
        clickOnTag("QuizCreatorAddBodyButton", checkFocus = true)
        when(quiz.bodyType.value){
            1 -> {
                clickOnTag("BodyTypeDialogTextButton")
                inputTextOnTag("QuizCreatorBodyTextField", quiz.bodyText)
            }
            3 -> {
                clickOnTag("BodyTypeDialogYoutubeButton")
                inputTextOnTag("QuizCreatorBodyYoutubeLinkTextField", youtubeLink, withIme = true)
                onIdle()
            }
        }
    }

    // NEED CODE TO ADD BODY, ADD OR REMOVE ANSWERS
    fun addQuiz1(quiz: TestQuiz1, youtubeLink: String = ""){
        val counts = quiz.answers.size - 5
        addQuizInit(quiz, 0, counts)
        addQuizBody(quiz, youtubeLink)
        for(i in 0 until quiz.answers.size){
            if(quiz.ans[i]){
                clickOnTag("QuizAnswerTextField${i}Checkbox", checkFocus = true)
            }
            inputTextOnTag("QuizAnswerTextField${i}TextField", quiz.answers[i], checkFocus = true, withIme = true)
        }
        waitFor(500)
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    // CURRENT CODE NEEDS answerDate to be on screen. No swiping around.
    fun addQuiz2(quiz: TestQuiz2, activity: MainActivity){
        addQuizInit(quiz, 1)
        replaceTextOnTag("YearMonthDropDownYearTextField", quiz.centerDate.year.toString(), checkFocus = true, withIme = true)
        clickOnTag("YearMonthDropDownMonthText")
        waitFor(500)
//        YearMonthDropDownMonth1
        clickOnTag("YearMonthDropDownMonth${quiz.centerDate.monthValue}", useUnmergedTree = true)
        for(i in quiz.answerDate){
            waitFor(100)
            clickOnTag(i.toString())
        }
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    fun addQuiz3(quiz: TestQuiz3, youtubeLink: String = ""){
        val counts = quiz.answers.size - 5
        addQuizInit(quiz, 2, counts)
        addQuizBody(quiz, youtubeLink)
        for(i in 0 until quiz.answers.size){
            inputTextOnTag("QuizAnswerTextField${i}", quiz.answers[i], checkFocus = true, withIme = true)
        }
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    fun addQuiz4(quiz: TestQuiz4, youtubeLink: String = ""){
        val counts = quiz.answers.size - 4
        addQuizInit(quiz, 3, counts)
        addQuizBody(quiz, youtubeLink)
        for(i in 0 until quiz.answers.size){
            inputTextOnTag("QuizCreatorAnswerLeftTextField${i}", quiz.answers[i], checkFocus = true, withIme = true)
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

    fun setImage(context: Context, packageName: String, image: Int, onImagePicked: (ByteArray) -> Unit = {}, width: Dp? = null, height: Dp? = null) {
        val mockRegistry = object : ActivityResultRegistry() {
            override fun <I : Any?, O : Any?> onLaunch(
                requestCode: Int,
                contract: ActivityResultContract<I, O>,
                input: I,
                options: ActivityOptionsCompat?
            ) {
                val result = Uri.parse("android.resource://$packageName/${image}")
                dispatchResult(requestCode, result as O)
            }
        }

        val launcher = mockRegistry.register("key", ActivityResultContracts.PickVisualMedia()) { result: Uri? ->
            if (result != null) {
                val byteArray = uriToByteArray(
                    context = context,
                    uri = result,
                    maxWidth = width,
                    maxHeight = height,
                )
                if(byteArray != null){
                    Logger().debug("Image Picked ${byteArray.size}")
                    onImagePicked(byteArray)
                }
            }
        }
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        onIdle()
        waitFor(300)
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
            onIdle()
            waitFor(50)
        }
        composeTestRule.onNodeWithTag(tag, useUnmergedTree = useUnmergedTree).performClick()
        onIdle()
        waitFor(50)
    }

    fun inputTextOnTag(tag: String, text: String, withIme: Boolean = false, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
            waitFor(50)
        }
        composeTestRule.onNodeWithTag(tag).performTextInput(text)
        onIdle()
        waitFor(50)
        if(withIme) {
            onIdle()
            composeTestRule.onNodeWithTag(tag).performImeAction()
            onIdle()
            waitFor(50)
        }
    }

    fun replaceTextOnTag(tag: String, newText: String, withIme: Boolean = false, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
            waitFor(50)
        }
        onIdle()
        composeTestRule.onNodeWithTag(tag).performTextClearance()
        onIdle()
        waitFor(50)
        composeTestRule.onNodeWithTag(tag).performTextInput(newText)
        onIdle()
        waitFor(50)
        if(withIme) {
            onIdle()
            composeTestRule.onNodeWithTag(tag).performImeAction()
            onIdle()
            waitFor(50)
        }
    }

    fun enterTextsOnTag(tag: String, texts: List<String>, withIme: Boolean, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
            waitFor(50)
        }
        texts.forEach {
            onIdle()
            composeTestRule.onNodeWithTag(tag).performTextInput(it)
            onIdle()
            waitFor(50)
            if(withIme) {
                onIdle()
                composeTestRule.onNodeWithTag(tag).performImeAction()
                onIdle()
                waitFor(50)
            }
            onIdle()
        }
    }
}