package com.asu1.quizzer.util

import com.asu1.quizzer.R
import com.asu1.quizzer.viewModels.UserViewModel
import com.materialkolor.Contrast
import com.materialkolor.PaletteStyle

val ColorList = listOf("PrimaryColor", "SecondaryColor", "TertiaryColor")
val paletteSize = PaletteStyle.entries.size
val contrastSize = Contrast.entries.size
enum class GenerateWith{
    TITLE_IMAGE, COLOR
}
val userDataTest = UserViewModel.UserDatas("whwkd122@gmail.com", "whwkd122", null, setOf("tag1", "tag2"))
val emptyUserDataTest = UserViewModel.UserDatas(null, null, null, setOf())
val questionTypes = listOf(R.drawable.questiontype1, R.drawable.questiontype2, R.drawable.questiontype3, R.drawable.questiontype4)