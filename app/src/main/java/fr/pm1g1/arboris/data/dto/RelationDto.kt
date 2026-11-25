package fr.pm1g1.arboris.data.dto

import kotlinx.serialization.Serializable

@Serializable
enum class RelationType {
    Child,
    Spouse
}

@Serializable
class RelationSendDto(
    val fromPersonId: Int,
    val toPersonId: Int,
    val relationType: RelationType
)

@Serializable
class RelationDetailDto(
    val parents: List<PersonCardDto> = emptyList(),
    val childs: List<PersonCardDto> = emptyList(),
    val spouses: List<PersonCardDto> = emptyList()
)