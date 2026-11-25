package fr.pm1g1.arboris.Navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.ui.View.EventForm.EventFormScreen
import fr.pm1g1.arboris.ui.View.EventListScreen
import fr.pm1g1.arboris.ui.View.PersonForm.PersonFormScreen
import fr.pm1g1.arboris.ui.View.PersonListScreen
import fr.pm1g1.arboris.ui.View.Trees.AddTree
import fr.pm1g1.arboris.ui.View.Trees.HomeMessage
import fr.pm1g1.arboris.ui.View.Trees.ListTrees
import fr.pm1g1.arboris.ui.View.viewModel.EventViewModel
import fr.pm1g1.arboris.ui.View.viewModel.PersonViewModel
import fr.pm1g1.arboris.ui.components.ArborisTopBar
import fr.pm1g1.arboris.ui.components.BottomSheetHost
import fr.pm1g1.arboris.ui.components.rememberBottomSheetController
import fr.pm1g1.arboris.ui.navigation.NavItem
import fr.pm1g1.arboris.ui.navigation.navItems
import fr.pm1g1.arboris.ui.screens.auth.AuthScreen
import fr.pm1g1.arboris.ui.screens.auth.AuthViewModel
import fr.pm1g1.arboris.ui.screens.tree.TreeViewModel
import fr.pm1g1.arboris.ui.theme.arborisPageBackgroundColor
import kotlinx.coroutines.launch

