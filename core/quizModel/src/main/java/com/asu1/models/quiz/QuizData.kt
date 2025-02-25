package com.asu1.models.quiz

import android.graphics.Bitmap
import com.asu1.utils.images.createEmptyBitmap

data class QuizData(
    var title: String = "",
    var image: Bitmap = createEmptyBitmap(),
    var description: String = "",
    var tags: Set<String> = emptySet(),
    var shuffleQuestions: Boolean = false,
    var creator: String = "GUEST",
    var uuid : String? = null,
)
