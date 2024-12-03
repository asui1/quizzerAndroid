package com.asu1.quizzer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import androidx.test.platform.app.InstrumentationRegistry
import com.asu1.quizzer.datacreation.fakertestData
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.UserViewModel
import org.junit.Before
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

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val testUtils = QuizLayoutTestUtils(composeTestRule)

    @Test
    fun testQuizCreateUpload() {
        composeTestRule.waitForIdle()
        val activity = composeTestRule.activity
        val context = activity.applicationContext
        val testQuizData = fakertestData
        val instContext = InstrumentationRegistry.getInstrumentation().context

        //Move to Create Quiz Layout
        testUtils.waitUntilTag("MainScreenCreateQuiz")
        testUtils.waitFor(1500)

        //LOGIN
        val userViewModel = ViewModelProvider(activity)[UserViewModel::class.java]
        userViewModel.logIn("whwkd122@gmail.com", "https://lh3.googleusercontent.com/a/ACg8ocJfoHUjigfS1fBoyEPXLv1pusBvf7WTJAfUoQV8YhPjr4Whq98=s96-c")
        onIdle()
        testUtils.waitFor(1500)
        testUtils.clickOnTag("MainScreenCreateQuiz")

        //AGREE POLICY
        testUtils.waitUntilTag("QuizLayoutBuilderAgreePolicyButton")
        testUtils.clickOnTag("QuizLayoutBuilderAgreePolicyButton")

        //SET TITLE
        testUtils.waitUntilTag("QuizLayoutTitleTextField")
        testUtils.inputTextOnTag("QuizLayoutTitleTextField", testQuizData.title)

        //Set Quiz Description
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.inputTextOnTag("QuizLayoutBuilderDescriptionTextField", testQuizData.description)

        //SET TAGS
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.enterTextsOnTag("TagSetterTextField", testQuizData.tags.toList(), true)

        //SET IMAGE
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        val quizLayoutViewModel = ViewModelProvider(activity)[QuizLayoutViewModel::class.java]
        testUtils.setImage(context, instContext.packageName, testQuizData.titleImage, onImagePicked = { image ->
            quizLayoutViewModel.setQuizImage(image)
        }, width = 200.dp, height = 200.dp)
        onIdle()
        testUtils.waitFor(100)

        //Set ColorScheme
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimaryColor")
        val colorInt = testQuizData.colorInt
        val primaryColor = if(testQuizData.primaryColor == "") primaryColors[colorInt] else testQuizData.primaryColor
        testUtils.replaceTextOnTag("QuizLayoutSetColorSchemeTextFieldPrimaryColor", primaryColor, true)
        testUtils.waitFor(100)
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimaryColor", true)
        testUtils.waitFor(100)
        testUtils.clickOnTag("QuizLayoutBuilderColorSchemeGenWithPrimaryColor")
        testUtils.waitFor(100)

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

        for(i in 0 until testQuizData.quizzes.size){
            val quiz = testQuizData.quizzes[i]
            when(quiz){
                is TestQuiz1 -> {
                    testUtils.addQuiz1(quiz, testQuizData.bodyYoutubeLinks[i])
                }
                is TestQuiz2 -> {
                    testUtils.addQuiz2(quiz, activity)
                }
                is TestQuiz3 -> {
                    testUtils.addQuiz3(quiz, testQuizData.bodyYoutubeLinks[i])
                }
                is TestQuiz4 -> {
                    testUtils.addQuiz4(quiz, testQuizData.bodyYoutubeLinks[i])
                }
            }
            if(quiz.bodyType is BodyType.IMAGE){
                testUtils.setImage(context, instContext.packageName, testQuizData.bodyImages[i], onImagePicked = { image ->
                    quizLayoutViewModel.setQuizBodyImage(image)
                })
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
        Espresso.pressBack()
        onIdle()

        testUtils.clickOnTag("DesignScoreCardSetBackgroundImageButton")
        testUtils.waitFor(100)
        testUtils.clickOnTag("DesignScoreCardBaseImage${testQuizData.backgroundImageIndex}", checkFocus = true)

        //SET COLOR FILTER FOR BACKGROUND BASE IMAGE
        testUtils.waitUntilTag("DesignScoreCardSetColorButton0")
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColorButton0")
        testUtils.waitFor(100)
        val color1 = if(testQuizData.backgroundColorFilter == "") backgroundColorFilters[colorInt] else testQuizData.backgroundColorFilter
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", color1, true)
        testUtils.waitFor(100)
        Espresso.pressBack()
        onIdle()

        //SET COLOR FOR EFFECT FILTER
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColorButton1")
        testUtils.waitFor(100)
        val color2 = if(testQuizData.effectColor == "") effectColorFilters[colorInt] else testQuizData.effectColor
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", color2, true)
        testUtils.waitFor(100)
        Espresso.pressBack()
        onIdle()

        testUtils.clickOnTag("DesignScoreCardAnimationButton")
        testUtils.waitFor(100)
        testUtils.clickOnTag("DesignScoreCardAnimationButton${testQuizData.effectIndex}", useUnmergedTree = true)
        onIdle()
        testUtils.waitFor(100)



        //UPLOAD
        testUtils.clickOnTag("DesignScoreCardUploadButton")
        onIdle()

        onIdle()
        testUtils.waitFor(1000)
        onIdle()

    }
}
