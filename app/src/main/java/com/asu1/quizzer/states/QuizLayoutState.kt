package com.asu1.quizzer.states

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.model.ImageColor
import com.asu1.quizzer.model.ImageColorState
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.viewModels.QuizLayoutViewModel


data class QuizLayoutState(
    val quizTitle: State<String>,
    val quizImage: State<ByteArray?>,
    val quizDescription: State<String>,
    val quizTags: State<List<String>?>,
    val flipStyle: State<Int?>,
    val colorScheme: State<ColorScheme>,
    val backgroundImage: State<ImageColor>,
    val shuffleQuestions: State<Boolean?>,
    val questionTextStyle: State<List<Int>>,
    val bodyTextStyle: State<List<Int>>,
    val answerTextStyle: State<List<Int>>,
    val creator: State<String?>,
    val uuid: State<String?>,
    val quizzes: State<List<Quiz>?>,
    val fullUpdate: State<Int>,
    val setQuizTitle: (String?) -> Unit = {},
    val setQuizImage: (ByteArray) -> Unit = {},
    val setQuizDescription: (String?) -> Unit = {},
    val addQuizTag: (String) -> Unit = {},
    val removeQuizTag: (String) -> Unit = {},
    val setFlipStyle: (Int) -> Unit = {},
    val initQuizLayout: (String?, ColorScheme) -> Unit = {_, _ -> },
    val setColorScheme: (String, Color) -> Unit = {_, _ -> },
    val setColorSchemeFull: (ColorScheme) -> Unit = {},
    val updateTextStyle: (Int, Int, Boolean) -> Unit = {_, _, _ -> },
    val updateBackgroundImage: (ImageColor) -> Unit = {_ -> },
)

@Composable
fun rememberQuizLayoutState(
    quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
): QuizLayoutState {
    val quizTitle by quizLayoutViewModel.quizTitle.observeAsState(initial = "")
    val quizImage by quizLayoutViewModel.quizImage.observeAsState(initial = null)
    val quizDescription by quizLayoutViewModel.quizDescription.observeAsState(initial = "")
    val quizTags by quizLayoutViewModel.quizTags.observeAsState(initial = null)
    val flipStyle by quizLayoutViewModel.flipStyle.observeAsState(initial = null)
    val colorScheme by quizLayoutViewModel.colorScheme.observeAsState(initial = MaterialTheme.colorScheme)
    val backgroundImage by quizLayoutViewModel.backgroundImage.observeAsState(initial = ImageColor(MaterialTheme.colorScheme.surface, byteArrayOf(), MaterialTheme.colorScheme.inverseSurface, ImageColorState.COLOR))
    val shuffleQuestions by quizLayoutViewModel.shuffleQuestions.observeAsState(initial = null)
    val questionTextStyle by quizLayoutViewModel.questionTextStyle.observeAsState(initial = listOf(0, 0, 1, 0))
    val bodyTextStyle by quizLayoutViewModel.bodyTextStyle.observeAsState(initial = listOf(0, 0, 2, 1))
    val answerTextStyle by quizLayoutViewModel.answerTextStyle.observeAsState(initial = listOf(0, 0, 0, 2))
    val creator by quizLayoutViewModel.creator.observeAsState(initial = null)
    val uuid by quizLayoutViewModel.uuid.observeAsState(initial = null)
    val quizzes by quizLayoutViewModel.quizzes.observeAsState(initial = null)
    val fullUpdate by quizLayoutViewModel.fullUpdate.observeAsState(initial = 0)


    return QuizLayoutState(
        quizTitle = rememberUpdatedState(quizTitle),
        quizImage = rememberUpdatedState(quizImage),
        quizDescription = rememberUpdatedState(quizDescription),
        quizTags = rememberUpdatedState(quizTags),
        flipStyle = rememberUpdatedState(flipStyle),
        colorScheme = rememberUpdatedState(colorScheme),
        backgroundImage = rememberUpdatedState(backgroundImage),
        shuffleQuestions = rememberUpdatedState(shuffleQuestions),
        questionTextStyle = rememberUpdatedState(questionTextStyle),
        bodyTextStyle = rememberUpdatedState(bodyTextStyle),
        answerTextStyle = rememberUpdatedState(answerTextStyle),
        creator = rememberUpdatedState(creator),
        uuid = rememberUpdatedState(uuid),
        quizzes = rememberUpdatedState(quizzes),
        fullUpdate = rememberUpdatedState(fullUpdate),
        setQuizTitle = {
            if (it != null) {
                quizLayoutViewModel.setQuizTitle(it)
            }
        },
        setQuizImage = {
            quizLayoutViewModel.setQuizImage(it)
        },
        setQuizDescription = {it ->
            if (it != null) {
                quizLayoutViewModel.setQuizDescription(it)
            }
        },
        addQuizTag = {it ->
            quizLayoutViewModel.addTag(it)
        },
        removeQuizTag = {it ->
            quizLayoutViewModel.removeTag(it)
        },
        setFlipStyle = {it ->
            quizLayoutViewModel.setFlipStyle(it)
        },
        initQuizLayout = {title, colorScheme ->
            quizLayoutViewModel.initQuizLayout(title, colorScheme)
        },
        setColorScheme = {key, color ->
            quizLayoutViewModel.setColorScheme(key, color)
        },
        setColorSchemeFull = {colorScheme ->
            quizLayoutViewModel.setColorScheme(colorScheme)
        },
        updateTextStyle = {targetSelector, indexSelector, isIncrease ->
            quizLayoutViewModel.updateTextStyle(targetSelector, indexSelector, isIncrease)
        },
        updateBackgroundImage = {imageColor ->
            quizLayoutViewModel.updateBackgroundImage(imageColor)
        }
    )
}