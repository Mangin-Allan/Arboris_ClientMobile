package fr.pm1g1.arboris.ui.View.EventForm

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.data.dto.DateSendDto
import fr.pm1g1.arboris.data.dto.EventSendDto
import fr.pm1g1.arboris.data.dto.GenericTypeDetailDto
import fr.pm1g1.arboris.data.dto.LocationSendDto
import fr.pm1g1.arboris.ui.GenericFormScreen
import fr.pm1g1.arboris.ui.View.viewModel.EventLabelViewModel
import fr.pm1g1.arboris.ui.View.viewModel.EventViewModel
import fr.pm1g1.arboris.ui.View.viewModel.UiState
import fr.pm1g1.arboris.ui.components.ArborisButton
import fr.pm1g1.arboris.ui.components.ArborisTextField
import fr.pm1g1.arboris.ui.components.BottomSheetController
import fr.pm1g1.arboris.ui.theme.arborisPageBackgroundColor
import fr.pm1g1.arboris.ui.theme.arborisPageTitleColor
import kotlinx.coroutines.launch

@Composable
fun EventFormScreen(
        eventId: Int,
        eventViewModel: EventViewModel,
        eventLabelViewModel: EventLabelViewModel,
        treeId: Int,
        onSave: (EventSendDto, Boolean) -> Unit,
        onUploadDocument: ((Uri, String) -> Unit)? = null,
        bottomSheetController: BottomSheetController,
        onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val currentState by eventViewModel.eventState.collectAsState()
    val labelState by eventLabelViewModel.eventLabelListState.collectAsState()
    var labels = listOf<GenericTypeDetailDto>()

    /* bricolage */
    LaunchedEffect(eventId) { eventLabelViewModel.loadEventsLabel() }
    when (labelState) {
        is UiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is UiState.Error -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                        text = (labelState as UiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                )
            }
        }
        is UiState.Success -> {
            labels = (labelState as UiState.Success<List<GenericTypeDetailDto>>).data
        }
    }

    GenericFormScreen(
            itemId = eventId,
            itemState = currentState,
            loadItem = { eventViewModel.loadEventById(it) }
    ) { event, isNew ->
        var typeLabel by remember(event) { mutableStateOf(event?.type?.label ?: "") }
        var typeId by remember(event) { mutableIntStateOf(event?.type?.id ?: 0) }
        var note by remember(event) { mutableStateOf(event?.note ?: "") }
        var city by remember(event) { mutableStateOf(event?.location?.city ?: "") }
        var country by remember(event) { mutableStateOf(event?.location?.country ?: "") }

        var day by remember(event) { mutableStateOf(event?.date?.startDay?.toString() ?: "") }
        var month by remember(event) { mutableStateOf(event?.date?.startMonth?.toString() ?: "") }
        var year by remember(event) { mutableStateOf(event?.date?.startYear?.toString() ?: "") }

        val typeError = typeLabel.isBlank()
        val isFormValid = !typeError

        var expanded by remember { mutableStateOf(false) }

        Box(
                modifier =
                        Modifier.fillMaxSize()
                                .background(arborisPageBackgroundColor())
                                .padding(16.dp)
        ) {
            Surface(modifier = Modifier.fillMaxSize(), color = Color.Transparent) {
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 14.dp)
                                        .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                            text =
                                    if (isNew) stringResource(R.string.create_event)
                                    else stringResource(R.string.update_event),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = arborisPageTitleColor()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        val list = labels
                        var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

                        val icon =
                                if (expanded) Icons.Filled.KeyboardArrowUp
                                else Icons.Filled.KeyboardArrowDown

                        Column {
                            Box() {
                                OutlinedTextField(
                                        value = typeLabel,
                                        onValueChange = { typeLabel = it },
                                        modifier =
                                                Modifier.fillMaxWidth().onGloballyPositioned {
                                                        coordinates ->
                                                    mTextFieldSize = coordinates.size.toSize()
                                                },
                                        label = { Text("label") },
                                        trailingIcon = {
                                            Icon(
                                                    icon,
                                                    "contentDescription",
                                            )
                                        },
                                        readOnly = true,
                                )

                                Box(Modifier.matchParentSize().clickable() { expanded = !expanded })
                            }

                            DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier =
                                            Modifier.width(
                                                    with(LocalDensity.current) {
                                                        mTextFieldSize.width.toDp()
                                                    }
                                            )
                            ) {
                                list.forEach { item ->
                                    DropdownMenuItem(
                                            text = { Text(text = item.label) },
                                            onClick = {
                                                typeLabel = item.label
                                                typeId = item.id
                                                expanded = false
                                            }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    ArborisTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = stringResource(R.string.note),
                            singleLine = false,
                            minLines = 2
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                            stringResource(R.string.date),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ArborisTextField(
                                value = day,
                                onValueChange = { day = it },
                                label = stringResource(R.string.day),
                                modifier = Modifier.weight(1f)
                        )
                        ArborisTextField(
                                value = month,
                                onValueChange = { month = it },
                                label = stringResource(R.string.month),
                                modifier = Modifier.weight(1f)
                        )
                        ArborisTextField(
                                value = year,
                                onValueChange = { year = it },
                                label = stringResource(R.string.year),
                                modifier = Modifier.weight(1.5f)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                            stringResource(R.string.location),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    ArborisTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = stringResource(R.string.city),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ArborisTextField(
                            value = country,
                            onValueChange = { country = it },
                            label = stringResource(R.string.country),
                    )

                    if (isNew == false) {
                        Spacer(modifier = Modifier.height(16.dp))
                        DocumentsSection(
                                eventId = eventId,
                                documents = event?.documents,
                                onUploadFile = { uri, name -> onUploadDocument?.invoke(uri, name) },
                                bottomSheetController = bottomSheetController
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    ArborisButton(
                            text =
                                    if (isNew) stringResource(R.string.create)
                                    else stringResource(R.string.save),
                            onClick = {
                                val dateComplete = day != "" && month != ""
                                val savedEvent =
                                        EventSendDto(
                                                treeId = treeId,
                                                typeId = typeId,
                                                note = note.ifBlank { null },
                                                date =
                                                        DateSendDto(
                                                                dateTypeId =
                                                                        if (dateComplete) 1 else 3,
                                                                startDay = day.toIntOrNull(),
                                                                startMonth = month.toIntOrNull(),
                                                                startYear = year.toIntOrNull() ?: 0,
                                                        ),
                                                location =
                                                        LocationSendDto(
                                                                city = city,
                                                                country = country,
                                                        )
                                        )
                                coroutineScope.launch {
                                    onSave(savedEvent, isNew)
                                    eventViewModel.loadEvents(treeId)
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
