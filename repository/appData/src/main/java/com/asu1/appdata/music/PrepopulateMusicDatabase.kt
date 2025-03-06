package com.asu1.appdata.music

suspend fun prepopulateMusicDatabase(repository: MusicRepository) {
    if (repository.getAllMusic().isNotEmpty()) {
        return
    }

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