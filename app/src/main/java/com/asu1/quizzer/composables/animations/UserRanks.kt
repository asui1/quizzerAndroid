package com.asu1.quizzer.composables.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@Composable
fun UserRankAnimation(modifier: Modifier = Modifier) {
    val thisweek = remember{
        getCurrentWeekDateRangeInKST()
    }
    Row(
        verticalAlignment = Alignment.CenterVertically ,
        modifier = modifier,
    ) {
        DotLottieAnimation(
            source = DotLottieSource.Asset("userrank.lottie"),
            autoplay = true,
            loop = false,
            speed = 1f,
            useFrameInterpolation = false,
            playMode = Mode.FORWARD,
            modifier = Modifier
                .background(Color.Transparent)
                .weight(1f)
                .fillMaxHeight()
        )
        Text(
            modifier = Modifier
                .background(Color.Transparent)
                .weight(2f),
            fontStyle = FontStyle.Italic,
            text = thisweek,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

fun getCurrentWeekDateRangeInKST(): String {
    val kstZoneId = ZoneId.of("Asia/Seoul")
    val currentDateInKST = LocalDate.now(kstZoneId)
    val startOfWeek = currentDateInKST.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = currentDateInKST.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    val formatter = DateTimeFormatter.ofPattern("MM/dd")
    val startOfWeekFormatted = startOfWeek.format(formatter)
    val endOfWeekFormatted = endOfWeek.format(formatter)
    return "$startOfWeekFormatted ~ $endOfWeekFormatted"
}
