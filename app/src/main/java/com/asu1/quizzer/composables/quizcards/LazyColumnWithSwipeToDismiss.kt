package com.asu1.quizzer.composables.quizcards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.DialogComposable
import com.asu1.quizzer.model.QuizCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LazyColumnWithSwipeToDismiss(
    quizList: List<QuizCard>,
    onLoadQuiz: (Int) -> Unit,
    getOrPutDismiss: @Composable (String, Int) -> DismissState,
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item{
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.my_quizzes),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            )
        }
        if (quizList.isEmpty()) {
            item {
                Text(
                    stringResource(R.string.no_quizzes_found),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            items(quizList.size) { index ->
                val quizCard = quizList[index]
                val currentDismissState = getOrPutDismiss(quizCard.id, index)
                SwipeToDismiss(
                    state = currentDismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    modifier = Modifier.padding(vertical = 2.dp),
                    background = {
                        val color = when (currentDismissState.dismissDirection) {
                            DismissDirection.EndToStart -> MaterialTheme.colorScheme.surfaceContainer
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete),
                                tint = Color.White
                            )
                        }
                    },
                    dismissContent = {
                        QuizCardHorizontal(
                            quizCard = quizCard,
                            onClick = {
                                onLoadQuiz(index)
                            }
                        )
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun LazyColumnWithSwipeToDismissPreview() {
    val quizList = listOf(
        QuizCard(
            id = "1",
            title = "Quiz 1",
            creator = "Creator 1",
            tags = listOf("Tag 1", "Tag 2"),
            image = byteArrayOf(),
            count = 0,
        ),
        QuizCard(
            id = "2",
            title = "Quiz 2",
            creator = "Creator 2",
            tags = listOf("Tag 3", "Tag 4"),
            image = byteArrayOf(),
            count = 0,
        ),
    )
    val dismissStates = SnapshotStateMap<String, DismissState>()
    LazyColumnWithSwipeToDismiss(
        quizList = quizList,
        onLoadQuiz = {},
        getOrPutDismiss = { uuid, index ->
            dismissStates.getOrPut(uuid) {
                rememberDismissState()
            }
        }
    )
}

@Composable
fun LazyColumnSwipeToDismissDialog(
    quizList: List<QuizCard>,
    deleteIndex: Int,
    onDelete: (Int) -> Unit,
    updateDialog: (Boolean) -> Unit,
    resetDismiss: (String) -> Unit,
){
    val scope = rememberCoroutineScope()
    DialogComposable(
        titleResource = R.string.delete_save,
        messageResource = R.string.delete_save_body,
        onContinue = {
            val uuid = quizList[deleteIndex].id
            scope.launch {
                resetDismiss(uuid)
                onDelete(deleteIndex)
            }
            updateDialog(false)
        },
        onContinueResource = R.string.delete,
        onCancel = {
            updateDialog(false)
            val uuid = quizList[deleteIndex].id
            scope.launch {
                resetDismiss(uuid)
            }
        },
        onCancelResource = R.string.cancel
    )
}

@Preview(showBackground = true)
@Composable
fun LazyColumnSwipeToDismissDialogPreview(){
    val quizList = listOf(
        QuizCard(
            id = "1",
            title = "Quiz 1",
            creator = "Creator 1",
            tags = listOf("Tag 1", "Tag 2"),
            image = byteArrayOf(),
            count = 0,
        ),
        QuizCard(
            id = "2",
            title = "Quiz 2",
            creator = "Creator 2",
            tags = listOf("Tag 3", "Tag 4"),
            image = byteArrayOf(),
            count = 0,
        ),
    )
    LazyColumnSwipeToDismissDialog(
        quizList = quizList,
        deleteIndex = 0,
        onDelete = {},
        updateDialog = {},
        resetDismiss = {},
    )
}