package com.escolaapp.features.teacher.presentation.classlist

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.escolaapp.core.navigation.NavigationEvent
import com.escolaapp.core.navigation.AppEventNavigator
import com.escolaapp.core.session.SessionManager
import com.escolaapp.features.teacher.data.repository.ClassRepository
import com.escolaapp.core.utils.toUserMessage
import com.escolaapp.features.teacher.domain.model.Class
import com.escolaapp.core.domain.model.ClassListMode
import com.escolaapp.shared.components.AppNavigationTab
import kotlinx.coroutines.CoroutineScope
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
    private val appEventNavigator: AppEventNavigator,
    private val sessionManager: SessionManager,
    private val teacherId: Int,
    private val coroutineScope: CoroutineScope? = null,
) : ScreenModel {

    private val scope = coroutineScope ?: screenModelScope
    private val _uiState = MutableStateFlow(ClassListUiState())
    val uiState: StateFlow<ClassListUiState> = _uiState.asStateFlow()

    init {
        loadClasses()
    }

    fun loadClasses() {
        scope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val classes = classRepository.getClassesByTeacher(sessionManager.token, teacherId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        classes = classes,
                        filteredClasses = applySearchFilter(classes, it.searchQuery),
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.toUserMessage())
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
        scope.launch {
            when (mode) {
                ClassListMode.SELECT_ACTION -> Unit
                ClassListMode.ATTENDANCE -> appEventNavigator.emit(
                    NavigationEvent.ToAttendanceCall(classId = classId)
                )

                ClassListMode.GRADEBOOK -> appEventNavigator.emit(
                    NavigationEvent.ToGradeBook(classId = classId, bimester = bimester)
                )
            }
        }
    }

    fun navigateToHome() {
        scope.launch {
            appEventNavigator.emit(NavigationEvent.ToDashboard(role = sessionManager.role))
        }
    }

    fun navigateToSettings() {
        scope.launch { appEventNavigator.emit(NavigationEvent.ToProfile) }
    }

    fun onTabSelected(tab: AppNavigationTab) {
        when (tab) {
            AppNavigationTab.CLASSES -> Unit
            AppNavigationTab.SETTINGS -> navigateToSettings()
            AppNavigationTab.HOME -> navigateToHome()
        }
    }
    // end navigation functions
}