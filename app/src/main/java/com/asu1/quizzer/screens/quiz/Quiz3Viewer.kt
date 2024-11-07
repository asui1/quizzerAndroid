package com.asu1.quizzer.screens.quiz

import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.asu1.quizzer.composables.GetTextStyle
import com.asu1.quizzer.model.Quiz3
import com.asu1.quizzer.model.sampleQuiz3
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.viewModels.QuizTheme
import com.asu1.quizzer.viewModels.quizModels.Quiz3ViewModel
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Quiz3Viewer(
    quiz: Quiz3ViewModel = viewModel(),
    quizTheme: QuizTheme = QuizTheme(),
    onExit: (Quiz3) -> Unit = {},
) {
    val view = LocalView.current
    val quizState by quiz.quiz3State.collectAsState()
    var quiz3List by remember{mutableStateOf(quizState.shuffledAnswers.subList(1, quizState.shuffledAnswers.size))}
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        // Update the list
//        quiz.switchShuffledAnswers(from.index-1, to.index-1)
        quiz3List = quiz3List.toMutableList().apply {
            add(to.index-3, removeAt(from.index-3))
        }
        view.performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
    }

    DisposableEffect(Unit) {
        onDispose {
            quiz.updateShuffledAnswers(quiz3List)
            onExit(quizState)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            GetTextStyle(
                quizState.question,
                quizTheme.questionTextStyle,
                quizTheme.colorScheme,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            BuildBody(
                quizState = quizState,
                quizTheme = quizTheme
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item{
            GetTextStyle(
                quizState.shuffledAnswers[0],
                quizTheme.answerTextStyle,
                quizTheme.colorScheme,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
        items(quiz3List, key = { it }) {
            ReorderableItem(reorderableLazyListState, key = it) { isDragging ->
                val item = it.replace(Regex("Q!Z2\\d+$"), "")
                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp, label = "")

                Surface(
                    color = Color.Transparent,
                    shadowElevation = elevation
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = "Reorder",
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            GetTextStyle(item, quizTheme.answerTextStyle, quizTheme.colorScheme, modifier = Modifier.weight(1f).padding(8.dp))
                            IconButton(
                                modifier = Modifier.draggableHandle(
                                    onDragStarted = {
                                        view.performHapticFeedback(HapticFeedbackConstants.DRAG_START)
                                    },
                                    onDragStopped = {
                                        view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                                    },
                                ),
                                onClick = {},
                            ) {
                                Icon(Icons.Rounded.DragHandle, contentDescription = "Reorder")
                            }
                        }
                    }
                }
            }
        }
        items(quizState.shuffledAnswers.size - 1){

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz3ViewPreview() {
    val quiz3ViewModel: Quiz3ViewModel = viewModel()
    quiz3ViewModel.loadQuiz(sampleQuiz3)

    Quiz3Viewer(quiz = quiz3ViewModel)
}