package com.asu1.appdata.music

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "music")
data class Music(
    @PrimaryKey val title: String,
    val artist: String,
)

@Entity(tableName = "mood")
data class Mood(
    @PrimaryKey val mood: String
)

@Entity(
    primaryKeys = ["title", "mood"],
    indices = [Index(value = ["mood"])]
)
data class MusicMoodCrossRef(
    val title: String,
    val mood: String
)

data class MusicWithMoods(
    @Embedded val music: Music,
    @Relation(
        parentColumn = "title",
        entityColumn = "mood",
        associateBy = Junction(
            value = MusicMoodCrossRef::class,
            parentColumn = "title",
            entityColumn = "mood"
        )
    )
    val moods: List<Mood>
)

data class MusicAllInOne(
    val music: Music,
    val moods: Set<String>,
    val duration: Long = 0,
){
    fun getUri(): String{
        return when(music.title){
            "sample1" -> "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd"
            "sample2" -> "https://bitmovin-a.akamaihd.net/content/sintel/sintel.mpd"
            "sample3" -> "https://storage.googleapis.com/shaka-demo-assets/angel-one/dash.mpd"
            else -> "https://storage.googleapis.com/shaka-demo-assets/sintel/dash.mpd"
        }
    }
}