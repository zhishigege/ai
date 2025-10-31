package com.aiefficiency.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aiefficiency.model.Task
import com.aiefficiency.model.SubTask
import com.aiefficiency.model.TimeLog
import com.aiefficiency.model.EfficiencyMetrics
import com.aiefficiency.repository.TaskRepository
import com.aiefficiency.repository.ApiConfigRepository
import com.aiefficiency.service.AIService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskRepository: TaskRepository,
    private val apiConfigRepository: ApiConfigRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Tasks
    val allTasks = taskRepository.getAllTasks().asLiveData()
    val pendingTasks = taskRepository.getTasksByStatus("pending").asLiveData()
    val completedTasks = taskRepository.getTasksByStatus("completed").asLiveData()

    // Efficiency Metrics
    val efficiencyMetrics = taskRepository.getEfficiencyMetrics().asLiveData()

    // API Config
    val apiConfig = apiConfigRepository.getConfig().asLiveData()

    // Add new task
    fun addTask(task: Task) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                val taskId = taskRepository.insertTask(task)
                _uiState.value = UiState.Success("Task added successfully")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Update task
    fun updateTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.updateTask(task)
                _uiState.value = UiState.Success("Task updated")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Delete task
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(task)
                _uiState.value = UiState.Success("Task deleted")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Update task progress
    fun updateTaskProgress(taskId: Int, progress: Int) {
        viewModelScope.launch {
            try {
                taskRepository.updateTaskProgress(taskId, progress)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Update task status
    fun updateTaskStatus(taskId: Int, status: String) {
        viewModelScope.launch {
            try {
                taskRepository.updateTaskStatus(taskId, status)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Add subtask
    fun addSubTask(subTask: SubTask) {
        viewModelScope.launch {
            try {
                taskRepository.insertSubTask(subTask)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Update API config
    fun updateApiConfig(baseUrl: String, apiKey: String, modelName: String) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                apiConfigRepository.updateConfig(baseUrl, apiKey, modelName, true)
                _uiState.value = UiState.Success("API configuration updated")
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    // Decompose task with AI
    fun decomposeTaskWithAI(
        mainTask: String,
        description: String,
        daysAvailable: Int,
        hoursPerDay: Float,
        priority: String,
        parentTaskId: Int
    ) {
        viewModelScope.launch {
            try {
                _uiState.value = UiState.Loading
                
                val config = apiConfig.value
                if (config == null || !config.isConfigured) {
                    _uiState.value = UiState.Error("API not configured. Please set up API configuration first.")
                    return@launch
                }

                val aiService = AIService(config)
                val result = aiService.decomposeTask(
                    mainTask = mainTask,
                    description = description,
                    daysAvailable = daysAvailable,
                    hoursPerDay = hoursPerDay,
                    priority = priority
                )

                result.onSuccess { subTasks ->
                    // Create subtasks in database
                    subTasks.forEach { generatedSubTask ->
                        val subTask = SubTask(
                            parentTaskId = parentTaskId,
                            title = generatedSubTask.taskTitle,
                            description = generatedSubTask.description,
                            scheduledDate = parseDate(generatedSubTask.date),
                            estimatedHours = generatedSubTask.estimatedHours
                        )
                        taskRepository.insertSubTask(subTask)
                    }
                    _uiState.value = UiState.Success("Task decomposed successfully")
                }

                result.onFailure { error ->
                    _uiState.value = UiState.Error(error.message ?: "Failed to decompose task")
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun parseDate(dateString: String): Long {
        return try {
            val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            format.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            System.currentTimeMillis()
        }
    }

    fun clearMessage() {
        _uiState.value = UiState.Idle
    }
}

sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val message: String) : UiState()
    data class Error(val message: String) : UiState()
}

class TaskViewModelFactory(
    private val taskRepository: TaskRepository,
    private val apiConfigRepository: ApiConfigRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskRepository, apiConfigRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
