package com.puciec.ghiblikmp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Design tokens: cores e espaçamento ficam AQUI e só aqui. As telas usam
// MaterialTheme.colorScheme.* e Spacing.* — nunca Color(0x...) ou "16.dp" soltos.
// TODO (UI/UX): tipografia própria (ex.: Typography com fonte customizada).

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F6F7E), // azul-céu
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCDE8EE),
    onPrimaryContainer = Color(0xFF0B2F38),
    secondary = Color(0xFF5E8A5A), // verde-campo
    onSecondary = Color.White,
    background = Color(0xFFFBF6EA), // creme
    onBackground = Color(0xFF1D2A2C),
    surface = Color(0xFFFFFCF4),
    onSurface = Color(0xFF1D2A2C),
    surfaceVariant = Color(0xFFE7E2D2),
    onSurfaceVariant = Color(0xFF4A5557),
    error = Color(0xFFB3261E),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8CCBD8),
    onPrimary = Color(0xFF00363F),
    primaryContainer = Color(0xFF1F4E5A),
    onPrimaryContainer = Color(0xFFCDE8EE),
    secondary = Color(0xFFA9D3A3),
    onSecondary = Color(0xFF133715),
    background = Color(0xFF111A1C),
    onBackground = Color(0xFFE3EAEB),
    surface = Color(0xFF172224),
    onSurface = Color(0xFFE3EAEB),
    surfaceVariant = Color(0xFF2A3A3D),
    onSurfaceVariant = Color(0xFFB9C6C8),
    error = Color(0xFFF2B8B5),
)

object Spacing {
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
}

@Composable
fun GhibliTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content,
    )
}
