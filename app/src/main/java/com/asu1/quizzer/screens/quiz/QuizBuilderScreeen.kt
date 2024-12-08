package com.asu1.quizzer.screens.quiz

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import kotlinx.coroutines.launch

val bodyHeight = 500.dp

@Composable
fun QuizBuilderScreen(navController: NavController,
                      quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
                      onMoveToScoringScreen: () -> Unit = {},
                      scoreCardViewModel: ScoreCardViewModel = viewModel(),
                      navigateToQuizLoad: () -> Unit = {},
) {
    val quizzes by quizLayoutViewModel.quizzes.collectAsStateWithLifecycle()
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsStateWithLifecycle()
    val colorScheme = quizTheme.colorScheme
    var showNewQuizDialog by remember { mutableStateOf(false) }
    var curIndex by remember{ mutableIntStateOf(0) }
    val screenWidthHalf = LocalConfiguration.current.screenWidthDp.dp/2
    val padding = screenWidthHalf - 150.dp
    val snapLayoutInfoProvider = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(snapLayoutInfoProvider)
    val initialIndex by quizLayoutViewModel.initIndex.observeAsState(0)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(snapLayoutInfoProvider) {
        snapshotFlow { snapLayoutInfoProvider.layoutInfo }
            .collect { layoutInfo ->
                val visibleItems = layoutInfo.visibleItemsInfo
                if (visibleItems.isNotEmpty()) {
                    val center = layoutInfo.viewportEndOffset / 2
                    val centerItem = visibleItems.minByOrNull {
                        kotlin.math.abs(it.offset + it.size / 2 - center)
                    }
                    centerItem?.let {
                        val centerIndex = it.index
                        curIndex = centerIndex
                        quizLayoutViewModel.updateInitIndex(curIndex)
                    }
                }
            }
    }

    LaunchedEffect(Unit) {
        snapLayoutInfoProvider.scrollToItem(initialIndex)
    }



    fun moveToQuizCaller(loadIndex: Int, quizType: QuizType, insertIndex: Int){
        NavMultiClickPreventer.navigate(navController,
            Route.QuizCaller(
                loadIndex = loadIndex,
                quizType = quizType,
                insertIndex = insertIndex
            )
        )
    }

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        if (showNewQuizDialog) {
            AlertDialog(
                onDismissRequest = { showNewQuizDialog = false },
                title = { Text(stringResource(R.string.select_question_type)) },
                text = {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        items(4) { index ->
                            val imageRes = when (index) {
                                0 -> R.drawable.questiontype1
                                1 -> R.drawable.questiontype2
                                2 -> R.drawable.questiontype3
                                3 -> R.drawable.questiontype4
                                else -> R.drawable.questiontype1
                            }
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = "Question Type ${index + 1}",
                                modifier = Modifier
                                    .size(width = 100.dp, height = 300.dp)
                                    .testTag("QuizBuilderScreenNewQuizDialogImage$index")
                                    .clickable {
                                        showNewQuizDialog = false
                                        moveToQuizCaller(
                                            loadIndex = -1,
                                            quizType = QuizType.entries[index],
                                            insertIndex = curIndex
                                        )
                                    }
                            )
                        }
                    }
                },
                confirmButton = {
                }
            )
        }

        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = colorScheme.primaryContainer),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    RowWithAppIconAndName(
                        showBackButton = true,
                        onBackPressed = {
                            navController.popBackStack()
                        }
                    )
                    IconButton(
                        onClick = {
                            navigateToQuizLoad()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Load Local Save"
                        )
                    }
                }
            },
            bottomBar = {
                QuizBuilderBottomBar(
                    onPreview = {
                        NavMultiClickPreventer.navigate(navController, Route.QuizSolver(
                            initIndex = curIndex
                        ))
                    },
                    onProceed = {
                        onMoveToScoringScreen()
                    },
                    onLocalSave = {
                        scope.launch {
                            quizLayoutViewModel.saveLocal(
                                context,
                                scoreCardViewModel.scoreCard.value
                            )
                        }
                    }
                )
            },

            ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                LazyRow(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    flingBehavior = snapFlingBehavior,
                    state = snapLayoutInfoProvider,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = padding, end = padding, top = 8.dp),
                ) {
                    items(quizzes.size,

                    ) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(width = 280.dp, height = bodyHeight)
                                .background(
                                    colorScheme.surface
                                )
                                .border(
                                    width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            QuizViewer(
                                quiz = quizzes[it],
                                quizTheme = quizTheme,
                                quizStyleManager = quizLayoutViewModel.getTextStyleManager(),
                                isPreview = true,
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        // Your item content here
                    }
                    item {
                        NewQuizAdd(
                            showDialog = {
                                showNewQuizDialog = it
                            }
                        )
                    }
                }
                QuizEditIconsRow(
                    deleteCurrentQuiz = {
                        quizLayoutViewModel.removeQuizAt(curIndex)
                    },
                    curIndex = curIndex,
                    totalQuizzes = quizzes.size,
                    editCurrentQuiz = {
                        if(curIndex >= quizzes.size){
                            return@QuizEditIconsRow
                        }
                        moveToQuizCaller(
                            loadIndex = curIndex,
                            quizType = quizzes[curIndex].layoutType,
                            insertIndex = curIndex
                        )
                    },
                    addQuiz = {
                        showNewQuizDialog = true
                    }
                )
                Text(
                    stringResource(R.string.too_many_big_images_might_not_be_uploaded),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }

}

@Composable
fun NewQuizAdd(
    showDialog: (Boolean) -> Unit = {},
    key: String = "QuizBuilderScreenNewQuizAdd",
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 300.dp, height = bodyHeight)
            .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp)),
    ) {
        FloatingActionButton(
            modifier = Modifier.testTag(key),
            onClick = { showDialog(true) }) {
            Text("+")
        }
    }
}

@Composable
fun QuizEditIconsRow(
    deleteCurrentQuiz: () -> Unit,
    curIndex: Int,
    totalQuizzes: Int,
    editCurrentQuiz: () -> Unit,
    addQuiz: () -> Unit,
){
    val curIndexText = if(totalQuizzes == 0) 0 else if(totalQuizzes == curIndex) curIndex else curIndex + 1
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {
                deleteCurrentQuiz()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete Current Quiz"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = buildString {
                append(stringResource(R.string.quiz))
                append("$curIndexText / $totalQuizzes")
            },
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            onClick = {
                if(totalQuizzes != 0 || curIndex != totalQuizzes){
                    editCurrentQuiz()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Current Quiz"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        IconButton(
            modifier = Modifier.testTag("QuizBuilderScreenAddQuizIconButton"),
            onClick = {
                addQuiz()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add New Quiz"
            )
        }
    }
}

@Composable
fun QuizBuilderBottomBar(
    onPreview: () -> Unit = {},
    onProceed: () -> Unit = {},
    onLocalSave: () -> Unit = {},
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(
            onClick = {
                onLocalSave()
            }
        ){
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save Local."
            )
        }
        TextButton(
            onClick = {
                onPreview()
            }
        ) {
            Text(stringResource(R.string.preview))
        }
        IconButton(
            modifier = Modifier.testTag("QuizBuilderScreenProceedButton"),
            onClick = {
                onProceed()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "Move to Scoring Screen"
            )
        }
    }

}

@Preview
@Composable
fun PreviewQuizBuilderScreen(){
    QuizzerAndroidTheme {

        QuizBuilderScreen(
            navController = rememberNavController(),
        )
    }
}