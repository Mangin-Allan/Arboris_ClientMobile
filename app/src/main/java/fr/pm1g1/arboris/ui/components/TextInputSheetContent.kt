package fr.pm1g1.arboris.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.pm1g1.arboris.R

@Composable
fun TextInputSheetContent(
    title: String,
    label: String,
    initialValue: String = "",
    onConfirm: (String) -> Unit,
    onCancel: () -> Unit,
    isInputValid: ((String) -> Boolean)? = null,
    errorMessage: String? = null,
    confirmText: String = stringResource(id = R.string.valid),
    cancelText: String = stringResource(id = R.string.cancel)
) {
    var inputValue by remember { mutableStateOf(initialValue) }
    val isValid = isInputValid?.invoke(inputValue) ?: inputValue.isNotBlank()
    val showError = !isValid && inputValue.isNotBlank() && errorMessage != null

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ArborisTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = label,
            isError = showError,
            errorMessage = errorMessage,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TextButton(onClick = onCancel) { 
                Text(cancelText) 
            }
            Button(
                onClick = {
                    if (isValid) {
                        onConfirm(inputValue)
                    }
                },
                enabled = isValid
            ) { 
                Text(confirmText) 
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
