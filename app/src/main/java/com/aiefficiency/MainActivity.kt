package com.aiefficiency

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aiefficiency.database.AppDatabase
import com.aiefficiency.repository.TaskRepository
import com.aiefficiency.repository.ApiConfigRepository
import com.aiefficiency.ui.navigation.AppNavigation
import com.aiefficiency.ui.theme.AIEfficiencyAppTheme
import com.aiefficiency.viewmodel.TaskViewModel
import com.aiefficiency.viewmodel.TaskViewModelFactory
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize database and repositories
        val database = AppDatabase.getDatabase(applicationContext)
        val taskRepository = TaskRepository(
            database.taskDao(),
            database.subTaskDao(),
            database.timeLogDao()
        )
        val apiConfigRepository = ApiConfigRepository(database.apiConfigDao())

        // Initialize ViewModel
        val factory = TaskViewModelFactory(taskRepository, apiConfigRepository)
        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        // Initialize default API config if not exists
        if (taskViewModel.apiConfig.value == null) {
            taskViewModel.updateApiConfig(
                "https://api.openai.com/v1/",
                "",
                "gpt-3.5-turbo"
            )
        }

        setContent {
            AIEfficiencyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(taskViewModel)
                }
            }
        }
    }
}
