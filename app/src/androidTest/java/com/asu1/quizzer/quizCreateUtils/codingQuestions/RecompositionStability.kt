package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidCodingTheme

val androidRecompositionStabilityQuiz = listOf(
    TestQuiz1(
        point = 5,
        question = "Which of the following statements about Compose stability is true?",
        answers = mutableListOf(
            "Lists are always considered stable",
            "Only immutable data classes can be stable",
            "A class marked @Stable is always recomposed",
            "A composable will never recompose if its parameters are stable",
            "MutableState<T> is considered stable"
        ),
        ans = mutableListOf(false, false, false, false, true)
    ),

    TestQuiz1(
        point = 5,
        question = "Which of the following types are considered unstable and may cause unnecessary recompositions?",
        answers = mutableListOf(
            "Data classes with all val properties",
            "MutableState",
            "List<T>",
            "Set<T>",
            "Classes from modules without Compose runtime"
        ),
        ans = mutableListOf(false, false, true, true, true)
    ),

    TestQuiz1(
        point = 5,
        question = "What happens when an unstable parameter is passed to a composable?",
        answers = mutableListOf(
            "Compose will ignore the parameter and optimize performance",
            "The app will crash",
            "Compose will convert the parameter into a stable object automatically",
            "Nothing, Compose handles everything internally",
            "The composable will always be recomposed when its parent recomposes"
        ),
        ans = mutableListOf(false, false, false, false, true)
    ),

    TestQuiz1(
        point = 5,
        question = "What determines if a composable can be skipped during recomposition?",
        answers = mutableListOf(
            "Whether it uses @Composable annotation",
            "The stability of its parameters",
            "If it is marked as inline",
            "If it is inside a Column or Row",
            "The size of the function",
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "How can you debug the stability of your composables?",
        answers = mutableListOf(
            "Use the Compose compiler reports",
            "Check the function signature for @Stable annotation",
            "Use reflection to inspect runtime behavior",
            "Modify the composable to always return Unit",
            "Measure performance manually with log statements",
        ),
        ans = mutableListOf(true, false, false, false,false)
    ),

    TestQuiz1(
        point = 5,
        question = "Which annotation explicitly guarantees that a class will never change after construction?",
        answers = mutableListOf(
            "@Stable",
            "@Immutable",
            "@Composable",
            "@Keep",
            "@Static"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Which of the following composable functions will always be recomposed?",
        answers = mutableListOf(
            "A composable with @Immutable data class parameter",
            "A composable with MutableState parameter",
            "A composable with List<T> parameter",
            "A composable inside an inline function",
            "A composable with a primitive type parameter"
        ),
        ans = mutableListOf(false, false, true, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Which composable will NOT trigger unnecessary recompositions?",
        answers = mutableListOf(
            "Composable using a MutableState",
            "Composable using a regular List<T>",
            "Composable using an ImmutableList<T>",
            "Composable using var properties inside a data class",
            "Composable that does not take any parameters"
        ),
        ans = mutableListOf(true, false, true, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Why are Kotlin collections like List and Set considered unstable in Compose?",
        answers = mutableListOf(
            "Because they can be modified at runtime",
            "Because they always contain mutable elements",
            "Because they are primitive types",
            "Because Compose does not support collections",
            "Because they are optimized for recomposition"
        ),
        ans = mutableListOf(true, false, false, false, false)
    )
)

val androidRecompositionStabilityQuizData = androidCodingTheme.copy(
    quizzes = androidRecompositionStabilityQuiz,
    title = "Android Recomposition Stability Quiz",
    description = "Test your knowledge on how Kotlin compiler determines stability, what causes unnecessary recompositions, and best practices in Jetpack Compose.",
    tags = setOf("Android", "Compose", "Recomposition", "State Management", "Coding Interview", "Technical Interview")
)


val androidRecompositionStabilityQuizKo = listOf(
    TestQuiz1(
        point = 5,
        question = "다음 중 Compose Stability에 대한 올바른 설명은 무엇인가요?",
        answers = mutableListOf(
            "List는 항상 Stable로 간주된다",
            "오직 Imutable 데이터 클래스만 Stable이다",
            "@Stable로 표시된 클래스는 항상 다시 컴포즈된다",
            "Composable의 매개변수가 Stable이면 절대로 다시 컴포즈되지 않는다",
            "MutableState<T>는 Stable이다"
        ),
        ans = mutableListOf(false, false, false, false, true)
    ),

    TestQuiz1(
        point = 5,
        question = "다음 중 Unstable로 간주되어 불필요한 recomposition을 유발할 수 있는 타입은 무엇인가요?",
        answers = mutableListOf(
            "모든 val 속성을 가진 데이터 클래스",
            "MutableState",
            "List<T>",
            "Set<T>",
            "Compose 런타임이 포함되지 않은 모듈의 클래스"
        ),
        ans = mutableListOf(false, false, true, true, true)
    ),

    TestQuiz1(
        point = 5,
        question = "Unstable한 매개변수가 Composable에 전달되면 어떻게 되나요?",
        answers = mutableListOf(
            "Compose는 매개변수를 무시하고 성능을 최적화한다",
            "앱이 충돌한다",
            "Compose가 자동으로 Stable한 객체로 변환한다",
            "아무 일도 일어나지 않는다",
            "부모 Composable이 다시 컴포즈될 때마다 항상 다시 컴포즈된다"
        ),
        ans = mutableListOf(false, false, false, false, true)
    ),

    TestQuiz1(
        point = 5,
        question = "Composable이 recomposition을 건너뛸 수 있는지 결정하는 요소는 무엇인가요?",
        answers = mutableListOf(
            "@Composable 어노테이션을 사용했는지 여부",
            "매개변수의 stability",
            "inline으로 표시되었는지 여부",
            "Column 또는 Row 내부에 있는지 여부",
            "함수의 크기"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Composable의 stability를 디버깅하는 방법은 무엇인가요?",
        answers = mutableListOf(
            "Compose 컴파일러 리포트를 사용한다",
            "@Stable 어노테이션이 있는지 함수 시그니처를 확인한다",
            "리플렉션을 사용하여 런타임 동작을 검사한다",
            "항상 Unit을 반환하도록 Composable을 수정한다",
            "로그 문을 사용하여 성능을 수동으로 측정한다"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "클래스가 생성된 후 절대 변경되지 않음을 보장하는 어노테이션은 무엇인가요?",
        answers = mutableListOf(
            "@Stable",
            "@Immutable",
            "@Composable",
            "@Keep",
            "@Static"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "다음 중 항상 recomposition이 발생하는 Composable은 무엇인가요?",
        answers = mutableListOf(
            "@Immutable 데이터 클래스를 매개변수로 사용하는 Composable",
            "MutableState 매개변수를 사용하는 Composable",
            "List<T> 매개변수를 사용하는 Composable",
            "inline 함수 내부의 Composable",
            "기본 타입(Primitive Type) 매개변수를 사용하는 Composable"
        ),
        ans = mutableListOf(false, false, true, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "다음 중 불필요한 recomposition을 유발하지 않는 Composable은 무엇인가요?",
        answers = mutableListOf(
            "MutableState를 사용하는 Composable",
            "일반 List<T>를 사용하는 Composable",
            "ImmutableList<T>를 사용하는 Composable",
            "데이터 클래스 내부에서 var 속성을 사용하는 Composable",
            "매개변수가 없는 Composable"
        ),
        ans = mutableListOf(true, false, true, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "왜 Kotlin의 List와 Set 같은 컬렉션이 Compose에서 Unstable으로 간주되나요?",
        answers = mutableListOf(
            "런타임에 수정될 수 있기 때문",
            "항상 변경 가능한 요소를 포함하기 때문",
            "기본 타입이기 때문",
            "Compose가 컬렉션을 지원하지 않기 때문",
            "recomposition에 최적화되어 있기 때문"
        ),
        ans = mutableListOf(true, false, false, false, false)
    )
)

val androidRecompositionStabilityQuizDataKo = androidCodingTheme.copy(
    quizzes = androidRecompositionStabilityQuizKo,
    title = "안드로이드 퀴즈: Stability",
    description = "Kotlin 컴파일러에서 안정성을 판단하는 방법, 불필요한 recomposition을 유발하는 원인, 그리고 최적화 방법을 알고 있는지 확인하세요.",
    tags = setOf("안드로이드", "Stability", "Recomposition", "상태 관리", "코딩 인터뷰", "기술 면접")
)
