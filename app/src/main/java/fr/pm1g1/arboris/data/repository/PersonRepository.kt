package fr.pm1g1.arboris.data.repository

import android.content.Context
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.core.network.IPersonService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.data.dto.PersonDetailDto
import fr.pm1g1.arboris.data.dto.PersonSendDto
import fr.pm1g1.arboris.data.dto.RelationSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams
import retrofit2.create

class PersonRepository(
    private val context: Context,
    private val apiService: IPersonService? = null
) {
    private val service: IPersonService by lazy { apiService ?: RetrofitClient.instance.create() }

    suspend fun getAll(queryParams: QueryParams): NetworkResult<PaginResult<PersonCardDto>> {
        return try {
            val result = service.getAll(queryParams.toMap())
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun getPerson(id: Int): NetworkResult<PersonDetailDto> {
        return try {
            val result = service.getById(id)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun updatePerson(id: Int, person: PersonSendDto): NetworkResult<PersonDetailDto> {
        return try {
            val result = service.update(id, person)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun createPerson(person: PersonSendDto): NetworkResult<PersonDetailDto> {
        return try {
            val result = service.create(person)
            if (result.isSuccessful && result.body() != null) {
                NetworkResult.Success(result.body()!!)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun deletePerson(id: Int): NetworkResult<Unit> {
        return try {
            val result = service.delete(id)
            if (result.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    ),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun addRelation(relation: RelationSendDto): NetworkResult<Unit> {
        return try {
            val result = service.addRelation(relation.fromPersonId, relation)
            if (result.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    )
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }

    suspend fun removeRelation(relation: RelationSendDto): NetworkResult<Unit> {
        return try {
            val result =
                service.removeRelation(relation.fromPersonId, relation.toPersonId, relation)
            if (result.isSuccessful) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error(
                    Exception(
                        context.getString(
                            R.string.fetching_error,
                            result.code(),
                            result.message()
                        )
                    )
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}
