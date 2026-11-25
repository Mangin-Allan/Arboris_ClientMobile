package fr.pm1g1.arboris.core.pagination

import fr.pm1g1.arboris.core.network.NetworkResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultPaginatorTest {


    @Test
    fun loadNextItems_calls_onSuccess_with_items_and_incremented_key() = runTest {
        val items = listOf("item1", "item2")
        var loadingState = false
        var successCalled = false

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { loadingState = it },
            onRequest = { NetworkResult.Success(items) },
            getNextKey = { _, currentKey -> currentKey + 1 },
            onError = { },
            onSuccess = { result, newKey ->
                assertEquals(items, result)
                assertEquals(1, newKey)
                successCalled = true
            }
        )

        paginator.loadNextItems()

        assertTrue(successCalled)
        assertFalse(loadingState) // loading should be false after completion
    }


    @Test
    fun loadNextItems_calls_onError_when_request_fails() = runTest {
        val exception = Exception("Network Error")
        var errorCalled = false
        var loadingState = false

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { loadingState = it },
            onRequest = { NetworkResult.Error(exception, 500) },
            getNextKey = { _, key -> key + 1 },
            onError = { thrown ->
                assertEquals(exception, thrown)
                errorCalled = true
            },
            onSuccess = { _, _ -> }
        )

        paginator.loadNextItems()

        assertTrue(errorCalled)
        assertFalse(loadingState) // loading should be reset to false after error
    }


    @Test
    fun loadNextItems_stops_requesting_when_end_of_pagination_is_reached() = runTest {
        var successCount = 0

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { },
            onRequest = { NetworkResult.Success(listOf("item")) },
            getNextKey = { _, currentKey -> currentKey }, // same key = end reached
            onError = { },
            onSuccess = { _, _ -> successCount++ }
        )

        paginator.loadNextItems()
        assertEquals(1, successCount)
    }


    @Test
    fun reset_allows_loading_again_after_end_of_pagination() = runTest {
        var successCount = 0

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { },
            onRequest = { NetworkResult.Success(listOf("item")) },
            getNextKey = { _, currentKey -> currentKey }, // same key = end reached
            onError = { },
            onSuccess = { _, _ -> successCount++ }
        )

        paginator.loadNextItems() // loads once, then end reached
        assertEquals(1, successCount)

        paginator.reset()

        paginator.loadNextItems() // should load again after reset
        assertEquals(2, successCount)
    }


    @Test
    fun loadNextItems_does_not_make_concurrent_requests() = runTest {
        var requestCount = 0

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { },
            onRequest = {
                requestCount++
                NetworkResult.Success(listOf("item"))
            },
            getNextKey = { _, key -> key + 1 },
            onError = { },
            onSuccess = { _, _ -> }
        )

        paginator.loadNextItems()
        assertEquals(1, requestCount)
    }


    @Test
    fun loadNextItems_sets_loading_to_true_then_false() = runTest {
        val loadingStates = mutableListOf<Boolean>()

        val paginator = DefaultPaginator<Int, String>(
            initialKey = 0,
            onLoadUpdated = { loadingStates.add(it) },
            onRequest = { NetworkResult.Success(listOf("item")) },
            getNextKey = { _, key -> key + 1 },
            onError = { },
            onSuccess = { _, _ -> }
        )

        paginator.loadNextItems()

        assertEquals(listOf(true, false), loadingStates)
    }
}

