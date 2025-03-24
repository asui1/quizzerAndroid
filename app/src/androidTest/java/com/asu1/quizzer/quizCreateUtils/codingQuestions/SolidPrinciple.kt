package com.asu1.quizzer.quizCreateUtils.codingQuestions

import com.asu1.models.serializers.BodyType
import com.asu1.quizzer.quizCreateUtils.TestQuiz1
import com.asu1.quizzer.quizCreateUtils.quizTheme.codingTheme

val solidPrinciplesQuiz = listOf(

    TestQuiz1(
        point = 5,
        question = "Which SOLID principle does the following code snippet violate?",
        answers = mutableListOf(
            "Single Responsibility Principle",
            "Open-Closed Principle",
            "Liskov Substitution Principle",
            "Interface Segregation Principle",
            "Dependency Inversion Principle"
        ),
        ans = mutableListOf(false, false, true, false, false),
        bodyType = BodyType.CODE("""
open class Rectangle(var width: Int, var height: Int) {
    open fun setWidth(w: Int) {
        width = w
    }
    open fun setHeight(h: Int) {
        height = h
    }
}

class Square(size: Int) : Rectangle(size, size) {
    override fun setWidth(w: Int) {
        super.setWidth(w)
        super.setHeight(w)
    }
    override fun setHeight(h: Int) {
        super.setWidth(h)
        super.setHeight(h)
    }
}
""")
    ),
    TestQuiz1(
        point = 5,
        question = "Which SOLID principle does the following code snippet violate?",
        answers = mutableListOf(
            "Single Responsibility Principle",
            "Open-Closed Principle",
            "Liskov Substitution Principle",
            "Interface Segregation Principle",
            "Dependency Inversion Principle"
        ),
        ans = mutableListOf(true, false, false, false, false),
        bodyType = BodyType.CODE("""
class UserManager {
    fun createUser(name: String) {
        // logic to create user
    }

    fun deleteUser(name: String) {
        // logic to delete user
    }

    fun logAction(action: String) {
        // logging code
    }
}

// This class handles user management and also includes logging.
// The responsibilities appear unrelated.
""")
    ),

    TestQuiz1(
        point = 5,
        question = "Which SOLID principle does the following code snippet violate?",
        answers = mutableListOf(
            "Single Responsibility Principle",
            "Open-Closed Principle",
            "Liskov Substitution Principle",
            "Interface Segregation Principle",
            "Dependency Inversion Principle"
        ),
        ans = mutableListOf(false, true, false, false, false),
        bodyType = BodyType.CODE("""
class PaymentProcessor {
    fun processCreditCard(amount: Double) {
        // ...
    }
    fun processPaypal(amount: Double) {
        // ...
    }
    // Additional payment methods would require editing this class.
}
""")
    ),

    TestQuiz1(
        point = 5,
        question = "Which SOLID principle does the following code snippet violate?",
        answers = mutableListOf(
            "Single Responsibility Principle",
            "Open-Closed Principle",
            "Liskov Substitution Principle",
            "Interface Segregation Principle",
            "Dependency Inversion Principle"
        ),
        ans = mutableListOf(false, false, false, false, true),
        bodyType = BodyType.CODE("""
class MySQLDatabase {
    fun saveData(data: String) {
        // ...
    }
}

class ReportGenerator {
    private val database = MySQLDatabase()

    fun generateReport(data: String) {
        // some logic
        database.saveData(data)
    }
}

// This class depends on a specific database implementation.
""")
    ),
    TestQuiz1(
        point = 5,
        question = "Which SOLID principle does the following code snippet violate?",
        answers = mutableListOf(
            "Single Responsibility Principle",
            "Open-Closed Principle",
            "Liskov Substitution Principle",
            "Interface Segregation Principle",
            "Dependency Inversion Principle"
        ),
        ans = mutableListOf(false, false, false, true, false),
        bodyType = BodyType.CODE("""
interface MultiDevice {
    fun print(document: String)
    fun scan(document: String)
    fun fax(document: String)
}

class SimplePrinter : MultiDevice {
    override fun print(document: String) {
        // printing
    }
    override fun scan(document: String) {
        // not needed, but forced to implement
    }
    override fun fax(document: String) {
        // not needed, but forced to implement
    }
}
""")
    ),
)

val solidPrinciplesQuizData = codingTheme.copy(
    quizzes = solidPrinciplesQuiz,
    title = "SOLID Principles Interview Quiz",
    description = "A set of realistic code snippets violating each principle, so you can identify which principle is at fault.",
    tags = setOf("SOLID", "Design Principles", "Coding Interview", "Technical Interview")
)


