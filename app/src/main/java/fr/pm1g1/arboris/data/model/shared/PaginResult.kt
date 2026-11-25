package fr.pm1g1.arboris.data.model.shared

import kotlinx.serialization.Serializable

@Serializable
data class PaginResult<T>(
    val items: List<T>,
    val page: Int,
    val totalPages: Int
)

fun <T, R> PaginResult<T>.map(transform: (T) -> R): PaginResult<R> {
    return PaginResult(
        items = this.items.map(transform),  // ← crée un NOUVEAU PaginResult
        page = this.page,
        totalPages = this.totalPages
    )
}