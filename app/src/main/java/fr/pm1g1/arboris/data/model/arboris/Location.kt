package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    // Adresse du lieu
    val addressLine : String? = null,

    // Code postal du lieu
    val postalCode : String? = null,

    // Ville du lieu
    val city : String? = null,

    // Pays du lieu
    val country : String? = null,

    // Latitude du lieu
    val latitude : Float? = null,

    // Longitude du lieu
    val longitude : Float? = null,
)
