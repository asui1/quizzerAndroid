package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.models.quiz.QuizData
import com.asu1.models.quizRefactor.MultipleChoiceQuiz
import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.quizCreateUtils.QuizBundle
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidQuizTheme
import com.asu1.quizzer.test.R

val androidCoroutinesQuiz = listOf(
    MultipleChoiceQuiz(
        question = "What is the main reason for using coroutines in Android?",
        options = mutableListOf(
            "To improve UI performance",
            "To efficiently manage background tasks and simplify asynchronous processing",
            "To reduce code size",
            "To speed up compilation",
            "To encrypt data"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the role of Dispatchers.IO in coroutines?",
        options = mutableListOf(
            "Handles UI-related tasks",
            "Performs CPU-intensive tasks",
            "Creates new threads",
            "Optimizes UI animations",
            "Handles tasks such as network requests and database operations"
        ),
        correctFlags = mutableListOf(false, false, false, false, true)
    ),

    MultipleChoiceQuiz(
        question = "What does the suspend keyword mean in coroutines?",
        options = mutableListOf(
            "Executes the function immediately",
            "Runs the function on a background thread",
            "Restricts execution to coroutines only",
            "Allows the function to run asynchronously",
            "Ensures the function never gets suspended"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    ),

    MultipleChoiceQuiz(
        question = "What is the role of ensureActive() in coroutines?",
        options = mutableListOf(
            "Checks if a coroutine is still active and throws an exception if canceled",
            "Restarts the coroutine",
            "Immediately stops the coroutine",
            "Adjusts the execution speed of the coroutine",
            "Moves the coroutine to another thread"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "Predict the output of the following code:",
        bodyValue = BodyType.CODE("""
suspend fun main() {
    val result = withContext(Dispatchers.Default) {
        delay(1000)
        "Completed"
    }
    println(result)
}
        """),
        options = mutableListOf(
            "'Completed' is printed.",
            "An exception occurs during execution.",
            "Nothing is printed because withContext does not create a new coroutine.",
            "Since withContext runs in the background, the result is unknown.",
            "The code blocks the UI, causing it to freeze."
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What potential issue does the following code have?",
        bodyValue = BodyType.CODE("""
suspend fun fetchData(): String {
    return coroutineScope {
        launch {
            delay(1000)
            throw RuntimeException("Error occurred")
        }
        "Success"
    }
}
        """),
        options = mutableListOf(
            "The coroutine throws an exception, causing coroutineScope to be canceled.",
            "The code executes normally and returns 'Success'.",
            "The exception in launch is ignored because launch is a suspend function.",
            "delay() does not work.",
            "Even if an exception occurs, the function successfully returns 'Success'."
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "What will be the output of the following code?",
        bodyValue = BodyType.CODE("""
suspend fun main() {
    val job = GlobalScope.launch {
        delay(1000)
        println("Coroutine Completed")
    }
    println("Before job join")
    job.join()
    println("After job join")
}
        """),
        options = mutableListOf(
            "Before job join -> After job join -> Coroutine Completed",
            "Coroutine Completed -> Before job join -> After job join",
            "Before job join -> Job is canceled -> After job join",
            "Before job join -> Coroutine Completed -> After job join",
            "A compilation error occurs"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    )
)

val androidCoroutinesQuizData = QuizData(
    title = "Android Quiz: Coroutines",
    description = "Test your understanding of coroutine concepts and asynchronous processing in Android.",
    tags = setOf("Android", "Coroutines", "Asynchronous Programming", "Coding Interview", "Technical Interview")
)

@Suppress("unused")
val androidCoroutinesQuizBundle = QuizBundle(
    data = androidCoroutinesQuizData,
    theme = androidQuizTheme,
    quizzes = androidCoroutinesQuiz,
    titleImage = R.drawable.android_coding_interview,
)

val androidCoroutinesQuizKo = listOf(
    MultipleChoiceQuiz(
        question = "안드로이드에서 코루틴을 사용하는 주요 이유는 무엇인가요?",
        options = mutableListOf(
            "UI 성능을 향상시키기 위해",
            "백그라운드 작업을 효율적으로 관리하고 비동기 처리를 간단하게 하기 위해",
            "코드 크기를 줄이기 위해",
            "컴파일 속도를 빠르게 하기 위해",
            "데이터 암호화를 위해"
        ),
        correctFlags = mutableListOf(false, true, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "코루틴에서 Dispatchers.IO의 역할은 무엇인가요?",
        options = mutableListOf(
            "UI 관련 작업을 처리한다",
            "CPU 집약적인 작업을 수행한다",
            "새로운 스레드를 생성한다",
            "UI의 애니메이션을 최적화한다",
            "네트워크 요청이나 데이터베이스 작업과 같은 작업을 처리한다"
        ),
        correctFlags = mutableListOf(false, false, false, false, true)
    ),

    MultipleChoiceQuiz(
        question = "suspend 키워드는 코루틴에서 무엇을 의미하나요?",
        options = mutableListOf(
            "함수를 즉시 실행하는 역할을 한다",
            "함수를 백그라운드 스레드에서 실행한다",
            "코루틴 내에서만 실행될 수 있도록 제한한다",
            "함수를 비동기적으로 실행할 수 있도록 한다",
            "함수가 절대 중단되지 않도록 보장한다"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    ),

    MultipleChoiceQuiz(
        question = "코루틴에서 ensureActive() 함수의 역할은 무엇인가요?",
        options = mutableListOf(
            "코루틴이 아직 활성 상태인지 확인하고, 취소되었다면 예외를 던진다",
            "코루틴을 다시 시작하는 역할을 한다",
            "코루틴을 즉시 중단한다",
            "코루틴의 실행 속도를 조절한다",
            "코루틴을 다른 스레드로 이동시킨다"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "다음 코드에서 실행될 결과를 예측하세요.",
        bodyValue = BodyType.CODE("""
suspend fun main() {
    val result = withContext(Dispatchers.Default) {
        delay(1000)
        "Completed"
    }
    println(result)
}
        """),
        options = mutableListOf(
            "'Completed'가 출력된다.",
            "코드 실행 중 예외가 발생한다.",
            "withContext는 새로운 코루틴을 만들지 않으므로, 아무것도 출력되지 않는다.",
            "withContext는 백그라운드에서 실행되므로 결과를 알 수 없다.",
            "코드가 블로킹되어 UI가 멈춘다."
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "다음 코드에서 발생할 수 있는 문제점은 무엇인가요?",
        bodyValue = BodyType.CODE("""
suspend fun fetchData(): String {
    return coroutineScope {
        launch {
            delay(1000)
            throw RuntimeException("Error occurred")
        }
        "Success"
    }
}
        """),
        options = mutableListOf(
            "코루틴이 예외를 던지면서 coroutineScope이 취소된다",
            "코드는 정상적으로 실행되며 'Success'를 반환한다",
            "launch 블록이 suspend 함수이므로 예외가 무시된다",
            "delay()가 동작하지 않는다",
            "예외가 발생해도 함수는 정상적으로 'Success'를 반환한다"
        ),
        correctFlags = mutableListOf(true, false, false, false, false)
    ),

    MultipleChoiceQuiz(
        question = "다음 코드의 실행 결과는 무엇인가요?",
        bodyValue = BodyType.CODE("""
suspend fun main() {
    val job = GlobalScope.launch {
        delay(1000)
        println("Coroutine Completed")
    }
    println("Before job join")
    job.join()
    println("After job join")
}
        """),
        options = mutableListOf(
            "Before job join -> After job join -> Coroutine Completed",
            "Coroutine Completed -> Before job join -> After job join",
            "Before job join -> Job is canceled -> After job join",
            "Before job join -> Coroutine Completed -> After job join",
            "컴파일 오류가 발생한다"
        ),
        correctFlags = mutableListOf(false, false, false, true, false)
    )
)

val androidCoroutinesQuizDataKo = QuizData(
    title = "안드로이드 퀴즈: 코루틴",
    description = "안드로이드에서 코루틴의 개념과 비동기 처리 방식에 대한 이해도를 테스트하세요.",
    tags = setOf("안드로이드", "코루틴", "비동기 프로그래밍", "코딩 면접", "기술 면접")
)

@Suppress("unused")
val androidCoroutinesQuizBundleKo = QuizBundle(
    data = androidCoroutinesQuizDataKo,
    theme = androidQuizTheme,
    quizzes = androidCoroutinesQuizKo,
    titleImage = R.drawable.android_coding_interview,
)

