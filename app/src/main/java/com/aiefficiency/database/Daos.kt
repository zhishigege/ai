package com.aiefficiency.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.aiefficiency.model.Task
import com.aiefficiency.model.SubTask
import com.aiefficiency.model.TimeLog
import com.aiefficiency.model.ApiConfig
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Int): Flow<Task?>

    @Query("SELECT * FROM tasks ORDER BY dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY dueDate ASC")
    fun getTasksByStatus(status: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE dueDate BETWEEN :startDate AND :endDate ORDER BY dueDate ASC")
    fun getTasksByDateRange(startDate: Long, endDate: Long): Flow<List<Task>>

    @Query("UPDATE tasks SET progress = :progress WHERE id = :id")
    suspend fun updateProgress(id: Int, progress: Int)

    @Query("UPDATE tasks SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: String)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT COUNT(*) FROM tasks WHERE status = 'completed'")
    fun getCompletedTaskCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks")
    fun getTotalTaskCount(): Flow<Int>
}

@Dao
interface SubTaskDao {
    @Insert
    suspend fun insert(subTask: SubTask): Long

    @Update
    suspend fun update(subTask: SubTask)

    @Delete
    suspend fun delete(subTask: SubTask)

    @Query("SELECT * FROM subtasks WHERE parentTaskId = :parentTaskId ORDER BY scheduledDate ASC")
    fun getSubTasksByParent(parentTaskId: Int): Flow<List<SubTask>>

    @Query("SELECT * FROM subtasks WHERE id = :id")
    fun getSubTaskById(id: Int): Flow<SubTask?>

    @Query("UPDATE subtasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :id")
    suspend fun updateCompletion(id: Int, isCompleted: Boolean, completedAt: Long?)

    @Query("SELECT COUNT(*) FROM subtasks WHERE parentTaskId = :parentTaskId AND isCompleted = 1")
    fun getCompletedSubTaskCount(parentTaskId: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM subtasks WHERE parentTaskId = :parentTaskId")
    fun getTotalSubTaskCount(parentTaskId: Int): Flow<Int>
}

@Dao
interface TimeLogDao {
    @Insert
    suspend fun insert(timeLog: TimeLog): Long

    @Update
    suspend fun update(timeLog: TimeLog)

    @Delete
    suspend fun delete(timeLog: TimeLog)

    @Query("SELECT * FROM time_logs WHERE taskId = :taskId ORDER BY startTime DESC")
    fun getTimeLogsByTask(taskId: Int): Flow<List<TimeLog>>

    @Query("SELECT SUM(duration) FROM time_logs WHERE taskId = :taskId")
    fun getTotalDurationByTask(taskId: Int): Flow<Long?>

    @Query("SELECT * FROM time_logs WHERE startTime BETWEEN :startDate AND :endDate ORDER BY startTime DESC")
    fun getTimeLogsByDateRange(startDate: Long, endDate: Long): Flow<List<TimeLog>>

    @Query("DELETE FROM time_logs WHERE id = :id")
    suspend fun deleteById(id: Int)
}

@Dao
interface ApiConfigDao {
    @Insert
    suspend fun insert(config: ApiConfig)

    @Update
    suspend fun update(config: ApiConfig)

    @Query("SELECT * FROM api_config WHERE id = 1")
    fun getConfig(): Flow<ApiConfig?>

    @Query("UPDATE api_config SET apiBaseUrl = :baseUrl, apiKey = :apiKey, modelName = :modelName, isConfigured = :isConfigured WHERE id = 1")
    suspend fun updateConfig(baseUrl: String, apiKey: String, modelName: String, isConfigured: Boolean)
}
