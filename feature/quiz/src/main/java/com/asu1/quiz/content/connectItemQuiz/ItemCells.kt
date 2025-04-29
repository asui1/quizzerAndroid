package com.asu1.quiz.content.connectItemQuiz

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.platform.LocalDensity
import com.asu1.quiz.content.dotSizeDp
import com.asu1.quiz.content.moveOffsetDp
import com.asu1.quiz.content.paddingDp
import com.asu1.quiz.ui.textStyleManager.AnswerTextStyle

@Composable
internal fun DotCell(
    boxPosition: Offset,
    setOffset: (Offset) -> Unit,
    index: Int,
    pointerEvent: suspend PointerInputScope.(Offset) -> Unit = {},
) {
    val moveOffset = with(LocalDensity.current) { moveOffsetDp.toPx() }
    DraggableDot(
        setOffset = setOffset,
        pointerEvent = { offset ->
            pointerEvent(offset)
        },
        boxPosition = boxPosition,
        dotSize     = dotSizeDp,
        padding     = paddingDp,
        moveOffset  = moveOffset,
        key         = "Dot$index"
    )
}

@Composable
internal fun TextCell(modifier: Modifier, text: String) {
    AnswerTextStyle.GetTextComposable(text, modifier)
}
