package fr.pm1g1.arboris.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.ui.graphics.Color

@Composable
fun arborisPageBackgroundColor(): Color {
    return if (isSystemInDarkTheme()) {
        ArborisPageBackgroundDark
    } else {
        ArborisPageBackgroundLight
    }
}

@Composable
fun arborisPageTitleColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    return if (isSystemInDarkTheme()) {
        colorScheme.onBackground
    } else {
        colorScheme.primary
    }
}

@Composable
fun arborisCardContainerColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    return if (isSystemInDarkTheme()) {
        colorScheme.surfaceVariant
    } else {
        colorScheme.background
    }
}

@Composable
fun arborisCardContentColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    return if (isSystemInDarkTheme()) {
        colorScheme.onSurfaceVariant
    } else {
        colorScheme.onBackground
    }
}

@Composable
fun arborisRadioTextColor(): Color {
    val colorScheme = MaterialTheme.colorScheme
    return if (isSystemInDarkTheme()) {
        Color.White
    } else {
        colorScheme.onBackground
    }
}

@Composable
fun arborisRadioColors() = if (isSystemInDarkTheme()) {
    RadioButtonDefaults.colors(
        selectedColor = Color.White,
        unselectedColor = Color.White.copy(alpha = 0.7f)
    )
} else {
    RadioButtonDefaults.colors()
}
