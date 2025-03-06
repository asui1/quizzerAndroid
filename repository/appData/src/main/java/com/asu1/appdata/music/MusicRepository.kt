package com.asu1.appdata.music

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val musicDao: MusicDao
){
    suspend fun insertMusic(music: Music){
        if(!musicDao.isMusicExists(music.title)){
            musicDao.insertMusic(music)
        }
    }

    suspend fun insertMood(mood: Mood){
        if(!musicDao.isMoodExists(mood.mood)){
            musicDao.insertMood(mood)
        }
    }

    suspend fun insertMusicMoodCrossRef(musicMoodCrossRef: MusicMoodCrossRef){
        if(!musicDao.isMusicMoodCrossRefExists(musicMoodCrossRef.title, musicMoodCrossRef.mood)){
            musicDao.insertMusicMoodCrossRef(musicMoodCrossRef)
        }
    }

    suspend fun getAllMusic(): List<Music> = musicDao.getAllMusic()

    suspend fun getMusicWithMoods(title: String): List<MusicWithMoods> = musicDao.getMusicWithMoods(title)

    suspend fun getMusicByMood(mood: String): List<MusicWithMoods> = musicDao.getMusicByMood(mood)

    suspend fun getAllMusicWithMoods(): List<MusicAllInOne>{
        val musicWithMoods = mutableListOf<MusicAllInOne>()
        getAllMusic().forEach {
            musicWithMoods.add(MusicAllInOne(it, getMusicWithMoods(it.title).first().moods.map { mood -> mood.mood }.toSet()))
        }
        return musicWithMoods
    }

    suspend fun insertMusicWithMoods(musicAllInOne: MusicAllInOne){
        insertMusic(musicAllInOne.music)
        musicAllInOne.moods.forEach {
            insertMood(Mood(it))
            insertMusicMoodCrossRef(MusicMoodCrossRef(musicAllInOne.music.title, it))
        }
    }



}