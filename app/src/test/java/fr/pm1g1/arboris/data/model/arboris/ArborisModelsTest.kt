package fr.pm1g1.arboris.data.model.arboris

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ArborisModelsTest {

    @Test
    fun `Person model instantiation and properties`() {
        val person = Person(id = 1, lastName = "Doe", firstNames = "John", gender = "M")
        assertEquals(1, person.id)
        assertEquals("Doe", person.lastName)
        assertEquals("John", person.firstNames)
        assertEquals("M", person.gender)
        assertNull(person.biography)
    }

    @Test
    fun `Date model instantiation and properties`() {
        val type = GenericType(1, "Birth")
        val date = Date(type = type, startDay = 1, startMonth = 1, startYear = 2000)
        assertEquals(type, date.type)
        assertEquals(1, date.startDay)
        assertEquals(2000, date.startYear)
        assertNull(date.isBefore)
    }

    @Test
    fun `Event model instantiation and properties`() {
        val type = GenericType(1, "Marriage")
        val date = Date(type = type, startDay = 1, startMonth = 1, startYear = 2020)
        val location = Location(city = "Paris", country = "France")
        val event = Event(id = 10, treeId = 1, eventType = type, date = date, location = location, persons = emptyList())
        
        assertEquals(10, event.id)
        assertEquals("Paris", event.location.city)
        assertEquals(0, event.persons.size)
    }

    @Test
    fun `Relation model instantiation and properties`() {
        val relation = Relation(id = 1, fromPersonId = "1", toPersonId = "2", relationType = RelationType.Parent)
        assertEquals(RelationType.Parent, relation.relationType)
        assertEquals("1", relation.fromPersonId)
    }

    @Test
    fun `Location model instantiation and properties`() {
        val location = Location(addressLine = "123 Main St", latitude = 48.8566f, longitude = 2.3522f)
        assertEquals("123 Main St", location.addressLine)
        assertEquals(48.8566f, location.latitude!!, 0.0001f)
    }

    @Test
    fun `AppUser model instantiation and properties`() {
        val user = AppUser(id = "user1", email = "test@test.com", password = "password", tokens = null, trees = mutableListOf())
        assertEquals("user1", user.id)
        assertEquals("test@test.com", user.email)
        assertEquals(0, user.trees.size)
    }

    @Test
    fun `SimplifiedPerson model instantiation and properties`() {
        val simplified = SimplifiedPerson(id = 1, lastName = "Smith", firstNames = "Jane", photoPath = "/path/to/photo")
        assertEquals(1, simplified.id)
        assertEquals("Smith", simplified.lastName)
        assertEquals("/path/to/photo", simplified.photoPath)
    }

    @Test
    fun `GenericType model instantiation and properties`() {
        val genericType = GenericType(id = 5, label = "Baptism")
        assertEquals(5, genericType.id)
        assertEquals("Baptism", genericType.label)
    }
}
