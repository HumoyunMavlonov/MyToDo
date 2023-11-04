package uz.gita.mytodo.domain.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import uz.gita.mytodo.data.local.dao.TaskDao
import uz.gita.mytodo.data.model.TaskData
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
):TaskRepository {
    override fun getAllTasks(): Flow<List<TaskData>> = dao.getAllTasks().flowOn(Dispatchers.IO)
    override fun getCompletedTasks(): Flow<List<TaskData>> = dao.getCompletedTasks().flowOn(Dispatchers.IO)
    override fun getInCompletedTasks(): Flow<List<TaskData>> = dao.getInCompletedTasks().flowOn(Dispatchers.IO)

    override suspend fun addTask(task: TaskData)  {
        dao.addTask(task)
    }

    override suspend fun deleteTask(task: TaskData) {
        withContext(Dispatchers.IO){
            dao.deleteTask(task)
        }
    }

    override suspend fun updateTask(task: TaskData) {
        withContext(Dispatchers.IO){
            dao.updateTask(task)
        }
    }

    override suspend fun getTask(id: Long): TaskData =
        withContext(Dispatchers.IO){
           dao.getTask(id)
        }

}
