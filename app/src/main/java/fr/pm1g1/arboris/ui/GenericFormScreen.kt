package fr.pm1g1.arboris.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import fr.pm1g1.arboris.ui.View.viewModel.UiState

@Composable
fun <T : Any> GenericFormScreen(
        itemId: Int,
        itemState: UiState<T>,
        loadItem: (Int) -> Unit,
        content: @Composable (item: T?, isNew: Boolean) -> Unit
) {
    val isNew = itemId == -1

    if (isNew) {
        content(null, true)
    } else {
        LaunchedEffect(itemId) { loadItem(itemId) }

        when (itemState) {
            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is UiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = itemState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is UiState.Success -> {
                content(itemState.data, false)
            }
        }
    }
}
