package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import com.asu1.models.quizRefactor.ConnectItemsQuiz

@Composable
internal fun rememberDotOffsets(
    quiz: ConnectItemsQuiz
): Pair<SnapshotStateList<Offset>, SnapshotStateList<Offset>> {
    val left  = remember { mutableStateListOf<Offset>().apply { repeat(quiz.answers.size)           { add(Offset.Zero) } } }
    val right = remember { mutableStateListOf<Offset>().apply { repeat(quiz.connectionAnswers.size) { add(Offset.Zero) } } }
    return left to right
}
