package uz.gita.mytodo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskData(
    @PrimaryKey(autoGenerate = false)
    val id:Long,
    val title:String,
    val body:String,
    val timer:String = "",
    var isCompleted:Boolean = false,
    var notified:Boolean = false
){
    fun getWorkerId() = "FOR_WORKER$id"
}