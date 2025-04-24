package com.asu1.quiz.content.reorderQuiz

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.models.sampleReorderQuiz
import com.asu1.quiz.content.QuizBase
import com.asu1.quiz.content.QuizMode
import com.asu1.quiz.viewmodel.quiz.QuizUserUpdates
import com.asu1.resources.QuizzerAndroidTheme
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@Composable
fun ReorderQuizViewer(
    quiz: ReorderQuiz,
    onUserInput: (QuizUserUpdates) -> Unit,
) {
    val view = LocalView.current
    val lazyState = rememberLazyListState()
    val reorderState = rememberReorderableLazyListState(lazyState) { from, to ->
        onUserInput(QuizUserUpdates.ReorderQuizUpdate(from.index, to.index))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
        }
    }

    QuizBase(
        quiz = quiz,
        mode = QuizMode.Viewer
    ) {
        LazyColumn(
            state               = lazyState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier            = Modifier
                .heightIn(min = 300.dp, max = 600.dp)
                .fillMaxWidth()
        ) {
            itemsIndexed(quiz.shuffledAnswers, key = {_, it -> it }) { index, value ->
                ReorderableItem(reorderState, key = value) { isDragging ->
                    val display = value.replace(Regex("Q!Z2\\d+$"), "")
                    val elevation by animateDpAsState(if (isDragging) 2.dp else 1.dp, label = "")
                    SurfaceWithAnswerComposable(
                        modifier         = Modifier.draggableHandle(
                            onDragStarted = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                    view.performHapticFeedback(HapticFeedbackConstants.GESTURE_START)
                            },
                            onDragStopped = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                                    view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                            }
                        ),
                        item             = display,
                        shadowElevation  = elevation
                    )
                }
                if(index != quiz.shuffledAnswers.size-1){
                    ArrowDownwardWithPaddings()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewReorderQuizViewer(){
    QuizzerAndroidTheme {
        ReorderQuizViewer(
            sampleReorderQuiz,
            onUserInput = {},
        )
    }
}