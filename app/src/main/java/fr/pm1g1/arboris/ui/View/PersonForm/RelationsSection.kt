package fr.pm1g1.arboris.ui.View.PersonForm

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.data.dto.RelationDetailDto
import fr.pm1g1.arboris.data.dto.RelationSendDto
import fr.pm1g1.arboris.data.dto.RelationType
import fr.pm1g1.arboris.ui.View.viewModel.UiState
import fr.pm1g1.arboris.ui.components.BottomSheetController

enum class ViewRelationCategory {
    PARENT,
    CHILD,
    SPOUSE
}

private data class RelationCategoryConfig(
    val category: ViewRelationCategory,
    @StringRes val titleRes: Int,
    @StringRes val addContentDescriptionRes: Int,
    val people: (RelationDetailDto?) -> List<PersonCardDto>,
)

@Composable
fun RelationsSection(
    personId: Int,
    relations: RelationDetailDto?,
    availablePeopleState: UiState<List<PersonCardDto>>,
    onAddRelation: (RelationSendDto) -> Unit,
    onRemoveRelation: (RelationSendDto) -> Unit,
    bottomSheetController: BottomSheetController,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            stringResource(R.string.relations),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        val openSheet = { type: ViewRelationCategory ->
            bottomSheetController.show {
                AddRelationSheet(
                    personId = personId,
                    relationCategory = type,
                    availablePeopleState = availablePeopleState,
                    currentRelations = relations,
                    onDismiss = { bottomSheetController.hide() },
                    onConfirm = { relation ->
                        onAddRelation(relation)
                        bottomSheetController.hide()
                    },
                )
            }
        }

        val configs =
            listOf(
                RelationCategoryConfig(
                    category = ViewRelationCategory.PARENT,
                    titleRes = R.string.parents,
                    addContentDescriptionRes = R.string.add_parent_relation,
                    people = { it?.parents ?: emptyList() },
                ),
                RelationCategoryConfig(
                    category = ViewRelationCategory.CHILD,
                    titleRes = R.string.children,
                    addContentDescriptionRes = R.string.add_child_relation,
                    people = { it?.childs ?: emptyList() },
                ),
                RelationCategoryConfig(
                    category = ViewRelationCategory.SPOUSE,
                    titleRes = R.string.spouses,
                    addContentDescriptionRes = R.string.add_spouse_relation,
                    people = { it?.spouses ?: emptyList() },
                ),
            )

        configs.forEach { config ->
            RelationCategory(
                title = stringResource(config.titleRes),
                addContentDescription = stringResource(config.addContentDescriptionRes),
                people = config.people(relations),
                onAdd = { openSheet(config.category) },
                onDelete = { targetId ->
                    onRemoveRelation(buildRelationSendDto(config.category, personId, targetId))
                },
            )
        }
    }
}

@Composable
fun RelationCategory(
    title: String,
    addContentDescription: String,
    people: List<PersonCardDto>,
    onAdd: () -> Unit,
    onDelete: (Int) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = onAdd) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = addContentDescription,
                    modifier = Modifier.size(25.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
        }

        people.forEach { person ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "- ${person.firstNames} ${person.lastName}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = { onDelete(person.id) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AddRelationSheet(
    personId: Int,
    relationCategory: ViewRelationCategory,
    availablePeopleState: UiState<List<PersonCardDto>>,
    currentRelations: RelationDetailDto?,
    onDismiss: () -> Unit,
    onConfirm: (RelationSendDto) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        val typeLabel =
            when (relationCategory) {
                ViewRelationCategory.PARENT -> stringResource(R.string.parent)
                ViewRelationCategory.CHILD -> stringResource(R.string.child)
                ViewRelationCategory.SPOUSE -> stringResource(R.string.spouse)
            }

        Text(
            text = stringResource(R.string.add_a, typeLabel),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text(stringResource(R.string.search)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Box(modifier = Modifier.heightIn(min = 100.dp, max = 400.dp)) {
            when (availablePeopleState) {
                is UiState.Loading ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                is UiState.Error ->
                    Text(
                        stringResource(R.string.loading_error),
                        modifier = Modifier.align(Alignment.Center)
                    )

                is UiState.Success -> {
                    val allPeople = availablePeopleState.data

                    val existingIds = currentRelations.allRelatedIds(personId)
                    val validPeople =
                        allPeople.filter {
                            it.id in existingIds == false &&
                                    (it.firstNames.contains(searchQuery, ignoreCase = true) ||
                                            it.lastName.contains(
                                                searchQuery,
                                                ignoreCase = true
                                            ))
                        }

                    if (validPeople.isEmpty()) {
                        Text(
                            text = stringResource(R.string.no_items_yet),
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    } else {
                        LazyColumn {
                            items(validPeople) { p ->
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onConfirm(
                                                    buildRelationSendDto(
                                                        relationCategory,
                                                        personId,
                                                        p.id
                                                    )
                                                )
                                            }
                                            .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) { Text("${p.firstNames} ${p.lastName}", fontSize = 16.sp) }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.cancel))
        }
    }
}

private fun buildRelationSendDto(
    category: ViewRelationCategory,
    personId: Int,
    targetId: Int,
): RelationSendDto {
    return when (category) {
        ViewRelationCategory.PARENT ->
            RelationSendDto(
                fromPersonId = targetId,
                toPersonId = personId,
                relationType = RelationType.Child,
            )

        ViewRelationCategory.CHILD ->
            RelationSendDto(
                fromPersonId = personId,
                toPersonId = targetId,
                relationType = RelationType.Child,
            )

        ViewRelationCategory.SPOUSE ->
            RelationSendDto(
                fromPersonId = personId,
                toPersonId = targetId,
                relationType = RelationType.Spouse,
            )
    }
}

private fun RelationDetailDto?.allRelatedIds(personId: Int): Set<Int> {
    val ids = mutableSetOf(personId)
    this?.parents?.mapTo(ids) { it.id }
    this?.childs?.mapTo(ids) { it.id }
    this?.spouses?.mapTo(ids) { it.id }
    return ids
}
