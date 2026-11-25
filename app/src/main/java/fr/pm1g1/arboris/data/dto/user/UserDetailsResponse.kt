package fr.pm1g1.arboris.data.dto.user

import fr.pm1g1.arboris.data.model.arboris.Tree
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailsResponse(
    val id: String,
    val trees: List<Tree>
)
