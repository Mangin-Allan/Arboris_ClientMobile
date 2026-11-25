package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class Tree(
    // Id de l'arbre
    val id : Int,

    // Nom d'affichage de l'arbres
    val name : String,
)