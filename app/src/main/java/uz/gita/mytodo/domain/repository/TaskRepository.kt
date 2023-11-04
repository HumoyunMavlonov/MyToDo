package uz.gita.mytodo.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.mytodo.data.model.TaskData

interface TaskRepository {

    fun getAllTasks(): Flow<List<TaskData>>
    fun getInCompletedTasks(): Flow<List<TaskData>>
    fun getCompletedTasks(): Flow<List<TaskData>>
    suspend fun addTask(task: TaskData)
    suspend fun deleteTask(task: TaskData)
    suspend fun updateTask(task: TaskData)
    suspend fun getTask(id:Long): TaskData
}