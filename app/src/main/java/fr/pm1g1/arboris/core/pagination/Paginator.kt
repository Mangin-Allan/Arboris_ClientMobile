package fr.pm1g1.arboris.core.pagination

import fr.pm1g1.arboris.core.network.NetworkResult

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}

class DefaultPaginator<Key, Item>(
        private val initialKey: Key,
        private val onLoadUpdated: (Boolean) -> Unit,
        private val onRequest: suspend (nextKey: Key) -> NetworkResult<List<Item>>,
        private val getNextKey: suspend (List<Item>, Key) -> Key,
        private val onError: suspend (Throwable?) -> Unit,
        private val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
) : Paginator<Key, Item> {

    private var currentKey = initialKey
    private var isMakingRequest = false
    private var endOfPaginationReached = false

    override suspend fun loadNextItems() {
        if (isMakingRequest == true || endOfPaginationReached == true) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items =
                when (result) {
                    is NetworkResult.Success -> {
                        result.data
                    }
                    is NetworkResult.Error -> {
                        onError(result.exception)
                        onLoadUpdated(false)
                        return
                    }
                    is NetworkResult.Loading -> {
                        emptyList()
                    }
                }

        val newKey = getNextKey(items, currentKey)
        if (newKey == currentKey) {
            endOfPaginationReached = true
        }
        currentKey = newKey
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
        endOfPaginationReached = false
        isMakingRequest = false
    }
}
