package fr.pm1g1.arboris.data.model.arboris

import kotlinx.serialization.Serializable

@Serializable
data class Date(
    // Type de date
    val type : GenericType,

    // Si DateType est APPROXIMATE, est-ce avant ou après la date indiquée
    val isBefore : Boolean? = null,

    // Date de début / Date unique
    val startDay : Int?,
    val startMonth : Int?,
    val startYear : Int?,

    // Date de fin (pour période)
    val endDay : Int? = null,
    val endMonth : Int? = null,
    val endYear : Int? = null,
)
