package fr.pm1g1.arboris.data.model.shared

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class PaginationParams(
    @Transient var page: Int = 1,
    @Transient var perPage: Int = 10
) {
    companion object {
        const val MAX_PER_PAGE = 100
    }

    init {
        if (page < 1) page = 1
        if (perPage > MAX_PER_PAGE) perPage = MAX_PER_PAGE
        else if (perPage < 1) perPage = 10
    }

    val skip: Int
        get() = (page - 1) * perPage
}
