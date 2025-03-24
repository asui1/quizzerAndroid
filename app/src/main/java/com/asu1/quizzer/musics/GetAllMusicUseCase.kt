package com.asu1.quizzer.musics

import com.asu1.appdata.music.MusicAllInOne
import com.asu1.appdata.music.MusicRepository
import javax.inject.Inject

class GetAllMusicUseCase @Inject constructor(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke() : List<MusicAllInOne>{
        val response = musicRepository.getAllMusic()
        return response.map {
            MusicAllInOne(it, emptySet())
        }
    }
}