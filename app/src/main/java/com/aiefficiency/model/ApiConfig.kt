package com.aiefficiency.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_config")
data class ApiConfig(
    @PrimaryKey
    val id: Int = 1,
    val apiBaseUrl: String = "https://api.openai.com/v1/",
    val apiKey: String = "",
    val modelName: String = "gpt-3.5-turbo",
    val maxTokens: Int = 2000,
    val temperature: Float = 0.7f,
    val isConfigured: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val role: String, // "user" or "assistant"
    val content: String
)

data class TaskDecompositionRequest(
    val mainTask: String,
    val description: String,
    val daysAvailable: Int,
    val hoursPerDay: Float,
    val priority: String
)

data class TaskDecompositionResponse(
    val mainTaskId: Int,
    val subTasks: List<GeneratedSubTask>
)

data class GeneratedSubTask(
    val date: String,
    val taskTitle: String,
    val description: String,
    val estimatedHours: Float,
    val priority: String
)
