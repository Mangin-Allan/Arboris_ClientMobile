package fr.pm1g1.arboris.ui.View.viewModel

import android.app.Application
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.data.dto.PersonDetailDto
import fr.pm1g1.arboris.data.dto.PersonSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams
import fr.pm1g1.arboris.data.repository.PersonRepository

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class PersonViewModel(application: Application) :
        BaseListViewModel<PersonCardDto, PersonDetailDto, PersonSendDto>(application) {

    private val personRepository = PersonRepository(application)

    val personListState = listState
    val personState = itemState

    override suspend fun fetchItems(
            queryParams: QueryParams
    ): NetworkResult<PaginResult<PersonCardDto>> {
        return personRepository.getAll(queryParams)
    }

    override suspend fun fetchItemById(id: Int): NetworkResult<PersonDetailDto> {
        return personRepository.getPerson(id)
    }

    override suspend fun save(id: Int, item: PersonSendDto): NetworkResult<PersonDetailDto> {
        return personRepository.updatePerson(id, item)
    }

    override suspend fun create(item: PersonSendDto): NetworkResult<PersonDetailDto> {
        return personRepository.createPerson(item)
    }

    suspend fun addRelation(
            relation: fr.pm1g1.arboris.data.dto.RelationSendDto
    ): NetworkResult<Unit> {
        return personRepository.addRelation(relation)
    }

    suspend fun removeRelation(
            relation: fr.pm1g1.arboris.data.dto.RelationSendDto
    ): NetworkResult<Unit> {
        return personRepository.removeRelation(relation)
    }

    fun loadUsers(treeId: Int?) = loadItems(treeId)
    fun loadUsersNext() = loadItemsNext()
    fun loadUserById(id: Int) = loadItemById(id)

    fun getGenderLabel(gender: String?, maleLabel: String, femaleLabel: String): String {
        return when (gender) {
            "M" -> maleLabel
            "F" -> femaleLabel
            else -> ""
        }
    }

    fun getGenderValue(selectedLabel: String, maleLabel: String): String {
        return if (selectedLabel == maleLabel) "M" else "F"
    }
}
