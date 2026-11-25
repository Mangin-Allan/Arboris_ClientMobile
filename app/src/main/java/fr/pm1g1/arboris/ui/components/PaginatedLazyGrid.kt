package fr.pm1g1.arboris.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaginatedLazyGrid(
    modifier: Modifier = Modifier,
    lazyGridState: LazyGridState = rememberLazyGridState(),
    loading: Boolean = false,
    loadNext: () -> Unit,
    columns: GridCells,
    horizontalArrangement: Arrangement.HorizontalOrVertical,
    verticalArrangement: Arrangement.HorizontalOrVertical,
    content: LazyGridScope.() -> Unit,
) {
    // Si on est a 5 lignes ou moins de la fin, on charge la suite
    val shouldLoadNext by remember {
        derivedStateOf {
            val visibleItems = lazyGridState.layoutInfo.visibleItemsInfo
            val totalItemsCount = lazyGridState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex = visibleItems.lastOrNull()?.index ?: 0
            val columnCount = visibleItems.maxOfOrNull { it.column + 1 } ?: 1

            totalItemsCount > 0 && (lastVisibleItemIndex + columnCount * 5 >= totalItemsCount)
        }
    }

    LaunchedEffect(Unit) {
        // permet de ne pas lancer a la premiere initialization, mais uniquement au changement de valeur
        snapshotFlow { shouldLoadNext }
            .collect { shouldLoad ->
                if (shouldLoad && !loading) {
                    loadNext()
                }
            }
    }

    /*LaunchedEffect(shouldLoadNext, loading) {
        if (shouldLoadNext == true && loading == false) {
            loadNext()
        }
    }*/

    LazyVerticalGrid(modifier = modifier, horizontalArrangement = horizontalArrangement, verticalArrangement = verticalArrangement, state = lazyGridState, columns = columns) {
        content()
        if (loading == true) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }
    }
}
