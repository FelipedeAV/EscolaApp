package com.escolaapp.features.teacher.presentation.classlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ClassListUiState(
    val isLoading: Boolean = false,
    val classes: List<Class> = emptyList(),
    val filteredClasses: List<Class> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null,
)

class ClassListViewModel(
    private val classRepository: ClassRepository,
    private val navigationViewModel: NavigationViewModel,
    private val token: String,
    private val teacherId: Int,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ClassListUiState())
    val uiState: StateFlow<ClassListUiState> = _uiState.asStateFlow()

    init {
        loadClasses()
    }

    fun loadClasses() {
        screenModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val classes = classRepository.getClassesByTeacher(token, teacherId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        classes = classes,
                        filteredClasses = applySearchFilter(classes, it.searchQuery),
                    )
                }
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = "Erro ao carregar turmas")
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
                filteredClasses = applySearchFilter(it.classes, query),
            )
        }
    }

    private fun applySearchFilter(classes: List<Class>, query: String): List<Class> {
        if (query.isBlank()) return classes
        val normalizedQuery = query.trim().lowercase()
        return classes.filter { schoolClass ->
            schoolClass.subject.lowercase().contains(normalizedQuery) ||
                    schoolClass.room.lowercase().contains(normalizedQuery)
        }
    }

    // navigation functions
    fun navigateToClass(classId: Int, mode: ClassListMode, bimester: Int = 1) {
        screenModelScope.launch {
            when (mode) {
                ClassListMode.SELECT_ACTION -> Unit
                ClassListMode.ATTENDANCE -> navigationViewModel.emit(
                    NavigationEvent.ToAttendanceCall(
                        token = token,
                        classId = classId,
                    )
                )

                ClassListMode.GRADEBOOK -> navigationViewModel.emit(
                    NavigationEvent.ToGradeBook(
                        token = token,
                        classId = classId,
                        bimester = bimester,
                    )
                )
            }
        }
    }

    fun navigateToHome(userId: Int, name: String, email: String, role: String) {
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

    fun navigateToSettings(userId: Int, name: String, email: String, role: String) {
        screenModelScope.launch {
            navigationViewModel.emit(
                NavigationEvent.ToProfile(
                    token = token,
                    userId = userId,
                    name = name,
                    email = email,
                    role = role,
                )
            )
        }
    }

    fun onTabSelected(
        tab: AppNavigationTab,
        userId: Int,
        name: String,
        email: String,
        role: String,
    ) {
        when (tab) {
            AppNavigationTab.CLASSES -> Unit

            AppNavigationTab.SETTINGS -> {
                navigateToSettings(userId, name, email, role)
            }

            AppNavigationTab.HOME -> {
                navigateToHome(userId, name, email, role)
            }
        }
    }
    // end navigation functions
}