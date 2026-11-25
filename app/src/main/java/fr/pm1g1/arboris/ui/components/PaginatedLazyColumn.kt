package fr.pm1g1.arboris.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PaginatedLazyColumn(
        modifier: Modifier = Modifier,
        lazyListState: LazyListState = rememberLazyListState(),
        loading: Boolean,
        loadNext: () -> Unit,
        content: LazyListScope.() -> Unit
) {
    // Si on est a 5 items ou moins de la fin, on charge la suite
    val shouldLoadNext by remember {
        derivedStateOf {
            val totalItemsCount = lazyListState.layoutInfo.totalItemsCount
            val lastVisibleItemIndex =
                    lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            totalItemsCount > 0 && (lastVisibleItemIndex + 5 >= totalItemsCount)
        }
    }

    LaunchedEffect(shouldLoadNext, loading) {
        if (shouldLoadNext == true && loading == false) {
            loadNext()
        }
    }

    LazyColumn(modifier = modifier, state = lazyListState) {
        content()
        if (loading == true) {
            item {
                Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }
    }
}
