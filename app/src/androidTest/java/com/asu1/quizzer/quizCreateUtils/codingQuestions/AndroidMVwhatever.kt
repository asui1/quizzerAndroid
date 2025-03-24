package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.quizTheme.androidCodingTheme

val androidMVWQuiz = listOf(
    // MVP, MVVM, MVI Questions
    TestQuiz1(
        point = 5,
        question = "What is the main characteristic of the MVP (Model-View-Presenter) pattern?",
        answers = mutableListOf(
            "It directly binds UI components to the data model",
            "The Presenter handles UI logic and acts as an intermediary between Model and View",
            "It uses unidirectional data flow",
            "It requires Jetpack Compose",
            "It replaces MVVM in modern Android apps"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "How does MVVM (Model-View-ViewModel) differ from MVP?",
        answers = mutableListOf(
            "MVVM uses LiveData or StateFlow to update the UI automatically",
            "MVVM requires more manual UI updates than MVP",
            "MVVM cannot handle background processes",
            "MVVM does not separate concerns as well as MVP",
            "MVVM does not work with Jetpack Compose"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "What is a key feature of the MVI (Model-View-Intent) pattern?",
        answers = mutableListOf(
            "It follows a unidirectional data flow",
            "It uses a bidirectional binding mechanism",
            "It directly updates the UI from the Model",
            "It does not rely on ViewModels",
            "It is less reactive compared to MVP and MVVM"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Which architectural pattern is most suitable for handling state in Jetpack Compose?",
        answers = mutableListOf(
            "MVP",
            "MVVM",
            "MVI",
            "None, Jetpack Compose has its own architecture",
            "MVVM and MVI are both suitable"
        ),
        ans = mutableListOf(false, false, false, false, true)
    ),

    TestQuiz1(
        point = 5,
        question = "In the MVVM pattern, how does the ViewModel communicate changes to the View?",
        answers = mutableListOf(
            "Directly modifying UI components",
            "Using callbacks from the Model",
            "Through observable data structures like LiveData or StateFlow",
            "By calling functions inside the View class",
            "Via Intent objects passed between components"
        ),
        ans = mutableListOf(false, false, true, false, false)
    ),

    // Real Coding Interview Style Questions
    TestQuiz1(
        point = 5,
        question = "What is the best way to test a ViewModel in MVVM?",
        answers = mutableListOf(
            "Using Espresso for UI testing",
            "Mocking dependencies and testing using JUnit and LiveData testing rules",
            "Manually verifying the UI",
            "Using Robolectric only",
            "Using only integration tests"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "How can you handle user input events efficiently in MVI?",
        answers = mutableListOf(
            "Using LiveData directly in the UI",
            "Passing events through an Intent and reducing state mutations",
            "Binding UI elements directly to the model",
            "Storing all events in a database",
            "Triggering updates from multiple sources without state management"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "How should the Presenter in MVP be structured for testability?",
        answers = mutableListOf(
            "Inject dependencies using Dependency Injection",
            "Keep all logic in the View",
            "Call APIs directly from the Presenter",
            "Store all state in the Presenter",
            "Use static methods to handle UI logic"
        ),
        ans = mutableListOf(true, false, false, false, false)
    )
)

val androidMVWQuizData = androidCodingTheme.copy(
    quizzes = androidMVWQuiz,
    title = "Android Quiz: MVP, MVVM, and MVI",
    description = "Test your understanding of MVP, MVVM, and MVI patterns, including their differences and use cases.",
    tags = setOf("Android", "MVP", "MVVM", "MVI", "Coding Interview", "Technical Interview")
)

val androidMVWQuizKo = listOf(
    // MVP, MVVM, MVI 질문
    TestQuiz1(
        point = 5,
        question = "MVP (Model-View-Presenter) 패턴의 주요 특징은 무엇인가요?",
        answers = mutableListOf(
            "UI 구성 요소를 데이터 모델과 직접 바인딩한다",
            "Presenter가 UI 로직을 처리하고 Model과 View 사이에서 중개자 역할을 한다",
            "단방향 데이터 흐름을 사용한다",
            "Jetpack Compose가 필요하다",
            "MVVM을 대체하는 현대적인 안드로이드 아키텍처이다"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "MVVM (Model-View-ViewModel)은 MVP와 어떻게 다른가요?",
        answers = mutableListOf(
            "MVVM은 LiveData 또는 StateFlow를 사용하여 UI를 자동으로 업데이트한다",
            "MVVM은 MVP보다 수동적인 UI 업데이트가 더 많이 필요하다",
            "MVVM은 백그라운드 프로세스를 처리할 수 없다",
            "MVVM은 MVP보다 관심사 분리가 덜 명확하다",
            "MVVM은 Jetpack Compose에서 작동하지 않는다"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "MVI (Model-View-Intent) 패턴의 핵심 특징은 무엇인가요?",
        answers = mutableListOf(
            "단방향 데이터 흐름을 따른다",
            "양방향 바인딩 메커니즘을 사용한다",
            "Model에서 UI를 직접 업데이트한다",
            "ViewModel을 필요로 하지 않는다",
            "MVP 및 MVVM보다 반응형이 적다"
        ),
        ans = mutableListOf(true, false, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "Jetpack Compose에서 상태 관리를 처리하기에 가장 적합한 아키텍처 패턴은 무엇인가요?",
        answers = mutableListOf(
            "MVP",
            "MVVM",
            "MVI",
            "Jetpack Compose는 자체 아키텍처를 사용하므로 필요 없다",
            "MVVM과 MVI 모두 적합하다"
        ),
        ans = mutableListOf(false, false, false, false, true)
    ),

    TestQuiz1(
        point = 5,
        question = "MVVM 패턴에서 ViewModel은 어떻게 View에 변경 사항을 전달하나요?",
        answers = mutableListOf(
            "UI 구성 요소를 직접 수정한다",
            "Model에서 콜백을 사용한다",
            "LiveData 또는 StateFlow와 같은 옵저버블 데이터 구조를 통해 전달한다",
            "View 클래스 내부의 함수를 호출한다",
            "Intent 객체를 사용하여 컴포넌트 간 전달한다"
        ),
        ans = mutableListOf(false, false, true, false, false)
    ),

    // 실제 코딩 면접 스타일 질문
    TestQuiz1(
        point = 5,
        question = "MVVM에서 ViewModel을 테스트하는 가장 좋은 방법은 무엇인가요?",
        answers = mutableListOf(
            "UI 테스트를 위해 Espresso를 사용한다",
            "의존성을 Mock으로 만들고 JUnit 및 LiveData 테스트 규칙을 사용하여 테스트한다",
            "UI를 수동으로 확인한다",
            "Robolectric만 사용한다",
            "통합 테스트만 사용한다"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "MVI에서 사용자 입력 이벤트를 효율적으로 처리하는 방법은 무엇인가요?",
        answers = mutableListOf(
            "LiveData를 UI에서 직접 사용한다",
            "Intent를 통해 이벤트를 전달하고 상태 변이를 최소화한다",
            "UI 요소를 Model에 직접 바인딩한다",
            "모든 이벤트를 데이터베이스에 저장한다",
            "여러 소스에서 업데이트를 트리거하여 상태 관리를 사용하지 않는다"
        ),
        ans = mutableListOf(false, true, false, false, false)
    ),

    TestQuiz1(
        point = 5,
        question = "MVP 패턴에서 Presenter를 테스트하기 위해 어떻게 설계해야 하나요?",
        answers = mutableListOf(
            "의존성 주입(Dependency Injection)을 사용하여 Presenter의 의존성을 주입한다",
            "모든 로직을 View 내부에 유지한다",
            "Presenter에서 API를 직접 호출한다",
            "Presenter 내부에서 모든 상태를 저장한다",
            "UI 로직을 처리하기 위해 정적 메서드를 사용한다"
        ),
        ans = mutableListOf(true, false, false, false, false)
    )
)

val androidMVWQuizDataKo = androidCodingTheme.copy(
    quizzes = androidMVWQuizKo,
    title = "안드로이드 퀴즈: MVP, MVVM, MVI",
    description = "MVP, MVVM 및 MVI 패턴의 차이점과 사용 사례를 테스트하세요.",
    tags = setOf("안드로이드", "MVP", "MVVM", "MVI", "코딩 면접", "기술 면접")
)
