package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.unit.dp

@Composable
internal fun ConnectColumn(
    modifier: Modifier,
    items: List<String>,
    dotOffsets: SnapshotStateList<Offset>?,
    reverse: Boolean = false,
    dragState: DragState?,
    pointerEvent: suspend PointerInputScope.(Offset, Int) -> Unit,
) {
    Column(
        verticalArrangement  = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier            = modifier,
    ) {
        items.forEachIndexed { index, text ->
            Row(Modifier.fillMaxWidth().padding(bottom = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                if (reverse) {
                    DotCell(
                        boxPosition = dragState?.boxPosition ?: Offset.Zero,
                        setOffset = { offset ->
                            dotOffsets?.set(index, offset)
                        },
                        index,
                        pointerEvent = {},
                    )
                    TextCell(
                        modifier = modifier.weight(1f),
                        text = text,
                    )
                } else {
                    TextCell(
                        modifier = modifier.weight(1f),
                        text = text,
                    )
                    DotCell(
                        boxPosition = dragState?.boxPosition ?: Offset.Zero,
                        setOffset = { offset ->
                            dotOffsets?.set(index, offset)
                        },
                        index,
                        pointerEvent = { offset ->
                            pointerEvent(offset, index)
                        }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}
