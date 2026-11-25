package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class SimplifiedPerson(
    // Id de la personne
    val id : Int,

    // Nom de la personne
    val lastName : String,

    // Prenoms de la personne
    val firstNames : String,

    // Url vers la photo de la personne
    val photoPath : String?,
)
