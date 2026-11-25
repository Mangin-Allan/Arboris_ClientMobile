package fr.pm1g1.arboris.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun ArborisTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    var startTyped = remember() { mutableStateOf(false) }
    val internalOnValueChange = { it: String ->
        startTyped.value = true
        onValueChange(it)
    }

    OutlinedTextField(
        value = value,
        onValueChange = internalOnValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        isError = isError && startTyped.value,
        visualTransformation = visualTransformation,
        singleLine = singleLine,
        minLines = minLines,
        supportingText = {
            if (isError && startTyped.value && errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    )
}
