package fr.pm1g1.arboris.data.dto

import kotlinx.serialization.Serializable

@Serializable
open class GenericTypeSendDto(
    val label: String
)

@Serializable
open class GenericTypeDetailDto(
    val id: Int,
    val label: String
)