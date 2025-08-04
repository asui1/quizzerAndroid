package com.asu1.models.quizRefactor

import androidx.compose.runtime.mutableStateListOf
import com.asu1.models.serializers.BodyType
import com.asu1.models.serializers.BodyTypeSerializer
import com.asu1.models.serializers.QuizError
import com.asu1.models.serializers.QuizType
import com.asu1.utils.normalizeMixedInputPrecisely
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
@SerialName("5")
data class FillInBlankQuiz(
    override var question: String = "",
    var rawText: String = "",
    var correctAnswers: List<String> = emptyList(),
    @Serializable(with = BodyTypeSerializer::class)
    override var bodyValue: BodyType = BodyType.NONE,
) : Quiz(
) {
    @Transient
    var userAnswers = mutableStateListOf<String>()
    @Transient
    override val uuid: String = UUID.randomUUID().toString()
    @Transient
    override val quizType: QuizType = QuizType.QUIZ6

    companion object {
        const val MAX_PLACEHOLDERS = 9
        const val PLACEHOLDER = "1\uFE0F\u20E3"  // fixed placeholder

        /** matches “ 1️⃣ ” … “ 9️⃣ ” and captures the digit */
        private val placeholderRegex = Regex("""([1-9])\uFE0F\u20E3""")

        /** turn 1→"1️⃣", 2→"2️⃣", … */
        fun numberToEmoji(n: Int): String =
            if (n in 1..9) "$n\uFE0F\u20E3" else PLACEHOLDER

        /** pull out all the digits currently in your placeholders, e.g. [1,2,3] */
        private fun extractOrdinals(text: String): List<Int> =
            placeholderRegex.findAll(text)
                .map { it.groupValues[1].toInt() }
                .toList()

        /** renumber *every* placeholder in order → " 1️⃣ "," 2️⃣ ",… */
        private fun renumberPlaceholders(text: String): String {
            var counter = 1
            return placeholderRegex.replace(text) {
                numberToEmoji(counter++)
            }
        }
    }

    init {
        initViewState()
    }

    override fun initViewState() {
        userAnswers.clear()
        repeat(correctAnswers.size){ userAnswers.add("") }
    }

    override fun validateQuiz(): QuizError  = when {
        question.isBlank() -> QuizError.EMPTY_QUESTION
        correctAnswers.isEmpty() -> QuizError.EMPTY_ANSWER
        else -> QuizError.NO_ERROR
    }

    override fun gradeQuiz(): Boolean =
        correctAnswers.indices.all { i ->
            normalizeMixedInputPrecisely(userAnswers[i]) ==
                    normalizeMixedInputPrecisely(correctAnswers[i])
        }

    override fun cloneQuiz(
        question: String,
        bodyType: BodyType,
        uuid: String
    ): Quiz {
        return copy(
            question       = question,
            rawText        = rawText,
            correctAnswers = correctAnswers,
            bodyValue      = bodyType
        )
    }

    fun addCorrectAnswer() {
        if(correctAnswers.size >= 9) return
        correctAnswers = correctAnswers + listOf("")
        rawText += " ${numberToEmoji(correctAnswers.size)}"
    }

    fun removeCorrectAnswer(index: Int) {
        if (index !in correctAnswers.indices) return

        val emoji = numberToEmoji(index + 1)
        val esc = Regex.escape(emoji)

        // “\\s?” means “zero or one whitespace character”
        rawText = rawText.replaceFirst(Regex("\\s?$esc"), "")
        for (n in (index+1)..correctAnswers.size+1) {
            val from = "${n+1}\uFE0F\u20E3"
            val to   = numberToEmoji(n)
            rawText = rawText.replaceFirst(from, to)
        }
        correctAnswers = correctAnswers.toMutableList().apply {
            removeAt(index)
        }
    }

    fun updateRawText(newRawText: String) {
        val oldOrd = extractOrdinals(rawText)
        val newOrd = extractOrdinals(newRawText)

        // 1) DELETIONS: dropped ordinals = oldOrd – newOrd
        val removed = oldOrd - newOrd
        if (removed.isNotEmpty()) {
            // remove from largest→smallest so indices stay valid
            removed.sortedDescending().forEach { ord ->
                removeCorrectAnswer(ord - 1)
            }
        }

        // 2) INSERTIONS: newCount > oldCount?
        val oldCount = oldOrd.size
        val newCount = newOrd.size
        if (newCount > oldCount) {
            val toAdd = minOf(newCount - oldCount, MAX_PLACEHOLDERS - oldCount)
            repeat(toAdd) { addCorrectAnswer() }
        }

        rawText = renumberPlaceholders(newRawText)
    }
}
