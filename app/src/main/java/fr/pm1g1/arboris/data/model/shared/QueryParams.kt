package fr.pm1g1.arboris.data.model.shared

import kotlinx.serialization.Serializable

class QueryParams(
    var searchTerm: String? = null,
    var treeId: Int? = null,
    var filters: Map<String, String> = emptyMap(),
    var sortBy: String? = null,
    var sortDescending: Boolean = false,
    page: Int = 1,
    perPage: Int = 10
) : PaginationParams(page, perPage) {

    val hasFilters: Boolean
        get() = searchTerm.isNullOrBlank() == false || filters.isNotEmpty()

    fun toMap(): Map<String, String> = buildMap {
        searchTerm?.let { put("searchTerm", it) }
        sortBy?.let { put("sortBy", it) }
        treeId?.let { put("TreeId", treeId.toString()) }
        put("sortDescending", sortDescending.toString())
        put("page", page.toString())
        put("perPage", perPage.toString())
    }
}