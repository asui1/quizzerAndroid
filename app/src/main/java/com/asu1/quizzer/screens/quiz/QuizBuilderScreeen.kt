package com.asu1.quizzer.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ColorScheme
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
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.animations.LoadingAnimation
import com.asu1.quizzer.composables.base.RowWithAppIconAndName
import com.asu1.quizzer.model.ImageColorBackground
import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.util.constants.questionTypes
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel
import kotlinx.coroutines.launch

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
    val snapLayoutInfoProvider = rememberLazyListState()
    val initialIndex by quizLayoutViewModel.initIndex.observeAsState(0)
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
    ){
        quizzes.size + 1
    }
    var isPreview by remember{ mutableStateOf(false) }
    val textStyleManager by rememberUpdatedState(quizLayoutViewModel.getTextStyleManager())

    LaunchedEffect(Unit) {
        snapLayoutInfoProvider.scrollToItem(initialIndex)
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                curIndex = page
            }
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
        if (isPreview) {
            BackHandler {
                isPreview = false
            }
            Scaffold { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    ImageColorBackground(
                        imageColor = quizTheme.backgroundImage,
                        modifier = Modifier.fillMaxSize()
                    )
                    QuizViewerPager(
                        pagerState = pagerState,
                        quizSize = quizzes.size,
                        visibleQuizzes = quizzes,
                        quizTheme = quizTheme,
                        textStyleManager = textStyleManager,
                        updateQuiz1 = { index, answerIndex ->
                            quizLayoutViewModel.updateQuiz1(
                                index,
                                answerIndex
                            )
                        },
                        updateQuiz2 = { index, date ->
                            quizLayoutViewModel.updateQuiz2(
                                index,
                                date
                            )
                        },
                        updateQuiz3 = { index, from, to ->
                            quizLayoutViewModel.updateQuiz3(
                                index,
                                from,
                                to
                            )
                        },
                        updateQuiz4 = { index, from, to ->
                            quizLayoutViewModel.updateQuiz4(
                                index,
                                from,
                                to
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        isPreview = false,
                        lastElement = {
                            QuizSubmit(
                                title = stringResource(R.string.end_of_quiz_do_you_want_to_submit_your_answers),
                                modifier = Modifier.fillMaxSize(),
                                onSubmit = {}
                            )
                        }
                    )
                }
            }
        }
        else {
            Scaffold(
                topBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorScheme.primaryContainer),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RowWithAppIconAndName(
                            showBackButton = true,
                            onBackPressed = {
                                navController.popBackStack(
                                    Route.CreateQuizLayout,
                                    inclusive = false,
                                )
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
                            isPreview = true
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
                if (showNewQuizDialog) {
                    AddNewQuizDialog(
                        updateShowNewQuizDialog = { update ->
                            showNewQuizDialog = update
                        },
                        backgroundColor = colorScheme.surfaceContainerHigh,
                        moveToQuizCaller = { index ->
                            moveToQuizCaller(
                                loadIndex = -1,
                                quizType = QuizType.entries[index],
                                insertIndex = curIndex
                            )
                        })
                }
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    QuizViewerPager(
                        pagerState = pagerState,
                        quizSize = quizzes.size,
                        visibleQuizzes = quizzes,
                        quizTheme = quizTheme,
                        textStyleManager = textStyleManager,
                        updateQuiz1 = { index, answerIndex ->
                        },
                        updateQuiz2 = { index, date ->
                        },
                        updateQuiz3 = { index, from, to ->
                        },
                        updateQuiz4 = { index, from, to ->
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .weight(1f)
                            .border(
                                width = 2.dp,
                                color = colorScheme.outline,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        isPreview = true,
                        lastElement = {
                            NewQuizAdd(
                                showDialog = { showDialog ->
                                    showNewQuizDialog = showDialog
                                }
                            )
                        }
                    )
                    QuizEditIconsRow(
                        deleteCurrentQuiz = {
                            quizLayoutViewModel.removeQuizAt(curIndex)
                        },
                        curIndex = curIndex,
                        totalQuizzes = quizzes.size,
                        editCurrentQuiz = {
                            if (curIndex >= quizzes.size) {
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
}

@Composable
private fun AddNewQuizDialog(
    updateShowNewQuizDialog: (Boolean) -> Unit,
    backgroundColor: Color,
    moveToQuizCaller: (Int) -> Unit,
) {
    Dialog(
        onDismissRequest = { updateShowNewQuizDialog(false) },
        content = {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.select_question_type))
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.wrapContentSize()
                ) {
                    itemsIndexed(
                        questionTypes,
                        key = { index, _ -> index },
                    ) { index, questionType ->
                        Image(
                            painter = painterResource(id = questionType),
                            contentDescription = "Question Type ${index + 1}",
                            modifier = Modifier
                                .width(100.dp)
                                .testTag("QuizBuilderScreenNewQuizDialogImage$index")
                                .clickable {
                                    updateShowNewQuizDialog(false)
                                    moveToQuizCaller(index)
                                }
                        )
                    }
                }
            }
        },
    )
}

@Composable
fun NewQuizAdd(
    showDialog: (Boolean) -> Unit = {},
    key: String = "QuizBuilderScreenNewQuizAdd",
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surfaceContainerHigh, shape = RoundedCornerShape(16.dp)),
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