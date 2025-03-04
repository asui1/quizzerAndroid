package com.asu1.quizcard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.asu1.customdialogs.DialogComposable
import com.asu1.quizcardmodel.QuizCard
import com.asu1.resources.R
import kotlinx.coroutines.launch

@Composable
fun <T : com.asu1.baseinterfaces.Identifiable> LazyColumnWithSwipeToDismiss(
    quizList: List<T>,
    deleteQuiz: (String) -> Unit = {},
    content: @Composable (T, Int) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    var deleteUuid by remember { mutableStateOf("") }
    val dismissStates = remember { mutableStateMapOf<String, SwipeToDismissBoxState>() }
    val scope = rememberCoroutineScope()
    val visibleItems = remember { mutableStateMapOf<String, Boolean>().apply {
        quizList.forEach { put(it.id, true) }
    } }

    @Composable
    fun getOrPutDismiss(uuid: String): SwipeToDismissBoxState {
        return dismissStates.getOrPut(uuid) {
            rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        deleteUuid = uuid
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
            items(quizList.size, key = {index -> quizList[index].id}) { index ->
                val quizCard = quizList[index]
                val currentDismissState = getOrPutDismiss(quizCard.id)
                val isVisible = visibleItems[quizCard.id] ?: true

                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    SwipeToDismissBox(
                        state = currentDismissState,
                        backgroundContent =  {
                            val backgroundColor = when (currentDismissState.dismissDirection) {
                                SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.surfaceContainer
                                else -> Color.Transparent
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(backgroundColor)
                                    .padding(16.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                if(backgroundColor != Color.Transparent){
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = stringResource(R.string.delete),
                                    )
                                }
                            }
                        },
                        enableDismissFromStartToEnd = false,
                        modifier = Modifier.padding(vertical = 2.dp),
                        content = {
                            content(quizCard, index)
                        }
                    )
                }
            }
        }
    }
    if (showDialog) {
        LazyColumnSwipeToDismissDialog(
            deleteUuid = deleteUuid,
            onDelete = {
                val uuid = deleteUuid
                visibleItems[uuid] = false
                scope.launch {
                    deleteQuiz(deleteUuid)
                }
            },
            resetDismiss = { uuid ->
                scope.launch {
                    dismissStates[uuid]?.reset()
                    showDialog = false
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
        content = { quizCard, index ->
            QuizCardHorizontal(quizCard = quizCard)
        }
    )
}

@Composable
fun LazyColumnSwipeToDismissDialog(
    deleteUuid: String,
    onDelete: () -> Unit,
    resetDismiss: (String) -> Unit,
){
    val scope = rememberCoroutineScope()
    DialogComposable(
        title = R.string.delete_save,
        message = R.string.delete_save_body,
        onContinue = {
            val uuid = deleteUuid
            scope.launch {
                onDelete()
                resetDismiss(uuid)
            }
        },
        onContinueText = R.string.delete,
        onCancel = {
            scope.launch {
                resetDismiss(deleteUuid)
            }
        },
        onCancelText = R.string.cancel
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
        deleteUuid = "",
        onDelete = {},
        resetDismiss = {},
    )
}