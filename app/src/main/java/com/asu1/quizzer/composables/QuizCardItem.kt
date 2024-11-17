import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asu1.quizzer.R
import com.asu1.quizzer.model.QuizCard
import com.asu1.quizzer.composables.HorizontalPagerIndicator
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Composable
fun HorizontalQuizCardItemVertical(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        items(quizCards) { quizCard ->
            QuizCardItemVertical(quizCard, onClick)
        }
    }
}

@Composable
fun QuizCardItemVertical(quizCard: QuizCard, onClick: (String) -> Unit = {}) {
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = quizCard.image?.let { byteArray ->
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    } ?: loadImageAsByteArray(context, R.drawable.question2).let { byteArray ->
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .width(166.dp)
            .wrapContentHeight()
            .padding(horizontal = 4.dp)
            .clickable { onClick(quizCard.id) }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            imageBitmap.let {
                Box(
                    modifier = Modifier
                        .size(150.dp) // Square shape
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        bitmap = it,
                        contentDescription = "Quiz Image for " + quizCard.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize() // Expand to fill the square shape
                    )
                }
            }
            Text(
                text = quizCard.title,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                minLines = 2,
                maxLines = 2,
            )
            Text(
                text = quizCard.creator,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
    }
}

@Composable
fun HorizontalQuizCardItemLarge(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {

    val listState = rememberPagerState(
        initialPage = 0,
    ){
        quizCards.size
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
            .wrapContentHeight()
    ){
        HorizontalPager(
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return ((availableSpace - 2 * pageSpacing) * 0.95f).toInt()
                }
            },
            pageSpacing = 4.dp,
            state = listState,
            modifier = Modifier.fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
        ) { page ->
            QuizCardLarge(quizCards[page], onClick)
        }
        HorizontalPagerIndicator(
            pageCount = quizCards.size,
            currentPage = listState.currentPage,
            targetPage = listState.targetPage,
            currentPageOffsetFraction = listState.currentPageOffsetFraction
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizCardLarge(quizCard: QuizCard, onClick: (String) -> Unit = {}) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    val imageBitmap: ImageBitmap = quizCard.image?.let { byteArray ->
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    } ?: loadImageAsByteArray(context, R.drawable.question2).let { byteArray ->
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
    }
    val minSize = minOf(screenWidth, screenHeight).times(0.5f)
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable { onClick(quizCard.id) }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            imageBitmap.let {
                Box(
                    modifier = Modifier
                        .size(minSize) // Square shape
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        bitmap = it,
                        contentDescription = "Quiz Image for " + quizCard.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize() // Expand to fill the square shape
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier
                .height(minSize),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = quizCard.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                Text(
                    text = quizCard.creator,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )
                ContextualFlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = 4.dp),
                    maxLines = 2,
                    itemCount = 1,
                ) {
                    quizCard.tags.forEach { tag ->
                        Text(text = tag,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Text(
                    text = quizCard.description,
                    style = MaterialTheme.typography.bodySmall,
                    minLines = 6,
                    maxLines = 6,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Solved: ${quizCard.count}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(name = "QuizCardItemVertical Preview")
@Composable
fun QuizCardItemPreview() {
    val context = LocalContext.current
    val imageByte = loadImageAsByteArray(context, R.drawable.question2)
    val quizCard = QuizCard(
        id = "1",
        title = "Quiz 1",
        tags = listOf("tag1", "tag2"),
        creator = "Creator",
        image = imageByte,
        count = 0
    )
    HorizontalQuizCardItemVertical(quizCards = listOf(quizCard, quizCard, quizCard))
}

@Preview(name = "QuizCardLarge Preview")
@Composable
fun QuizCardLargePreview() {
    val context = LocalContext.current
    val imageByte = loadImageAsByteArray(context, R.drawable.question2)
    val quizCard = QuizCard(
        id = "1",
        title = "Quiz 1",
        tags = listOf("tag1", "tag1", "tag2", "tag2"),
        creator = "Creator",
        image = imageByte,
        count = 0,
        description = "This is Example of a long description for the quiz card. This is Example of a long description for the quiz card. This is Example of a long description for the quiz card. This is Example of a long description for the quiz card."
    )
    HorizontalQuizCardItemLarge(quizCards = listOf(quizCard, quizCard, quizCard))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizCardHorizontal(quizCard: QuizCard, onClick: (String) -> Unit = {}) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    val imageBitmap: ImageBitmap =
        if(quizCard.image == null || quizCard.image.size < 100){
            loadImageAsByteArray(context, R.drawable.question2).let { byteArray ->
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap()
            }
        }else{
            BitmapFactory.decodeByteArray(quizCard.image, 0, quizCard.image.size).asImageBitmap()
        }
    val minSize = minOf(screenWidth, screenHeight).times(0.35f)
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .wrapContentHeight()
            .width(screenWidth)
            .padding(8.dp)
            .clickable { onClick(quizCard.id) }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            imageBitmap.let {
                Box(
                    modifier = Modifier
                        .size(minSize) // Square shape
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        bitmap = it,
                        contentDescription = "Quiz Image for " + quizCard.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize() // Expand to fill the square shape
                    )
                }
            }
            Column(modifier = Modifier
                .height(minSize)
                .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = quizCard.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = quizCard.creator,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                ContextualFlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .height(32.dp),
                    maxLines = 2,
                    itemCount = 1,
                ) {
                    quizCard.tags.forEach { tag ->
                        Text(text = tag,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(end = 4.dp),
                            style = MaterialTheme.typography.bodySmall,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Solved: ${quizCard.count}",
                    style = MaterialTheme.typography.bodySmall
                )

            }
        }
    }
}

@Composable
fun QuizCardHorizontalList(quizCards: List<QuizCard>, onClick: (String) -> Unit = {}) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(quizCards) { quizCard ->
            QuizCardHorizontal(quizCard, onClick)
        }
    }

}

@Preview
@Composable
fun QuizCardHorizontalPreview() {
    val context = LocalContext.current
    val imageByte = loadImageAsByteArray(context, R.drawable.question2)
    val quizCard = QuizCard(
        id = "1",
        title = "Quiz 11111111111111111111111111111111111111111111111 Long Quiz",
        tags = listOf("tag1", "tag2", "tag2"),
        creator = "Creator",
        image = imageByte,
        count = 0
    )
    QuizCardHorizontalList(quizCards = listOf(quizCard, quizCard, quizCard))
}

fun loadImageAsByteArray(context: Context, resId: Int): ByteArray {
    val bitmap = BitmapFactory.decodeResource(context.resources, resId)
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}