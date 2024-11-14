package com.asu1.quizzer

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onIdle
import com.asu1.quizzer.viewModels.QuizData
import com.asu1.quizzer.viewModels.UserViewModel
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

val quizData = QuizData(
    title = "Quiz Test Title",
    image = byteArrayOf(),
    description = "This is description for Testing Quiz",
    tags= setOf("Test", "Quiz", "아슈"),
    shuffleQuestions = false,
    creator = "GUEST",
    uuid = null
)
val titleImage: Int = R.drawable.question1
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
val gradientColors1: List<String> = listOf(
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

val gradientColors2: List<String> = listOf(
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

val questionTextStyle : List<Int> = listOf(0, 0, 0)
val bodyTextStyle : List<Int> = listOf(0, 0, 0)
val answerTextStyle : List<Int> = listOf(0, 0, 0)
val shaders: List<String> = listOf(
    "Left Bottom",
    "Bottom",
    "Center",
    "Repeat",
    "Vertical Wave",
    "Horizontal Wave"
)

class MyComposeTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val testUtils = QuizLayoutTestUtils(composeTestRule)

    @Test
    fun testAppLaunchAndStabilize() {
        composeTestRule.waitForIdle()
        val activity = composeTestRule.activity
        val context = activity.applicationContext

        //Move to Create Quiz Layout
        testUtils.waitUntilTag("MainScreenCreateQuiz")
        testUtils.waitFor(1500)

        //LOGIN
        val userViewModel = ViewModelProvider(activity)[UserViewModel::class.java]
        userViewModel.logIn("whwkd122@gmail.com", "https://lh3.googleusercontent.com/a/ACg8ocJfoHUjigfS1fBoyEPXLv1pusBvf7WTJAfUoQV8YhPjr4Whq98=s96-c")
        onIdle()
        testUtils.waitFor(2000)
        testUtils.clickOnTag("MainScreenCreateQuiz")

        //AGREE POLICY
        testUtils.waitUntilTag("QuizLayoutBuilderAgreePolicyButton")
        testUtils.clickOnTag("QuizLayoutBuilderAgreePolicyButton")

        //SET TITLE
        testUtils.waitUntilTag("QuizLayoutTitleTextField")
        testUtils.inputTextOnTag("QuizLayoutTitleTextField", quizData.title)
        testUtils.waitFor(300)

        //Set Quiz Description
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.inputTextOnTag("QuizLayoutBuilderDescriptionTextField", quizData.description)
        testUtils.waitFor(300)

        //SET TAGS
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.enterTextsOnTag("TagSetterTextField", quizData.tags.toList(), true)
        testUtils.waitFor(300)

        //SET IMAGE
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
//        val quizLayoutViewModel = ViewModelProvider(activity)[QuizLayoutViewModel::class.java]
//        testUtils.setImage(context, titleImage, onImagePicked = { image ->
//            quizLayoutViewModel.setQuizImage(image)
//        })
        onIdle()
        testUtils.waitFor(300)

        // composeRule.onNode(hasContentDescription("Description of the image")).assertExists()
        // replace tags test to contentDescription

        //Set ColorScheme
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimary Color")
        val colorInt = Random.nextInt(primaryColors.size)
        val primaryColor = primaryColors[colorInt]
        testUtils.replaceTextOnTag("QuizLayoutSetColorSchemeTextFieldPrimary Color", primaryColor, true)
        testUtils.waitFor(300)
        testUtils.clickOnTag("QuizLayoutSetColorSchemeButtonPrimary Color", true)
        testUtils.waitFor(300)
        testUtils.clickOnTag("QuizLayoutBuilderColorSchemeGenWithPrimaryColor")
        testUtils.waitFor(300)

        // NEED TESTING FROM HERE
        //Set TextStyles
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.setTextStyle(questionTextStyle, "setTextStyleQuestion")
        testUtils.setTextStyle(bodyTextStyle, "setTextStyleBody")
        testUtils.setTextStyle(answerTextStyle, "setTextStyleAnswer")
        testUtils.waitFor(300)

        //QUIZ BUILDER
        testUtils.clickOnTag("QuizLayoutBuilderProceedButton")
        testUtils.waitFor(1000)

        //ADD QUIZ1
        testUtils.addQuiz1(quiz1)

        //ADD QUIZ2
        testUtils.addQuiz2(quiz2)

        //ADD QUIZ3
        testUtils.addQuiz3(quiz3)

        //ADD QUIZ4
        testUtils.addQuiz4(quiz4)

        //DESIGN SCORECARD
        testUtils.clickOnTag("QuizBuilderScreenProceedButton")
        testUtils.waitFor(500)

        testUtils.clickOnTag("DesignScoreCardSetTextColorButton")
        testUtils.waitFor(200)
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", textColors[colorInt], true)
        testUtils.waitFor(200)
        Espresso.pressBack()
        onIdle()

        testUtils.waitUntilTag("DesignScoreCardSetColor1Button")
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColor1Button")
        testUtils.waitFor(200)
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", gradientColors1[colorInt], true)
        testUtils.waitFor(200)
        Espresso.pressBack()
        onIdle()

        testUtils.waitUntilTag("DesignScoreCardSetColor2Button")
        onIdle()
        testUtils.clickOnTag("DesignScoreCardSetColor2Button")
        testUtils.waitFor(200)
        testUtils.replaceTextOnTag("DesignScoreCardTextColorPicker", gradientColors2[colorInt], true)
        testUtils.waitFor(200)
        Espresso.pressBack()
        onIdle()

        testUtils.waitUntilTag("DesignScoreCardShaderButton")
        onIdle()
        testUtils.clickOnTag("DesignScoreCardShaderButton")
        testUtils.waitFor(500)
        val shader = shaders.random()
        testUtils.clickOnTag("DesignScoreCardShaderButton$shader", useUnmergedTree = true)
        onIdle()
        testUtils.waitFor(300)

        //UPLOAD
        testUtils.clickOnTag("DesignScoreCardUploadButton")
        onIdle()
        testUtils.waitFor(5000)
        onIdle()

        onIdle()
        testUtils.waitFor(5000)
        onIdle()

    }
}
