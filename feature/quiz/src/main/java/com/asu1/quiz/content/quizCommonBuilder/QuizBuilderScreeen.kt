package com.asu1.quiz.content.quizCommonBuilder

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.models.quiz.QuizTheme
import com.asu1.models.quizRefactor.Quiz
import com.asu1.models.serializers.QuizType
import com.asu1.quiz.ui.ImageColorBackground
import com.asu1.quiz.ui.QuizLayoutBottomBar
import com.asu1.quiz.viewmodel.LoadLocalQuizViewModel
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorViewModel
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.coroutines.launch

@Composable
fun QuizBuilderScreen(
    navController: NavController
) {
    // 1) collect shared state and helpers
    val vm: QuizCoordinatorViewModel   = hiltViewModel()
    val loadVm: LoadLocalQuizViewModel = viewModel()
    val userVm: UserViewModel = hiltViewModel()
    val state by vm.quizUIState.collectAsStateWithLifecycle()

    val quizzes     = state.quizContentState.quizzes
    val theme       = state.quizTheme
    val colors      = theme.colorScheme
    val context     = LocalContext.current
    val scope       = rememberCoroutineScope()

    // 2) remember UI state
    var isPreview by remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(
        initialPage = state.quizContentState.quizInitIndex
    ) { quizzes.size + 1 }

    MaterialTheme(colorScheme = colors) {
        if (isPreview) {
            QuizBuilderPreview(
                theme        = theme,
                pagerState   = pagerState,
                quizzes      = quizzes,
                onExitPreview = { isPreview = false }
            )
        } else {
            QuizBuilderEditor(
                navController  = navController,
                theme          = theme,
                quizzes        = quizzes,
                pagerState     = pagerState,
                onEnterPreview = { isPreview = true },
                onSaveLocal    = { scope.launch { vm.saveLocal(context) } },
                onLoadLocal    = {
                    loadVm.loadLocalQuiz(
                        context,
                        userVm.userData.value?.email ?: "GUEST"
                    )
                    navController.navigate(Route.LoadLocalQuiz) { launchSingleTop = true }
                },
                onMoveToCaller = { loadIndex, quizType, insertIndex ->
                    navController.navigate(
                        Route.QuizCaller(
                            loadIndex   = loadIndex,
                            quizType    = quizType,
                            insertIndex = insertIndex
                        )
                    ) { launchSingleTop = true }
                },
            )
        }
    }
}

@Composable
private fun QuizBuilderPreview(
    theme: QuizTheme,
    pagerState: PagerState,
    quizzes: List<Quiz>,
    onExitPreview: () -> Unit
) {
    BackHandler { onExitPreview() }
    Scaffold { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ImageColorBackground(
                imageColor = theme.backgroundImage,
                modifier   = Modifier.fillMaxSize()
            )
            QuizViewerPager(
                pagerState = pagerState,
                quizSize   = quizzes.size,
                quizzes    = quizzes,
                modifier   = Modifier.fillMaxSize(),
                lastElement = {
                    QuizSubmit(
                        title    = stringResource(R.string.end_of_quiz_do_you_want_to_submit_your_answers),
                        modifier = Modifier.fillMaxSize(),
                        onSubmit = {}
                    )
                }
            )
        }
    }
}

@Composable
private fun QuizBuilderEditor(
    navController:   NavController,
    theme:           QuizTheme,
    quizzes:         List<Quiz>,
    pagerState:      PagerState,
    onEnterPreview:  () -> Unit,
    onSaveLocal:     () -> Unit,
    onLoadLocal:     () -> Unit,
    onMoveToCaller:  (Int, QuizType, Int) -> Unit,
) {
    var showNewQuizDialog by remember { mutableStateOf(false) }
    if (showNewQuizDialog) {
        AddNewQuizDialog(
            updateShowNewQuizDialog = { showNewQuizDialog = it },
            moveToQuizCaller = { index ->
                onMoveToCaller(
                    -1,
                    QuizType.entries[index],
                    pagerState.currentPage
                )
            }
        )
    }
    Scaffold(
        topBar    = { EditorTopBar(navController, theme, onLoadLocal) },
        bottomBar = { EditorBottomBar( navController, onEnterPreview, onSaveLocal) }
    ) { padding ->
        EditorContent(
            padding       = padding,
            quizzes       = quizzes,
            pagerState    = pagerState,
            onAddQuiz     = { showNewQuizDialog = true },
            onMoveToCaller= onMoveToCaller
        )
    }
}

@Composable
private fun EditorTopBar(
    navController: NavController,
    theme: QuizTheme,
    onLoadLocal: () -> Unit
) {
    QuizzerTopBarBase(
        modifier = Modifier
            .fillMaxWidth()
            .background(theme.colorScheme.primaryContainer),
        header = {
            IconButton(onClick = {
                navController.popBackStack(Route.CreateQuizLayout, inclusive = false)
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = stringResource(R.string.move_back_home)
                )
            }
        },
        body = {
            Text(
                text  = stringResource(R.string.app_name),
                style = QuizzerTypographyDefaults.quizzerHeadlineMedium
            )
        },
        actions = {
            IconButton(onClick = onLoadLocal) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = stringResource(R.string.my_quizzes)
                )
            }
        }
    )
}

@Composable
private fun EditorBottomBar(
    navController: NavController,
    onEnterPreview: () -> Unit,
    onSaveLocal:    () -> Unit
) {
    QuizLayoutBottomBar(
        moveBack    = {
            navController.popBackStack(Route.CreateQuizLayout, inclusive = false)
        },
        moveForward = {
            onEnterPreview()
        }
    ) {
        IconButtonWithText(
            imageVector = Icons.Default.Save,
            text        = stringResource(R.string.temp_save),
            onClick     = onSaveLocal,
            description = stringResource(R.string.temp_save),
            iconSize    = 24.dp
        )
        IconButtonWithText(
            imageVector = Icons.Default.Visibility,
            text        = stringResource(R.string.preview),
            onClick     = onEnterPreview,
            description = stringResource(R.string.preview),
            iconSize    = 24.dp
        )
    }
}

@Composable
private fun EditorContent(
    padding:       PaddingValues,
    quizzes:       List<Quiz>,
    pagerState:    PagerState,
    onAddQuiz:     () -> Unit,
    onMoveToCaller:(Int, QuizType, Int) -> Unit
) {
    val vm: QuizCoordinatorViewModel = hiltViewModel()

    Column(
        Modifier
            .padding(padding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state    = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
        ) { page ->
            if (page < quizzes.size) {
                QuizPreview(quizzes[page])
            } else {
                NewQuizAdd(showDialog = { /* show dialog in parent scope */ })
            }
        }
        QuizPagerIndicator(
            currentPage = pagerState.currentPage,
            pageCount   = quizzes.size
        )
        QuizEditIconsRow(
            deleteCurrentQuiz = {
                vm.updateQuizCoordinator(QuizCoordinatorActions.RemoveQuizAt(pagerState.currentPage))
            },
            editCurrentQuiz   = {
                val currentPage = pagerState.currentPage
                if (currentPage < quizzes.size) {
                    onMoveToCaller(
                        currentPage,
                        quizzes[currentPage].quizType,
                        currentPage
                    )
                }
            },
            addQuiz = { onAddQuiz() }
        )
    }
}

@Composable
private fun QuizPagerIndicator(
    currentPage: Int,
    pageCount: Int
) {
    LinearProgressIndicator(
        progress = { (currentPage + 1) / (pageCount + 1).toFloat() },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = ProgressIndicatorDefaults.linearColor,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
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
