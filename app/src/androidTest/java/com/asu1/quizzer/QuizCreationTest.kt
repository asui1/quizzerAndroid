package com.asu1.quizzer

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onIdle
import androidx.test.platform.app.InstrumentationRegistry
import com.asu1.customComposable.colorPicker.toRgbHex
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.quizzer.quizCreateUtils.QuizLayoutTestUtils
import com.asu1.quizzer.quizCreateUtils.datacreation.iuQuizBundle1
import org.junit.Rule
import org.junit.Test


// Default goes with addbody:true, upload: false
const val addBody = true
const val upLoad = false

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val testUtils = QuizLayoutTestUtils(composeTestRule)

    @Test
    fun testQuizCreateUpload() {
        composeTestRule.waitForIdle()
        val activity = composeTestRule.activity
        val context = activity.applicationContext
        val testQuizBundle = iuQuizBundle1
        val instContext = InstrumentationRegistry.getInstrumentation().context

        //Move to Create Quiz Layout
        testUtils.waitUntilTag("MainScreenCreateQuiz")
        testUtils.waitFor(2000)
        val scoreCardViewModel = ViewModelProvider(activity)[ScoreCardViewModel::class.java]

        //LOGIN
        val userViewModel = ViewModelProvider(activity)[UserViewModel::class.java]
        userViewModel.login("whwkd122@gmail.com", "")
        onIdle()
        testUtils.waitFor(1500)
        testUtils.clickOnTag("MainScreenCreateQuiz")

        //AGREE POLICY
        testUtils.waitUntilTag("QuizLayoutBuilderAgreePolicyButton")
        testUtils.clickOnTag("QuizLayoutBuilderAgreePolicyButton")

        //SET TITLE
        testUtils.waitFor(500)
        composeTestRule.mainClock.advanceTimeBy(4_000L)
        onIdle()
        testUtils.waitFor(500)
        testUtils.waitUntilTag("QuizLayoutTitleTextField")
        testUtils.inputTextOnTag("QuizLayoutTitleTextField", testQuizBundle.data.title, withIme = false)
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")

        testUtils.waitFor(500)
        //Set Quiz Description
        testUtils.inputTextOnTag("QuizLayoutBuilderDescriptionTextField", testQuizBundle.data.description, withIme = false)
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")

        //SET TAGS
        testUtils.waitFor(500)
        testUtils.enterTextsOnTag("TagSetterTextField", testQuizBundle.data.tags.toList(), true)

        //SET IMAGE
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.waitFor(500)
        val quizGeneralViewModel = ViewModelProvider(activity)[QuizGeneralViewModel::class.java]
        testUtils.setImage(context, instContext.packageName, testQuizBundle.titleImage, onImagePicked = { image ->
            quizGeneralViewModel.updateQuizImage(image)
        }, width = 200.dp, height = 200.dp)
        onIdle()
        testUtils.waitFor(100)

        //Set ColorScheme
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        onIdle()
        testUtils.waitFor(500)
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimary")
        onIdle()
        testUtils.waitFor(500)
        val primaryColor = testQuizBundle.theme.colorScheme.primary.toRgbHex()
        println("PrimaryColor Hex: $primaryColor")
        testUtils.replaceTextOnTag("QuizLayoutSetColorSchemeTextField", primaryColor, withIme = true)
        onIdle()
        composeTestRule.mainClock.advanceTimeBy(2_000L)
        testUtils.waitFor(1000)
        onIdle()
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimary")
        testUtils.waitFor(1000)
        onIdle()
        testUtils.clickOnTag("QuizLayoutBuilderColorSchemeGenWithPrimaryColor")
        testUtils.waitFor(1000)

        //Set TextStyles
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.setTextStyle(testQuizBundle.theme.questionTextStyle, "setTextStyleQuestion")
        testUtils.setTextStyle(testQuizBundle.theme.bodyTextStyle, "setTextStyleBody")
        testUtils.setTextStyle(testQuizBundle.theme.answerTextStyle, "setTextStyleAnswer")
        onIdle()
        testUtils.waitFor(100)

        //QUIZ BUILDER
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.waitFor(300)

        if(addBody) {
            for(i in testQuizBundle.quizzes.size -1 downTo 0){
                val quiz = testQuizBundle.quizzes[i]
                testUtils.addNewQuiz(quiz.quizType.value)
                testUtils.addQuiz(quiz)
            }
        }
        return

        //DESIGN SCORECARD
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.waitFor(300)

        testUtils.clickOnTag("DesignScoreCardSetTextColorButton")
        testUtils.waitFor(100)
        val textColor = testQuizBundle.scoreCard.textColor.toRgbHex()
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", textColor, true)
        testUtils.waitFor(100)
        onIdle()

        testUtils.clickOnTag("DesignScoreCardSetBackgroundImageButton")
        testUtils.waitFor(100)
        composeTestRule
            .onNodeWithTag("BaseImagePickerLazyVerticalGrid")
            .performScrollToNode(hasTestTag("DesignScoreCardBaseImage${testQuizBundle.scoreCard.background.backgroundBase.ordinal}"))
        onIdle()
        testUtils.clickOnTag("DesignScoreCardBaseImage${testQuizBundle.scoreCard.background.backgroundBase.ordinal}", checkFocus = true, useUnmergedTree = true)

        //SET COLOR FILTER FOR BACKGROUND BASE IMAGE
        testUtils.waitUntilTag("DesignScoreCardSetColorButton0")
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColorButton0")
        testUtils.waitFor(100)
        val backgroundColor = testQuizBundle.scoreCard.background.color.toRgbHex()
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", backgroundColor, true)
        testUtils.waitFor(100)
        onIdle()

        //SET COLOR FOR EFFECT FILTER
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColorButton1")
        testUtils.waitFor(100)
        val effectColor = testQuizBundle.scoreCard.background.color2.toRgbHex()
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", effectColor, true)
        testUtils.waitFor(100)
        onIdle()

        testUtils.clickOnTag("DesignScoreCardAnimationButton")
        testUtils.waitFor(100)
        testUtils.clickOnTag("DesignScoreCardAnimationButton${testQuizBundle.scoreCard.background.effect.ordinal}", useUnmergedTree = true)
        onIdle()
        testUtils.waitFor(100)

        if(testQuizBundle.overlayImage != 0){
            testUtils.setImage(context, instContext.packageName, testQuizBundle.overlayImage, onImagePicked = { image ->
                scoreCardViewModel.updateOverLayImage(image)
            })
        }
        onIdle()
        testUtils.waitFor(2000)

        //UPLOAD
        if(upLoad) testUtils.clickOnTag("DesignScoreCardUploadButton")
        onIdle()

        onIdle()
        testUtils.waitFor(5000)
        onIdle()

    }
}
