package com.aiefficiency.service

import android.util.Log
import com.aiefficiency.api.ChatCompletionRequest
import com.aiefficiency.api.Message
import com.aiefficiency.api.OpenAIApiService
import com.aiefficiency.api.SystemPrompts
import com.aiefficiency.model.ApiConfig
import com.aiefficiency.model.GeneratedSubTask
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AIService(private val apiConfig: ApiConfig) {
    private val gson = Gson()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(apiConfig.apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    suspend fun decomposeTask(
        mainTask: String,
        description: String,
        daysAvailable: Int,
        hoursPerDay: Float,
        priority: String
    ): Result<List<GeneratedSubTask>> = withContext(Dispatchers.IO) {
        try {
            if (!apiConfig.isConfigured || apiConfig.apiKey.isEmpty()) {
                return@withContext Result.failure(Exception("API not configured"))
            }

            val apiService = createRetrofit().create(OpenAIApiService::class.java)

            val userPrompt = """
                Please decompose the following task into daily subtasks:
                
                Main Task: $mainTask
                Description: $description
                Days Available: $daysAvailable
                Hours Per Day: $hoursPerDay
                Priority: $priority
                
                Today's date: ${dateFormat.format(System.currentTimeMillis())}
                
                Please return a JSON response with the following structure:
                {
                    "subtasks": [
                        {
                            "date": "YYYY-MM-DD",
                            "taskTitle": "Task title",
                            "description": "Brief description",
                            "estimatedHours": 2.5,
                            "priority": "high/medium/low"
                        }
                    ],
                    "summary": "Brief summary of the plan"
                }
            """.trimIndent()

            val request = ChatCompletionRequest(
                model = apiConfig.modelName,
                messages = listOf(
                    Message(role = "system", content = SystemPrompts.TASK_DECOMPOSITION),
                    Message(role = "user", content = userPrompt)
                ),
                temperature = apiConfig.temperature,
                max_tokens = apiConfig.maxTokens
            )

            val response = apiService.createChatCompletion(
                authorization = "Bearer ${apiConfig.apiKey}",
                request = request
            )

            if (response.isSuccessful && response.body() != null) {
                val responseBody = response.body()!!
                val content = responseBody.choices.firstOrNull()?.message?.content ?: ""

                // Parse JSON response
                val jsonResponse = gson.fromJson(content, JsonObject::class.java)
                val subtasksArray = jsonResponse.getAsJsonArray("subtasks")

                val subTasks = mutableListOf<GeneratedSubTask>()
                subtasksArray.forEach { element ->
                    val obj = element.asJsonObject
                    subTasks.add(
                        GeneratedSubTask(
                            date = obj.get("date").asString,
                            taskTitle = obj.get("taskTitle").asString,
                            description = obj.get("description").asString,
                            estimatedHours = obj.get("estimatedHours").asFloat,
                            priority = obj.get("priority").asString
                        )
                    )
                }

                Result.success(subTasks)
            } else {
                Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AIService", "Task decomposition error", e)
            Result.failure(e)
        }
    }

    suspend fun getEfficiencyAnalysis(
        completedTasks: Int,
        totalTasks: Int,
        averageEstimatedHours: Float,
        averageActualHours: Float,
        onTimeCompletionRate: Float
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            if (!apiConfig.isConfigured || apiConfig.apiKey.isEmpty()) {
                return@withContext Result.failure(Exception("API not configured"))
            }

            val apiService = createRetrofit().create(OpenAIApiService::class.java)

            val userPrompt = """
                Based on my task completion history, please provide efficiency analysis and recommendations:
                
                - Completed Tasks: $completedTasks
                - Total Tasks: $totalTasks
                - Average Estimated Hours: $averageEstimatedHours
                - Average Actual Hours: $averageActualHours
                - On-Time Completion Rate: ${String.format("%.1f", onTimeCompletionRate)}%
                
                Please provide:
                1. Current efficiency assessment
                2. Key bottlenecks
                3. Specific recommendations to improve productivity
                4. Estimated improvement potential
                
                Keep the response concise and actionable.
            """.trimIndent()

            val request = ChatCompletionRequest(
                model = apiConfig.modelName,
                messages = listOf(
                    Message(role = "system", content = SystemPrompts.EFFICIENCY_ANALYSIS),
                    Message(role = "user", content = userPrompt)
                ),
                temperature = apiConfig.temperature,
                max_tokens = apiConfig.maxTokens
            )

            val response = apiService.createChatCompletion(
                authorization = "Bearer ${apiConfig.apiKey}",
                request = request
            )

            if (response.isSuccessful && response.body() != null) {
                val content = response.body()!!.choices.firstOrNull()?.message?.content ?: ""
                Result.success(content)
            } else {
                Result.failure(Exception("API Error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AIService", "Efficiency analysis error", e)
            Result.failure(e)
        }
    }
}
