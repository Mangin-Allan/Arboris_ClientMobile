package fr.pm1g1.arboris.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetHost (
    controller: BottomSheetController,
    content: @Composable () -> Unit
){
    Box{
        content()

        controller.content?.let { sheetContent ->
            ModalBottomSheet(
                onDismissRequest = { controller.hide() }
            ) {
                sheetContent()
            }
        }
    }
}