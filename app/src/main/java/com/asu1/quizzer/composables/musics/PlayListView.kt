package com.asu1.quizzer.composables.musics

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.quizzer.R
import com.asu1.quizzer.model.TextStyles
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.screens.quiz.BuildBody
import com.asu1.quizzer.util.Logger
import com.asu1.quizzer.util.constants.sampleMusicList
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import com.asu1.quizzer.util.musics.HomeUiEvents

//TODO: 클릭하면 재생하는 기능 + 드래그 끝났을떄 musicList 업데이트 하도록 전달.
@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun PlayListView(
    isPlaying: Boolean,
    currentMusicIndex: Int,
    musicList: List<MusicAllInOne>,
    initOpen: Boolean = true,
    updatePlayer: (HomeUiEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentMusic = remember(currentMusicIndex) { musicList[currentMusicIndex].music.title }
    val localMusicList = remember { mutableStateListOf(*musicList.toTypedArray()) }
    val view = LocalView.current
    val isOpen = remember{ mutableStateOf(initOpen) }
    val lazyListState = rememberLazyListState()
    val reorderableLazyListState = rememberReorderableLazyListState(lazyListState) { from, to ->
        // Update the list
        localMusicList.add(to.index - 1, localMusicList.removeAt(from.index -1))
    }
    val paddingValues = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
    val painter = rememberAnimatedVectorPainter(
        animatedImageVector = AnimatedImageVector.animatedVectorResource(id = R.drawable.music_player),
        atEnd = true
    )
    val dragStart  = remember { mutableStateOf(-1) }

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .wrapContentHeight()
            .heightIn(max = 300.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().clickable(
                    onClick = { isOpen.value = !isOpen.value }
                )
                    .then(paddingValues),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Play List",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                )
                Icon(
                    imageVector = if (isOpen.value) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp,
                    contentDescription = "Toggle Play List",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        items(localMusicList, key = { it.music.title }) { musicItem ->
            AnimatedVisibility(
                visible = isOpen.value,
                enter = slideInVertically(
                    initialOffsetY = { it * 2 },
                    animationSpec = tween(durationMillis = 300)
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it * 2 },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                ReorderableItem(reorderableLazyListState, key = musicItem.music.title) { isDragging ->
                    val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp, label = "")

                    Surface(
                        color = Color.Transparent,
                        shadowElevation = elevation
                    ) {
                        MusicListItem(
                            musicAllInOne = musicItem,
                            modifier = Modifier.fillMaxWidth()
                                .background(
                                    if (currentMusic == musicItem.music.title) MaterialTheme.colorScheme.primaryContainer
                                    else Color.Transparent
                                )
                                .clickable {
                                    updatePlayer(HomeUiEvents.CurrentAudioChanged(localMusicList.indexOf(musicItem)))
                                }
                                .then(paddingValues),
                            playingIcon = { modifier ->
                                if (isPlaying && currentMusic == musicItem.music.title) {
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        modifier = modifier,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            },
                            handle = { modifier ->
                                IconButton(
                                    modifier = modifier.draggableHandle(
                                        onDragStarted = {
                                            dragStart.value = localMusicList.indexOf(musicItem)
                                            if (Build.VERSION_CODES.R <= Build.VERSION.SDK_INT)
                                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_START)
                                        },
                                        onDragStopped = {
                                            Logger.debug("Dragged from ${dragStart.value} to ${localMusicList.indexOf(musicItem)}")
                                            updatePlayer(HomeUiEvents.ChangeItemOrder(dragStart.value, localMusicList.indexOf(musicItem)))
                                            dragStart.value = -1
                                            if (Build.VERSION_CODES.R <= Build.VERSION.SDK_INT)
                                                view.performHapticFeedback(HapticFeedbackConstants.GESTURE_END)
                                        },
                                    ),
                                    onClick = {},
                                ) {
                                    Icon(Icons.Rounded.DragHandle, contentDescription = "Reorder")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PlayListViewPreview() {
    PlayListView(
        isPlaying = false,
        currentMusicIndex = 0,
        musicList = sampleMusicList,
        updatePlayer = {},
    )
}