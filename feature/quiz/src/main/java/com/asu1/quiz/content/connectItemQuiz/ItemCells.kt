package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.platform.LocalDensity
import com.asu1.quiz.content.DOT_SIZE_DP
import com.asu1.quiz.content.MOVE_OFFSET_DP
import com.asu1.quiz.content.PADDING_SIZE_DP
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle

@Composable
internal fun DotCell(
    boxPosition: Offset,
    setOffset: (Offset) -> Unit,
    index: Int,
    pointerEvent: suspend PointerInputScope.(Offset) -> Unit = {},
) {
    val moveOffset = with(LocalDensity.current) { MOVE_OFFSET_DP.toPx() }
    DraggableDot(
        setOffset = setOffset,
        pointerEvent = { offset ->
            pointerEvent(offset)
        },
        boxPosition = boxPosition,
        dotSize     = DOT_SIZE_DP,
        padding     = PADDING_SIZE_DP,
        moveOffset  = moveOffset,
        key         = "Dot$index"
    )
}

@Composable
internal fun TextCell(modifier: Modifier, text: String) {
    AnswerTextStyle.GetTextComposable(text, modifier)
}
