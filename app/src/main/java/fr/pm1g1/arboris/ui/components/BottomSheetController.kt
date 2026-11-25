package fr.pm1g1.arboris.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class BottomSheetController {
    var content by mutableStateOf<(@Composable () -> Unit)?>(null)
        private set

    fun show(sheet: @Composable () -> Unit ) {
        content = sheet
    }

    fun hide(){
        content = null
    }
}

@Composable
fun rememberBottomSheetController(): BottomSheetController {
    return remember { BottomSheetController() }
}