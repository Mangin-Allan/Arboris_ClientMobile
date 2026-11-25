package fr.pm1g1.arboris.data.repository

import android.content.Context
import fr.pm1g1.arboris.core.network.IEventService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.EventDetailDto
import fr.pm1g1.arboris.data.dto.EventSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class EventRepositoryTest {

    private lateinit var context: Context
    private lateinit var apiService: IEventService
    private lateinit var repository: EventRepository

    @Before
    fun setUp() {
        context = mockk()
        apiService = mockk()
        repository = EventRepository(context, apiService)
        
        coEvery { context.getString(any(), any(), any()) } returns "Error message"
    }

    @Test
    fun `getAll returns Success when API call is successful`() = runTest {
        val queryParams = QueryParams()
        val expectedResult = PaginResult<EventDetailDto>(emptyList(), 0, 1)
        coEvery { apiService.getAll(any()) } returns Response.success(expectedResult)

        val result = repository.getAll(queryParams)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `getEvent returns Success when API call is successful`() = runTest {
        val expectedResult = mockk<EventDetailDto>()
        coEvery { apiService.getById(1) } returns Response.success(expectedResult)

        val result = repository.getEvent(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `createEvent returns Success when API call is successful`() = runTest {
        val sendDto = mockk<EventSendDto>()
        val expectedResult = mockk<EventDetailDto>()
        coEvery { apiService.create(sendDto) } returns Response.success(expectedResult)

        val result = repository.createEvent(sendDto)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `updateEvent returns Success when API call is successful`() = runTest {
        val sendDto = mockk<EventSendDto>()
        val expectedResult = mockk<EventDetailDto>()
        coEvery { apiService.update(1, sendDto) } returns Response.success(expectedResult)

        val result = repository.updateEvent(1, sendDto)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `getAll returns Error when API call fails`() = runTest {
        val queryParams = QueryParams()
        coEvery { apiService.getAll(any()) } returns Response.error(500, mockk(relaxed = true))

        val result = repository.getAll(queryParams)

        assertTrue(result is NetworkResult.Error)
        assertEquals(500, (result as NetworkResult.Error).code)
    }
}
