package org.vocaminder.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import org.vocaminder.generated.resources.Res
import org.vocaminder.generated.resources.abeezee
import org.vocaminder.generated.resources.abeezee_italic

private val DarkColorScheme = darkColorScheme(
    background = Color.Black
)
private val LightColorScheme = lightColorScheme(
    background = Color.White
)


private val lineHeightStyle = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None
)

val ColorScheme.textSecondary
    @Composable
    get() = Color(0xFF8E8E8F)

val ColorScheme.textTertiary
    @Composable
    get() = Color(0xFFCACACA)

val ColorScheme.cardPrimary
    @Composable
    get() = Color(0xFF212121)

val ColorScheme.pagerIndicatorBackground
    @Composable
    get() = Color(0xFF121212)

@Composable
fun rememberTypography(): Typography {
    val regularFontFamily = FontFamily(
        Font(Res.font.abeezee, FontWeight.Normal, FontStyle.Normal),
        Font(Res.font.abeezee_italic, FontWeight.Normal, FontStyle.Italic)

    )

    return remember {
        Typography(
            bodyLarge = TextStyle(
                fontFamily = regularFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                lineHeightStyle = lineHeightStyle
            ),
            bodyMedium = TextStyle(
                fontFamily = regularFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                lineHeightStyle = lineHeightStyle
            ),
            titleMedium = TextStyle(
                fontFamily = regularFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                lineHeightStyle = lineHeightStyle
            ),
            headlineSmall = TextStyle(
                fontFamily = regularFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                lineHeightStyle = lineHeightStyle
            ),
            headlineMedium = TextStyle(
                fontFamily = regularFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                lineHeightStyle = lineHeightStyle
            ),
            labelMedium = TextStyle(
                fontFamily = regularFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                lineHeight = 28.sp,
                lineHeightStyle = lineHeightStyle
            ),
        )
    }
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme =if(isSystemInDarkTheme()) DarkColorScheme else LightColorScheme,
        shapes = MaterialTheme.shapes.copy(),
        typography = rememberTypography(),
        content = {
            Surface(
                content = content,
                color = MaterialTheme.colorScheme.background
            )
        }
    )
}
