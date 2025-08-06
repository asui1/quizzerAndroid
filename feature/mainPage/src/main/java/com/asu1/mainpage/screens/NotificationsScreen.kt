package com.asu1.mainpage.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ContentTransform
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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

@Composable
fun NotificationScreen(
    navController: NavController = rememberNavController(),
    notificationViewModel: NotificationViewModel = hiltViewModel()
) {
    // Collect state from ViewModel
    val pages by notificationViewModel.notificationPages.observeAsState(1)
    val list by notificationViewModel.notificationList.collectAsStateWithLifecycle(persistentListOf())
    val currentPage by notificationViewModel.currentPage.observeAsState(1)
    val detail by notificationViewModel.currentNotificationDetail.observeAsState(null)

    // Remember screen state
    val state = rememberNotificationScreenState(
        notificationPages = pages,
        notificationList = list,
        currentPage = currentPage,
        currentDetail = detail
    )
    val onEvent: (NotificationViewModelEvent) -> Unit = { notificationViewModel.updateNotificationViewModel(it) }

    // Delegate to content with state
    NotificationScreenContent(
        navController = navController,
        state = state,
        onEvent = onEvent
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NotificationScreenContent(
    navController: NavController,
    state: NotificationScreenState,
    onEvent: (NotificationViewModelEvent) -> Unit
) {
    // Top App Bar
    Scaffold(topBar = { NotificationTopBar(navController) }) { paddingValues ->
        // Shared Transitions for list/detail views
        SharedTransitionLayout(modifier = Modifier.padding(paddingValues)) {
            AnimatedContent(
                targetState = state.currentDetail == null,
                transitionSpec = { notificationTransitionSpec() },
                modifier = Modifier.padding(8.dp)
            ) { showList ->
                if (showList) {
                    NotificationListView(
                        notifications = state.notifications,
                        currentPage = state.currentPage,
                        totalPages = state.totalPages,
                        onClickItem = { id -> onEvent(NotificationViewModelEvent.GetNotificationDetail(id)) },
                        onPageSelected = { page -> onEvent(NotificationViewModelEvent.GetNotificationList(page)) },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                    )
                } else {
                    NotificationDetailView(
                        notification = state.currentDetail!!,
                        onBack = { onEvent(NotificationViewModelEvent.RemoveNotificationDetail) },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedContent,
                    )
                }
            }
        }
    }
}

// --- State holder encapsulating all screen state ---
@Stable
data class NotificationScreenState(
    val notifications: List<Notification>,
    val currentPage: Int,
    val totalPages: Int,
    val currentDetail: Notification?
)

@Composable
fun rememberNotificationScreenState(
    notificationPages: Int,
    notificationList: PersistentList<Notification>,
    currentPage: Int,
    currentDetail: Notification?
): NotificationScreenState = remember(notificationList, currentPage, currentDetail) {
    NotificationScreenState(
        notifications = notificationList,
        currentPage = currentPage,
        totalPages = notificationPages,
        currentDetail = currentDetail
    )
}

// --- Top Bar ---
@Composable
private fun NotificationTopBar(navController: NavController) {
    QuizzerTopBarBase(
        header = {
            IconButton(onClick = { navController.popBackStack(Route.Home, false) }) {
                Icon(Icons.Default.ArrowBackIosNew, contentDescription = stringResource(R.string.move_back))
            }
        },
        body = {
            Text(stringResource(R.string.notification), style = QuizzerTypographyDefaults.quizzerHeadlineSmallNormal)
        }
    )
}

// --- List View ---
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun NotificationListView(
    notifications: List<Notification>,
    currentPage: Int,
    totalPages: Int,
    onClickItem: (Int) -> Unit,
    onPageSelected: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        notifications.forEachIndexed { index, notification ->
            NotificationCard(
                modifier = Modifier.fillMaxWidth(),
                notification = notification,
                onClick = { onClickItem(notification.id) },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
        PageSelector(
            modifier = Modifier.fillMaxWidth(),
            currentPage = currentPage,
            totalPages = totalPages,
            moveToPage = onPageSelected
        )
    }
}

// --- Detail View ---
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun NotificationDetailView(
    notification: Notification,
    onBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
        }
        NotificationDetail(
            notification = notification,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun NotificationDetail(
    notification: Notification,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        // Shared element title transition
        with(sharedTransitionScope) {
            NotificationTitle(
                modifier = Modifier
                    .sharedElement(
                        rememberSharedContentState(key = notification.id),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                cleanTitle = notification.cleanTitle,
                notificationDate = notification.date,
                tagId = notification.tag
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Body text
        Text(
            text = notification.body,
            style = QuizzerTypographyDefaults.quizzerBodySmallNormal,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 22.sp
        )
    }
}

// --- Transition Spec ---
private fun notificationTransitionSpec(): ContentTransform =
        (slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth }, // Enter from the right
            animationSpec = tween(durationMillis = 500)
        ) + fadeIn(animationSpec = tween(500))) togetherWith
                (slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth }, // Exit to the right
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(500)))

@Preview
@Composable
fun NotificationScreenPreview() {
    val navController = rememberNavController()
    val state = rememberNotificationScreenState(
        notificationPages = 5,
        notificationList = sampleNotificationList.toPersistentList(),
        currentPage = 2,
        currentDetail = null,
    )
    QuizzerAndroidTheme {
        NotificationScreenContent(
            navController = navController,
            state = state,
            onEvent = {},
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
