package com.asu1.quizzer

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.test.core.app.ApplicationProvider
import com.asu1.quizzer.service.MusicServiceHandler
import com.asu1.quizzer.util.constants.sampleMusicList
import com.asu1.quizzer.util.musics.MediaStateEvents
import com.asu1.quizzer.util.musics.MusicStates
import com.asu1.utils.Logger
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ExoPlayerHandlerTest {

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var musicServiceHandler: MusicServiceHandler
    private lateinit var mainHandler: Handler
    companion object {
        private lateinit var mediaItems: List<MediaItem>
        private lateinit var itemTitles: List<String>

        @JvmStatic
        @BeforeClass
        fun setUpClass() {
            val sampleMusics = sampleMusicList
            itemTitles = sampleMusics.map { it.music.title }
            mediaItems = sampleMusics.map { audioItem ->
                MediaItem.Builder()
                    .setUri(audioItem.getUri())
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setAlbumArtist(audioItem.music.artist)
                            .setDisplayTitle(audioItem.music.title)
                            .build()
                    )
                    .build()
            }
        }
    }
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val looper = Looper.getMainLooper()
        exoPlayer = ExoPlayer.Builder(context)
            .setLooper(looper).build()
        musicServiceHandler = MusicServiceHandler(exoPlayer)
        mainHandler = Handler(looper)
    }

    @Test
    fun setMediaItemList_validList_success() {
        val latch = CountDownLatch(1)
        mainHandler.post{
            musicServiceHandler.setMediaItemList(mediaItems)
        }
        latch.await(5, TimeUnit.SECONDS)
        assertEquals(musicServiceHandler.musicStates.value, MusicStates.CurrentMediaPlaying(0))
    }

    @Test
    fun setMediaItemList_emptyList_noUpdate(){
        val latch = CountDownLatch(1)
        mainHandler.post {
            musicServiceHandler.setMediaItemList(emptyList())
        }
        latch.await(5, TimeUnit.SECONDS)
        assertNotEquals(musicServiceHandler.musicStates.value, MusicStates.CurrentMediaPlaying(0))
    }

    @Test
    fun onMediaStateEventsChangeItemOrder_from1to3_changeToOrder1342(){
        val latch = CountDownLatch(3)
        mainHandler.post {
            musicServiceHandler.setMediaItemList(mediaItems)
        }
        latch.await(5, TimeUnit.SECONDS)
        mainHandler.post {
            runBlocking {
                musicServiceHandler.onMediaStateEvents(MediaStateEvents.ChangeItemOrder(1, 3))
            }
        }
        latch.await(5, TimeUnit.SECONDS)
        checkMediaItemOrder(listOf(0, 2, 3, 1))
    }

    @Test
    fun onMediaStateEventsChangeItemOrder_from3to1_changeToOrder1423(){
        val latch = CountDownLatch(3)
        mainHandler.post {
            musicServiceHandler.setMediaItemList(mediaItems)
        }
        latch.await(5, TimeUnit.SECONDS)
        mainHandler.post {
            runBlocking {
                musicServiceHandler.onMediaStateEvents(MediaStateEvents.ChangeItemOrder(3, 1))
            }
        }
        latch.await(7, TimeUnit.SECONDS)
        checkMediaItemOrder(listOf(0, 3, 1, 2))
    }

    @Test
    fun onMediaStateEventsChangeItemOrder_from1to1_ignoreKeepOrder1234(){
        val latch = CountDownLatch(2)
        mainHandler.post {
            musicServiceHandler.setMediaItemList(mediaItems)
        }
        latch.await(5, TimeUnit.SECONDS)
        mainHandler.post {
            runBlocking {
                musicServiceHandler.onMediaStateEvents(MediaStateEvents.ChangeItemOrder(1, 1))
            }
        }
        latch.await(5, TimeUnit.SECONDS)
        checkMediaItemOrder(listOf(0, 1, 2, 3))
    }

    @Test
    fun onMediaStateEventsChangeItemOrder_from5to1OutOfRange_ignoreKeepOrder1234(){
        val latch = CountDownLatch(2)
        mainHandler.post {
            musicServiceHandler.setMediaItemList(mediaItems)
        }
        latch.await(5, TimeUnit.SECONDS)
        mainHandler.post {
            runBlocking {
                musicServiceHandler.onMediaStateEvents(MediaStateEvents.ChangeItemOrder(5, 1))
            }
        }
        latch.await(5, TimeUnit.SECONDS)
        checkMediaItemOrder(listOf(0, 1, 2, 3))
    }

    private fun checkMediaItemOrder(expectedOrder: List<Int>){
        if(itemTitles.size != expectedOrder.size) throw IllegalArgumentException("Items and order size should be same")
        val latch = CountDownLatch(1)
        val items = Array(itemTitles.size){""}
        mainHandler.post {
            for(i in expectedOrder.indices){
                items[i] = musicServiceHandler.getMediaItem(i)?.mediaMetadata?.displayTitle.toString()
            }
        }
        latch.await(3, TimeUnit.SECONDS)
        for(i in itemTitles.indices){
            Logger.debug("Expected: ${itemTitles[i]}, Actual: ${items[i]}")
            Logger.debug("Expected: ${itemTitles[i]}, expected: ${expectedOrder.indexOf(i)} Actual: ${items[expectedOrder.indexOf(i)]}")
        }

        for(i in itemTitles.indices){
            assertEquals(itemTitles[i], items[expectedOrder.indexOf(i)])
        }
    }
}

