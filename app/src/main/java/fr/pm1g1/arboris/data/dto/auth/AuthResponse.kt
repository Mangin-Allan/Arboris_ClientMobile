package fr.pm1g1.arboris.data.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String,
    val expiresIn: Long,
    val refreshToken: String
)
