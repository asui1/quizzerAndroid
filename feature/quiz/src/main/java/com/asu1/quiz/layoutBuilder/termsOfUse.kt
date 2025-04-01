package com.asu1.quiz.layoutBuilder

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun getTermsOfUseEn(): AnnotatedString {
    return buildAnnotatedString {
        // Bullet-style content
        append("✓ Quizzes containing content that may cause social controversy, such as disparagement or ridicule, may be deleted.\n\n")
        append("✓ Quizzes that infringe on rights such as copyright, portrait rights, or include inappropriate content (e.g., pornography) may be removed without consent.\n\n")
        append("✓ The creator is solely responsible for any issues arising from the quizzes they create.\n\n")
        append("✓ ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Quizzer does not take any responsibility for such issues.\n\n")
        }

        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
            append("If you agree with the above policies, please click the ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("AGREE")
        }
        append(" button.")
    }
}

fun getTermsOfUseKo(): AnnotatedString {
    return buildAnnotatedString {
        append("✓ 조롱, 비방 등 사회적 논란을 일으킬 수 있는 내용을 포함한 퀴즈는 삭제될 수 있습니다.\n\n")
        append("✓ 저작권, 초상권 침해 또는 음란물 등 부적절한 내용을 포함한 퀴즈는 작성자의 동의 없이 삭제될 수 있습니다.\n\n")
        append("✓ 사용자가 생성한 퀴즈로 인해 발생하는 문제의 책임은 전적으로 사용자 본인에게 있습니다.\n\n")
        append("✓ ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Quizzer는 해당 문제에 대해 책임을 지지 않습니다.\n\n")
        }

        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium)) {
            append("위 내용에 동의하신다면 ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("동의")
        }
        append(" 버튼을 눌러주세요.")
    }
}