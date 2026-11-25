package fr.pm1g1.arboris.ui.View

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.unit.dp
import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.ui.GenericListScreen
import fr.pm1g1.arboris.ui.View.viewModel.PersonViewModel
import fr.pm1g1.arboris.ui.View.viewModel.UiState
import fr.pm1g1.arboris.ui.components.PersonCard
import fr.pm1g1.arboris.ui.theme.arborisCardContainerColor
import fr.pm1g1.arboris.ui.theme.arborisCardContentColor

@Composable
fun PersonListScreen(
    viewModel: PersonViewModel,
    onAdd: () -> Unit,
    onCardClicked: (PersonCardDto) -> Unit,
    treeId: Int,
) {
    val cardContainerColor = arborisCardContainerColor()
    val cardContentColor = arborisCardContentColor()
    val state by viewModel.personListState.collectAsState()
    LaunchedEffect(treeId) {
        if (state is UiState.Loading || (state is UiState.Success && (state as UiState.Success).data.isEmpty())) {
            viewModel.loadUsers(treeId)
        }
    }
    GenericListScreen<PersonCardDto>(
        title = null,
        state = state,
        onAdd = onAdd,
        onCardClicked = onCardClicked,
        itemContent = { person, function ->
            PersonCard(person, function, cardContainerColor, cardContentColor)
        },
        loadNext = { viewModel.loadUsersNext() },
        gridHorizontalSpacing = 20.dp,
        gridVerticalSpacing = 20.dp,
        gridContentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        fabContainerColor = MaterialTheme.colorScheme.primary,
        fabContentColor = MaterialTheme.colorScheme.onPrimary
    )
}
