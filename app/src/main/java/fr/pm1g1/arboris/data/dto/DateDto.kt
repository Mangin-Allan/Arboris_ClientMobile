package fr.pm1g1.arboris.data.dto

import kotlinx.serialization.Serializable

@Serializable
class DateSendDto(
    val dateTypeId: Int,
    val startYear: Int,
    val startMonth: Int? = null,
    val startDay: Int? = null,
    val endYear: Int? = null,
    val endMonth: Int? = null,
    val endDay: Int? = null,
    val isBefore: Boolean? = null
)

@Serializable
class DateDetailDto(
    val startYear: Int,
    val startMonth: Int? = null,
    val startDay: Int? = null,
    val endYear: Int? = null,
    val endMonth: Int? = null,
    val endDay: Int? = null,
    val isBefore: Boolean? = null,
    val dateType: DateTypeDetailDto
)

@Serializable
class DateTypeSendDto(val label: String)

@Serializable
class DateTypeDetailDto(val id: Int, val label: String)