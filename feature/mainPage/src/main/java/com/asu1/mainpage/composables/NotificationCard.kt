package com.asu1.mainpage.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.appdatamodels.Notification
import com.asu1.appdatamodels.sampleNotification
import com.asu1.resources.QuizzerAndroidTheme
import com.asu1.resources.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NotificationCard(
    modifier: Modifier = Modifier,
    notification: Notification,
    onClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            with(sharedTransitionScope) {
                NotificationTitle(
                    modifier = Modifier.weight(1f)
                        .sharedElement(
                            rememberSharedContentState(key = notification.id),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                    cleanTitle = notification.cleanTitle,
                    notificationDate = notification.date,
                    tagId = notification.tag,
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "View",
                tint = Color.Gray,
            )
        }
    }
}

@Composable
fun TagBadge(
    modifier: Modifier = Modifier,
    tag: Int
) {
    val (backgroundColor, overlayColor) = when (tag) {
        R.string.update -> Pair(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
        R.string.notification -> Pair(MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onTertiaryContainer)
        else -> Pair(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.onPrimaryContainer)
    }

    Box(
        modifier = modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = stringResource(tag),
            color = overlayColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun PreviewNotificationCard() {
    QuizzerAndroidTheme {
        SharedTransitionLayout {
            AnimatedContent(
                targetState = true,
                label = "PreviewTransition"
            ) { state ->
                if(state){
                    NotificationCard(
                        notification = sampleNotification,
                        animatedVisibilityScope = this@AnimatedContent,
                        sharedTransitionScope = this@SharedTransitionLayout,
                        onClick = {},
                    )
                }
            }
        }
    }
}
