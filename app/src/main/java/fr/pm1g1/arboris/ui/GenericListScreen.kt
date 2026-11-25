package fr.pm1g1.arboris.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.ui.View.viewModel.UiState
import fr.pm1g1.arboris.ui.components.PaginatedLazyGrid
import fr.pm1g1.arboris.ui.theme.arborisCardContainerColor
import fr.pm1g1.arboris.ui.theme.arborisCardContentColor
import fr.pm1g1.arboris.ui.theme.arborisPageBackgroundColor

@Composable
fun <T : Any> GenericListScreen(
        title: String? = null,
        onAdd: () -> Unit,
        onCardClicked: (T) -> Unit,
        itemContent: @Composable (T, () -> Unit) -> Unit,
        state: UiState<List<T>>,
        loadNext: () -> Unit,
        emptyMessage: String? = null,
        gridHorizontalSpacing: androidx.compose.ui.unit.Dp = 20.dp,
        gridVerticalSpacing: androidx.compose.ui.unit.Dp = 20.dp,
        gridContentPadding: PaddingValues = PaddingValues(horizontal = 16.dp),
        fabContainerColor: Color? = null,
        fabContentColor: Color? = null,
) {
    Box(modifier = Modifier.fillMaxSize().background(arborisPageBackgroundColor())) {
        val resolvedFabContainerColor = fabContainerColor ?: arborisCardContainerColor()
        val resolvedFabContentColor = fabContentColor ?: arborisCardContentColor()
        Column(modifier = Modifier.fillMaxSize()) {
            if (title != null) {
                Text(
                        text = title,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                )
            }

            when (state) {
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    if (state.data.isEmpty() && emptyMessage != null) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) {
                            Text(
                                    color = MaterialTheme.colorScheme.secondary,
                                    text = emptyMessage,
                                    style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        PaginatedLazyGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(gridHorizontalSpacing),
                                verticalArrangement = Arrangement.spacedBy(gridVerticalSpacing),
                                loadNext = loadNext,
                                modifier = Modifier.padding(gridContentPadding).weight(1f)
                        ) { items(state.data) { item -> itemContent(item) { onCardClicked(item) } } }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }

        FloatingActionButton(
                onClick = onAdd,
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
                containerColor = resolvedFabContainerColor,
                contentColor = resolvedFabContentColor
        ) {
            Icon(
                    Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_button),
            )
        }
    }
}
