package fr.pm1g1.arboris.data.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import fr.pm1g1.arboris.core.network.IFileService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.FileDetailDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Response
import java.io.ByteArrayInputStream

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class FileRepositoryTest {

    private lateinit var context: Context
    private lateinit var apiService: IFileService
    private lateinit var repository: FileRepository
    private lateinit var contentResolver: ContentResolver

    @Before
    fun setUp() {
        // Utilisation de relaxed = true pour éviter les erreurs sur getType() ou d'autres appels secondaires
        context = mockk(relaxed = true)
        apiService = mockk()
        contentResolver = mockk(relaxed = true)
        
        every { context.contentResolver } returns contentResolver
        repository = FileRepository(context, apiService)
    }

    @Test
    fun `uploadFile returns Success when API call is successful`() = runTest {
        val uri = mockk<Uri>()
        val fileContent = "test content".toByteArray()
        val expectedDto = FileDetailDto(id = 1, name = "file.txt", url = "url")

        every { contentResolver.getType(uri) } returns "text/plain"
        every { contentResolver.openInputStream(uri) } returns ByteArrayInputStream(fileContent)
        
        coEvery { apiService.upload(any(), any(), any()) } returns Response.success(expectedDto)

        val result = repository.uploadFile(1, "test.txt", uri)

        assertTrue(result is NetworkResult.Success)
        assertEquals(expectedDto, (result as NetworkResult.Success).data)
    }

    @Test
    fun `uploadFile returns Error when file cannot be read`() = runTest {
        val uri = mockk<Uri>()
        // On simule explicitement un échec d'ouverture de flux
        every { contentResolver.openInputStream(uri) } returns null
        // On s'assure que getType ne bloque pas
        every { contentResolver.getType(uri) } returns "text/plain"

        val result = repository.uploadFile(1, "test.txt", uri)

        assertTrue(result is NetworkResult.Error)
        assertEquals("Cannot read file", (result as NetworkResult.Error).exception.message)
    }

    @Test
    fun `uploadFile returns Error when API returns failure`() = runTest {
        val uri = mockk<Uri>()
        val fileContent = "test content".toByteArray()
        
        every { contentResolver.getType(uri) } returns "text/plain"
        every { contentResolver.openInputStream(uri) } returns ByteArrayInputStream(fileContent)
        every { context.getString(any(), any(), any()) } returns "API Error 500"
        
        coEvery { apiService.upload(any(), any(), any()) } returns Response.error(500, "Internal Server Error".toResponseBody())

        val result = repository.uploadFile(1, "test.txt", uri)

        assertTrue(result is NetworkResult.Error)
        assertEquals(500, (result as NetworkResult.Error).code)
    }
}
