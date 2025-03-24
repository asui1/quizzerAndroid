package com.asu1.quizzer.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
            style = TextStyle(fontSize = androidx.compose.ui.unit.TextUnit.Unspecified, textAlign = TextAlign.Start)
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

    // Regex patterns
    val keywordPattern =
        "\\b(package|public|protected|external|internal|private|open|abstract|constructor|final|override|import|for|while|as|typealias|get|set|data|enum|annotation|sealed|class|this|super|val|var|fun|is|in|throw|return|break|continue|companion|object|if|try|else|do|when|init|interface|typeof)\\b".toRegex()

    val typePattern = "\\b[A-Z][a-zA-Z0-9_]*\\b".toRegex()
    val stringPattern = "\"{3}[\\s\\S]*?[^\\\\]\"{3}|\"([^\"\\\\]|\\\\.)*\"|'.'".toRegex()
    val numberPattern = "\\b(0[xX][0-9a-fA-F_]+L?|0[bB][0-1]+L?|[0-9_.]+([eE]-?[0-9]+)?[fFL]?)\\b".toRegex()
    val commentPattern = "//.*|/\\*[\\s\\S]*?\\*/".toRegex()
    val annotationPattern = "@([a-zA-Z0-9_]+)".toRegex()
    val punctuationPattern = "[.!%&()*+,\\-;<=>?\\[\\\\\\]^{|}:]+".toRegex()

    // Apply styles
    applyTextStyle(annotatedString, code, keywordPattern, keywordColor)
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
