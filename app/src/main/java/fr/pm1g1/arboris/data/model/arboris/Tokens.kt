package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class Tokens(
    // Token d'accès
    val accessToken : String?,

    // Token de rafraîchissement
    val refreshToken : String?,

    // Timestamp d'expiration du token
    val expireIn : Long?
)
