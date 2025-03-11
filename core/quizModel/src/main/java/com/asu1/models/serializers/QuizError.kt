package com.asu1.models.serializers

import com.asu1.resources.R

enum class QuizError(val messageId: Int) {
    NO_ERROR(messageId = R.string.empty_string),
    EMPTY_QUESTION(messageId = R.string.question_cannot_be_empty),
    EMPTY_ANSWER(messageId = R.string.answers_cannot_be_empty),
    EMPTY_OPTION(messageId = R.string.correct_answer_not_selected),
}