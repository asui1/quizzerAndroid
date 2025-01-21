package com.asu1.resources

enum class LayoutSteps(val value: Int, val stringResourceId: Int) {
    POLICY(0, R.string.agree_policy),
    TITLE(1, R.string.set_quiz_title),
    DESCRIPTION(2, R.string.set_quiz_description),
    TAGS(3, R.string.set_quiz_tags),
    IMAGE(4, R.string.set_quiz_image),
    THEME(5, R.string.set_color_setting),
    TEXTSTYLE(6, R.string.set_text_setting),;

    operator fun minus(i: Int): LayoutSteps {
        return when (this) {
            POLICY -> POLICY
            TITLE -> POLICY
            DESCRIPTION -> TITLE
            TAGS -> DESCRIPTION
            IMAGE -> TAGS
            THEME -> IMAGE
            TEXTSTYLE -> THEME
        }
    }

    operator fun plus(i: Int): LayoutSteps {
        return when (this) {
            POLICY -> TITLE
            TITLE -> DESCRIPTION
            DESCRIPTION -> TAGS
            TAGS -> IMAGE
            IMAGE -> THEME
            THEME -> TEXTSTYLE
            TEXTSTYLE -> TEXTSTYLE
        }
    }
}
