package com.asu1.mainpage.composables

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.asu1.resources.R

@Composable
fun NotificationTitle(
    modifier: Modifier = Modifier,
    cleanTitle: String,
    notificationDate: String,
    tagId: Int,
){
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (notificationTitle, notificationTime, notificationTag) = createRefs()

        Text(
            text = cleanTitle,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.constrainAs(notificationTitle) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            },
        )
        Text(
            text = notificationDate,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Light,
            modifier = Modifier.constrainAs(notificationTime) {
                top.linkTo(notificationTitle.bottom, margin = 8.dp)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
            },
        )
        if(tagId != R.string.empty_string){
            TagBadge(
                modifier = Modifier.constrainAs(notificationTag) {
                    top.linkTo(notificationTitle.top)
                    bottom.linkTo(notificationTitle.bottom)
                    start.linkTo(notificationTitle.end, margin = 8.dp)
                },
                tag = tagId
            )
        }
    }
}