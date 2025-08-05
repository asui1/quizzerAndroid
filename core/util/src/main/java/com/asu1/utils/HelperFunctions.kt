package com.asu1.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

fun <T> MutableCollection<T>.toggle(element: T) {
    if (!add(element)) remove(element)
}

const val DRAG_OFFSET_SIZE = 30

fun getDragIndex(target: Offset, items: List<Offset>): Int? {
    Logger.debug("update with $target")
    Logger.debug(items)

    // only search if we're close enough in X
    val candidate: Int? = if (abs(target.x - items[0].x) <= DRAG_OFFSET_SIZE) {
        // find the first Y-match, or null
        items.indices.firstOrNull { i ->
            abs(target.y - items[i].y) < DRAG_OFFSET_SIZE
        }
    } else {
        null
    }
    // log if we actually found something
    candidate?.let { Logger.debug(it) }
    return candidate
}
