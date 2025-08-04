package com.asu1.mainpage.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.mainpage.viewModels.UserViewModel
import com.asu1.resources.BASE_URL_API
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.userdatamodels.UserActivity
import com.asu1.userdatamodels.sampleUserActivityList
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun MyActivitiesScreen(
    navController: NavController,
) {
    val userViewModel: UserViewModel = viewModel()
    val userActivities by userViewModel.userActivities.collectAsStateWithLifecycle(
        persistentListOf()
    )
    MyActivitiesBody(
        moveBackHome = {
            navController.popBackStack(
                Route.Home,
                inclusive = false
            )
        },
        userActivities = userActivities,
    )
}

@Composable
fun MyActivitiesBody(
    moveBackHome: () -> Unit = {},
    userActivities: PersistentList<UserActivity> = persistentListOf()
){
    Scaffold(
        topBar = {
            QuizzerTopBarBase(
                header = @Composable {
                    IconButton(onClick = moveBackHome
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.move_back)
                        )
                    }
                },
                body = {
                    Text(
                        stringResource(R.string.my_activities),
                        style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal
                    )
                },
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues).padding(8.dp).padding(top = 16.dp)
                .verticalScroll(rememberScrollState())
        ){
            if(userActivities.isEmpty()){
                Text(
                    stringResource(
                        R.string.my_activities_empty
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            else{
                userActivities.forEach { userActivity ->
                    UserActivityItem(userActivity)
                    HorizontalDivider(
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UserActivityItem(userActivity: UserActivity) {
    val imageUrl = "${BASE_URL_API}images/${userActivity.quizId}.png"

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (quizTitle, quizImage, quizSolvedDate, quizProgressBar, quizProgessData) = createRefs()
        val startBarrier = createEndBarrier(quizImage)
        GlideImage(
            imageModel = {imageUrl},
            modifier = Modifier.size(60.dp)
                .clip(RoundedCornerShape(4.dp))
                .constrainAs(quizImage){
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
            failure = {
                Image(
                    painter = painterResource(id = R.drawable.question2),
                    contentDescription = "Image for quiz ${userActivity.quizTitle}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            },
        )
        Text(
            userActivity.quizTitle,
            modifier = Modifier.constrainAs(quizTitle){
                start.linkTo(quizImage.end, margin = 4.dp)
                top.linkTo(parent.top)
            },
            style = QuizzerTypographyDefaults.quizzerBodySmallBold
        )

        Text(
            "${userActivity.correctCount} / ${userActivity.totalCount}",
            modifier = Modifier.constrainAs(quizProgessData){
                start.linkTo(quizImage.end, margin = 4.dp)
                top.linkTo(quizTitle.bottom, margin = 4.dp)
            },
            style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        QuizProgressBar(
            modifier = Modifier.constrainAs(quizProgressBar) {
                start.linkTo(startBarrier, margin = 4.dp)
                top.linkTo(quizProgessData.bottom)
                end.linkTo(parent.end, margin = 4.dp)
                width = Dimension.fillToConstraints
            },
            solved = userActivity.correctCount,
            total = userActivity.totalCount,
        )
        Text(
            userActivity.solvedDate,
            modifier = Modifier.constrainAs(quizSolvedDate){
                start.linkTo(quizImage.end, margin = 4.dp)
                top.linkTo(quizProgressBar.bottom, margin = 4.dp)
                bottom.linkTo(parent.bottom)
            },
            style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
        )
    }
}

@Composable
fun QuizProgressBar(
    modifier: Modifier = Modifier,
    solved: Int,
    total: Int,
) {
    val progress = remember(solved, total) {
        if (total == 0) 0f else (solved.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    }
    androidx.compose.material3.LinearProgressIndicator(
        progress = { progress},
        modifier = modifier
            .height(6.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp)),
        color = MaterialTheme.colorScheme.primary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant
    )
}

@Preview(showBackground = true)
@Composable
fun MyActivitiesScreenPreview() {
    MyActivitiesBody(
        userActivities = sampleUserActivityList.toPersistentList()
    )
}