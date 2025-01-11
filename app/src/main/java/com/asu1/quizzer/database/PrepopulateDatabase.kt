package com.asu1.quizzer.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.musics.Music
import com.asu1.quizzer.musics.MusicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PrepopulateDatabase @Inject constructor(
    private val repository: MusicRepository,
    private val scope: CoroutineScope
): RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch(Dispatchers.IO) {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {

        val items = listOf(
            MusicAllInOne(
                music = Music(title = "Fearless pt. II", artist = "TULE, Chris Linton"),
                moods = setOf("Trap", "Scary", "Dark", "Gloomy", "Fear")
            ),
            MusicAllInOne(
                music = Music(title = "Symbolism", artist = "Electro-Light"),
                moods = setOf("Trap", "Peaceful", "Relaxed", "Quirky", "Weird", "Dreamy", "Hopeful")
            ),
            MusicAllInOne(
                music = Music(title = "Why We Lose (feat. Coleman Trapp)", artist = "Cartoon, Coleman Trapp, Jéja"),
                moods = setOf("Drum & Bass", "Sad", "Angry", "Dark", "Gloomy", "Energetic")
            ),
            MusicAllInOne(
                music = Music(title = "Blank", artist = "Disfigure"),
                moods = setOf("Melodic Dubstep", "Happy", "Hopeful", "Quirky", "Relaxed")
            ),
            MusicAllInOne(
                music = Music(title = "Sky High", artist = "Elektronomia"),
                moods = setOf("House", "Happy", "Hopeful", "Epic", "Euphoric", "Energetic")
            ),
            MusicAllInOne(
                music = Music(title = "My Heart", artist = "Different Heaven, EH!DE"),
                moods = setOf("Drumstep", "Quirky", "Happy", "Weird", "Energetic")
            ),
            MusicAllInOne(
                music = Music(title = "Invincible", artist = "DEAF KEV"),
                moods = setOf("Melodic Dubstep", "Happy", "Euphoric", "Dreamy", "Hopeful")
            ),
            MusicAllInOne(
                music = Music(title = "Mortals (feat. Laura Brehm)", artist = "Warriyo, Laura Brehm"),
                moods = setOf("Trap", "Gloomy", "Angry", "Dark", "Fear", "Suspense")
            ),
            MusicAllInOne(
                music = Music(title = "Heroes Tonight (feat. Johnning)", artist = "Janji, Johnning"),
                moods = setOf("House", "Dreamy", "Euphoric", "Hopeful", "Happy", "Energetic")
            ),
            MusicAllInOne(
                music = Music(title = "On and On", artist = "Sayfro, BAYZY"),
                moods = setOf("House", "Happy", "Epic", "Euphoric", "Restless")
            ),
        )

        for (item in items) {
            repository.insertMusicWithMoods(item)
        }
    }
}