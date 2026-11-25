package fr.pm1g1.arboris.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.ui.components.ArborisButton
import fr.pm1g1.arboris.ui.components.ArborisTextField

@Composable
fun AuthScreen(
        onLoginSuccess: () -> Unit = {},
        viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var isLoginMode by remember { mutableStateOf(true) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess == true && isLoginMode == true) {
            onLoginSuccess()
        } else if (uiState.isSuccess == true && isLoginMode == false) {
            isLoginMode = true
            viewModel.resetState()
        }
    }

    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = stringResource(
                    id = if (isLoginMode == true) {
                        R.string.auth_title_login
                    } else {
                        R.string.auth_title_register
                    }
                ),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        ArborisTextField(
                value = email,
                onValueChange = {
                    email = it
                    viewModel.clearError()
                },
                label = stringResource(R.string.auth_email_label),
                isError = uiState.error != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        ArborisTextField(
                value = password,
                onValueChange = {
                    password = it
                    viewModel.clearError()
                },
                label = stringResource(R.string.auth_password_label),
                visualTransformation = PasswordVisualTransformation(),
                isError = uiState.error != null,
                errorMessage = uiState.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        ArborisButton(
                text = stringResource(
                    id = if (isLoginMode == true) {
                        R.string.auth_login_button
                    } else {
                        R.string.auth_register_button
                    }
                ),
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading,
                onClick = {
                    if (isLoginMode == true) {
                        viewModel.login(email, password)
                    } else {
                        viewModel.register(email, password)
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
                onClick = {
                    isLoginMode = !isLoginMode
                    viewModel.resetState()
                }
        ) {
            Text(
                    stringResource(
                        id = if (isLoginMode == true) {
                            R.string.auth_toggle_to_register
                        } else {
                            R.string.auth_toggle_to_login
                        }
                    )
            )
        }
    }
}
