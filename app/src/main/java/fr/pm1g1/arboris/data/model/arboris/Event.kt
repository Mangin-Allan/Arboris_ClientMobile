package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class Event(
    // Identifiant de l'événement
    var id : Int,

    // Identifiant de l'arbre auquel l'événement est lié
    val treeId : Int,

    // Type de l'événement
    val eventType : GenericType,

    // Note associée à l'événement
    val note : String? = null,

    // Date de l'événement
    val date : Date,

    // Lieu de l'événement
    val location : Location,

    // Personnes associées à l'événement
    val persons : List<Person>,
)
