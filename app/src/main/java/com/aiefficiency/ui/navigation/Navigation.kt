package com.aiefficiency.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aiefficiency.ui.screens.HomeScreen
import com.aiefficiency.ui.screens.AddTaskScreen
import com.aiefficiency.ui.screens.TaskDetailScreen
import com.aiefficiency.ui.screens.SettingsScreen
import com.aiefficiency.ui.screens.EfficiencyAnalysisScreen
import com.aiefficiency.viewmodel.TaskViewModel

@Composable
fun AppNavigation(viewModel: TaskViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, viewModel)
        }
        composable("add_task") {
            AddTaskScreen(navController, viewModel)
        }
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull() ?: 0
            TaskDetailScreen(navController, viewModel, taskId)
        }
        composable("settings") {
            SettingsScreen(navController, viewModel)
        }
        composable("efficiency_analysis") {
            EfficiencyAnalysisScreen(navController, viewModel)
        }
    }
}
