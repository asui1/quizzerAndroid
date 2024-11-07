package com.asu1.quizzer.screens.quiz

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.model.BodyType
import com.asu1.quizzer.model.Quiz
import com.asu1.quizzer.model.Quiz1
import com.asu1.quizzer.model.sampleQuiz1
import com.asu1.quizzer.viewModels.QuizTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun Quiz1Viewer(
    quiz: Quiz1ViewModel = viewModel(),
    quizTheme: QuizTheme = QuizTheme(),
    toggleUserAns: (Int) -> Unit = {},
    onExit: (Quiz1) -> Unit = {},
)
{
    val quizState by quiz.quiz1State.collectAsState()

    DisposableEffect(key1 = quizState){
        onDispose {
            onExit(quizState)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            GetTextStyle(quizState.question, quizTheme.questionTextStyle, quizTheme.colorScheme, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            BuildBody(
                quizState = quizState,
                quizTheme = quizTheme
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(quizState.userAns.size){ index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable {
                        quiz.toggleUserAnsAt(index)
                    }
            ){
                Checkbox(
                    checked = quizState.userAns[index],
                    onCheckedChange = {
                        toggleUserAns(index)
                    }
                )
                GetTextStyle(quizState.shuffledAnswers[index], quizTheme.answerTextStyle, quizTheme.colorScheme)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BuildBody(
    quizState: Quiz,
    quizTheme: QuizTheme = QuizTheme(),
){
    val context = LocalContext.current

    when(quizState.bodyType){
        BodyType.NONE -> {}
        BodyType.TEXT -> GetTextStyle(quizState.bodyText, quizTheme.bodyTextStyle, quizTheme.colorScheme, modifier = Modifier.fillMaxWidth())
        BodyType.IMAGE -> {
            Image(
                bitmap = BitmapFactory.decodeByteArray(quizState.bodyImage, 0, quizState.bodyImage!!.size).asImageBitmap(),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        BodyType.YOUTUBE -> {
            AndroidView(factory = {
                val youTubePlayerView = YouTubePlayerView(context)
                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo(quizState.youtubeId, quizState.youtubeStartTime.toFloat())
                    }
                })
                youTubePlayerView
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz1ViewerPreview()
{
    val quiz1ViewModel: Quiz1ViewModel = viewModel()
    quiz1ViewModel.loadQuiz(sampleQuiz1)

    Quiz1Viewer(
        quiz = quiz1ViewModel,
        quizTheme = QuizTheme(),
    )
}