package fr.pm1g1.arboris.data.model.shared

import org.junit.Assert.assertEquals
import org.junit.Test

class PaginResultTest {

    @Test
    fun `map transforms items correctly while preserving metadata`() {
        // Condition normale
        val source = PaginResult(
            items = listOf(1, 2, 3),
            page = 1,
            totalPages = 5
        )

        val result = source.map { it.toString() }

        assertEquals(listOf("1", "2", "3"), result.items)
        assertEquals(1, result.page)
        assertEquals(5, result.totalPages)
    }

    @Test
    fun `map works with empty list`() {
        // Condition limite
        val source = PaginResult<Int>(
            items = emptyList(),
            page = 0,
            totalPages = 0
        )

        val result = source.map { it * 2 }

        assertEquals(0, result.items.size)
        assertEquals(0, result.page)
    }
}
