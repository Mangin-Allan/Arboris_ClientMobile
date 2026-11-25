package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class AppUser(
    // Id de l'utilisateur
    val id : String?,

    // Email de l'utilisateur
    val email : String?,

    // Mot de passe de l'utilisateur
    val password : String?,

    // Les tokens d'authentification pour l'API
    val tokens : Tokens?,

    // Les arbres de l'utilisateur
    val trees : MutableList<Tree>,
)