package com.escolaapp.features.teacher.presentation.classlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.NavigationViewModel
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.features.teacher.domain.model.ClassListMode
import com.escolaapp.features.teacher.presentation.components.TeacherNavigationTab
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
) : ScreenModel {

    private val _uiState = MutableStateFlow(ClassListUiState())
    val uiState: StateFlow<ClassListUiState> = _uiState.asStateFlow()

    fun loadClasses(token: String, teacherId: Int) {
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
    fun navigateToClass(token: String, classId: Int, mode: ClassListMode) {
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
                    )
                )
            }
        }
    }

    fun navigateToHome(token: String, userId: Int, name: String, email: String, role: String) {
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

    fun navigateToSettings(token: String, userId: Int, name: String, email: String, role: String) {
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
        tab: TeacherNavigationTab,
        token: String,
        userId: Int,
        name: String,
        email: String,
        role: String,
    ) {
        when (tab) {
            TeacherNavigationTab.CLASSES -> Unit

            TeacherNavigationTab.SETTINGS -> {
                navigateToSettings(token, userId, name, email, role)
            }

            TeacherNavigationTab.HOME -> {
                navigateToHome(token, userId, name, email, role)
            }
        }
    }
    // end navigation functions
}