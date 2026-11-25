package fr.pm1g1.arboris.ui.View.viewModel

import android.app.Application
import fr.pm1g1.arboris.core.network.IEventLabelService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.data.dto.GenericTypeDetailDto
import fr.pm1g1.arboris.data.dto.GenericTypeSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams
import fr.pm1g1.arboris.data.repository.EventLabelRepository
import retrofit2.create

class EventLabelViewModel(application: Application) :
    BaseListViewModel<GenericTypeDetailDto, GenericTypeDetailDto, GenericTypeSendDto>(application){

    private val eventLabelRepository = EventLabelRepository(
        application,
        RetrofitClient.instance.create<IEventLabelService>()
    )

    val eventLabelListState = listState
    val eventLabelState = itemState

    override suspend fun fetchItems(
        queryParams: QueryParams
    ): NetworkResult<PaginResult<GenericTypeDetailDto>> {
        return eventLabelRepository.getAll(queryParams)
    }

    override suspend fun fetchItemById(id: Int): NetworkResult<GenericTypeDetailDto> {
        return eventLabelRepository.getEventLabel(id)
    }

    override suspend fun save(
        id: Int,
        item: GenericTypeSendDto
    ): NetworkResult<GenericTypeDetailDto> {
        return eventLabelRepository.updateEventLabel(id, item)
    }

    override suspend fun create(item: GenericTypeSendDto): NetworkResult<GenericTypeDetailDto> {
        return eventLabelRepository.createEventLabel(item)
    }

    fun loadEventsLabel() = loadItems(-1)
    fun loadEventsLabelNext() = loadItemsNext()
    fun loadEventLabelById(id: Int) = loadItemById(id)
}