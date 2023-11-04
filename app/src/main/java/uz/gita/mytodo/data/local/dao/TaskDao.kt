package uz.gita.mytodo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uz.gita.mytodo.data.model.TaskData

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks():Flow<List<TaskData>>

    @Query("SELECT * FROM tasks WHERE isCompleted=1")
    fun getCompletedTasks():Flow<List<TaskData>>

    @Query("SELECT * FROM tasks WHERE isCompleted=0")
    fun getInCompletedTasks():Flow<List<TaskData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTask(task:TaskData): Long

    @Delete
    suspend fun deleteTask(task: TaskData)

    @Update
    suspend fun updateTask(task: TaskData)

    @Query("SELECT * FROM tasks WHERE id=:id")
    suspend fun getTask(id:Long):TaskData
}