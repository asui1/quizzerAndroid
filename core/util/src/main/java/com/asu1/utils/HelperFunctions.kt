package com.asu1.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

fun <T> MutableCollection<T>.toggle(element: T) {
    if (!add(element)) remove(element)
}

const val dragOffsetSize = 30

fun getDragIndex(target: Offset, items: List<Offset>) : Int?{
    Logger.debug("update with $target")
    Logger.debug(items)
    if(abs(target.x - items[0].x) > dragOffsetSize) return null
    for(i in items.indices){
        if(abs(target.y - items[i].y) < dragOffsetSize) {
            Logger.debug(i)
            return i
        }
    }
    return null
}