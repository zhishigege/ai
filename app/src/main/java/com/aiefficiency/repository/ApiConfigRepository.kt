package com.aiefficiency.repository

import com.aiefficiency.database.ApiConfigDao
import com.aiefficiency.model.ApiConfig
import kotlinx.coroutines.flow.Flow

class ApiConfigRepository(private val apiConfigDao: ApiConfigDao) {
    
    fun getConfig(): Flow<ApiConfig?> = apiConfigDao.getConfig()

    suspend fun updateConfig(
        baseUrl: String,
        apiKey: String,
        modelName: String,
        isConfigured: Boolean = true
    ) {
        apiConfigDao.updateConfig(baseUrl, apiKey, modelName, isConfigured)
    }

    suspend fun insertDefaultConfig() {
        apiConfigDao.insert(ApiConfig())
    }
}
