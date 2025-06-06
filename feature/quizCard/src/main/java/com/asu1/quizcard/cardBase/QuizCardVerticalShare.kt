package com.asu1.quizcard.cardBase

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import com.asu1.customComposable.dialog.ShareDialog
import com.asu1.quizcardmodel.QuizCard
import com.asu1.quizcardmodel.sampleQuizCard
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VerticalQuizCardLargeShare(
    modifier: Modifier = Modifier,
    quizCard: QuizCard,
    onClick: () -> Unit = {},
    onIconClick: (String) -> Unit = {},
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    val screenHeight = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.height.toDp() }
    }
    val screenWidth = remember(windowInfo, density) {
        with(density) { windowInfo.containerSize.width.toDp() }
    }
    val minSize = remember{minOf(screenWidth, screenHeight).times(0.45f).coerceAtMost(400.dp)}
    var showShareBottomSheet by remember{ mutableStateOf(false) }

    if(showShareBottomSheet) {
        ModalBottomSheet(onDismissRequest = {showShareBottomSheet = false },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ) {
            ShareDialog(
                quizId = quizCard.id,
                onDismiss = {
                    showShareBottomSheet = false
                }
            )
        }
    }
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                VerticalQuizCardLargeBody(
                    quizCard = quizCard,
                    minSize = minSize,
                ){
                    with(sharedTransitionScope){
                        QuizImage(
                            uuid = quizCard.id,
                            title = quizCard.title,
                            modifier = Modifier
                                .size(minSize) // Square shape
                                .sharedElement(
                                    rememberSharedContentState(key = quizCard.id),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                ){
                    IconButton(
                        onClick = { showShareBottomSheet = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Quiz",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = {onIconClick(quizCard.id) },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer, // Soft background
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer  // Readable text
                        ),
                        shape =
                            RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            stringResource(R.string.get_quiz),
                            style = QuizzerTypographyDefaults.quizzerLabelSmallMedium
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
fun VerticalQuizCardLargeSharePreview() {
    val quizCard = sampleQuizCard
    SharedTransitionLayout {
        AnimatedContent(
            true,
            label = "",
        ){targetState ->
            if(targetState){
                VerticalQuizCardLargeShare(
                    quizCard = quizCard,
                    onClick = {},
                    onIconClick = {},
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedContent,
                )
            }
        }
    }
}