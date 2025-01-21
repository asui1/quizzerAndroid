package com.asu1.customdialogs

import ToastManager
import ToastType
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asu1.custombuttons.IconButtonWithText
import com.asu1.resources.BASE_URL
import com.asu1.resources.R
import com.asu1.utils.generateUniqueId

const val resultUrlBase = "${BASE_URL}?resultId="
const val quizUrlBase = "${BASE_URL}?quizId="

@Composable
fun ShareDialog(
    quizId: String,
    userName: String = "Guest",
    onDismiss: () -> Unit = { },
){
    val quizUrl = remember(quizId){ quizUrlBase + quizId}
    val resultUrl = remember(quizId){ resultUrlBase + generateUniqueId(quizId, userName) }
    val shareLink = if(userName == "Guest") quizUrl else resultUrl
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 16.dp, horizontal = 24.dp)
    ){
        Text(
            text = stringResource(R.string.share),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        RowWithShares(
            shareLink = shareLink,
            onDismiss = onDismiss,
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(8.dp))
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .clickable {
                    clipboardManager.setText(AnnotatedString(resultUrl))
                    ToastManager.showToast(
                        message = R.string.link_copied_to_clipboard,
                        type = ToastType.SUCCESS,
                    )
                    onDismiss()
                },
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Quiz Link",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.copy_quiz_link),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .fillMaxWidth(),
                maxLines = 1,
            )
        }
        if(userName != "Guest"){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .clickable {
                        clipboardManager.setText(AnnotatedString(resultUrl))
                        ToastManager.showToast(
                            message = R.string.link_copied_to_clipboard,
                            type = ToastType.SUCCESS,
                        )
                        onDismiss()
                    },
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy Quiz Result Link",
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.copy_quiz_result_link),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .fillMaxWidth(),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
fun RowWithShares(
    shareLink: String = "",
    onDismiss: () -> Unit = { },
    modifier: Modifier = Modifier,
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
                        data = Uri.parse("https://www.facebook.com/sharer/sharer.php?u=$shareLink")
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
                        data = Uri.parse("https://twitter.com/intent/tweet?url=$shareLink")
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

@Preview(showBackground = true)
@Composable
fun ShareDialogPreview(){
    ShareDialog(
        quizId = "1234",
        userName = "ASUI"
    )
}