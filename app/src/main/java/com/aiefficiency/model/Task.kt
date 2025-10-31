package com.aiefficiency.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val startDate: Long,
    val dueDate: Long,
    val priority: Int = 1, // 1: Low, 2: Medium, 3: High
    val status: String = "pending", // pending, in_progress, completed
    val estimatedHours: Float = 0f,
    val actualHours: Float = 0f,
    val progress: Int = 0, // 0-100
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isAIGenerated: Boolean = false,
    val parentTaskId: Int? = null // For subtasks
)

@Entity(tableName = "subtasks")
data class SubTask(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val parentTaskId: Int,
    val title: String,
    val description: String,
    val scheduledDate: Long,
    val estimatedHours: Float,
    val actualHours: Float = 0f,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "time_logs")
data class TimeLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskId: Int,
    val startTime: Long,
    val endTime: Long? = null,
    val duration: Long = 0, // in milliseconds
    val createdAt: Long = System.currentTimeMillis()
)

data class TaskWithSubTasks(
    val task: Task,
    val subTasks: List<SubTask> = emptyList(),
    val timeLogs: List<TimeLog> = emptyList()
)

data class EfficiencyMetrics(
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val completionRate: Float = 0f,
    val averageEstimatedHours: Float = 0f,
    val averageActualHours: Float = 0f,
    val efficiencyScore: Float = 0f, // 0-100
    val onTimeCompletionRate: Float = 0f
)