// ================ KOREAN VERSION =================

val solidPrinciplesQuizKorean = listOf(
    TestQuiz1(
        point = 5,
        question = "다음 코드 스니펫이 위반하고 있는 SOLID 원칙은 무엇입니까?",
        answers = mutableListOf(
            "단일 책임 원칙",
            "개방-폐쇄 원칙",
            "리스코프 치환 원칙",
            "인터페이스 분리 원칙",
            "의존성 역전 원칙"
        ),
        ans = mutableListOf(false, true, false, false, false),
        bodyType = BodyType.CODE("""
class PaymentProcessor {
    fun processCreditCard(amount: Double) {
        // ...
    }
    fun processPaypal(amount: Double) {
        // ...
    }
    // 새로운 결제 방식을 추가할 때마다 이 클래스를 수정해야 합니다.
}
""")
    ),
    TestQuiz1(
        point = 5,
        question = "다음 코드 스니펫이 위반하고 있는 SOLID 원칙은 무엇입니까?",
        answers = mutableListOf(
            "단일 책임 원칙",
            "개방-폐쇄 원칙",
            "리스코프 치환 원칙",
            "인터페이스 분리 원칙",
            "의존성 역전 원칙"
        ),
        ans = mutableListOf(true, false, false, false, false),
        bodyType = BodyType.CODE(
            """
class UserManager {
    fun createUser(name: String) {
        // logic to create user
    }

    fun deleteUser(name: String) {
        // logic to delete user
    }

    fun logAction(action: String) {
        // logging code
    }
}""")
    ),
    TestQuiz1(
        point = 5,
        question = "다음 코드 스니펫이 위반하고 있는 SOLID 원칙은 무엇입니까?",
        answers = mutableListOf(
            "단일 책임 원칙",
            "개방-폐쇄 원칙",
            "리스코프 치환 원칙",
            "인터페이스 분리 원칙",
            "의존성 역전 원칙"
        ),
        ans = mutableListOf(false, false, true, false, false),
        bodyType = BodyType.CODE("""
open class Rectangle(var width: Int, var height: Int) {
    open fun setWidth(w: Int) {
        width = w
    }
    open fun setHeight(h: Int) {
        height = h
    }
}

class Square(size: Int) : Rectangle(size, size) {
    override fun setWidth(w: Int) {
        super.setWidth(w)
        super.setHeight(w)
    }
    override fun setHeight(h: Int) {
        super.setWidth(h)
        super.setHeight(h)
    }
}
""")
    ),
    TestQuiz1(
        point = 5,
        question = "다음 코드 스니펫이 위반하고 있는 SOLID 원칙은 무엇입니까?",
        answers = mutableListOf(
            "단일 책임 원칙",
            "개방-폐쇄 원칙",
            "리스코프 치환 원칙",
            "인터페이스 분리 원칙",
            "의존성 역전 원칙"
        ),
        ans = mutableListOf(false, false, false, false, true),
        bodyType = BodyType.CODE("""
class MySQLDatabase {
    fun saveData(data: String) {
        // ...
    }
}

class ReportGenerator {
    private val database = MySQLDatabase()

    fun generateReport(data: String) {
        // 일부 로직
        database.saveData(data)
    }
}
""")
    ),
    TestQuiz1(
        point = 5,
        question = "다음 코드 스니펫이 위반하고 있는 SOLID 원칙은 무엇입니까?",
        answers = mutableListOf(
            "단일 책임 원칙",
            "개방-폐쇄 원칙",
            "리스코프 치환 원칙",
            "인터페이스 분리 원칙",
            "의존성 역전 원칙"
        ),
        ans = mutableListOf(false, false, false, true, false),
        bodyType = BodyType.CODE("""
interface MultiDevice {
    fun print(document: String)
    fun scan(document: String)
    fun fax(document: String)
}

class SimplePrinter : MultiDevice {
    override fun print(document: String) {
        // 문서 출력
    }
    override fun scan(document: String) {
        // 필요 없지만 구현해야 함
    }
    override fun fax(document: String) {
        // 필요 없지만 구현해야 함
    }
}
""")
    ),


    )

val solidPrinciplesQuizDataKorean = codingTheme.copy(
    quizzes = solidPrinciplesQuizKorean,
    title = "SOLID 원칙 퀴즈",
    description = "각 SOLID 원칙을 위반하는 실제 예시 코드를 통해 위반된 원칙을 식별해보세요.",
    tags = setOf("SOLID", "객체 지향 원칙", "디자인 원칙", "코딩 인터뷰", "기술 면접")
)
