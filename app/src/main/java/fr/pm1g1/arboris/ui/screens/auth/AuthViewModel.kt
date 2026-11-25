package fr.pm1g1.arboris.ui.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.lifecycleScope
import fr.pm1g1.arboris.R
import fr.pm1g1.arboris.core.network.NetworkResult
import fr.pm1g1.arboris.core.network.RetrofitClient
import fr.pm1g1.arboris.core.network.IAuthApiService
import fr.pm1g1.arboris.core.network.IUserApiService
import fr.pm1g1.arboris.core.storage.TokenManager
import fr.pm1g1.arboris.core.storage.UserManager
import fr.pm1g1.arboris.data.dto.user.UserDetailsResponse
import fr.pm1g1.arboris.data.repository.AuthRepository
import fr.pm1g1.arboris.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository: AuthRepository
    private val userRepository: UserRepository
    private val tokenManager: TokenManager
    private val userManager: UserManager

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<UserDetailsResponse?>(null)
    val currentUser: StateFlow<UserDetailsResponse?> = _currentUser.asStateFlow()

    init {
        val authApiService = RetrofitClient.authInstance.create(IAuthApiService::class.java)
        val userApiService = RetrofitClient.instance.create(IUserApiService::class.java)
        tokenManager = TokenManager(application)
        userManager = UserManager(application)

        authRepository = AuthRepository(
            authApiService,
        )

        userRepository = UserRepository(
            userApiService,
        )

        _currentUser.value = userManager.getUserDetails()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.login(email, password)) {
                is NetworkResult.Success -> {
                    tokenManager.saveTokens(result.data)

                    when (val userResult = userRepository.fetchCurrentUser()) {
                        is NetworkResult.Success -> {
                            userManager.saveUser(userResult.data)
                            _currentUser.value = userResult.data
                        }

                        else -> {
                            // En cas d'erreur, on considère que l'utilisateur n'est plus connecté
                            userManager.clearUser()
                            _currentUser.value = null
                        }
                    }
                    _uiState.value = AuthUiState(isSuccess = true)
                }

                is NetworkResult.Error -> {
                    val errorMessage = when (result.code) {
                        401 -> getApplication<Application>().getString(R.string.auth_login_invalid_credentials)
                        else -> getApplication<Application>().getString(
                            R.string.auth_login_error,
                            result.code,
                            result.exception.message
                        )
                    }
                    _uiState.value =
                        AuthUiState(
                            error = errorMessage
                        )
                }

                is NetworkResult.Loading -> {
                    _uiState.value = AuthUiState(isLoading = true)
                }
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = AuthUiState(isLoading = true)
            when (val result = authRepository.register(email, password)) {
                is NetworkResult.Success -> {
                    login(email, password)
                }

                is NetworkResult.Error -> {
                    var apiErrorMessage: String? = null
                    if (result.code == 400 && result.exception.message != null) {
                        try {
                            val jsonObject = org.json.JSONObject(result.exception.message!!)
                            if (jsonObject.has("errors")) {
                                val errorsObj = jsonObject.getJSONObject("errors")
                                val keys = errorsObj.keys()
                                if (keys.hasNext()) {
                                    val firstKey = keys.next()
                                    val firstErrorArray = errorsObj.getJSONArray(firstKey)
                                    if (firstErrorArray.length() > 0) {
                                        apiErrorMessage = firstErrorArray.getString(0)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            // Ignorer l'erreur si le format ne correspond pas
                        }
                    }

                    val errorMessage = apiErrorMessage ?: when (result.code) {
                        400 -> getApplication<Application>().getString(R.string.auth_register_email_taken)
                        else -> getApplication<Application>().getString(
                            R.string.auth_register_error,
                            result.code
                        )
                    }
                    _uiState.value =
                        AuthUiState(
                            error = errorMessage
                        )
                }

                is NetworkResult.Loading -> {
                    _uiState.value = AuthUiState(isLoading = true)
                }
            }
        }
    }

    fun refreshCurrentUser() {
        viewModelScope.launch {
            // Actualise les arbres du user
            when (val result = userRepository.fetchCurrentUser()) {
                is NetworkResult.Success -> {
                    userManager.saveUser(result.data)
                    _currentUser.value = result.data
                }

                else -> {
                    // En cas d'erreur, on considère que l'utilisateur n'est plus connecté
                    userManager.clearUser()
                    _currentUser.value = null
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun resetState() {
        _uiState.value = AuthUiState()
    }

    fun logout() {
        tokenManager.clearTokens()
        userManager.clearUser()
        _currentUser.value = null
        _uiState.value = AuthUiState()
    }
}
