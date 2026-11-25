package fr.pm1g1.arboris.data.model.shared

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QueryParamsTest {

    @Test
    fun `toMap includes all parameters correctly`() {
        // Condition normale
        val params = QueryParams(
            searchTerm = "oak",
            treeId = 42,
            sortBy = "name",
            sortDescending = true,
            page = 2,
            perPage = 20
        )

        val map = params.toMap()

        assertEquals("oak", map["searchTerm"])
        assertEquals("42", map["TreeId"])
        assertEquals("name", map["sortBy"])
        assertEquals("true", map["sortDescending"])
        assertEquals("2", map["page"])
        assertEquals("20", map["perPage"])
    }

    @Test
    fun `toMap excludes null values`() {
        // Condition limite
        val params = QueryParams(searchTerm = null, treeId = null, sortBy = null)
        val map = params.toMap()

        assertFalse(map.containsKey("searchTerm"))
        assertFalse(map.containsKey("TreeId"))
        assertFalse(map.containsKey("sortBy"))
        // Les booléens et entiers avec valeurs par défaut sont présents
        assertEquals("false", map["sortDescending"])
        assertEquals("1", map["page"])
    }

    @Test
    fun `hasFilters returns true when searchTerm is present`() {
        val params = QueryParams(searchTerm = "test")
        assertTrue(params.hasFilters)
    }

    @Test
    fun `hasFilters returns false when empty`() {
        val params = QueryParams()
        assertFalse(params.hasFilters)
    }
}
