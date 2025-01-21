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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.models.quiz.Quiz
import com.asu1.models.quiz.Quiz1
import com.asu1.models.sampleQuiz1
import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.model.TextStyleManager
import com.asu1.quizzer.viewModels.quizModels.Quiz1ViewModel
import com.asu1.resources.TextStyles
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

@Composable
fun Quiz1Viewer(
    quiz: Quiz1,
    toggleUserAnswer: (Int) -> Unit = {},
    quizStyleManager: TextStyleManager,
    isPreview: Boolean = false,
)
{
    val userAnswers = remember { mutableStateListOf(*quiz.userAns.toTypedArray()) }
    fun toggleLocalUserAnswers(index: Int){
        userAnswers[index ] = !userAnswers[index]
    }
    LazyColumn(
        userScrollEnabled = !isPreview,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        item{
            quizStyleManager.GetTextComposable(TextStyles.QUESTION, quiz.question, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            BuildBody(
                quizState = quiz,
                quizStyleManager = quizStyleManager,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(userAnswers.size){ index ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable {
                    if(isPreview) return@clickable
                    toggleLocalUserAnswers(index)
                    toggleUserAnswer(index)
                }
            ){
                Checkbox(
                    checked = userAnswers[index],
                    onCheckedChange = {
                        if(isPreview) return@Checkbox
                        toggleLocalUserAnswers(index)
                        toggleUserAnswer(index)
                    }
                )
                quizStyleManager.GetTextComposable(TextStyles.ANSWER, quiz.shuffledAnswers[index])
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun BuildBody(
    quizState: Quiz,
    quizStyleManager: TextStyleManager,
){
    val context = LocalContext.current

    when(quizState.bodyType){
        is BodyType.TEXT -> quizStyleManager.GetTextComposable(TextStyles.BODY, (quizState.bodyType as BodyType.TEXT).bodyText, modifier = Modifier.fillMaxWidth())
        is BodyType.IMAGE -> {
            val image = remember((quizState.bodyType as BodyType.IMAGE).bodyImage[0]){
                BitmapFactory.decodeByteArray((quizState.bodyType as BodyType.IMAGE).bodyImage, 0, (quizState.bodyType as BodyType.IMAGE).bodyImage.size).asImageBitmap().apply{
                    prepareToDraw()
                }
            }
            Image(
                bitmap = image,
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        is BodyType.YOUTUBE -> {
            AndroidView(factory = {
                val youTubePlayerView = YouTubePlayerView(context)
                youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        youTubePlayer.cueVideo((quizState.bodyType as BodyType.YOUTUBE).youtubeId, (quizState.bodyType as BodyType.YOUTUBE).youtubeStartTime.toFloat())
                    }
                })
                youTubePlayerView
            })
        }
        else -> {}
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz1ViewerPreview()
{
    val quiz1ViewModel: Quiz1ViewModel = viewModel()
    quiz1ViewModel.loadQuiz(sampleQuiz1)

    Quiz1Viewer(
        quiz = sampleQuiz1,
        quizStyleManager = TextStyleManager()
    )
}