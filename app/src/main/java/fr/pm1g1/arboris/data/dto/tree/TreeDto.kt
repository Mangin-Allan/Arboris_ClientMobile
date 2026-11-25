package fr.pm1g1.arboris.data.dto.tree

import kotlinx.serialization.Serializable

@Serializable
data class TreeRequest(
    val name: String,
)