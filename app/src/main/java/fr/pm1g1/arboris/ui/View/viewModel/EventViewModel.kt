package fr.pm1g1.arboris.ui.View.viewModel

import android.app.Application
import android.net.Uri
import fr.pm1g1.arboris.core.network.IEventService
import fr.pm1g1.arboris.core.network.IFileService
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.data.dto.EventDetailDto
import fr.pm1g1.arboris.data.dto.EventSendDto
import fr.pm1g1.arboris.data.model.shared.PaginResult
import fr.pm1g1.arboris.data.model.shared.QueryParams
import fr.pm1g1.arboris.data.repository.EventRepository
import fr.pm1g1.arboris.data.repository.FileRepository
import retrofit2.create

class EventViewModel(application: Application) :
    BaseListViewModel<EventDetailDto, EventDetailDto, EventSendDto>(application)
 {

    private val eventRepository = EventRepository(
        application,
        RetrofitClient.instance.create<IEventService>()
    )
    private val fileRepository = FileRepository(
        application,
        RetrofitClient.instance.create<IFileService>()
    )

    val eventListState = listState
    val eventState = itemState

    override suspend fun fetchItems(
        queryParams: QueryParams
    ): NetworkResult<PaginResult<EventDetailDto>> {
        return eventRepository.getAll(queryParams)
    }

    override suspend fun fetchItemById(id: Int): NetworkResult<EventDetailDto> {
        return eventRepository.getEvent(id)
    }

    override suspend fun save(
        id: Int,
        item: EventSendDto
    ): NetworkResult<EventDetailDto> {
        return eventRepository.updateEvent(id, item)
    }

    override suspend fun create(item: EventSendDto): NetworkResult<EventDetailDto> {
        return eventRepository.createEvent(item)
    }

    suspend fun uploadDocument(eventId: Int, name: String, uri: Uri?) {
        if (uri == null) return
        val result = fileRepository.uploadFile(eventId, name, uri)
        if (result is NetworkResult.Success) {
            loadEventById(eventId)
        } else if (result is NetworkResult.Error) {
            // TODO : Gestion d'erreur
        }
    }

    fun loadEvents(treeId: Int?) = loadItems(treeId)
    fun loadEventsNext() = loadItemsNext()
    fun loadEventById(id: Int) = loadItemById(id)
}
