package com.asu1.quizzer.screens.quiz

import android.os.Build
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
import androidx.compose.runtime.mutableStateListOf
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
import java.lang.Thread.sleep

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Quiz3Viewer(
    quiz: Quiz3,
    quizTheme: QuizTheme = QuizTheme(),
    onUserInput: (Int, Int) -> Unit = {_, _ ->},
) {
    val view = LocalView.current
    val quiz3List = remember { mutableStateListOf(*quiz.shuffledAnswers.toTypedArray()) }
    val lazyListState = rememberLazyListState()
    fun shuffleQuiz3List(from: Int, to: Int){
        quiz3List.add(to, quiz3List.removeAt(from))
    }
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            GetTextStyle(
                quiz.question,
                quizTheme.questionTextStyle,
                quizTheme.colorScheme,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            BuildBody(
                quizState = quiz,
                quizTheme = quizTheme
            )
            Spacer(modifier = Modifier.height(8.dp))
            GetTextStyle(
                quiz.shuffledAnswers[0],
                quizTheme.answerTextStyle,
                quizTheme.colorScheme,
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            )
        }
        items(quiz3List.subList(1, quiz3List.size), key = { it }) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun Quiz3ViewPreview() {
    val quiz3ViewModel: Quiz3ViewModel = viewModel()
    quiz3ViewModel.loadQuiz(sampleQuiz3)

    Quiz3Viewer(quiz = sampleQuiz3)
}