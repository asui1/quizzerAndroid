package com.asu1.quizzer.quizCreateUtils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.Dp
import androidx.core.app.ActivityOptionsCompat
import androidx.test.espresso.Espresso.onIdle
import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.util.uriToByteArray
import com.asu1.utils.Logger
import kotlinx.coroutines.runBlocking

class QuizLayoutTestUtils(private val composeTestRule: ComposeTestRule) {

    fun addQuizInit(quiz: TestQuiz, quizType: Int, clickAddAnswerNTimes: Int = 0, clickAddRightAnswerNTimes: Int = 0){
        clickOnTag("QuizBuilderScreenAddQuizIconButton")
        clickOnTag("QuizBuilderScreenNewQuizDialogImage${quizType}", true)
        waitFor(200)
        if(quiz is TestQuiz4) addAnswer(clickAddAnswerNTimes, clickAddRightAnswerNTimes)
        else addAnswer(clickAddAnswerNTimes)
        inputTextOnTag("QuizQuestionTextField", quiz.question, checkFocus = true)
        replaceTextOnTag("QuizPointTextField", quiz.point.toString(), checkFocus = true)
    }

    fun addAnswer(clickAddAnswerNTimes: Int){
        for(i in 0 until clickAddAnswerNTimes){
            clickOnTag("QuizCreatorAddAnswerButton", checkFocus = true)
        }
    }

    fun addAnswer(clickAddAnswerLeftNTimes: Int, clickAddAnswerRightNTimes: Int){
        for(i in 0 until clickAddAnswerLeftNTimes){
            clickOnTag("QuizCreatorAddAnswerLeftButton", checkFocus = true)
        }
        for(i in 0 until clickAddAnswerRightNTimes){
            clickOnTag("QuizCreatorAddAnswerRightButton", checkFocus = true)
        }
    }

    fun addQuizBody(quiz: TestQuiz, youtubeLink: String = ""){
        if(quiz.bodyType is BodyType.NONE) return
        if(quiz.bodyType is BodyType.IMAGE) return
        clickOnTag("QuizCreatorAddBodyButton", checkFocus = true)
        when(quiz.bodyType.value){
            1 -> {
                clickOnTag("BodyTypeDialogTEXTButton")
                onIdle()
                waitFor(100)
                inputTextOnTag("QuizCreatorBodyTextField", (quiz.bodyType as BodyType.TEXT).bodyText)
            }
            3 -> {
                clickOnTag("BodyTypeDialogYOUTUBEButton")
                inputTextOnTag("QuizCreatorBodyYoutubeLinkTextField", youtubeLink, withIme = true)
                onIdle()
            }
            4 -> {
                clickOnTag("BodyTypeDialogCODEButton")
                onIdle()
                waitFor(100)
                inputTextOnTag("QuizCreatorBodyTextField", (quiz.bodyType as BodyType.CODE).code)
            }
        }
    }

    // NEED CODE TO ADD BODY, ADD OR REMOVE ANSWERS
    fun addQuiz1(quiz: TestQuiz1, youtubeLink: String = ""){
        val counts = quiz.answers.size - 5
        addQuizInit(quiz, 0, counts)
        addQuizBody(quiz, youtubeLink)
        composeTestRule
            .onNodeWithTag("Quiz1CreatorLazyColumn")
            .performScrollToNode(hasTestTag("QuizAnswerTextField0Checkbox"))
        onIdle()
        waitFor(100)
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
    fun addQuiz2(quiz: TestQuiz2){
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
        val leftCounts = quiz.answers.size - 4
        val rightCounts = quiz.connectionAnswers.size - 4
        addQuizInit(quiz, 3, leftCounts, rightCounts)
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

    @Suppress("UNCHECKED_CAST")
    fun setImage(context: Context, packageName: String, image: Int, onImagePicked: (Bitmap) -> Unit = {}, width: Dp? = null, height: Dp? = null) {
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
                runBlocking {
                    val bitmap = uriToByteArray(
                        context = context,
                        uri = result,
                        maxWidth = width,
                        maxHeight = height,
                    )
                    if(bitmap != null){
                        Logger.debug("Image Picked ${bitmap.width}")
                        onImagePicked(bitmap)
                    }
                }
            }
        }
        launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        onIdle()
        waitFor(300)
    }

    fun setTextStyle(textStyle: List<Int>, targetTag: String){
        for(i in 0 until textStyle[0]){
            clickOnTag(targetTag + "FontFlipperNext", checkFocus = true)
        }
        for(i in 0 until textStyle[1]){
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
        interactWithNode(tag, checkFocus, useUnmergedTree) {
            performClick()
        }
    }

    fun inputTextOnTag(tag: String, text: String, withIme: Boolean = false, checkFocus: Boolean = false) {
        interactWithNode(tag, checkFocus) {
            performTextInput(text)
            if (withIme) performImeAction()
        }
    }

    fun replaceTextOnTag(tag: String, newText: String, withIme: Boolean = false, checkFocus: Boolean = false) {
        interactWithNode(tag, checkFocus) {
            performTextClearance()
            onIdle()
            waitFor(500)
            performTextInput(newText)
            onIdle()
            waitFor(500)
            // Introduce a small delay before performing the IME action
            if (withIme) {
                onIdle()
                waitFor(500)
                performImeAction()
                onIdle()
            }
        }
    }

    private fun interactWithNode(tag: String, checkFocus: Boolean = false, useUnmergedTree: Boolean = false, action: SemanticsNodeInteraction.() -> Unit) {
        onIdle()
        val node = composeTestRule.onNodeWithTag(tag, useUnmergedTree = useUnmergedTree)
        if (checkFocus && !node.isDisplayed()) {
            node.performScrollTo()
            onIdle()
        }
        node.action()
        onIdle()
    }

    fun enterTextsOnTag(tag: String, texts: List<String>, withIme: Boolean, checkFocus: Boolean = false) {
        onIdle()
        if(checkFocus) {
            composeTestRule.onNodeWithTag(tag).performScrollTo()
            onIdle()
            waitFor(100)
        }
        texts.forEach {
            onIdle()
            composeTestRule.onNodeWithTag(tag).performTextInput(it)
            onIdle()
            waitFor(100)
            if(withIme) {
                onIdle()
                composeTestRule.onNodeWithTag(tag).performImeAction()
                onIdle()
                waitFor(100)
            }
            onIdle()
        }
    }
}