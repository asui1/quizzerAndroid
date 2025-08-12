package com.asu1.mainpage.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.activityNavigation.Route
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.quiz.viewmodel.UserViewModel
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

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActivityImage(
            modifier = Modifier.size(60.dp),
            imageUrl = imageUrl,
            quizTitle = userActivity.quizTitle
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            ActivityTitle(title = userActivity.quizTitle)
            Spacer(modifier = Modifier.height(4.dp))
            ActivityProgressInfo(
                correct = userActivity.correctCount,
                total = userActivity.totalCount
            )
            Spacer(modifier = Modifier.height(4.dp))
            ActivityProgressBar(
                solved = userActivity.correctCount,
                total = userActivity.totalCount
            )
            Spacer(modifier = Modifier.height(4.dp))
            ActivitySolvedDate(date = userActivity.solvedDate)
        }
    }
}

@Composable
private fun ActivityImage(
    modifier: Modifier = Modifier,
    imageUrl: String,
    quizTitle: String,
) {
    GlideImage(
        imageModel = { imageUrl },
        modifier = modifier
            .clip(RoundedCornerShape(4.dp)),
        failure = {
            Image(
                painter = painterResource(id = R.drawable.question2),
                contentDescription = "Image for quiz $quizTitle",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    )
}

@Composable
private fun ActivityTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = QuizzerTypographyDefaults.quizzerBodySmallBold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun ActivityProgressInfo(
    correct: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = "$correct / $total",
        style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
    )
}

@Composable
private fun ActivityProgressBar(
    solved: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
    progress = { if (total > 0) solved / total.toFloat() else 0f },
    modifier = modifier
                .fillMaxWidth()
                .height(4.dp),
    color = ProgressIndicatorDefaults.linearColor,
    trackColor = ProgressIndicatorDefaults.linearTrackColor,
    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
    )
}

@Composable
private fun ActivitySolvedDate(
    date: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = date,
        style = QuizzerTypographyDefaults.quizzerLabelSmallLight,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun MyActivitiesScreenPreview() {
    MyActivitiesBody(
        userActivities = sampleUserActivityList.toPersistentList()
    )
}
