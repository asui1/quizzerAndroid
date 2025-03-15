package com.asu1.quizzer.musicTests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.asu1.appdata.music.Mood
import com.asu1.appdata.music.Music
import com.asu1.appdata.music.MusicAllInOne
import com.asu1.appdata.music.MusicDao
import com.asu1.appdata.music.MusicMoodCrossRef
import com.asu1.appdata.music.MusicRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


@ExperimentalCoroutinesApi
class MusicRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val musicDao = mock(MusicDao::class.java)
    private val repository = MusicRepository(musicDao)
    private val music = Music("title", "artist")
    private val mood = Mood("Happy")
    private val musicMoodCrossRef = MusicMoodCrossRef(music.title, mood.mood)
    private val musicAllInOne = MusicAllInOne(
        music = Music(title = "Fearless pt. II", artist = "TULE, Chris Linton"),
        moods = setOf("Trap", "Scary", "Dark")
    )

    @Test
    fun insertMusic_whenMusicDoesNotExist_shouldInsertMusic() = runTest {
        `when`(musicDao.isMusicExists(music.title)).thenReturn(false)

        repository.insertMusic(music)
        verify(musicDao).insertMusic(music)
    }

    @Test
    fun insertMusic_whenMusicExists_shouldNotInsertMusic() = runTest {
        `when`(musicDao.isMusicExists(music.title)).thenReturn(true)

        repository.insertMusic(music)

        verify(musicDao, never()).insertMusic(music)
    }

    @Test
    fun insertMood_whenMoodDoesNotExist_shouldInsertMood() = runTest {
        `when`(musicDao.isMoodExists(mood.mood)).thenReturn(false)

        repository.insertMood(mood)
        verify(musicDao).insertMood(mood)
    }

    @Test
    fun insertMood_whenMoodExists_shouldNotInsertMood() = runTest {
        `when`(musicDao.isMoodExists(mood.mood)).thenReturn(true)

        repository.insertMood(mood)
        verify(musicDao, never()).insertMood(mood)
    }

    @Test
    fun insertMusicMoodCrossRef_whenMusicMoodCrossRefDoesNotExist_shouldInsertMusicMoodCrossRef() = runTest {
        `when`(musicDao.isMusicMoodCrossRefExists(musicMoodCrossRef.title, musicMoodCrossRef.mood)).thenReturn(false)

        repository.insertMusicMoodCrossRef(musicMoodCrossRef)
        verify(musicDao).insertMusicMoodCrossRef(musicMoodCrossRef)
    }

    @Test
    fun insertMusicMoodCrossRef_whenMusicMoodCrossRefExists_shouldNotInsertMusicMoodCrossRef() = runTest {
        `when`(musicDao.isMusicMoodCrossRefExists(musicMoodCrossRef.title, musicMoodCrossRef.mood)).thenReturn(true)

        repository.insertMusicMoodCrossRef(musicMoodCrossRef)
        verify(musicDao, never()).insertMusicMoodCrossRef(musicMoodCrossRef)
    }

    @Test
    fun insertMusicWithMoods_whenNoDuplicates_shouldInsertAllMusicWithMoods() = runTest {
        `when`(musicDao.isMusicExists(musicAllInOne.music.title)).thenReturn(false)
        musicAllInOne.moods.forEach { mood ->
            `when`(musicDao.isMoodExists(mood)).thenReturn(false)
            `when`(musicDao.isMusicMoodCrossRefExists(musicAllInOne.music.title, mood)).thenReturn(false)
        }

        repository.insertMusicWithMoods(musicAllInOne)

        verify(musicDao).insertMusic(musicAllInOne.music)
        musicAllInOne.moods.forEach { mood ->
            verify(musicDao).insertMood(Mood(mood))
            verify(musicDao).insertMusicMoodCrossRef(MusicMoodCrossRef(musicAllInOne.music.title, mood))
        }
    }

    @Test
    fun insertMusicWithMoods_whenSomeMoodDuplicates_shouldInsertNonDuplicates() =  runTest {
        val dupMoods = musicAllInOne.moods.take(2)
        val uniqueMoods = musicAllInOne.moods.distinct() // 중복 제거된 Mood 리스트

        `when`(musicDao.isMusicExists(musicAllInOne.music.title)).thenReturn(false)

        `when`(musicDao.isMusicExists(musicAllInOne.music.title)).thenReturn(false)
        `when`(musicDao.isMoodExists(anyString())).thenAnswer { invocation ->
            val mood = invocation.arguments[0] as String
            mood in dupMoods
        }
        `when`(musicDao.isMusicMoodCrossRefExists(anyString(), anyString())).thenReturn(false)

        repository.insertMusicWithMoods(musicAllInOne)

        verify(musicDao, times(1)).insertMusic(musicAllInOne.music)

        uniqueMoods.forEach { mood ->
            if (mood in dupMoods) {
                verify(musicDao, never()).insertMood(Mood(mood))
            } else {
                verify(musicDao, times(1)).insertMood(Mood(mood))
            }
        }

        uniqueMoods.forEach { mood ->
            verify(musicDao, times(1)).insertMusicMoodCrossRef(MusicMoodCrossRef(musicAllInOne.music.title, mood))
        }
    }

    @Test
    fun insertMusicWithMoods_whenMusicExists_shouldNotInsertMusicUpdateMoods() = runTest {
        val existingMoods = musicAllInOne.moods.take(2)
        `when`(musicDao.isMusicExists(musicAllInOne.music.title)).thenReturn(true)
        musicAllInOne.moods.forEach { mood ->
            if(existingMoods.contains(mood)){
                `when`(musicDao.isMoodExists(mood)).thenReturn(true)
                `when`(musicDao.isMusicMoodCrossRefExists(musicAllInOne.music.title, mood)).thenReturn(true)
            } else {
                `when`(musicDao.isMoodExists(mood)).thenReturn(false)
                `when`(musicDao.isMusicMoodCrossRefExists(musicAllInOne.music.title, mood)).thenReturn(false)
            }
        }

        repository.insertMusicWithMoods(musicAllInOne)

        verify(musicDao, never()).insertMusic(musicAllInOne.music)
        musicAllInOne.moods.distinct().forEach { mood ->
            if(existingMoods.contains(mood)){
                verify(musicDao, never()).insertMood(Mood(mood))
                verify(musicDao, never()).insertMusicMoodCrossRef(MusicMoodCrossRef(musicAllInOne.music.title, mood))
            } else{
                verify(musicDao).insertMood(Mood(mood))
                verify(musicDao).insertMusicMoodCrossRef(MusicMoodCrossRef(musicAllInOne.music.title, mood))
            }
        }
    }

}