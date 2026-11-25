package fr.pm1g1.arboris.core.network

import fr.pm1g1.arboris.data.dto.tree.TreeRequest
import fr.pm1g1.arboris.data.model.arboris.Tree
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ITreeApiService {

    @POST("api/v1/Trees")
    suspend fun addNewTree(@Body request: TreeRequest): Response<Tree>
}