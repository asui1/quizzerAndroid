package com.asu1.quiz.viewer

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
fun KotlinCodeHighlighter(
    modifier: Modifier = Modifier,
    code: String,
) {
    val colors = MaterialTheme.colorScheme

    val highlightedCode = remember { getHighlightedKotlinCode(code, colors) }

    SelectionContainer {
        BasicText(
            text = highlightedCode,
            modifier = modifier.padding(16.dp),
            style = TextStyle(fontSize = TextUnit.Unspecified, textAlign = TextAlign.Start)
        )
    }
}

// Function to highlight Kotlin code with dynamic colors
private fun getHighlightedKotlinCode(
    code: String,
    colors: ColorScheme
): AnnotatedString {
    val annotatedString = AnnotatedString.Builder(code)

    // Dynamic colors based on MaterialTheme
    val keywordColor = colors.primary       // Keywords
    val typeColor = colors.secondary        // Types (Class, Interface, etc.)
    val stringColor = colors.tertiary       // Strings
    val numberColor = colors.onSurface  // Numbers
    val commentColor = colors.onSurfaceVariant // Comments
    val annotationColor = colors.error // Annotations
    val punctuationColor = colors.outline   // Punctuation

    // Unified Patterns (for both Kotlin and Python)
    val keywordPattern = """
        \b(class|fun|val|var|if|else|for|while|return|break|continue|import|from|as|def|try|except|finally|with|is|in|raise|print|object|interface|package|typealias|sealed|companion|abstract|open|override|constructor|init|private|protected|public|internal|not|and|or|async|await|assert|lambda|global|nonlocal|yield|del|pass|None|True|False|match|case)\b
    """.trimIndent().toRegex()

    // Python Built-in Functions (Colored as Keywords)
    val builtInFunctionPattern = """
        \b(abs|all|any|ascii|bin|bool|breakpoint|bytearray|bytes|callable|chr|classmethod|compile|complex|delattr|dict|dir|divmod|enumerate|eval|exec|filter|float|format|frozenset|getattr|globals|hasattr|hash|help|hex|id|input|int|isinstance|issubclass|iter|len|list|locals|map|max|memoryview|min|next|object|oct|open|ord|pow|print|property|range|repr|reversed|round|set|setattr|slice|sorted|staticmethod|str|sum|super|tuple|type|vars|zip)\b
    """.trimIndent().toRegex()
    val typePattern = "\\b[A-Z][a-zA-Z0-9_]*\\b".toRegex()
    val stringPattern = "\"{3}[\\s\\S]*?[^\\\\]\"{3}|\"([^\"\\\\]|\\\\.)*\"|'.*?'".toRegex()
    val numberPattern = "\\b(0[xX][0-9a-fA-F_]+|0[bB][0-1_]+|[0-9]+(\\.[0-9]+)?([eE]-?[0-9]+)?)\\b".toRegex()
    val commentPattern = "#.*|//.*|/\\*[\\s\\S]*?\\*/".toRegex()
    val annotationPattern = "@[a-zA-Z_][a-zA-Z0-9_]*".toRegex()
    val punctuationPattern = "[\\[\\]{}().,:;=+\\-*/%<>!?|&^~]+".toRegex()


    // Apply styles
    applyTextStyle(annotatedString, code, keywordPattern, keywordColor)
    applyTextStyle(annotatedString, code, builtInFunctionPattern, keywordColor)
    applyTextStyle(annotatedString, code, typePattern, typeColor)
    applyTextStyle(annotatedString, code, stringPattern, stringColor)
    applyTextStyle(annotatedString, code, numberPattern, numberColor)
    applyTextStyle(annotatedString, code, commentPattern, commentColor)
    applyTextStyle(annotatedString, code, annotationPattern, annotationColor)
    applyTextStyle(annotatedString, code, punctuationPattern, punctuationColor)

    return annotatedString.toAnnotatedString()
}

// Function to apply styles
private fun applyTextStyle(
    annotatedString: AnnotatedString.Builder,
    code: String,
    pattern: Regex,
    color: ComposeColor
) {
    pattern.findAll(code).forEach {
        annotatedString.addStyle(
            SpanStyle(color = color),
            it.range.first,
            it.range.last + 1
        )
    }
}

// Preview with MaterialTheme
@Preview(showBackground = true)
@Composable
fun KotlinCodeHighlighterPreview() {
    val codeSnippet = """
        fun main() {
            val greeting: String = "Hello, Kotlin!"
            val number = 123
            println(greeting) // Print the message
            
            /* Multi-line comment */
            @Annotation
            class ExampleClass {
                fun exampleMethod() {
                    println("Example")
                }
            }
        }
    """.trimIndent()

    MaterialTheme {
        KotlinCodeHighlighter(
            code = codeSnippet,
        )
    }
}
