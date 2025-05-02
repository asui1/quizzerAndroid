package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.models.quiz.QuizData
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.quizRefactor.ReorderQuiz
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidQuizTheme
import com.asu1.quizzer.test.R

val androidLifecycleQuiz = listOf(
    ReorderQuiz(
        answers = mutableListOf("onCreate", "onStart", "onResume", "onPause", "onStop", "onDestroy"),
        question = "Arrange the Activity lifecycle methods in the correct order when an Activity is created and then destroyed."
    ),

    MultipleChoiceQuiz(
        question = "Which lifecycle method is called when the user navigates away but the Activity is still partially visible?",
        options = mutableListOf(
            "onPause",
            "onStop",
            "onDestroy",
            "onResume",
            "onStart"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Which lifecycle method is called when an Activity becomes visible to the user?",
        options = mutableListOf(
            "onCreate",
            "onStart",
            "onResume",
            "onPause",
            "onStop"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Which lifecycle method is called when an Activity is fully interactive?",
        options = mutableListOf(
            "onStart",
            "onResume",
            "onPause",
            "onDestroy",
            "onStop"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Which lifecycle method is guaranteed to be called before an Activity is destroyed?",
        options = mutableListOf(
            "onPause",
            "onStop",
            "onDestroy",
            "onCreate",
            "onStart"
        ),
        correctFlags = mutableListOf(false, true, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What happens when the screen is rotated while an Activity is running?",
        options = mutableListOf(
            "Activity is destroyed and recreated",
            "Only onResume() is called",
            "Nothing changes",
            "Android automatically saves all data",
            "Android only updates screen configuration"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Which method should be used to retain data across configuration changes, such as screen rotation?",
        options = mutableListOf(
            "onSaveInstanceState",
            "onDestroy",
            "onStop",
            "onPause",
            "onResume",
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "You need to register a BroadcastReceiver to listen for network connectivity changes. In which lifecycle method should you register it?",
        options = mutableListOf(
            "onCreate",
            "onStart",
            "onResume",
            "onPause",
            "onStop"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "A user starts a video in an Activity, and you need to pause playback when they navigate away. In which lifecycle method should you handle this?",
        options = mutableListOf(
            "onPause",
            "onStop",
            "onDestroy",
            "onCreate",
            "onResume"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "You want to save user input when they navigate away from an Activity but restore it later. Which lifecycle method should you use?",
        options = mutableListOf(
            "onSaveInstanceState",
            "onPause",
            "onStop",
            "onDestroy",
            "onResume"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    )
)
val androidLifecycleQuizData = QuizData(
    title = "Android Lifecycle Interview Quiz",
    description = "Test your knowledge of Android's Activity and Fragment lifecycle methods and their behavior.",
    tags = setOf("Android", "Activity Lifecycle", "Coding Interview", "Technical Interview")
)

@Suppress("unused")
val androidLifecycleQuizBundle = QuizBundle(
    data = androidLifecycleQuizData,
    theme = androidQuizTheme,
    quizzes = androidLifecycleQuiz,
    titleImage = R.drawable.android_coding_interview,
)

// ================ KOREAN VERSION =================

val androidLifecycleQuizKo = listOf(
    ReorderQuiz(
        answers = mutableListOf("onCreate", "onStart", "onResume", "onPause", "onStop", "onDestroy"),
        question = "Activity가 생성되고 종료될 때까지의 올바른 생명주기 메서드 순서를 배열하세요."
    ),

    MultipleChoiceQuiz(
        question = "사용자가 화면을 벗어났지만 Activity가 여전히 부분적으로 보이는 경우 호출되는 생명주기 메서드는 무엇인가요?",
        options = mutableListOf(
            "onPause",
            "onStop",
            "onDestroy",
            "onResume",
            "onStart"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Activity가 사용자에게 표시될 때 호출되는 생명주기 메서드는 무엇인가요?",
        options = mutableListOf(
            "onCreate",
            "onStart",
            "onResume",
            "onPause",
            "onStop"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Activity가 완전히 상호작용 가능한 상태가 될 때 호출되는 생명주기 메서드는 무엇인가요?",
        options = mutableListOf(
            "onStart",
            "onResume",
            "onPause",
            "onDestroy",
            "onStop"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Activity가 종료되기 전에 반드시 호출되는 생명주기 메서드는 무엇인가요?",
        options = mutableListOf(
            "onPause",
            "onStop",
            "onDestroy",
            "onCreate",
            "onStart"
        ),
        correctFlags = mutableListOf(false, true, true, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Activity가 실행 중일 때 화면이 회전하면 어떤 일이 발생하나요?",
        options = mutableListOf(
            "Activity가 종료되고 다시 생성된다",
            "Only onResume() is called",
            "변화가 없다",
            "Android가 모든 데이터를 자동으로 저장한다",
            "Android가 Screen Configuration만 갱신한다."
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "화면 회전과 같은 구성 변경 시 데이터를 유지하려면 어떤 메서드를 사용해야 하나요?",
        options = mutableListOf(
            "onSaveInstanceState",
            "onDestroy",
            "onStop",
            "onPause",
            "onResume",
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "네트워크 연결 상태 변경을 감지하기 위해 BroadcastReceiver를 등록해야 합니다. 어느 생명주기 메서드에서 등록해야 하나요?",
        options = mutableListOf(
            "onCreate",
            "onStart",
            "onResume",
            "onPause",
            "onStop"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "사용자가 Activity에서 동영상을 시작했을 때, 다른 화면으로 이동하면 재생을 일시 중지해야 합니다. 이 작업을 수행해야 하는 생명주기 메서드는 무엇인가요?",
        options = mutableListOf(
            "onPause",
            "onStop",
            "onDestroy",
            "onCreate",
            "onResume"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "사용자가 Activity를 떠날 때 입력한 데이터를 저장하고 나중에 복원하려고 합니다. 어떤 생명주기 메서드를 사용해야 하나요?",
        options = mutableListOf(
            "onSaveInstanceState",
            "onPause",
            "onStop",
            "onDestroy",
            "onResume"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    )
)

val androidLifecycleQuizDataKo = QuizData(
    title = "Android 생명주기 퀴즈",
    description = "안드로이드의 생명주기 메서드와 동작에 대한 이해도를 테스트하세요.",
    tags = setOf("안드로이드", "안드로이드 생명주기", "코딩 인터뷰", "기술 면접")
)

@Suppress("unused")
val androidLifecycleQuizBundleKo = QuizBundle(
    data = androidLifecycleQuizDataKo,
    theme = androidQuizTheme,
    quizzes = androidLifecycleQuizKo,
    titleImage = R.drawable.android_coding_interview,
)
