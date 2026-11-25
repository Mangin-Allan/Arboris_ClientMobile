package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class Person(
    // Identifiant de la personne
    val id: Int,

    // Identifiant de l'arbre généalogique auquel appartient la personne
    val treeId: Int? = null,

    // Nom de famille de la personne
    val lastName: String,

    // Prénoms de la personne
    val firstNames: String,

    // Genre de la personne
    val gender: String? = null,

    // Nationalité de la personne
    val nationality: String? = null,

    // Biographie de la personne
    val biography: String? = null,

    // Chemin vers la photo de la personne
    val photoPath: String? = null,

    // Relations familiales (parents, enfants, conjoints)
    val relations: List<Person>? = null,

    // Événements de vie
    val events: List<Event>? = null,
)