package fr.pm1g1.arboris.ui.navigation

import fr.pm1g1.arboris.R
import org.junit.Assert.assertEquals
import org.junit.Test

class NavItemTest {

    @Test
    fun navItems_list_contains_the_correct_items_in_order() {
        assertEquals(3, navItems.size)
        assertEquals(NavItem.EventsRoute, navItems[0])
        assertEquals(NavItem.TreesRoute, navItems[1])
        assertEquals(NavItem.PersonsRoute, navItems[2])
    }

    @Test
    fun TreesRoute_has_correct_icon_and_label() {
        assertEquals(R.drawable.ic_home, NavItem.TreesRoute.icon)
        assertEquals(R.string.trees_name, NavItem.TreesRoute.label)
    }

    @Test
    fun EventsRoute_has_correct_icon_and_label() {
        assertEquals(R.drawable.ic_events, NavItem.EventsRoute.icon)
        assertEquals(R.string.events_name, NavItem.EventsRoute.label)
    }

    @Test
    fun PersonsRoute_has_correct_icon_and_label() {
        assertEquals(R.drawable.ic_persons, NavItem.PersonsRoute.icon)
        assertEquals(R.string.persons_name, NavItem.PersonsRoute.label)
    }
}