package com.asu1.quizzer.screens.mainScreen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.appdatamodels.Notification
import com.asu1.appdatamodels.sampleNotification
import com.asu1.appdatamodels.sampleNotificationList
import com.asu1.quizzer.composables.PageSelector
import com.asu1.quizzer.composables.QuizzerTopBarBase
import com.asu1.quizzer.util.Route
import com.asu1.quizzer.viewModels.NotificationViewModel
import com.asu1.quizzer.viewModels.NotificationViewModelEvent
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.map

@Composable
fun NotificationScreen(
    navController: NavController = rememberNavController(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    val notificationPages by notificationViewModel.notificationPages.observeAsState(1)
    val notificationList by notificationViewModel.notificationList
        .map { it.toPersistentList()}
        .collectAsStateWithLifecycle(
            persistentListOf()
        )
    val currentPage by notificationViewModel.currentPage.observeAsState(1)
    val currentNotificationDetail by notificationViewModel.currentNotificationDetail.observeAsState(null)

    BackHandler {
        if(currentNotificationDetail!=null){
            notificationViewModel.updateNotificationViewModel(NotificationViewModelEvent.RemoveNotificationDetail)
        }else{
            navController.popBackStack(Route.Home, inclusive = false)
        }
    }

    NotificationScreenBody(
        navController = navController,
        notificationPages = notificationPages,
        notificationList = notificationList,
        currentPage = currentPage,
        currentNotificationDetail = currentNotificationDetail,
        updateNotificationViewModel = {event ->
            notificationViewModel.updateNotificationViewModel(event)
        }
    )
}

@Composable
fun NotificationScreenBody(
    navController: NavController = rememberNavController(),
    notificationPages: Int = 1,
    notificationList: PersistentList<Notification> = persistentListOf(),
    currentPage: Int = 1,
    currentNotificationDetail: Notification? = null,
    updateNotificationViewModel: (event: NotificationViewModelEvent) -> Unit = {},
){


    Scaffold(
        topBar = {
            QuizzerTopBarBase(
                header = @Composable {
                    IconButton(onClick = {
                        navController.popBackStack(
                            Route.Home,
                            inclusive = false
                        )
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = stringResource(R.string.move_back)
                        )
                    }
                },
                body = {
                    Text(
                        stringResource(R.string.notification),
                        style = QuizzerTypographyDefaults.quizzerTopBarTitle,
                    )
                },
            )
        },
    ){ paddingValues ->
        AnimatedContent(
            targetState = currentNotificationDetail==null, // true if no detail
            label = "Notification Detail Transition",
            modifier = Modifier.padding(paddingValues).padding(8.dp).padding(top = 16.dp),
            transitionSpec = {
                (slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth }, // Enter from the right
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(500))) togetherWith
                        (slideOutHorizontally(
                            targetOffsetX = { fullWidth -> fullWidth }, // Exit to the right
                            animationSpec = tween(durationMillis = 500)
                        ) + fadeOut(animationSpec = tween(500)))
            }
        ) { isEmpty ->
            if(isEmpty){
                Column(
                    modifier = Modifier
                ) {
                    notificationList.forEachIndexed { index, notification ->
                        NotificationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    updateNotificationViewModel(NotificationViewModelEvent.GetNotificationDetail(notification.id))
                                },
                            notification = notification,
                            trailingIcon = {modifier ->
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = stringResource(
                                        R.string.next
                                    ),
                                    modifier = modifier,
                                )
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                        )
                    }
                    PageSelector(
                        modifier = Modifier.fillMaxWidth(),
                        currentPage = currentPage,
                        totalPages = notificationPages,
                    ) {
                        updateNotificationViewModel(NotificationViewModelEvent.GetNotificationList(it))
                    }
                }
            }else{
                if(currentNotificationDetail != null)
                    NotificationDetail(
                        modifier = Modifier.fillMaxWidth(),
                        notification = currentNotificationDetail,
                    )
            }
        }
    }
}

@Composable
fun NotificationItem(
    modifier: Modifier = Modifier,
    notification: Notification,
    trailingIcon: @Composable (Modifier) -> Unit = {}
) {
    ConstraintLayout(
        modifier = modifier
    ) {
        val (notificationTitle, notificationTime, proceedIcon) = createRefs()
        Text(
            text = notification.title,
            style = QuizzerTypographyDefaults.quizzerListItemTitle,
            maxLines = 1,
            modifier = Modifier.constrainAs(notificationTitle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
        )
        Text(
            text = notification.date,
            style = QuizzerTypographyDefaults.quizzerListItemSub,
            modifier = Modifier.constrainAs(notificationTime) {
                top.linkTo(notificationTitle.bottom, margin = 2.dp)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
            },
        )
        trailingIcon(
            Modifier.constrainAs(proceedIcon) {
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
        )
    }
}

@Composable
fun NotificationDetail(
    modifier: Modifier = Modifier,
    notification: Notification,
){
    Column(
        modifier = modifier,
    ){
        NotificationItem(
            notification = notification
        )
        Spacer(
            modifier = Modifier.padding(8.dp)
        )
        Text(
            notification.body,
            style = QuizzerTypographyDefaults.quizzerQuizCardDescription,
        )
    }
}

@Preview
@Composable
fun NotificationScreenPreview() {
    QuizzerAndroidTheme {
        NotificationScreenBody(
            notificationPages = 5,
            currentPage = 2,
            currentNotificationDetail = null,
            notificationList =sampleNotificationList.toPersistentList()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationItemPreview(){
    QuizzerAndroidTheme {
        NotificationItem(
            notification = sampleNotification,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationDetailPreview(){
    QuizzerAndroidTheme{
        NotificationDetail(
            notification = sampleNotification
        )
    }
}