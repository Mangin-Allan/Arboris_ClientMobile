package fr.pm1g1.arboris.data.repository

import fr.pm1g1.arboris.core.network.ITreeApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.tree.TreeRequest
import fr.pm1g1.arboris.data.model.arboris.Tree
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class TreeRepositoryTest {

    private lateinit var treeApiService: ITreeApiService
    private lateinit var treeRepository: TreeRepository

    @Before
    fun setUp() {
        treeApiService = mockk()
        treeRepository = TreeRepository(treeApiService)
    }

    @Test
    fun `insertNewTree success returns Success result`() = runTest {
        val treeName = "My Tree"
        val expectedTree = Tree(id = 1, name = treeName)
        val treeRequest = TreeRequest(treeName)

        coEvery { treeApiService.addNewTree(treeRequest) } returns Response.success(expectedTree)

        val result = treeRepository.insertNewTree(treeName)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedTree, (result as NetworkResult.Success).data)
        coVerify(exactly = 1) { treeApiService.addNewTree(treeRequest) }
    }

    @Test
    fun `insertNewTree failure returns Error result`() = runTest {
        val treeName = "My Tree"
        val treeRequest = TreeRequest(treeName)
        val errorMessage = "Bad Request"

        coEvery { treeApiService.addNewTree(treeRequest) } returns Response.error(400, "Error".toResponseBody(null))

        val result = treeRepository.insertNewTree(treeName)

        assertTrue(result is NetworkResult.Error)
        assertEquals(400, (result as NetworkResult.Error).code)
     
        coVerify(exactly = 1) { treeApiService.addNewTree(treeRequest) }
    }

    @Test
    fun `insertNewTree exception returns Error result`() = runTest {
        val treeName = "My Tree"
        val treeRequest = TreeRequest(treeName)
        val exception = RuntimeException("Network error")

        coEvery { treeApiService.addNewTree(treeRequest) } throws exception

        val result = treeRepository.insertNewTree(treeName)

        assertTrue(result is NetworkResult.Error)
        assertEquals(exception, (result as NetworkResult.Error).exception)
        coVerify(exactly = 1) { treeApiService.addNewTree(treeRequest) }
    }
}

