package com.asu1.quiz.creator

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.models.serializers.QuizType
import com.asu1.quiz.preview.QuizPreview
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.quiz.ui.QuizLayoutBottomBar
import com.asu1.quiz.viewer.QuizSubmit
import com.asu1.quiz.viewer.QuizViewerPager
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.coroutines.launch
import kotlin.collections.isNotEmpty

const val scale = 0.66f

@Composable
fun QuizBuilderScreen(
    navController: NavController,
    quizCoordinatorViewModel: QuizCoordinatorViewModel = viewModel(),
    onMoveToScoringScreen: () -> Unit = {},
    navigateToQuizLoad: () -> Unit = {},
) {
    val quizState by quizCoordinatorViewModel.quizUIState.collectAsStateWithLifecycle()
    val quizzes = quizState.quizContentState.quizzes
    val quizTheme = quizState.quizTheme
    val colorScheme = quizTheme.colorScheme
    var showNewQuizDialog by remember { mutableStateOf(false) }
    var curIndex by remember{ mutableIntStateOf(0) }
    val snapLayoutInfoProvider = rememberLazyListState()
    val initialIndex = quizState.quizContentState.quizInitIndex
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = initialIndex,
    ){
        quizzes.size + 1
    }
    var isPreview by remember{ mutableStateOf(false) }
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }

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
        navController.navigate(
            Route.QuizCaller(
                loadIndex = loadIndex,
                quizType = quizType,
                insertIndex = insertIndex
            )
        ){
            launchSingleTop = true
        }
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
                        updateQuizCoordinator = { quizCoordinatorAction ->
                            quizCoordinatorViewModel.updateQuizCoordinator(quizCoordinatorAction)
                        },
                        modifier = Modifier.fillMaxSize(),
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
                    QuizzerTopBarBase(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = colorScheme.primaryContainer),
                        header = @Composable {
                            IconButton(onClick = {
                                navController.popBackStack(
                                    Route.CreateQuizLayout,
                                    inclusive = false,
                                )
                            }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = stringResource(R.string.move_back_home)
                                )
                            }
                        },
                        body = {
                            Text(
                                text = "Quizzer",
                                style = QuizzerTypographyDefaults.quizzerHeadlineMedium,
                            )
                        },
                        actions = {
                            IconButton(
                                modifier = Modifier.width(30.dp),
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
                    )
                },
                bottomBar = {
                    QuizLayoutBottomBar(
                        moveBack = {
                            navController.popBackStack(
                                Route.CreateQuizLayout,
                                inclusive = false,
                            )
                        },
                        moveForward = {
                            onMoveToScoringScreen()
                        },
                    ){
                        IconButtonWithText(
                            imageVector = Icons.Default.Save,
                            text = stringResource(R.string.temp_save),
                            onClick = {
                                scope.launch {
                                    quizCoordinatorViewModel.saveLocal(
                                        context,
                                    )
                                }
                            },
                            description = "Save Local",
                            iconSize = 24.dp
                        )
                        IconButtonWithText(
                            imageVector = Icons.Default.Visibility,
                            text = stringResource(R.string.preview),
                            onClick = {
                                isPreview = true
                            },
                            description = "Preview Quiz",
                            iconSize = 24.dp
                        )
                    }
                },

                ) { innerPadding ->
                if (showNewQuizDialog) {
                    AddNewQuizDialog(
                        updateShowNewQuizDialog = { update ->
                            showNewQuizDialog = update
                        },
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
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .size(width = screenWidth * scale, height = screenHeight * scale)
                            .requiredSize(
                                width = screenWidth, height = screenHeight
                            )
                            .scale(scale)
                            .border(
                                width = 2.dp,
                                color = colorScheme.outline,
                                shape = RoundedCornerShape(16.dp)
                            ),
                    ) { page ->
                        if(page < quizzes.size){
                            QuizPreview(
                                quizzes[page]
                            )
                        }
                        else{
                            NewQuizAdd(
                                showDialog = { showDialog ->
                                    showNewQuizDialog = showDialog
                                }
                            )
                        }
                    }
                    Text(
                        text = buildString {
                            append(stringResource(R.string.quiz))
                            append("${if (quizzes.isNotEmpty()) curIndex + 1 else curIndex} / ${quizzes.size}")
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    QuizEditIconsRow(
                        deleteCurrentQuiz = {
                            quizCoordinatorViewModel.updateQuizCoordinator(
                                QuizCoordinatorActions.RemoveQuizAt(curIndex)
                            )
                        },
                        editCurrentQuiz = {
                            if (curIndex >= quizzes.size) {
                                return@QuizEditIconsRow
                            }
                            moveToQuizCaller(
                                loadIndex = curIndex,
                                quizType = quizzes[curIndex].quizType,
                                insertIndex = curIndex
                            )
                        },
                        addQuiz = {
                            showNewQuizDialog = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AddNewQuizDialog(
    updateShowNewQuizDialog: (Boolean) -> Unit,
    moveToQuizCaller: (Int) -> Unit,
) {
    Dialog(
        onDismissRequest = { updateShowNewQuizDialog(false) },
        content = {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    stringResource(R.string.select_question_type),
                    fontWeight =  FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    QuizType.entries.forEachIndexed { index, item ->
                        Card(
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .testTag("QuizBuilderScreenNewQuizDialogImage$index")
                                .clickable{
                                    updateShowNewQuizDialog(false)
                                    moveToQuizCaller(index)
                                }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    stringResource(item.stringResourceId),
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAddNewQuizDialog(){
    QuizzerAndroidTheme {
        AddNewQuizDialog(
            updateShowNewQuizDialog = {},
            moveToQuizCaller = {},
        )
    }
}

@Composable
fun NewQuizAdd(
    showDialog: (Boolean) -> Unit = {},
    key: String = "QuizBuilderScreenNewQuizAdd",
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            ),
    ) {
        Text(
            stringResource(R.string.add_new_quiz),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(
            modifier = Modifier.testTag(key).padding(horizontal = 8.dp),
            onClick = { showDialog(true) }) {
            Text(
                stringResource(R.string.add_quiz),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun QuizEditIconsRow(
    deleteCurrentQuiz: () -> Unit,
    editCurrentQuiz: () -> Unit,
    addQuiz: () -> Unit,
){
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
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
        IconButton(
            onClick = {
                editCurrentQuiz()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Current Quiz"
            )
        }
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

@Preview
@Composable
fun PreviewQuizBuilderScreen(){
    QuizzerAndroidTheme {

        QuizBuilderScreen(
            navController = rememberNavController(),
        )
    }
}