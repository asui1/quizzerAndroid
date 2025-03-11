package com.asu1.quizzer.screens.mainScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.asu1.quizzer.composables.QuizzerTopBarBase
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.UserViewModel
import com.asu1.resources.BASE_URL_API
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.userdatamodels.UserActivity
import com.asu1.userdatamodels.sampleUserActivityList
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map
import java.util.Locale

@Composable
fun MyActivitiesScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    val userActivities by userViewModel.userActivities.map {
        it.toPersistentList()
    }.collectAsStateWithLifecycle(
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
                        style = QuizzerTypographyDefaults.quizzerTopBarTitle
                    )
                },
            )
        }
    ) {paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(8.dp).padding(top = 16.dp)
        ){
            userActivities.forEach { userActivity ->
                UserActivityItem(userActivity)
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
fun UserActivityItem(userActivity: UserActivity) {
    val formattedScore = remember(userActivity.score) {
        String.format(Locale.US, "%.1f", userActivity.score)
    }
    val imageUrl = "${BASE_URL_API}images/${userActivity.quizId}.png"
    ConstraintLayout() {
        val (quizTitle, quizImage, quizSolvedData) = createRefs()
        GlideImage(
            imageModel = {imageUrl},
            modifier = Modifier.size(60.dp)
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
                top.linkTo(parent.top)
                start.linkTo(quizImage.end, margin = 4.dp)
                bottom.linkTo(quizSolvedData.top)
            },
            style = QuizzerTypographyDefaults.quizzerQuizCardDescription
        )
        Text(
            StringBuilder()
                .append(userActivity.solvedDate)
                .append("  /  ")
                .append(formattedScore)
                .toString(),
            modifier = Modifier.constrainAs(quizSolvedData){
                top.linkTo(quizTitle.bottom)
                start.linkTo(quizImage.end, margin = 4.dp)
                bottom.linkTo(parent.bottom)
            },
            style = QuizzerTypographyDefaults.quizzerQuizCardCreator,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyActivitiesScreenPreview() {
    MyActivitiesBody(
        userActivities = sampleUserActivityList.toPersistentList()
    )
}