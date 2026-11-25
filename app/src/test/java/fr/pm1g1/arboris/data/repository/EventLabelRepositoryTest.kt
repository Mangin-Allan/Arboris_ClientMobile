package fr.pm1g1.arboris.data.repository

import android.content.Context
import fr.pm1g1.arboris.core.network.IEventLabelService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.GenericTypeDetailDto
import fr.pm1g1.arboris.data.dto.GenericTypeSendDto
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

class EventLabelRepositoryTest {

    private lateinit var context: Context
    private lateinit var apiService: IEventLabelService
    private lateinit var repository: EventLabelRepository

    @Before
    fun setUp() {
        context = mockk()
        apiService = mockk()
        repository = EventLabelRepository(context, apiService)
        
        // Mock de l'accès aux ressources Android si nécessaire
        coEvery { context.getString(any(), any(), any()) } returns "Error message"
    }

    @Test
    fun `getAll returns Success when API call is successful`() = runTest {
        val queryParams = QueryParams()
        val expectedResult = PaginResult<GenericTypeDetailDto>(emptyList(), 0, 1)
        coEvery { apiService.getAll(any()) } returns Response.success(expectedResult)

        val result = repository.getAll(queryParams)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `getAll returns Error when API call fails`() = runTest {
        val queryParams = QueryParams()
        coEvery { apiService.getAll(any()) } returns Response.error(404, mockk(relaxed = true))

        val result = repository.getAll(queryParams)

        assertTrue(result is NetworkResult.Error)
        assertEquals(404, (result as NetworkResult.Error).code)
    }

    @Test
    fun `getEventLabel returns Success when API call is successful`() = runTest {
        val expectedResult = GenericTypeDetailDto(1, "Label")
        coEvery { apiService.getById(1) } returns Response.success(expectedResult)

        val result = repository.getEventLabel(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `createEventLabel returns Success when API call is successful`() = runTest {
        val sendDto = GenericTypeSendDto("New Label")
        val expectedResult = GenericTypeDetailDto(1, "New Label")
        coEvery { apiService.create(sendDto) } returns Response.success(expectedResult)

        val result = repository.createEventLabel(sendDto)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `updateEventLabel returns Success when API call is successful`() = runTest {
        val sendDto = GenericTypeSendDto("Updated Label")
        val expectedResult = GenericTypeDetailDto(1, "Updated Label")
        coEvery { apiService.update(1, sendDto) } returns Response.success(expectedResult)

        val result = repository.updateEventLabel(1, sendDto)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }
}
