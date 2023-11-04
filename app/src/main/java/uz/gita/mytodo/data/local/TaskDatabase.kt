package uz.gita.mytodo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.gita.mytodo.data.local.dao.TaskDao
import uz.gita.mytodo.data.model.TaskData

@Database(entities = [TaskData::class], version = 1, exportSchema = false)
abstract class TaskDatabase:RoomDatabase() {

    companion object{
        const val DATABASE_NAME = "ToDoTasks"
    }

    abstract fun getTaskDao():TaskDao
}