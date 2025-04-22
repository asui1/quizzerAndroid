package com.asu1.utils

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

fun <T> MutableCollection<T>.toggle(element: T) {
    if (!add(element)) remove(element)
}

fun getDragIndex(target: Offset, items: List<Offset>) : Int?{
    if(abs(target.x - items[0].x) > 25) return null
    for(i in items.indices){
        if(abs(target.y - items[i].y) < 25) {
            return i
        }
    }
    return null
}