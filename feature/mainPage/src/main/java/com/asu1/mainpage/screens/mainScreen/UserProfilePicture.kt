package com.asu1.mainpage.screens.mainScreen

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asu1.quiz.viewmodel.UserViewModel
import com.asu1.quiz.viewmodel.sampleUserData
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun UserProfilePic(
    modifier: Modifier = Modifier,
    userData: UserViewModel.UserData?,
    onClick: () -> Unit = {},
    iconSIze: Dp = 30.dp) {
    val urlToImage = userData?.urlToImage
    IconButton(onClick = onClick, modifier = modifier) {
        if (userData != null) {
            UriImageButton(modifier = Modifier
                .size(iconSIze)
                .clip(shape = RoundedCornerShape(8.dp)),
                urlToImage = urlToImage,
                nickname = userData.nickname?.get(0) ?: 'O'
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserProfilePic(){
    QuizzerAndroidTheme {
        UserProfilePic(
            modifier = Modifier,
            userData = sampleUserData,
            onClick = {},
        )
    }
}
