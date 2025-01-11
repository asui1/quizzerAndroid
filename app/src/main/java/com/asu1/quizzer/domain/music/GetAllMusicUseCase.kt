package com.asu1.quizzer.domain.music

import com.asu1.quizzer.musics.MusicAllInOne
import com.asu1.quizzer.musics.MusicRepository
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
