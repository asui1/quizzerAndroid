package com.asu1.quizzer

import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso.onIdle
import androidx.test.platform.app.InstrumentationRegistry
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.models.serializers.BodyType
import com.asu1.quiz.viewmodel.quizLayout.QuizGeneralViewModel
import com.asu1.quiz.viewmodel.quizLayout.ScoreCardViewModel
import com.asu1.quizzer.quizCreateUtils.QuizLayoutTestUtils
import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.TestQuiz2
import com.asu1.quizzer.quizCreateUtils.TestQuiz3
import com.asu1.quizzer.quizCreateUtils.TestQuiz4
import com.asu1.quizzer.quizCreateUtils.codingQuestions.pythonUtilityQuizDataKo
import org.junit.Rule
import org.junit.Test

val primaryColors: List<String> = listOf(
    "FFBBDEFB", // Light Blue
    "FFFFCDD2", // Light Red
    "FFC8E6C9", // Light Green
    "FFFFF9C4", // Light Yellow
    "FFE1BEE7", // Light Purple
    "FFFFE0B2", // Light Orange
    "FFB3E5FC", // Light Cyan
    "FFD7CCC8", // Light Brown
    "FFB2DFDB", // Light Teal
    "FFFFF3E0"  // Light Amber
)
val backgroundColorFilters: List<String> = listOf(
    "FF266489",
    "FF8e4954",
    "FF326940",
    "FFede68c",
    "FF784f83",
    "FFffdeac",
    "FFbbe9ff",
    "FFffdbcc",
    "FF006a66",
    "ffffdf99",
)

val effectColorFilters: List<String> = listOf(
    "FF64597b",
    "FF785831",
    "FF3a656e",
    "FFc1ecd5",
    "FF82524e",
    "FFd0eabf",
    "FFe2dfff",
    "FFede4a9",
    "FF49607b",
    "FFcbebc6",
)

val textColors: List<String> = listOf(
    "FFFFFFFF",
    "FFFFFFFF",
    "FFFFFFFF",
    "FF1e1c00",
    "ffffffff",
    "FF281900",
    "FF001f29",
    "FF351000",
    "ffffffff",
    "ff251a00",
)

// Default goes with addbody:true, upload: false
const val addBody = true
const val upLoad = true

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val testUtils = QuizLayoutTestUtils(composeTestRule)

    @Test
    fun testQuizCreateUpload() {
        composeTestRule.waitForIdle()
        val activity = composeTestRule.activity
        val context = activity.applicationContext
        val testQuizData = pythonUtilityQuizDataKo
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
        testUtils.waitUntilTag("QuizLayoutTitleTextField")
        testUtils.inputTextOnTag("QuizLayoutTitleTextField", testQuizData.title, withIme = false)
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")

        testUtils.waitFor(300)
        //Set Quiz Description
        testUtils.inputTextOnTag("QuizLayoutBuilderDescriptionTextField", testQuizData.description, withIme = false)
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")

        //SET TAGS
        testUtils.waitFor(300)
        testUtils.enterTextsOnTag("TagSetterTextField", testQuizData.tags.toList(), true)

        //SET IMAGE
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.waitFor(500)
        val quizGeneralViewModel = ViewModelProvider(activity)[QuizGeneralViewModel::class.java]
        testUtils.setImage(context, instContext.packageName, testQuizData.titleImage, onImagePicked = { image ->
            quizGeneralViewModel.updateQuizImage(image)
        }, width = 200.dp, height = 200.dp)
        onIdle()
        testUtils.waitFor(100)

        //Set ColorScheme
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimaryColor")
        val colorInt = testQuizData.colorInt
        val primaryColor = if(testQuizData.primaryColor == "") primaryColors[colorInt] else testQuizData.primaryColor
        testUtils.replaceTextOnTag("QuizLayoutSetColorSchemeTextFieldPrimaryColor", primaryColor, true)
        testUtils.clickOnTag("QuizLayoutBuilderColorSchemeGenWithPrimaryColor")
        testUtils.waitFor(1000)

        // NEED TESTING FROM HERE
        //Set TextStyles
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.setTextStyle(testQuizData.questionTextStyle, "setTextStyleQuestion")
        testUtils.setTextStyle(testQuizData.bodyTextStyle, "setTextStyleBody")
        testUtils.setTextStyle(testQuizData.answerTextStyle, "setTextStyleAnswer")
        onIdle()
        testUtils.waitFor(100)

        //QUIZ BUILDER
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.waitFor(300)

        if(addBody) {
            for(i in testQuizData.quizzes.size -1 downTo 0){
                val quiz = testQuizData.quizzes[i]
                when (quiz) {
                    is TestQuiz1 -> {
                        testUtils.addQuiz1(quiz, testQuizData.bodyYoutubeLinks[i])
                    }

                    is TestQuiz2 -> {
                        testUtils.addQuiz2(quiz)
                    }

                    is TestQuiz3 -> {
                        testUtils.addQuiz3(quiz, testQuizData.bodyYoutubeLinks[i])
                    }

                    is TestQuiz4 -> {
                        testUtils.addQuiz4(quiz, testQuizData.bodyYoutubeLinks[i])
                    }
                }
                if (quiz.bodyType is BodyType.IMAGE) {
                    testUtils.setImage(
                        context,
                        instContext.packageName,
                        testQuizData.bodyImages[i],
                        onImagePicked = { image ->
                        })
                }
            }
        }

        //DESIGN SCORECARD
        testUtils.clickOnTag("QuizBuilderScreenProceedButton")
        testUtils.waitFor(300)

        testUtils.clickOnTag("DesignScoreCardSetTextColorButton")
        testUtils.waitFor(100)
        val textColor = if(testQuizData.textColor == "") textColors[colorInt] else testQuizData.textColor
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", textColor, true)
        testUtils.waitFor(100)
        onIdle()

        testUtils.clickOnTag("DesignScoreCardSetBackgroundImageButton")
        testUtils.waitFor(100)
        composeTestRule
            .onNodeWithTag("BaseImagePickerLazyVerticalGrid")
            .performScrollToNode(hasTestTag("DesignScoreCardBaseImage${testQuizData.backgroundImageIndex}"))
        onIdle()
        testUtils.clickOnTag("DesignScoreCardBaseImage${testQuizData.backgroundImageIndex}", checkFocus = true, useUnmergedTree = true)

        //SET COLOR FILTER FOR BACKGROUND BASE IMAGE
        testUtils.waitUntilTag("DesignScoreCardSetColorButton0")
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColorButton0")
        testUtils.waitFor(100)
        val color1 = if(testQuizData.backgroundColorFilter == "") backgroundColorFilters[colorInt] else testQuizData.backgroundColorFilter
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", color1, true)
        testUtils.waitFor(100)
        onIdle()

        //SET COLOR FOR EFFECT FILTER
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColorButton1")
        testUtils.waitFor(100)
        val color2 = if(testQuizData.effectColor == "") effectColorFilters[colorInt] else testQuizData.effectColor
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", color2, true)
        testUtils.waitFor(100)
        onIdle()

        testUtils.clickOnTag("DesignScoreCardAnimationButton")
        testUtils.waitFor(100)
        testUtils.clickOnTag("DesignScoreCardAnimationButton${testQuizData.effectIndex}", useUnmergedTree = true)
        onIdle()
        testUtils.waitFor(100)

        if(testQuizData.overlayImage != 0){
            testUtils.setImage(context, instContext.packageName, testQuizData.overlayImage, onImagePicked = { image ->
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
