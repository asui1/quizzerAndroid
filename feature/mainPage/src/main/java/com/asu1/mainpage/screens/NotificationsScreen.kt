package com.asu1.mainpage.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.asu1.activityNavigation.Route
import com.asu1.appdatamodels.Notification
import com.asu1.appdatamodels.sampleNotification
import com.asu1.appdatamodels.sampleNotificationList
import com.asu1.customComposable.pageSelector.PageSelector
import com.asu1.customComposable.topBar.QuizzerTopBarBase
import com.asu1.mainpage.composables.NotificationCard
import com.asu1.mainpage.composables.NotificationTitle
import com.asu1.mainpage.viewModels.NotificationViewModel
import com.asu1.mainpage.viewModels.NotificationViewModelEvent
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

@OptIn(ExperimentalSharedTransitionApi::class)
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
                        style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal,
                    )
                },
            )
        },
    ) { paddingValues ->
        SharedTransitionLayout {
            AnimatedContent(
                targetState = currentNotificationDetail == null, // true if no detail
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
                if (isEmpty) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(WindowInsets.safeDrawing.asPaddingValues())
                    ) {
                        notificationList.forEachIndexed { index, notification ->
                            NotificationCard(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                notification = notification,
                                onClick = {
                                    updateNotificationViewModel(
                                        NotificationViewModelEvent.GetNotificationDetail(
                                            notification.id
                                        )
                                    )
                                },
                                animatedVisibilityScope = this@AnimatedContent,
                                sharedTransitionScope = this@SharedTransitionLayout,
                            )
                        }
                        PageSelector(
                            modifier = Modifier.fillMaxWidth(),
                            currentPage = currentPage,
                            totalPages = notificationPages,
                        ) {
                            updateNotificationViewModel(
                                NotificationViewModelEvent.GetNotificationList(
                                    it
                                )
                            )
                        }
                    }
                } else {
                    if (currentNotificationDetail != null)
                        NotificationDetail(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            notification = currentNotificationDetail,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = this@AnimatedContent,
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NotificationDetail(
    modifier: Modifier = Modifier,
    notification: Notification,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
){
    Column(
        modifier = modifier,
    ){
        with(sharedTransitionScope) {
            NotificationTitle(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = notification.id),
                        animatedVisibilityScope = animatedVisibilityScope,
                    ),
                cleanTitle = notification.cleanTitle,
                notificationDate = notification.date,
                tagId = notification.tag,
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            notification.body,
            style = QuizzerTypographyDefaults.quizzerBodySmallNormal,
            lineHeight = 22.sp,
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun NotificationDetailPreview(){
    QuizzerAndroidTheme{
        SharedTransitionLayout {
            AnimatedContent(
                targetState = true,
                label = "PreviewTransition"
            ) { state ->
                if (state) {
                    NotificationDetail(
                        notification = sampleNotification,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                    )
                }
            }
        }
    }
}