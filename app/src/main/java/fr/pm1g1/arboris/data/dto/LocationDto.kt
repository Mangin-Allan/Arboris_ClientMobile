package fr.pm1g1.arboris.data.dto

import kotlinx.serialization.Serializable

@Serializable
class LocationSendDto(
    val addressLine: String? = null,
    val postalCode: String? = null,
    val city: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

@Serializable
class LocationDetailDto(
    val addressLine: String? = null,
    val postalCode: String? = null,
    val city: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)