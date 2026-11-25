package fr.pm1g1.arboris.core.storage

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse
import fr.pm1g1.arboris.data.model.arboris.Tree
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class UserManagerTest {

    private lateinit var userManager: UserManager

    private val fakeTree1 = Tree(id = 1, name = "Famille Dupont")
    private val fakeTree2 = Tree(id = 2, name = "Famille Martin")
    private val fakeUser = UserDetailsResponse(
        id = "user-42",
        trees = listOf(fakeTree1, fakeTree2)
    )

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Application>()
        userManager = UserManager(context)
        userManager.clearUser()
    }


    @Test
    fun saveUser_stores_userId_correctly() {
        userManager.saveUser(fakeUser)
        assertEquals("user-42", userManager.getUserId())
    }

    @Test
    fun getUserId_returns_null_when_no_user_saved() {
        assertNull(userManager.getUserId())
    }


    @Test
    fun saveUser_stores_trees_correctly() {
        userManager.saveUser(fakeUser)
        val trees = userManager.getTrees()
        assertEquals(2, trees.size)
        assertEquals(fakeTree1, trees[0])
        assertEquals(fakeTree2, trees[1])
    }

    @Test
    fun getTrees_returns_empty_list_when_no_user_saved() {
        assertTrue(userManager.getTrees().isEmpty())
    }

    @Test
    fun getTrees_returns_empty_list_after_clearUser() {
        userManager.saveUser(fakeUser)
        userManager.clearUser()
        assertTrue(userManager.getTrees().isEmpty())
    }

    @Test
    fun getUserDetails_returns_UserDetailsResponse_when_user_is_saved() {
        userManager.saveUser(fakeUser)
        val result = userManager.getUserDetails()
        assertNotNull(result)
        assertEquals("user-42", result!!.id)
        assertEquals(2, result.trees.size)
        assertEquals("Famille Dupont", result.trees[0].name)
        assertEquals("Famille Martin", result.trees[1].name)
    }

    @Test
    fun getUserDetails_returns_null_when_no_user_saved() {
        assertNull(userManager.getUserDetails())
    }

    @Test
    fun getUserDetails_returns_null_after_clearUser() {
        userManager.saveUser(fakeUser)
        userManager.clearUser()
        assertNull(userManager.getUserDetails())
    }



    @Test
    fun hasUser_returns_false_when_no_user_saved() {
        assertFalse(userManager.hasUser())
    }

    @Test
    fun hasUser_returns_true_when_user_is_saved() {
        userManager.saveUser(fakeUser)
        assertTrue(userManager.hasUser())
    }

    @Test
    fun hasUser_returns_false_after_clearUser() {
        userManager.saveUser(fakeUser)
        userManager.clearUser()
        assertFalse(userManager.hasUser())
    }


    @Test
    fun clearUser_removes_userId() {
        userManager.saveUser(fakeUser)
        userManager.clearUser()
        assertNull(userManager.getUserId())
    }

    @Test
    fun saveUser_overwrites_previously_saved_user() {
        val newUser = UserDetailsResponse(
            id = "user-99",
            trees = listOf(Tree(id = 3, name = "Famille Bernard"))
        )
        userManager.saveUser(fakeUser)
        userManager.saveUser(newUser)
        assertEquals("user-99", userManager.getUserId())
        assertEquals(1, userManager.getTrees().size)
        assertEquals("Famille Bernard", userManager.getTrees()[0].name)
    }

    // ─── user with empty trees ────────────────────────────────────────────────

    @Test
    fun saveUser_with_empty_trees_list_stores_empty_list() {
        val userNoTrees = UserDetailsResponse(id = "user-0", trees = emptyList())
        userManager.saveUser(userNoTrees)
        assertTrue(userManager.getTrees().isEmpty())
        assertEquals("user-0", userManager.getUserId())
    }
}
