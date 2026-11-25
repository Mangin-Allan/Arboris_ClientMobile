package fr.pm1g1.arboris.data

import androidx.compose.runtime.mutableStateListOf
import fr.pm1g1.arboris.data.model.arboris.Date
import fr.pm1g1.arboris.data.model.arboris.Event
import fr.pm1g1.arboris.data.model.arboris.GenericType
import fr.pm1g1.arboris.data.model.arboris.Location
import fr.pm1g1.arboris.data.model.arboris.Person
import fr.pm1g1.arboris.data.model.arboris.Tree
import kotlin.reflect.KClass

object Stub {

    fun <T : Any> getList(clazz: KClass<T>): List<T>? {
        return when(clazz) {
            Event::class -> events as List<T>
            Person::class -> persons as List<T>
            Tree::class -> trees as List<T>
            else -> null
        }
    }

    var persons: MutableList<Person> =
        mutableStateListOf(
            Person(
                id = 0,
                treeId = 1,
                lastName = "Dupont",
                firstNames = "Marie",
                gender = "f",
                nationality = "FR",
                biography = "Née en 1985 à Paris, mère de famille.",
                photoPath = "photos/marie.jpg",
                relations = emptyList(),
                events = emptyList()
            ),
            Person(
                id = 1,
                treeId = 1,
                lastName = "Martin",
                firstNames = "Pierre",
                gender = "m",
                nationality = "FR",
                biography = "Ingénieur chez Michelin, né en 1982.",
                photoPath = "photos/pierre.jpg",
                relations = emptyList(),
                events = emptyList()
            ),
            Person(
                id = 2,
                treeId = 1,
                lastName = "Lefèvre",
                firstNames = "Sophie",
                gender = "f",
                nationality = "FR",
                biography = "Étudiante en informatique à Clermont.",
                photoPath = "photos/sophie.jpg",
                relations = emptyList(),
                events = emptyList()
            ),
            Person(
                id = 3,
                treeId = 1,
                lastName = "Roux",
                firstNames = "Jean",
                gender = "m",
                nationality = "FR",
                biography = "Retraité, grand-père de la famille.",
                photoPath = "photos/jean.jpg",
                relations = emptyList(),
                events = emptyList()
            ),
            Person(
                id = 4,
                treeId = 1,
                lastName = "Moreau",
                firstNames = "Claire",
                gender = "f",
                nationality = "FR",
                biography = "Enseignante à l'IUT Auvergne.",
                photoPath = "photos/claire.jpg",
                relations = emptyList(),
                events = emptyList()
            )
        )


    var events: MutableList<Event> =
        mutableStateListOf(
            Event(
                id = 0,
                treeId = 0,
                eventType = GenericType(0, "Wedding"),
                date = Date(
                    type = GenericType(1, "tkt"),
                    startDay = 1,
                    startMonth = 1,
                    startYear = 2002
                ),
                location = Location(city = "Paris", country = "France"),
                persons = listOf(persons.first(), persons.last())
            ),
            Event(
                id = 1,
                treeId = 0,
                eventType = GenericType(0, "Birth"),
                date = Date(
                    type = GenericType(1, "tkt"),
                    startDay = 1,
                    startMonth = 1,
                    startYear = 1980
                ),
                location = Location(city = "Paris", country = "France"),
                persons = listOf(persons.first())
            ),
            Event(
                id = 2,
                treeId = 0,
                eventType = GenericType(0, "Birth"),
                date = Date(
                    type = GenericType(1, "tkt"),
                    startDay = 1,
                    startMonth = 1,
                    startYear = 1980
                ),
                location = Location(city = "Paris", country = "France"),
                persons = listOf(persons.first())
            ),
            Event(
                id = 3,
                treeId = 0,
                eventType = GenericType(0, "Birth"),
                date = Date(
                    type = GenericType(1, "tkt"),
                    startDay = 1,
                    startMonth = 1,
                    startYear = 1980
                ),
                location = Location(city = "Paris", country = "France"),
                persons = listOf(persons.first())
            ),
            Event(
                id = 4,
                treeId = 0,
                eventType = GenericType(0, "Birth"),
                date = Date(
                    type = GenericType(1, "tkt"),
                    startDay = 1,
                    startMonth = 1,
                    startYear = 1980
                ),
                location = Location(city = "Paris", country = "France"),
                persons = listOf(persons.first())
            ),
        )

    var trees: MutableList<Tree> =
        mutableStateListOf(
            Tree(
                id = 0,
                name = "My Beautiful Tree"
            ),
            Tree(
                id = 1,
                name = "My Awesome Tree"
            )
        )
    }
