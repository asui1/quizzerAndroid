package com.asu1.quizzer.musics

import com.asu1.appdata.music.Music
import com.asu1.appdata.music.MusicAllInOne

val sampleMusicList = listOf<MusicAllInOne>(
    MusicAllInOne(
        music = Music(
            title = "sample1",
            artist = "Test1",
        ),
        moods = setOf("Happy"),
        duration = 634000
    ),
    MusicAllInOne(
        music = Music(
            title = "sample2",
            artist = "Test2",
        ),
        moods = setOf("Scared"),
        duration = 888000
    ),
    MusicAllInOne(
        music = Music(
            title = "sample3",
            artist = "Test3",
        ),
        moods = setOf("Sad"),
        duration = 60000
    ),
    MusicAllInOne(
        music = Music(
            title = "sample4",
            artist = "Test4",
        ),
        moods = setOf("Dance"),
        duration = 888000
    ),
)