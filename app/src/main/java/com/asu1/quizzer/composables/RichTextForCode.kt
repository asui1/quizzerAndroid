package com.asu1.quizzer.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.asu1.resources.QuizzerAndroidTheme
import com.wakaztahir.codeeditor.model.CodeLang
import com.wakaztahir.codeeditor.prettify.PrettifyParser
import com.wakaztahir.codeeditor.theme.CodeThemeType
import com.wakaztahir.codeeditor.utils.parseCodeAsAnnotatedString

@Composable
fun RichTextForCode(
    modifier: Modifier = Modifier,
    code: String,
){
    // Step 1. Declare Language & Code
    val language = CodeLang.Kotlin

    // Step 2. Create Parser & Theme
    val parser = remember { PrettifyParser() } // try getting from LocalPrettifyParser.current
    var themeState by remember { mutableStateOf(CodeThemeType.Monokai) }
    val theme = remember(themeState) { themeState.theme }
    // Step 3. Parse Code For Highlighting
    val parsedCode = remember {
        parseCodeAsAnnotatedString(
            parser = parser,
            theme = theme,
            lang = language,
            code = code
        )
    }

    // Step 4. Display In A Text Composable
    Text(parsedCode)
}

@Preview(showBackground = true)
@Composable
fun RichTextForCodePreview(){
    QuizzerAndroidTheme(darkTheme = true) {
        RichTextForCode(
            code =     """             
    package com.wakaztahir.codeeditor
    
    fun main(){
        println("Hello World");
    }
    """.trimIndent()
        )
    }
}