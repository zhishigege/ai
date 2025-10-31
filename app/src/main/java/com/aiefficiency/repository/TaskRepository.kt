package com.aiefficiency.repository

import com.aiefficiency.database.TaskDao
import com.aiefficiency.database.SubTaskDao
import com.aiefficiency.database.TimeLogDao
import com.aiefficiency.model.Task
import com.aiefficiency.model.SubTask
import com.aiefficiency.model.TimeLog
import com.aiefficiency.model.EfficiencyMetrics
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class TaskRepository(
    private val taskDao: TaskDao,
    private val subTaskDao: SubTaskDao,
    private val timeLogDao: TimeLogDao
) {
    // Task operations
    suspend fun insertTask(task: Task): Long = taskDao.insert(task)

    suspend fun updateTask(task: Task) = taskDao.update(task)

    suspend fun deleteTask(task: Task) = taskDao.delete(task)

    fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    fun getTasksByStatus(status: String): Flow<List<Task>> = taskDao.getTasksByStatus(status)

    fun getTasksByDateRange(startDate: Long, endDate: Long): Flow<List<Task>> =
        taskDao.getTasksByDateRange(startDate, endDate)

    suspend fun updateTaskProgress(taskId: Int, progress: Int) =
        taskDao.updateProgress(taskId, progress)

    suspend fun updateTaskStatus(taskId: Int, status: String) =
        taskDao.updateStatus(taskId, status)

    // SubTask operations
    suspend fun insertSubTask(subTask: SubTask): Long = subTaskDao.insert(subTask)

    suspend fun updateSubTask(subTask: SubTask) = subTaskDao.update(subTask)

    fun getSubTasksByParent(parentTaskId: Int): Flow<List<SubTask>> =
        subTaskDao.getSubTasksByParent(parentTaskId)

    suspend fun updateSubTaskCompletion(subTaskId: Int, isCompleted: Boolean, completedAt: Long?) =
        subTaskDao.updateCompletion(subTaskId, isCompleted, completedAt)

    // TimeLog operations
    suspend fun insertTimeLog(timeLog: TimeLog): Long = timeLogDao.insert(timeLog)

    suspend fun updateTimeLog(timeLog: TimeLog) = timeLogDao.update(timeLog)

    fun getTimeLogsByTask(taskId: Int): Flow<List<TimeLog>> = timeLogDao.getTimeLogsByTask(taskId)

    fun getTotalDurationByTask(taskId: Int): Flow<Long?> = timeLogDao.getTotalDurationByTask(taskId)

    fun getTimeLogsByDateRange(startDate: Long, endDate: Long): Flow<List<TimeLog>> =
        timeLogDao.getTimeLogsByDateRange(startDate, endDate)

    // Efficiency metrics
    fun getEfficiencyMetrics(): Flow<EfficiencyMetrics> =
        combine(
            taskDao.getTotalTaskCount(),
            taskDao.getCompletedTaskCount()
        ) { total, completed ->
            val completionRate = if (total > 0) (completed.toFloat() / total) * 100 else 0f
            EfficiencyMetrics(
                totalTasks = total,
                completedTasks = completed,
                completionRate = completionRate,
                efficiencyScore = calculateEfficiencyScore(completionRate)
            )
        }

    private fun calculateEfficiencyScore(completionRate: Float): Float {
        // Simple efficiency score calculation
        return minOf(completionRate, 100f)
    }

    // Task decomposition helper
    fun createSubTasksFromDecomposition(
        parentTaskId: Int,
        subTasks: List<Map<String, Any>>,
        baseDate: Long
    ) {
        // This will be called from the AI service after decomposition
    }
}
