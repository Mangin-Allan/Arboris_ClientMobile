package fr.pm1g1.arboris.data.dto

import fr.pm1g1.arboris.data.model.arboris.Person
import kotlinx.serialization.Serializable

@Serializable
class PersonSendDto(
    val treeId: Int,
    val lastName: String,
    val firstNames: String,
    val gender: Char?,
    val nationality: String? = null,
    val biography: String? = null
)

@Serializable
open class PersonCardDto(
    val id: Int,
    val lastName: String,
    val firstNames: String,
    val photoPath: String? = null,
    val gender: String? = null,
    val nationality: String? = null
) {
    fun toModel(): Person {
        return Person (
            id = this.id,
            lastName = this.lastName,
            firstNames = this.firstNames,
            photoPath = this.photoPath,
            gender = this.gender,
            nationality = this.nationality
        )
    }
}

@Serializable
open class PersonCardWithRelationsDto(
    val id: Int,
    val lastName: String,
    val firstNames: String,
    val photoPath: String? = null,
    val relations: RelationDetailDto? = null
)
@Serializable
class PersonDetailDto(
    val id: Int,
    val lastName: String,
    val firstNames: String,
    val photoPath: String? = null,
    val relations: RelationDetailDto? = null,
    val gender: Char?,
    val nationality: String? = null,
    val biography: String? = null,
    val events: List<EventDetailDto>
) {
    fun toModel(): Person {
        return Person(
            id = this.id,
            lastName = this.lastName,
            firstNames = this.firstNames,
            gender = this.gender.toString(),
            nationality = this.nationality,
            biography = this.biography,
            photoPath = this.photoPath,
        )
    }
}

