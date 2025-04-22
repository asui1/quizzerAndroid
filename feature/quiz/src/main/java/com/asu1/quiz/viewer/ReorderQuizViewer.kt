package com.asu1.quiz.viewer

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.sampleReorderQuiz
import com.asu1.quiz.ui.ArrowDownwardWithPaddings
import com.asu1.quiz.ui.SurfaceWithAnswerComposable
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle
import com.asu1.quiz.ui.textStyleManager.QuestionTextStyle
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun ReorderQuizViewer(
    quiz: ReorderQuiz,
    onUserInput: (Int, Int) -> Unit = {_, _ ->},
) {
    val view = LocalView.current
    val quiz3List = remember { mutableStateListOf(*quiz.shuffledAnswers.toTypedArray()) }
    fun shuffleQuiz3List(from: Int, to: Int){
        quiz3List.add(to, quiz3List.removeAt(from))
    }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        // Update the list
        shuffleQuiz3List(from.index, to.index)
        onUserInput(from.index, to.index)
        if(Build.VERSION_CODES.UPSIDE_DOWN_CAKE <= Build.VERSION.SDK_INT){
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        }
    }

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            QuestionTextStyle.GetTextComposable(quiz.question, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            BuildBody(
                quizBody = quiz.bodyValue,
            )
            Spacer(modifier = Modifier.height(8.dp))
            AnswerTextStyle.GetTextComposable(quiz.shuffledAnswers[0],
                modifier = Modifier.fillMaxWidth())
        }
        items(quiz3List.subList(1, quiz3List.size), key = { it }) {
            ArrowDownwardWithPaddings()
            ReorderableItem(reorderableLazyListState, key = it) { isDragging ->
                val item = it.replace(Regex("Q!Z2\\d+$"), "")
                val elevation by animateDpAsState(if (isDragging) 2.dp else 1.dp, label = "")

                SurfaceWithAnswerComposable(
                    modifier = Modifier.draggableHandle(
                        onDragStarted = {
                            if(Build.VERSION_CODES.R <= Build.VERSION.SDK_INT)
                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_START)
                        },
                        onDragStopped = {
                            if(Build.VERSION_CODES.R <= Build.VERSION.SDK_INT)
                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                        },
                    ),
                    item = item,
                    shadowElevation = elevation
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz3ViewPreview() {
    ReorderQuizViewer(quiz = sampleReorderQuiz,
        onUserInput = { _, _ -> },
    )
}