package fr.pm1g1.arboris.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import fr.pm1g1.arboris.R
import kotlinx.serialization.Serializable

sealed interface NavItem {
    @get:DrawableRes val icon: Int
    @get:StringRes val label: Int

    @Serializable data object LoginRoute
    @Serializable data object CreateTreeRoute

    @Serializable data object TreesRoute : NavItem {
        override val icon: Int get() = R.drawable.ic_home
        override val label: Int get() = R.string.trees_name
    }

    @Serializable data object EventsRoute : NavItem {
        override val icon: Int get() = R.drawable.ic_events
        override val label: Int get() = R.string.events_name
    }

    @Serializable data object PersonsRoute : NavItem {
        override val icon: Int get() = R.drawable.ic_persons
        override val label: Int get() = R.string.persons_name
    }

    @Serializable data class PersonRoute(val personId: Int = -1)
    @Serializable data class EventRoute(val eventId: Int = -1)
}

val navItems = listOf(
    NavItem.EventsRoute,
    NavItem.TreesRoute,
    NavItem.PersonsRoute,
)

