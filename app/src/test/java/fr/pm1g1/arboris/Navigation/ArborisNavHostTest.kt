package fr.pm1g1.arboris.Navigation

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse
import fr.pm1g1.arboris.ui.screens.auth.AuthViewModel
import fr.pm1g1.arboris.ui.screens.tree.TreeViewModel
import fr.pm1g1.arboris.ui.View.viewModel.PersonViewModel
import fr.pm1g1.arboris.ui.View.viewModel.EventViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

//@RunWith(AndroidJUnit4::class)
//@Config(sdk = [33])
//class ArborisNavHostTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private val authViewModel: AuthViewModel = mockk(relaxed = true)
//    private val personViewModel: PersonViewModel = mockk(relaxed = true)
//    private val eventViewModel: EventViewModel = mockk(relaxed = true)
//    private val treeViewModel: TreeViewModel = mockk(relaxed = true)
//
////    @Test
////    fun `when user is null, should show login screen`() {
////        // Condition : Non connecté
////        every { authViewModel.currentUser } returns MutableStateFlow(null)
////
////        composeTestRule.setContent {
////            ArborisNavHost(
////                authViewModel = authViewModel,
////                personViewModel = personViewModel,
////                eventViewModel = eventViewModel,
////                treeViewModel = treeViewModel
////            )
////        }
//
//        // On vérifie qu'on est sur l'écran de login (en cherchant un texte spécifique au login par exemple)
//        // Note: Comme on n'a pas accès aux ressources facilement ici sans R.string,
//        // on se fie à la logique de redirection testée par l'état du NavHost si possible,
//        // ou on mock les sous-composables.
//    }
//
////    @Test
////    fun `when user is logged in and has no trees, should show create tree screen`() {
////        // Condition limite : Connecté mais pas de données (arbre)
////        val user = UserDetailsResponse(id = "user1", trees = emptyList())
////        every { authViewModel.currentUser } returns MutableStateFlow(user)
////
////        composeTestRule.setContent {
////            ArborisNavHost(
////                authViewModel = authViewModel,
////                personViewModel = personViewModel,
////                eventViewModel = eventViewModel,
////                treeViewModel = treeViewModel
////            )
////        }
////    }
////}
