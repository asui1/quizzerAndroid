package com.asu1.quizzer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.quizzer.R
import com.asu1.quizzer.composables.RowWithAppIconAndName
import com.asu1.quizzer.model.QuizType
import com.asu1.quizzer.screens.quiz.QuizViewer
import com.asu1.quizzer.ui.theme.QuizzerAndroidTheme
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.NavMultiClickPreventer
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.QuizLayoutViewModel
import com.asu1.quizzer.viewModels.ScoreCardViewModel

val bodyHeight = 600.dp

@Composable
fun QuizBuilderScreen(navController: NavController,
                      quizLayoutViewModel: QuizLayoutViewModel = viewModel(),
                      onMoveToScoringScreen: () -> Unit = {},
                      scoreCardViewModel: ScoreCardViewModel = viewModel(),
) {
    val quizzes by quizLayoutViewModel.quizzes.observeAsState(emptyList())
    val quizTheme by quizLayoutViewModel.quizTheme.collectAsState()
    val colorScheme = quizTheme.colorScheme
    var showNewQuizDialog by remember { mutableStateOf(false) }
    var curIndex by remember{mutableStateOf(0)}
    val screenWidthHalf = LocalConfiguration.current.screenWidthDp.dp/2
    val padding = screenWidthHalf - 150.dp
    val snapLayoutInfoProvider = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(snapLayoutInfoProvider)
    val initialIndex by quizLayoutViewModel.initIndex.observeAsState(0)
    val context = LocalContext.current

    LaunchedEffect(snapLayoutInfoProvider) {
        snapshotFlow { snapLayoutInfoProvider.firstVisibleItemIndex }
            .collect { index ->
                curIndex = index
                quizLayoutViewModel.updateInitIndex(curIndex)
                Logger().debug("Current Index : $index")
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
                title = { Text("Select Question Type") },
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
                                    .clickable{
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
                    modifier = Modifier.fillMaxWidth(),
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
                            TODO("Implement Load Local.")
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
                        quizLayoutViewModel.saveLocal(context, scoreCardViewModel.scoreCard.value)
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
                        .padding(start = padding, end = padding),
                ) {
                    items(quizzes.size) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier.pointerInput(Unit) { detectTapGestures {} }
                                .size(width = 292.dp, height = bodyHeight)
                                .border(
                                    width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(16.dp)
                                )
                        ) {
                            QuizViewer(
                                quiz = quizzes[it],
                                quizTheme = quizTheme,
                                updateUserInput = { newQuiz ->
                                },
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
            }
        }
    }

}

@Composable
fun NewQuizAdd(
    showDialog: (Boolean) -> Unit = {},
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(width = 300.dp, height = bodyHeight)
            .background(color = Color.LightGray, shape = RoundedCornerShape(16.dp)),
    ) {
        FloatingActionButton(onClick = { showDialog(true) }) {
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
            .padding(16.dp)
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
            text = "Quiz : $curIndexText / $totalQuizzes",
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
            Text("Preview")
        }
        IconButton(
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