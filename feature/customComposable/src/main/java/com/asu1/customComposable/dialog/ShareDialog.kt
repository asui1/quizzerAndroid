package com.asu1.customComposable.dialog

import SnackBarManager
import ToastType
import android.content.ClipData
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.Clipboard
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.asu1.customComposable.button.IconButtonWithText
import com.asu1.resources.BASE_URL
import com.asu1.resources.QuizzerTypographyDefaults
import com.asu1.resources.R
import com.asu1.utils.generateUniqueId
import kotlinx.coroutines.launch

const val RESULT_URL_BASE = "${BASE_URL}?resultId="
const val QUIZ_URL_BASE = "${BASE_URL}?quizId="

@Composable
fun ShareDialog(
    quizId: String,
    userName: String = "Guest",
    onDismiss: () -> Unit = { },
){
    val quizUrl = remember(quizId){ QUIZ_URL_BASE + quizId}
    val resultUrl = remember(quizId){ RESULT_URL_BASE + generateUniqueId(quizId, userName) }
    val clipboard: Clipboard = LocalClipboard.current
    val scope = rememberCoroutineScope()

    fun copyAndClose(url: String, @StringRes msgRes: Int) {
        scope.launch {
            val clip = ClipData.newPlainText(url, url)
            clipboard.setClipEntry(ClipEntry(clip))
            SnackBarManager.showSnackBar(msgRes, ToastType.SUCCESS)
            onDismiss()
        }
    }
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ){
        ShareDialogHeader()

        Spacer(modifier = Modifier.height(8.dp))

        RowWithShares(
            shareLink = if (userName == "Guest") quizUrl else resultUrl,
            onDismiss = onDismiss,
            modifier  = Modifier
                .border(1.dp,
                    MaterialTheme.colorScheme.outline,
                    RoundedCornerShape(8.dp))
        )

        Spacer(Modifier.height(8.dp))

        // 3) Copy‐link rows
        CopyLinkRow(
            textRes = R.string.copy_quiz_link,
            onClick = { copyAndClose(quizUrl, R.string.link_copied_to_clipboard) }
        )

        if (userName != "Guest") {
            CopyLinkRow(
                textRes = R.string.copy_quiz_result_link,
                onClick = { copyAndClose(resultUrl, R.string.link_copied_to_clipboard) }
            )
        }
    }
}

@Composable
fun RowWithShares(
    modifier: Modifier = Modifier,
    shareLink: String = "",
    onDismiss: () -> Unit = { },
){
    val context = LocalContext.current
    LazyRow(
        horizontalArrangement = Arrangement.Start,
        contentPadding = PaddingValues(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        item{
            IconButtonWithText(
                text = "Facebook",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setData("https://www.facebook.com/sharer/sharer.php?u=$shareLink".toUri())
                        setPackage("com.facebook.katana")
                    }
                    context.startActivity(intent)
                    onDismiss()
                },
                imageVector = Icons.Default.Facebook,
                iconSize = 40.dp,
            )
        }
        item{
            IconButtonWithText(
                resourceId = R.drawable.x_logo,
                text = "X",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setData("https://twitter.com/intent/tweet?url=$shareLink".toUri())
                        setPackage("com.twitter.android")
                    }
                    context.startActivity(intent)
                    onDismiss()
                },
                iconSize = 40.dp,
            )
        }
    }
}

@Composable
private fun ShareDialogHeader() {
    Text(
        text = stringResource(R.string.share),
        style = QuizzerTypographyDefaults.quizzerHeadlineMediumBold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    )
}

@Composable
private fun CopyLinkRow(
    @StringRes textRes: Int,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector    = Icons.Default.ContentCopy,
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text  = stringResource(textRes),
            style = QuizzerTypographyDefaults.quizzerBodyMediumNormal,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShareDialogPreview(){
    ShareDialog(
        quizId = "1234",
        userName = "ASUI"
    )
}