@Composable
fun ArborisNavHost(
    authViewModel: AuthViewModel = viewModel(),
    personViewModel: PersonViewModel = viewModel(),
    eventViewModel: EventViewModel = viewModel(),
    treeViewModel: TreeViewModel = viewModel()
) {
    val navController = rememberNavController()
    val bottomSheetController = rememberBottomSheetController()
    val coroutineScope = rememberCoroutineScope()

    val currentUser by authViewModel.currentUser.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val trees by treeViewModel.trees.collectAsState()
    val personsRouteQualified = NavItem.PersonsRoute::class.qualifiedName.orEmpty()
    val personRouteQualified = NavItem.PersonRoute::class.qualifiedName.orEmpty()
    val eventsRouteQualified = NavItem.EventsRoute::class.qualifiedName.orEmpty()
    val eventRouteQualified = NavItem.EventRoute::class.qualifiedName.orEmpty()
    val topBarTitleRes =
        when {
            currentRoute == personsRouteQualified ||
                    currentRoute?.contains(personRouteQualified) == true ->
                R.string.persons_name

            currentRoute == eventsRouteQualified ||
                    currentRoute?.contains(eventRouteQualified) == true -> R.string.events_name

            else -> null
        }

    var currentTree by remember(trees) { mutableStateOf(trees.firstOrNull()) }
    var selectedNavItemIndex by remember { mutableIntStateOf(2) }

    // Actualise les arbres du user au démarrage de l'application
    LaunchedEffect(Unit) { authViewModel.refreshCurrentUser() }

    LaunchedEffect(currentUser) { currentUser?.trees?.let { treeViewModel.setTrees(it) } }

    // Gere la redirection selon l'état de connexion
    LaunchedEffect(currentUser, currentRoute) {
        val loginRouteQualified = NavItem.LoginRoute::class.qualifiedName

        // Non connecté et pas sur login
        if (currentUser == null && currentRoute != loginRouteQualified) {
            navController.navigate(NavItem.LoginRoute) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }

            // Connecté et sur login
        } else if (currentUser != null && currentRoute == loginRouteQualified) {
            // Si l'utilisateur a déjà un arbre
            if (currentUser!!.trees.isNotEmpty()) {
                selectedNavItemIndex = 2
                navController.navigate(NavItem.PersonsRoute) {
                    popUpTo<NavItem.LoginRoute> { inclusive = true }
                    launchSingleTop = true
                }

                // Si l'utilisateur n'a pas d'arbre
            } else {
                navController.navigate(NavItem.CreateTreeRoute) {
                    popUpTo<NavItem.LoginRoute> { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    BottomSheetHost(controller = bottomSheetController) {
        val colorScheme = MaterialTheme.colorScheme
        Scaffold(
            containerColor = arborisPageBackgroundColor(),
            topBar = {
                val loginRouteQualified = NavItem.LoginRoute::class.qualifiedName
                if (currentUser != null &&
                    currentRoute != loginRouteQualified &&
                    topBarTitleRes != null
                ) {
                    ArborisTopBar(
                        titleRes = topBarTitleRes,
                        showActionIcon = true,
                        onActionClick = { authViewModel.logout() },
                        actionContentDescriptionRes = R.string.auth_logout_button
                    )
                }
            },
            bottomBar = {
                val loginRouteQualified = NavItem.LoginRoute::class.qualifiedName
                // Affiche la barre de navigation que si l'utilisateur est connecté et a un
                // arbre
                if (currentUser != null &&
                    currentRoute != loginRouteQualified &&
                    currentTree != null
                ) {
                    NavigationBar(
                        containerColor = arborisPageBackgroundColor(),
                        contentColor = colorScheme.onBackground,
                        tonalElevation = 1.dp
                    ) {
                        navItems.forEachIndexed { index, item ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(item.icon),
                                        contentDescription = stringResource(item.label),
                                        Modifier.size(20.dp)
                                    )
                                },
                                selected = index == selectedNavItemIndex,
                                colors =
                                    NavigationBarItemDefaults.colors(
                                        selectedIconColor = colorScheme.onPrimary,
                                        unselectedIconColor =
                                            colorScheme.onBackground.copy(
                                                alpha = 0.62f
                                            ),
                                        indicatorColor = colorScheme.primary
                                    ),
                                onClick = {
                                    if (item is NavItem.TreesRoute) {
                                        bottomSheetController.show {
                                            ListTrees(
                                                trees = trees,
                                                bottomSheetController =
                                                    bottomSheetController,
                                                onAddTree = { newTreeName ->
                                                    treeViewModel.insertNewTree(
                                                        newTreeName
                                                    ) {
                                                        authViewModel
                                                            .refreshCurrentUser()
                                                    }
                                                }
                                            ) { tree ->
                                                currentTree = tree
                                                navController.navigate(
                                                    navItems[selectedNavItemIndex]
                                                ) {
                                                    popUpTo(
                                                        navController
                                                            .graph
                                                            .startDestinationId
                                                    )
                                                    launchSingleTop = true
                                                }
                                            }
                                        }
                                    } else {
                                        selectedNavItemIndex = index
                                        navController.navigate(item) {
                                            popUpTo(navController.graph.startDestinationId)
                                            launchSingleTop = true
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination =
                    if (currentUser != null) {
                        if (currentUser!!.trees.isNotEmpty()) NavItem.PersonsRoute
                        else NavItem.CreateTreeRoute
                    } else NavItem.LoginRoute,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(bottom = 8.dp)
                    .fillMaxSize()
            ) {
                composable<NavItem.LoginRoute> {
                    AuthScreen(viewModel = authViewModel, onLoginSuccess = {})
                }

                composable<NavItem.CreateTreeRoute> {
                    AddTree(
                        onDismiss = { navController.popBackStack() },
                        onTreeCreated = { newTreeName ->
                            treeViewModel.insertNewTree(newTreeName) {
                                authViewModel.refreshCurrentUser()
                            }
                        }
                    )
                }

                composable<NavItem.PersonsRoute> {
                    currentTree?.let { tree ->
                        PersonListScreen(
                            treeId = tree.id,
                            viewModel = personViewModel,
                            onCardClicked = {
                                navController.navigate(NavItem.PersonRoute(personId = it.id))
                            },
                            onAdd = { navController.navigate(NavItem.PersonRoute()) },
                        )
                    }
                        ?: androidx.compose.foundation.layout.Box(
                            modifier = Modifier.fillMaxSize()
                        )
                }

                composable<NavItem.PersonRoute> { backStackEntry ->
                    val personRoute = backStackEntry.toRoute<NavItem.PersonRoute>()

                    currentTree?.let { tree ->
                        PersonFormScreen(
                            personId = personRoute.personId,
                            uiState = personViewModel.personState.collectAsState().value,
                            loadItem = { personViewModel.loadUserById(it) },
                            treeId = tree.id,
                            onSave = { person, isNew ->
                                coroutineScope.launch {
                                    if (isNew == true) {
                                        personViewModel.create(person)
                                    } else {
                                        personViewModel.save(personRoute.personId, person)
                                    }
                                }
                            },
                            onBack = { navController.popBackStack() },
                            onAddRelation = { relation ->
                                coroutineScope.launch {
                                    personViewModel.addRelation(relation)
                                    personViewModel.loadUserById(personRoute.personId)
                                }
                            },
                            onRemoveRelation = { relation ->
                                coroutineScope.launch {
                                    personViewModel.removeRelation(relation)
                                    personViewModel.loadUserById(personRoute.personId)
                                }
                            },
                            availablePeopleState =
                                personViewModel.personListState.collectAsState().value,
                            bottomSheetController = bottomSheetController,
                            personViewModel = personViewModel
                        )
                    }
                }

                composable<NavItem.EventsRoute> {
                    currentTree?.let { tree ->
                        EventListScreen(
                            viewModel = eventViewModel,
                            treeId = tree.id,
                            onAdd = { navController.navigate(NavItem.EventRoute()) },
                            onCardClicked = {
                                navController.navigate(NavItem.EventRoute(eventId = it.id))
                            }
                        )
                    }
                        ?: androidx.compose.foundation.layout.Box(
                            modifier = Modifier.fillMaxSize()
                        )
                }

                composable<NavItem.EventRoute> { backStackEntry ->
                    val eventRoute = backStackEntry.toRoute<NavItem.EventRoute>()
                    val eventLabelViewModel:
                            fr.pm1g1.arboris.ui.View.viewModel.EventLabelViewModel =
                        viewModel()
                    currentTree?.let { tree ->
                        EventFormScreen(
                            eventId = eventRoute.eventId,
                            eventViewModel = eventViewModel,
                            eventLabelViewModel = eventLabelViewModel,
                            treeId = tree.id,
                            onSave = { event, isNew ->
                                coroutineScope.launch {
                                    if (isNew == true) {
                                        eventViewModel.create(event)
                                    } else {
                                        eventViewModel.save(eventRoute.eventId, event)
                                    }
                                }
                            },
                            onUploadDocument = { uri, documentName ->
                                coroutineScope.launch {
                                    eventViewModel.uploadDocument(
                                        eventRoute.eventId,
                                        documentName,
                                        uri
                                    )
                                }
                            },
                            bottomSheetController = bottomSheetController,
                            onBack = { navController.popBackStack() },
                        )
                    }
                        ?: androidx.compose.foundation.layout.Box(
                            modifier = Modifier.fillMaxSize()
                        )
                }

                composable<NavItem.TreesRoute> { HomeMessage() }
            }
        }
    }
}
