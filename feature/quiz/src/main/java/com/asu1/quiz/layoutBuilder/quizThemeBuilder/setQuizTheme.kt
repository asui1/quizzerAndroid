package com.asu1.quiz.layoutBuilder.quizThemeBuilder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.models.quiz.QuizTheme
import com.asu1.quiz.viewmodel.quizLayout.QuizCoordinatorActions
import com.asu1.resources.QuizzerAndroidTheme

@Composable
fun QuizLayoutSetQuizTheme(
    quizTheme: QuizTheme = QuizTheme(),
    isTitleImageSet: Boolean = false,
    updateQuizCoordinator: (QuizCoordinatorActions) -> Unit = {},
){
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 상단 -> Generate New Scheme: 배경/메인 색/세팅
        GenerateNewColorScheme(
            isTitleImageSet = isTitleImageSet,
            generateColorScheme = {quizCoordinatorAction ->
                updateQuizCoordinator(quizCoordinatorAction)
            }
        )
        // 중앙 -> Scroll 가능한 퀴즈 예시
        Spacer(modifier = Modifier.height(16.dp))
        QuizThemeExample(
            quizTheme = quizTheme,
        )
        // 하단 -> Scroll 가능한 Color Scheme의  색들 표시.
        Spacer(modifier = Modifier.height(16.dp))
        Text("Bakground") // 선택하면 아래에 배경 선택 열림.
        Text("Scrollable Colors") // 옆으로 펼쳐진 색 중 하나 선택하면 color picker 열림.
        Text("Scrollable Texts") // 텍스트 포맷 누르면 수정 가능한 뭔가 열림.
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewQuizLayoutSetQuizTheme(){
    QuizzerAndroidTheme {
        QuizLayoutSetQuizTheme()
    }
}

