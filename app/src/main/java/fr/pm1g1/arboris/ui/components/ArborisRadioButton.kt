package fr.pm1g1.arboris.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import fr.pm1g1.arboris.ui.theme.arborisRadioColors
import fr.pm1g1.arboris.ui.theme.arborisRadioTextColor

@Composable
fun ArborisRadioButton(
    radioOptions: List<String>,
    selectedOption: String? = null,
    onOptionSelected: ((String) -> Unit)? =null,
    modifier: Modifier = Modifier
) {
    val textColor = arborisRadioTextColor()
    val radioColors = arborisRadioColors()
    val (internalSelected, setInternalSelected) =
        remember(radioOptions) { mutableStateOf(radioOptions.firstOrNull().orEmpty()) }
    val currentSelected = selectedOption ?: internalSelected
    val selectOption: (String) -> Unit = { option ->
        if (selectedOption == null || onOptionSelected == null) {
            setInternalSelected(option)
        }
        onOptionSelected?.invoke(option)
    }
    Column(modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 44.dp)
                    .selectable(
                        selected = (text == currentSelected),
                        onClick = { selectOption(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == currentSelected),
                    onClick = null,
                    colors = radioColors
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColor,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
