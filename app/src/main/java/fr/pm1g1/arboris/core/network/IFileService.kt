package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.dto.FileDetailDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface IFileService {
    @Multipart
    @POST("api/v1/Files/upload")
    suspend fun upload(
            @Part file: MultipartBody.Part,
            @Part("Name") name: RequestBody,
            @Part("EventId") eventId: RequestBody,
            @Part("Url") url: RequestBody? = null
    ): Response<FileDetailDto>
}
