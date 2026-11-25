package fr.pm1g1.arboris.ui.View.Trees

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.data.model.arboris.Tree
import fr.pm1g1.arboris.ui.components.BottomSheetController
import fr.pm1g1.arboris.ui.components.TextInputSheetContent
import fr.pm1g1.arboris.ui.theme.arborisPageTitleColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTrees(
        trees: List<Tree>,
        bottomSheetController: BottomSheetController? = null,
        onAddTree: (String) -> Unit = {},
        onTreeChange: (Tree) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items = trees) { tree ->
                Button(onClick = { onTreeChange(tree) }) { Text(tree.name) }
            }
        }
        Button(
                onClick = {
                    bottomSheetController?.show {
                        TextInputSheetContent(
                                title = stringResource(R.string.addTree),
                                label = stringResource(R.string.tree_description_form),
                                errorMessage = stringResource(R.string.treeNameMandatory),
                                onConfirm = { treeName ->
                                    onAddTree(treeName)
                                    bottomSheetController.hide()
                                },
                                onCancel = { bottomSheetController.hide() }
                        )
                    }
                }
        ) { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.addTree)) }
    }
}

@Composable
fun HomeMessage() {
    Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
                text = stringResource(R.string.welcome_message),
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp,
                lineHeight = 58.sp,
                textAlign = TextAlign.Center,
                color = arborisPageTitleColor()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTree(onDismiss: () -> Unit, onTreeCreated: (String) -> Unit = {}) {
    androidx.compose.material3.ModalBottomSheet(onDismissRequest = onDismiss) {
        TextInputSheetContent(
                title = stringResource(R.string.addTree),
                label = stringResource(R.string.tree_description_form),
                errorMessage = stringResource(R.string.treeNameMandatory),
                onConfirm = { treeName ->
                    onTreeCreated(treeName)
                    onDismiss()
                },
                onCancel = onDismiss
        )
    }
}
