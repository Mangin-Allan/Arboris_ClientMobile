package fr.pm1g1.arboris.core.pagination

import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.model.shared.PaginResult

fun <Item> createArborisPaginator(
    initialKey: Int = 1,
    onLoadUpdated: (Boolean) -> Unit,
    onRequest: suspend (nextKey: Int) -> NetworkResult<PaginResult<Item>>,
    onError: suspend (Throwable?) -> Unit,
    onSuccess: suspend (items: List<Item>, newKey: Int) -> Unit
): Paginator<Int, Item> {
    
    var currentTotalPages = 1

    return DefaultPaginator(
        initialKey = initialKey,
        onLoadUpdated = onLoadUpdated,
        onRequest = { key ->
            val result = onRequest(key)
            when (result) {
                is NetworkResult.Success -> {
                    currentTotalPages = result.data.totalPages
                    NetworkResult.Success(result.data.items)
                }
                is NetworkResult.Error -> NetworkResult.Error(result.exception, result.code)
                is NetworkResult.Loading -> NetworkResult.Loading
            }
        },
        getNextKey = { _, currentKey ->
            if (currentKey < currentTotalPages) {
                currentKey + 1
            } else {
                currentKey
            }
        },
        onError = onError,
        onSuccess = onSuccess
    )
}
