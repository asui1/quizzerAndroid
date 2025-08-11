package com.asu1.quizcard.cardBase

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.asu1.quizcardmodel.HasUniqueId
import com.asu1.customComposable.dialog.DialogComposable
import com.asu1.quizcardmodel.QuizCard
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
fun getOrPutDismiss(
    uuid: String,
    dismissStates: MutableMap<String, SwipeToDismissBoxState>,
    onDismiss: (String) -> Unit
): SwipeToDismissBoxState {
    // ✅ Remember or create state
    val state = dismissStates.getOrPut(uuid) {
        rememberSwipeToDismissBoxState(
            initialValue = SwipeToDismissBoxValue.Settled,
            positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
        )
    }

    // ✅ Observe state changes and call onDismiss
    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDismiss(uuid)
        }
    }
    return state
}



@Composable
fun <T : HasUniqueId> LazyColumnWithSwipeToDismiss(
    modifier: Modifier = Modifier,
    stringResourceWhenEmpty: Int = R.string.no_quizzes_found,
    inputList: PersistentList<T>,
    deleteItemWithId: (String) -> Unit = {},
    content: @Composable (T, Int) -> Unit,
) {
    // shell: 공용 상태만 둠
    var showDialog by remember { mutableStateOf(false) }
    var deleteUuid by remember { mutableStateOf("") }
    val dismissStates = remember { mutableStateMapOf<String, SwipeToDismissBoxState>() }
    val visibleItems = remember(inputList) {
        mutableStateMapOf<String, Boolean>().apply { inputList.forEach { put(it.id, true) } }
    }
    val scope = rememberCoroutineScope()

    SwipeListContent(
        modifier = modifier,
        stringResWhenEmpty = stringResourceWhenEmpty,
        inputList = inputList,
        visibleItems = visibleItems,
        dismissStates = dismissStates,
        onRequestDelete = { id ->
            deleteUuid = id
            showDialog = true
        },
        itemContent = content
    )

    if (showDialog) {
        ConfirmDeleteDialog(
            deleteUuid = deleteUuid,
            onDelete = {
                val uuid = deleteUuid
                visibleItems[uuid] = false
                deleteItemWithId(uuid)
                dismissStates.remove(uuid)
                showDialog = false
            },
            onReset = { uuid ->
                scope.launch {
                    dismissStates[uuid]?.reset()
                    showDialog = false
                }
            }
        )
    }
}

/* ---------------- list content ---------------- */

@Composable
private fun <T : HasUniqueId> SwipeListContent(
    modifier: Modifier,
    @androidx.annotation.StringRes stringResWhenEmpty: Int,
    inputList: PersistentList<T>,
    visibleItems: MutableMap<String, Boolean>,
    dismissStates: MutableMap<String, SwipeToDismissBoxState>,
    onRequestDelete: (String) -> Unit,
    itemContent: @Composable (T, Int) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        if (inputList.isEmpty()) {
            item {
                Text(
                    text = stringResource(stringResWhenEmpty),
                    style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
                )
            }
        } else {
            items(inputList.size, key = { index -> inputList[index].id }) { index ->
                val item = inputList[index]
                val state = getOrPutDismiss(
                    uuid = item.id,
                    dismissStates = dismissStates,
                    onDismiss = onRequestDelete,
                )
                val isVisible = visibleItems[item.id] != false
                SwipeToDismissItem(
                    state = state,
                    isVisible = isVisible
                ) {
                    itemContent(item, index)
                }
            }
        }
    }
}

/* ---------------- item ---------------- */

@Composable
private fun SwipeToDismissItem(
    state: SwipeToDismissBoxState,
    isVisible: Boolean,
    content: @Composable RowScope.() -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = { DismissBackground(state) },
            enableDismissFromStartToEnd = false,
            modifier = Modifier.padding(vertical = 4.dp),
            content = content
        )
    }
}

@Composable
private fun DismissBackground(state: SwipeToDismissBoxState) {
    val bg = when (state.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.surfaceContainer
        else -> Color.Transparent
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(16.dp)
    )
}

/* ---------------- dialog wrapper ---------------- */
@Composable
private fun ConfirmDeleteDialog(
    deleteUuid: String,
    onDelete: () -> Unit,
    onReset: (String) -> Unit,
) {
    LazyColumnSwipeToDismissDialog(
        deleteUuid = deleteUuid,
        onDelete = onDelete,
        resetDismiss = onReset
    )
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
        inputList = quizList.toPersistentList(),
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
    listOf(
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
