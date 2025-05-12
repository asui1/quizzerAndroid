package com.asu1.quizzer.quizCreateUtils

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHasClickAction
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
import com.asu1.models.quizRefactor.ConnectItemsQuiz
import com.asu1.models.quizRefactor.DateSelectionQuiz
import com.asu1.models.quizRefactor.FillInBlankQuiz
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.quizRefactor.ShortAnswerQuiz
import com.asu1.models.quizRefactor.connectItemsQuizDefaultSize
import com.asu1.models.quizRefactor.defaultMultipleChoiceQuizSize
import com.asu1.models.quizRefactor.reorderQuizDefaultSize
import com.asu1.models.serializers.BodyType
import com.asu1.utils.Logger
import com.asu1.utils.uriToByteArray
import kotlinx.coroutines.runBlocking

class QuizLayoutTestUtils(private val composeTestRule: ComposeTestRule) {

    fun addNewQuiz(quizType: Int){
        clickOnTag("QuizBuilderScreenAddQuizIconButton")
        clickOnTag("QuizBuilderScreenNewQuizDialogImage${quizType}", true)
        waitFor(200)
    }

    fun addQuiz(quiz: Quiz){
        addQuizBody(quiz)
        inputTextOnTag("QuizQuestionTextField", quiz.question, withIme = true, checkFocus = true)
        when(quiz){
            is ConnectItemsQuiz -> addConnectItemsQuiz(quiz)
            is DateSelectionQuiz -> addDateSelectionQuiz(quiz)
            is FillInBlankQuiz -> addFillInBlankQuiz(quiz)
            is MultipleChoiceQuiz -> addMultipleChoiceQuiz(quiz)
            is ReorderQuiz -> addReorderQuiz(quiz)
            is ShortAnswerQuiz -> addShortAnswerQuiz(quiz)
        }
        clickOnTag("QuizCreatorSaveButton")
        waitFor(1000)
    }

    fun addFillInBlankQuiz(quiz: FillInBlankQuiz){
        setAnswer(
            answerSIze = quiz.correctAnswers.size,
            defaultSize = 0,
        )
        replaceTextOnTag(
            tag = "FillInBlankQuizTextFieldBody",
            newText = quiz.rawText,
            checkFocus = true,
        )
        for(i in quiz.correctAnswers.indices){
            inputTextOnTag(
                tag = "FillInBlankQuizTextFieldAnswer$i",
                text = quiz.correctAnswers[i],
                withIme = true,
            )
        }
    }

    fun addShortAnswerQuiz(quiz: ShortAnswerQuiz){
        inputTextOnTag(
            tag = "ShortAnswerQuizAnswerTextField",
            text = quiz.answer,
            withIme = true,
        )
    }

    fun setAnswer(
        answerSIze: Int,
        defaultSize: Int,
        removeTag: String = "QuizAnswerTextField0delete",
        addTag: String = "QuizCreatorAddAnswerButton",){
        if(answerSIze < defaultSize){
            repeat(defaultSize - answerSIze) {
                clickOnTag(removeTag, checkFocus = true)
            }
        }else if(answerSIze > defaultSize){
            repeat(answerSIze - defaultSize) {
                clickOnTag(addTag, checkFocus = true)
            }
        }
    }

    fun addQuizBody(quiz: Quiz){
        if(quiz.bodyValue is BodyType.NONE) return
        if(quiz.bodyValue is BodyType.IMAGE) TODO("MAKE UP FOR IMAGE")
        clickOnTag("QuizCreatorAddBodyButton", checkFocus = true)
        when(quiz.bodyValue.value){
            1 -> {
                clickOnTag("BodyTypeDialogTEXTButton")
                onIdle()
                waitFor(100)
                inputTextOnTag("QuizCreatorBodyTextField", (quiz.bodyValue as BodyType.TEXT).bodyText)
            }
            3 -> {
                clickOnTag("BodyTypeDialogYOUTUBEButton")
                inputTextOnTag("QuizCreatorBodyYoutubeLinkTextField", (quiz.bodyValue as BodyType.YOUTUBE).youtubeId, withIme = true)
                onIdle()
            }
            4 -> {
                clickOnTag("BodyTypeDialogCODEButton")
                onIdle()
                waitFor(100)
                inputTextOnTag("QuizCreatorBodyTextField", (quiz.bodyValue as BodyType.CODE).code)
            }
        }
    }

