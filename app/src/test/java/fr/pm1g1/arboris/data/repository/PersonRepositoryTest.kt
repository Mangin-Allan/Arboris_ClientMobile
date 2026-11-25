package fr.pm1g1.arboris.data.repository

import android.content.Context
import fr.pm1g1.arboris.core.network.IPersonService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.data.dto.PersonDetailDto
import fr.pm1g1.arboris.data.dto.PersonSendDto
import fr.pm1g1.arboris.data.dto.RelationSendDto
import fr.pm1g1.arboris.data.dto.RelationType
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

class PersonRepositoryTest {

    private lateinit var context: Context
    private lateinit var apiService: IPersonService
    private lateinit var repository: PersonRepository

    @Before
    fun setUp() {
        context = mockk()
        apiService = mockk()
        repository = PersonRepository(context, apiService)
        
        coEvery { context.getString(any(), any(), any()) } returns "Error message"
    }

    @Test
    fun `getAll returns Success when API call is successful`() = runTest {
        val queryParams = QueryParams()
        val expectedResult = PaginResult<PersonCardDto>(emptyList(), 0, 1)
        coEvery { apiService.getAll(any()) } returns Response.success(expectedResult)

        val result = repository.getAll(queryParams)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `getPerson returns Success when API call is successful`() = runTest {
        val expectedResult = mockk<PersonDetailDto>()
        coEvery { apiService.getById(1) } returns Response.success(expectedResult)

        val result = repository.getPerson(1)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `createPerson returns Success when API call is successful`() = runTest {
        val sendDto = mockk<PersonSendDto>()
        val expectedResult = mockk<PersonDetailDto>()
        coEvery { apiService.create(sendDto) } returns Response.success(expectedResult)

        val result = repository.createPerson(sendDto)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedResult, (result as NetworkResult.Success).data)
    }

    @Test
    fun `deletePerson returns Success when API call is successful`() = runTest {
        coEvery { apiService.delete(1) } returns Response.success(Unit)

        val result = repository.deletePerson(1)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `addRelation returns Success when API call is successful`() = runTest {
        val relation = RelationSendDto(1, 2, RelationType.Child)
        coEvery { apiService.addRelation(1, relation) } returns Response.success(Unit)

        val result = repository.addRelation(relation)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `removeRelation returns Success when API call is successful`() = runTest {
        val relation = RelationSendDto(1, 2, RelationType.Spouse)
        coEvery { apiService.removeRelation(1, 2, relation) } returns Response.success(Unit)

        val result = repository.removeRelation(relation)

        assertTrue(result is NetworkResult.Success)
    }

    @Test
    fun `getAll returns Error when API call fails`() = runTest {
        val queryParams = QueryParams()
        coEvery { apiService.getAll(any()) } returns Response.error(400, mockk(relaxed = true))

        val result = repository.getAll(queryParams)

        assertTrue(result is NetworkResult.Error)
    }
}
