package com.aiefficiency.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApiService {
    @POST("chat/completions")
    suspend fun createChatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: ChatCompletionRequest
    ): Response<ChatCompletionResponse>
}

data class ChatCompletionRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Float = 0.7f,
    val max_tokens: Int = 2000,
    val top_p: Float = 1.0f,
    val frequency_penalty: Float = 0f,
    val presence_penalty: Float = 0f
)

data class Message(
    val role: String, // "system", "user", "assistant"
    val content: String
)

data class ChatCompletionResponse(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice(
    val index: Int,
    val message: Message,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)

// System prompts for task decomposition
object SystemPrompts {
    const val TASK_DECOMPOSITION = """
        You are an AI task management assistant. Your job is to help users break down large tasks into smaller, manageable daily tasks.
        
        When a user provides a main task, deadline, and available hours per day, you should:
        1. Analyze the task complexity
        2. Create a daily schedule that spans from today to the deadline
        3. Break the main task into specific subtasks for each day
        4. Estimate time for each subtask
        5. Prioritize subtasks based on dependencies and importance
        
        Return the response in JSON format with the following structure:
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
    """

    const val EFFICIENCY_ANALYSIS = """
        You are an AI efficiency analyst. Based on the user's task completion history, provide insights and recommendations.
        
        Analyze the following metrics:
        1. Task completion rate
        2. Time estimation accuracy
        3. Task priority distribution
        4. Completion patterns
        
        Provide actionable recommendations to improve productivity.
    """
}
