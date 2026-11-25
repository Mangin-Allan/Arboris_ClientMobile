package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class GenericType(
    // Id du type
    val id : Int?,

    // Le texte affiché pour ce type
    val label : String?,
)
