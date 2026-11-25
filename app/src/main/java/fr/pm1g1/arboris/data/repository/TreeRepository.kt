package fr.pm1g1.arboris.data.repository
import fr.pm1g1.arboris.core.network.ITreeApiService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.tree.TreeRequest
import fr.pm1g1.arboris.data.model.arboris.Tree

class TreeRepository(
    private val treeApiService: ITreeApiService
) {
    suspend fun insertNewTree(treeName: String): NetworkResult<Tree> {
        return try{
            val response = treeApiService.addNewTree(TreeRequest(treeName))
            if(response.isSuccessful && response.body() != null) {
                NetworkResult.Success(response.body()!!)
            }
            else {
                val errorMessage = response.message()
                NetworkResult.Error(Exception(errorMessage), response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}



