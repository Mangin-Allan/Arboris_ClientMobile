package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

enum class RelationType {
    Parent,
    Child,
    Spouse,
}

@Serializable
data class Relation(
    // Id de la relation
    val id : Int?,

    // Id de la personne principale
    val fromPersonId : String,

    // id de la personne liée
    val toPersonId : String,

    // Type de relation ('Parent', 'Child', 'Spouse')
    val relationType : RelationType,
)
