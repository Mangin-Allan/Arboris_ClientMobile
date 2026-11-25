package fr.pm1g1.arboris.ui.View.EventForm

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import fr.pm1g1.arboris.BuildConfig
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.data.dto.FileDetailDto
import fr.pm1g1.arboris.ui.components.BottomSheetController
import fr.pm1g1.arboris.ui.components.TextInputSheetContent
import fr.pm1g1.arboris.ui.theme.arborisPageTitleColor
import java.io.File

@Composable
fun DocumentsSection(
    eventId: Int,
    documents: List<FileDetailDto>?,
    onUploadFile: (Uri, String) -> Unit,
    bottomSheetController: BottomSheetController
) {
    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }

    var photoUri by remember { mutableStateOf<Uri>(Uri.EMPTY) }

    fun createTempUri(): Uri {
        val tempFile = File.createTempFile("photo_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
    }

    val filePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedUri = it
                bottomSheetController.show {
                    TextInputSheetContent(
                        title = stringResource(id = R.string.document_name),
                        label = stringResource(id = R.string.enter_document_name),
                        confirmText = stringResource(id = R.string.send),
                        onConfirm = { documentName ->
                            if (selectedUri != null) {
                                onUploadFile(selectedUri!!, documentName)
                                bottomSheetController.hide()
                                selectedUri = null
                            }
                        },
                        onCancel = {
                            bottomSheetController.hide()
                            selectedUri = null
                        }
                    )
                }
            }
        }

    val cameraStarter =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                bottomSheetController.show {
                    TextInputSheetContent(
                        title = stringResource(id = R.string.document_name),
                        label = stringResource(id = R.string.enter_document_name),
                        confirmText = stringResource(id = R.string.send),
                        onConfirm = { documentName ->
                            onUploadFile(photoUri, "$documentName.jpg")
                            bottomSheetController.hide()
                            photoUri = Uri.EMPTY
                        },
                        onCancel = {
                            bottomSheetController.hide()
                            selectedUri = Uri.EMPTY
                        }
                    )
                }
            }
        }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.documents),
                style = MaterialTheme.typography.titleMedium,
                color = arborisPageTitleColor(),
                fontWeight = FontWeight.Bold
            )
            if (eventId != 0) {
                IconButton(
                    onClick = {
                        bottomSheetController.show {
                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.camera),
                                    contentDescription = stringResource(R.string.camera),
                                    Modifier
                                        .size(50.dp)
                                        .clickable {
                                            photoUri = createTempUri()
                                            cameraStarter.launch(photoUri)
                                        }
                                )
                                Icon(
                                    painter = painterResource(R.drawable.file_search),
                                    contentDescription =
                                        stringResource(R.string.file_selection),
                                    Modifier
                                        .size(50.dp)
                                        .clickable {
                                            filePickerLauncher.launch("*/*")
                                        }
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.add_document),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        if (documents.isNullOrEmpty()) {
            Text(
                text = stringResource(id = R.string.no_document),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(start = 8.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                documents.forEach { doc ->
                    val defaultNameUnderscore =
                        stringResource(id = R.string.document_id_underscore, doc.id)
                    val defaultName = stringResource(id = R.string.document_id, doc.id)
                    val downloadDesc = stringResource(id = R.string.download)
                    Card(
                        modifier =
                            Modifier.clickable {
                                val fullUrl =
                                    BuildConfig.BASE_URL.removeSuffix("/") +
                                            "/${doc.url.removePrefix("/")}"
                                var fileName = doc.name ?: defaultNameUnderscore
                                val extension = doc.url.substringAfterLast('.', "")
                                if (extension.isNotEmpty() &&
                                    fileName.endsWith(
                                        ".$extension",
                                        ignoreCase = true
                                    ) == false
                                ) {
                                    fileName = "$fileName.$extension"
                                }
                                downloadDocumentWithManager(context, fullUrl, fileName)
                            },
                        colors =
                            CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = doc.name ?: defaultName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = downloadDesc,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