    fun addMultipleChoiceQuiz(quiz: MultipleChoiceQuiz){
        setAnswer(
            answerSIze = quiz.options.size,
            defaultSize = defaultMultipleChoiceQuizSize,
        )
        composeTestRule
            .onNodeWithTag("MultipleChoiceQuizCreatorLazyColumn")
            .performScrollToNode(hasTestTag("QuizAnswerTextField0Checkbox"))
        onIdle()
        waitFor(100)
        for(i in 0 until quiz.options.size){
            if(quiz.correctFlags[i]){
                clickOnTag("QuizAnswerTextField${i}Checkbox", checkFocus = true)
            }
            inputTextOnTag("QuizAnswerTextField${i}TextField", quiz.options[i], checkFocus = true, withIme = true)
        }
    }

    fun addDateSelectionQuiz(quiz: DateSelectionQuiz){
        replaceTextOnTag("YearMonthDropDownYearTextField", quiz.centerDate.year.toString(), checkFocus = true, withIme = true)
        onIdle()
        waitFor(500)
        onIdle()
        clickOnTag("YearMonthDropDownMonth", useUnmergedTree = true)
        waitFor(500)
        clickOnTag("YearMonthDropDownMonth${quiz.centerDate.monthValue}", useUnmergedTree = true)
        for(i in quiz.answerDate){
            onIdle()
            waitFor(100)
            clickOnTag(i.toString())
        }
    }

    fun addReorderQuiz(quiz: ReorderQuiz){
        setAnswer(
            answerSIze = quiz.answers.size,
            defaultSize = reorderQuizDefaultSize)
        for(i in 0 until quiz.answers.size){
            inputTextOnTag("QuizAnswerTextField${i}", quiz.answers[i], checkFocus = true, withIme = true)
        }
    }

    fun addConnectItemsQuiz(quiz: ConnectItemsQuiz){
        setAnswer(
            answerSIze = quiz.answers.size,
            defaultSize = connectItemsQuizDefaultSize,
        )
        setAnswer(
            answerSIze = quiz.connectionAnswers.size,
            defaultSize = connectItemsQuizDefaultSize,
            removeTag = "QuizCreatorRemoveAnswerButton",
            addTag = "QuizCreatorAddAnswerButtonRight",
        )
        for(i in 0 until quiz.answers.size){
            inputTextOnTag("QuizCreatorAnswerLeftTextField${i}", quiz.answers[i], checkFocus = true, withIme = true)
            if(i == quiz.answers.size - 1){
                inputTextOnTag("QuizCreatorAnswerRightTextField${i}", quiz.connectionAnswers[i], checkFocus = true, withIme = true)
            }
            else{
                inputTextOnTag("QuizCreatorAnswerRightTextField${i}", quiz.connectionAnswers[i], checkFocus = true)
            }
        }

//        update with Offset(537.7, 981.6) 540 -> 578 981 -> 995 x -> + 38, y + 15
//        update with Offset(545.0, 1366.1) 1366 -> 1403
//        update with Offset(547.4, 1203.9) 1203 -> 1200
//        update with Offset(537.7, 1593.6) 1593.6 -> 1607.9
//        SnapshotStateList(value=[Offset(578.9, 995.9), Offset(578.9, 1199.9), Offset(578.9, 1403.9), Offset(578.9, 1607.9)])@54832633
        for(i in 0 until quiz.answers.size){
            composeTestRule.onNodeWithTag("QuizCreatorLeftDot$i").performTouchInput {
                val startPosition = this.center
                val startPositionRoot = composeTestRule.onNodeWithTag("QuizCreatorLeftDot$i")
                    .fetchSemanticsNode()
                    .positionInRoot
                val connectionAnswerIndex = quiz.connectionAnswerIndex[i] ?: i
                val endPosition = composeTestRule.onNodeWithTag("QuizCreatorRightDot${connectionAnswerIndex}")
                    .fetchSemanticsNode()
                    .positionInRoot + Offset(40f, 15f)
                swipe(startPosition, endPosition-startPositionRoot,
                    durationMillis = 1000
                )
            }
            onIdle()
            waitFor(500)
        }
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
        clickOnTag(targetTag + "StyleTab")
        repeat(textStyle[0]) {
            clickOnTag("SetTextStyleFontFlipperNext", checkFocus = true)
        }
        repeat(textStyle[1]) {
            clickOnTag("SetTextStyleColorFlipperNext", checkFocus = true)
        }
        repeat(textStyle[2]) {
            clickOnTag("SetTextStyleBorderFlipperNext", checkFocus = true)
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
            assertHasClickAction()
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
        val node = composeTestRule
            .onNodeWithTag(tag, useUnmergedTree = useUnmergedTree)
            .assertExists("$tag not exists.")
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