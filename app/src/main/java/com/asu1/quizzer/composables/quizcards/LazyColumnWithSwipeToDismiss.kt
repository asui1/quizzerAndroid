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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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

@Composable
fun LazyColumnWithSwipeToDismiss(
    quizList: List<QuizCard>,
    onLoadQuiz: (Int) -> Unit,
    deleteQuiz: (Int) -> Unit = {},
) {
    var showDialog by remember { mutableStateOf(false) }
    var deleteIndex by remember { mutableIntStateOf(-1) }
    val dismissStates = remember { mutableStateMapOf<String, SwipeToDismissBoxState>() }
    val scope = rememberCoroutineScope()

    @Composable
    fun getOrPutDismiss(uuid: String, index: Int): SwipeToDismissBoxState {
        return dismissStates.getOrPut(uuid) {
            rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        deleteIndex = index
                        showDialog = true
                    }
                    true
                }
            )
        }
    }

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
                SwipeToDismissBox(
                    state = currentDismissState,
                    backgroundContent =  {
                        val color = when (currentDismissState.dismissDirection) {
                            SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.surfaceContainer
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
                    enableDismissFromStartToEnd = false,
                    modifier = Modifier.padding(vertical = 2.dp),
                    content = {
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
    if (showDialog) {
        LazyColumnSwipeToDismissDialog(
            quizList = quizList,
            deleteIndex = deleteIndex,
            onDelete = {
                deleteQuiz(deleteIndex)
            },
            updateDialog = { showDialog = it },
            resetDismiss = { uuid ->
                scope.launch {
                    dismissStates[uuid]?.reset()
                }
            }
        )
    }
}

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
    LazyColumnWithSwipeToDismiss(
        quizList = quizList,
        onLoadQuiz = {},
    )
}

@Composable
fun LazyColumnSwipeToDismissDialog(
    quizList: List<QuizCard>,
    deleteIndex: Int,
    onDelete: () -> Unit,
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
                onDelete()
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