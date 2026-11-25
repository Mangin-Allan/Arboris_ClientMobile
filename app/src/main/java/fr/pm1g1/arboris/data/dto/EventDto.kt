package fr.pm1g1.arboris.data.dto

import kotlinx.serialization.Serializable

@Serializable
class EventSendDto(
        val treeId: Int,
        val typeId: Int,
        val note: String? = null,
        val date: DateSendDto,
        val location: LocationSendDto?
)

@Serializable
class EventDetailDto(
        val id: Int,
        val note: String? = null,
        val type: EventTypeDetailDto? = null,
        val date: DateDetailDto? = null,
        val location: LocationDetailDto? = null,
        val persons: List<PersonDetailDto> = emptyList(),
        val documents: List<FileDetailDto> = emptyList()
)

@Serializable class EventTypeSendDto(val label: String)

@Serializable class EventTypeDetailDto(val id: Int, val label: String)
