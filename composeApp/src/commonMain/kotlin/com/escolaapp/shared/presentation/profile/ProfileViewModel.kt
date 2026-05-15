package com.escolaapp.shared.presentation.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.data.repository.UserRepository
import com.escolaapp.core.domain.model.User
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.features.teacher.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val navigationViewModel: NavigationViewModel,
    private val token: String,
    private val userId: Int,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val user = userRepository.getUserById(token, userId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = user,
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erro ao carregar perfil",
                    )
                }
            }
        }
    }

    fun onTabSelected(tab: AppNavigationTab, name: String, email: String, role: String) {
        when (tab) {
            AppNavigationTab.CLASSES -> {
                navigateToClassList(
                    mode = ClassListMode.SELECT_ACTION,
                    name = name,
                    email = email,
                    role = role,
                )
            }

            AppNavigationTab.SETTINGS -> Unit

            AppNavigationTab.HOME -> {
                navigateToHome(
                    name = name,
                    email = email,
                    role = role,
                )
            }
        }
    }

    fun navigateToClassList(
        mode: ClassListMode,
        name: String,
        email: String,
        role: String,
    ) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToClassList(
                    token     = token,
                    teacherId = userId,
                    name      = name,
                    email     = email,
                    role      = role,
                    mode      = mode,
                )
            )
        }
    }

    fun navigateToHome(name: String, email: String, role: String) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToDashboard(
                    token = token,
                    userId = userId,
                    name = name,
                    email = email,
                    role = role,
                )
            )
        }
    }

//    fun navigateBack() {
//        screenModelScope.launch {
//            navigationViewModel.emit(NavigationEvent.Back)
//        }
//    }

    fun logout() {
        screenModelScope.launch {
            navigationViewModel.emit(NavigationEvent.ToLogin)
        }
    }
}