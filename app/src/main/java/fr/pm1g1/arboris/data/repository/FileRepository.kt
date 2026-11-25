package fr.pm1g1.arboris.data.repository

import android.content.Context
import android.net.Uri
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.core.network.IFileService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.FileDetailDto
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class FileRepository(
    private val context: Context,
    private val apiService: IFileService
) {
    suspend fun uploadFile(eventId: Int, name: String, uri: Uri): NetworkResult<FileDetailDto> {
        return try {
            val contentResolver = context.contentResolver
            val contentType = contentResolver.getType(uri) ?: "application/octet-stream"
            val inputStream = contentResolver.openInputStream(uri)
            val bytes = inputStream?.readBytes() ?: return NetworkResult.Error(Exception("Cannot read file"))
            inputStream.close()

            val requestFile = bytes.toRequestBody(contentType.toMediaTypeOrNull())
            val filename = name.ifEmpty { "file" }
            val filePart = MultipartBody.Part.createFormData("File", filename, requestFile)
            
            val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val eventIdBody = eventId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.upload(filePart, nameBody, eventIdBody)
            if (response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error(Exception(context.getString(R.string.fetching_error, response.code(), response.message())), response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}
