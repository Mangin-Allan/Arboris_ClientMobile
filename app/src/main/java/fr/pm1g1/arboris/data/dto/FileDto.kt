package fr.pm1g1.arboris.data.dto

import kotlinx.serialization.Serializable

@Serializable
class FileSendDto(
    val file: String? = null,
    val name: String? = null
)

@Serializable
class FileDetailDto(
    val id: Int,
    val name: String? = null,
    val url: String
)