package fr.pm1g1.arboris.ui.View.PersonForm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.data.dto.PersonCardDto
import fr.pm1g1.arboris.data.dto.PersonDetailDto
import fr.pm1g1.arboris.data.dto.PersonSendDto
import fr.pm1g1.arboris.data.dto.RelationSendDto
import fr.pm1g1.arboris.ui.GenericFormScreen
import fr.pm1g1.arboris.ui.View.viewModel.PersonViewModel
import fr.pm1g1.arboris.ui.View.viewModel.UiState
import fr.pm1g1.arboris.ui.components.ArborisButton
import fr.pm1g1.arboris.ui.components.ArborisRadioButton
import fr.pm1g1.arboris.ui.components.ArborisTextField
import fr.pm1g1.arboris.ui.components.BottomSheetController
import fr.pm1g1.arboris.ui.theme.arborisPageBackgroundColor
import fr.pm1g1.arboris.ui.theme.arborisPageTitleColor
import kotlinx.coroutines.launch

@Composable
fun PersonFormScreen(
    personId: Int,
    uiState: UiState<PersonDetailDto>,
    treeId: Int,
    loadItem: (Int) -> Unit,
    onSave: (PersonSendDto, Boolean) -> Unit,
    onBack: () -> Unit,
    onAddRelation: (RelationSendDto) -> Unit = {},
    onRemoveRelation: (RelationSendDto) -> Unit = {},
    availablePeopleState: UiState<List<PersonCardDto>> = UiState.Loading,
    bottomSheetController: BottomSheetController,
    personViewModel: PersonViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    GenericFormScreen(
        itemId = personId,
        itemState = uiState,
        loadItem = loadItem,
    ) { person, isNew ->
        var lastName by remember(person) { mutableStateOf(person?.lastName ?: "") }
        var firstNames by remember(person) { mutableStateOf(person?.firstNames ?: "") }
        var nationality by remember(person) { mutableStateOf(person?.nationality ?: "") }
        var biography by remember(person) { mutableStateOf(person?.biography ?: "") }

        var lastChanged by remember { mutableStateOf("") }
        val lastNameError = lastName.trim().isBlank()
        val firstNamesError = firstNames.trim().isBlank()

        val isFormValid = !lastNameError || !firstNamesError

        val genderOptions =
            listOf(
                stringResource(R.string.male),
                stringResource(R.string.female),
                stringResource(R.string.gender_unknown),
            )
        val initialGenderIndex =
            when (person?.gender?.uppercase()) {
                "F" -> 1
                "M" -> 0
                "U" -> 2
                else -> 2
            }
        var genderIndex by remember(person) { mutableIntStateOf(initialGenderIndex) }

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(arborisPageBackgroundColor())
                    .padding(16.dp)
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text =
                            if (isNew) stringResource(R.string.createPersonString)
                            else stringResource(R.string.modifyPersonString),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = arborisPageTitleColor()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ArborisTextField(
                        value = lastName,
                        onValueChange = {
                            lastName = it
                            lastChanged = "lastName"
                        },
                        label = stringResource(R.string.last_name),
                        isError = lastNameError && firstNamesError && lastChanged == "lastName",
                        errorMessage =
                            if (lastNameError) stringResource(R.string.lastNameMandatory)
                            else null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ArborisTextField(
                        value = firstNames,
                        onValueChange = {
                            firstNames = it
                            lastChanged = "firstName"
                        },
                        label = stringResource(R.string.first_names),
                        isError =
                            firstNamesError && lastNameError && lastChanged == "firstName",
                        errorMessage =
                            if (firstNamesError) stringResource(R.string.firstNameMandatory)
                            else null
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(R.string.gender),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ArborisRadioButton(
                        radioOptions = genderOptions,
                        selectedOption = genderOptions.getOrNull(genderIndex)
                            ?: genderOptions[0],
                        onOptionSelected = { option ->
                            val index = genderOptions.indexOf(option)
                            genderIndex = if (index >= 0) index else 0
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    ArborisTextField(
                        value = nationality,
                        onValueChange = { nationality = it },
                        label = stringResource(R.string.nationality)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ArborisTextField(
                        value = biography,
                        onValueChange = { biography = it },
                        label = stringResource(R.string.biography),
                        singleLine = false,
                        minLines = 2
                    )

                    if (!isNew) {
                        Spacer(modifier = Modifier.height(16.dp))
                        RelationsSection(
                            personId = personId,
                            relations = person?.relations,
                            availablePeopleState = availablePeopleState,
                            onAddRelation = onAddRelation,
                            onRemoveRelation = onRemoveRelation,
                            bottomSheetController = bottomSheetController,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    ArborisButton(
                        text =
                            if (isNew) stringResource(R.string.create)
                            else stringResource(R.string.save),
                        onClick = {
                            val savedPerson =
                                PersonSendDto(
                                    treeId = treeId,
                                    lastName = lastName.trim(),
                                    firstNames = firstNames.trim(),
                                    gender = when (genderIndex) {
                                        1 -> 'F'
                                        2 -> 'U'
                                        else -> 'M'
                                    },
                                    nationality = nationality.trim().ifBlank { null },
                                    biography = biography.trim().ifBlank { null },
                                )
                            coroutineScope.launch {
                                onSave(savedPerson, isNew)
                                personViewModel.loadUsers(treeId)
                                onBack()
                            }
                        },
                        enabled = isFormValid
                    )
                    ArborisButton(text = stringResource(R.string.cancel), onClick = onBack)
                }
            }
        }
    }
}
