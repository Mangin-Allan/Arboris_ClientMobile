package fr.pm1g1.arboris.core.pagination

import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.model.shared.PaginResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PaginatorFactoryTest {

    @Test
    fun `createArborisPaginator success maps PaginResult to list of items`() = runTest {
        val items = listOf("item1", "item2")
        val paginResult = PaginResult(items = items, page = 1, totalPages = 2)
        
        var receivedItems: List<String> = emptyList()
        var nextKey: Int = -1

        val paginator = createArborisPaginator<String>(
            initialKey = 1,
            onLoadUpdated = {},
            onRequest = { key -> NetworkResult.Success(paginResult) },
            onError = {},
            onSuccess = { resultItems, newKey ->
                receivedItems = resultItems
                nextKey = newKey
            }
        )

        paginator.loadNextItems()

        assertEquals(items, receivedItems)
        assertEquals(2, nextKey) // Next key should be 2 if current < totalPages
    }

    @Test
    fun `createArborisPaginator error returns Error result`() = runTest {
        val exception = Exception("API Error")
        var errorThrown: Throwable? = null

        val paginator = createArborisPaginator<String>(
            initialKey = 1,
            onLoadUpdated = {},
            onRequest = { key -> NetworkResult.Error(exception, 500) },
            onError = { errorThrown = it },
            onSuccess = { _, _ -> }
        )

        paginator.loadNextItems()

        assertEquals(exception, errorThrown)
    }

    @Test
    fun `createArborisPaginator reaches end of pagination`() = runTest {
        val items = listOf("item1")
        val paginResult = PaginResult(items = items, page = 1, totalPages = 1)
        
        var nextKey: Int = -1

        val paginator = createArborisPaginator<String>(
            initialKey = 1,
            onLoadUpdated = {},
            onRequest = { key -> NetworkResult.Success(paginResult) },
            onError = {},
            onSuccess = { _, newKey ->
                nextKey = newKey
            }
        )

        paginator.loadNextItems()

        assertEquals(1, nextKey) // Key should not increment if current == totalPages
    }
}
