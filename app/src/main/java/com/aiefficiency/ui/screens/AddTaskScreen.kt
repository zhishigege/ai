package com.aiefficiency.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aiefficiency.model.Task
import com.aiefficiency.viewmodel.TaskViewModel
import java.util.Calendar

@Composable
fun AddTaskScreen(navController: NavHostController, viewModel: TaskViewModel) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var daysAvailable by remember { mutableStateOf("") }
    var hoursPerDay by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("medium") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("添加新任务", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "任务信息",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                label = { Text("任务名称") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                label = { Text("任务描述") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "时间规划",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = daysAvailable,
                onValueChange = { daysAvailable = it },
                label = { Text("可用天数") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = hoursPerDay,
                onValueChange = { hoursPerDay = it },
                label = { Text("每天可用小时数") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "优先级",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Priority selector (simplified)
            Text(
                "优先级: $priority",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (taskTitle.isNotEmpty() && daysAvailable.isNotEmpty() && hoursPerDay.isNotEmpty()) {
                        val dueDate = Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_MONTH, daysAvailable.toIntOrNull() ?: 1)
                        }.timeInMillis

                        val newTask = Task(
                            title = taskTitle,
                            description = taskDescription,
                            startDate = System.currentTimeMillis(),
                            dueDate = dueDate,
                            priority = when (priority) {
                                "high" -> 3
                                "medium" -> 2
                                else -> 1
                            },
                            estimatedHours = hoursPerDay.toFloatOrNull() ?: 0f,
                            isAIGenerated = true
                        )

                        viewModel.addTask(newTask)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("使用AI拆解任务", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (taskTitle.isNotEmpty()) {
                        val dueDate = Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_MONTH, daysAvailable.toIntOrNull() ?: 1)
                        }.timeInMillis

                        val newTask = Task(
                            title = taskTitle,
                            description = taskDescription,
                            startDate = System.currentTimeMillis(),
                            dueDate = dueDate,
                            priority = when (priority) {
                                "high" -> 3
                                "medium" -> 2
                                else -> 1
                            },
                            estimatedHours = hoursPerDay.toFloatOrNull() ?: 0f,
                            isAIGenerated = false
                        )

                        viewModel.addTask(newTask)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("创建任务", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
