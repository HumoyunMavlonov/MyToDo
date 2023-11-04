package uz.gita.mytodo.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.gita.mytodo.data.model.TaskData
import uz.gita.mytodo.domain.repository.TaskRepository
import uz.gita.mytodo.util.getCalendar
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val direction: HomeDirection
) : ViewModel(), HomeContract.Model {
    override val uiState = MutableStateFlow(HomeContract.UiState())

    override fun eventDispatcher(intent: HomeContract.Intent) {
        when (intent) {
            is HomeContract.Intent.DeleteTask -> {
                viewModelScope.launch {
                    deleteTask(intent.task)
                }
            }

            is HomeContract.Intent.OpenAddTaskScreen -> {
                viewModelScope.launch {
                    direction.navigateToAddScreen(intent.task)
                }
            }

            is HomeContract.Intent.UpdateTask -> {
                updateTask(intent.task)
            }

            is HomeContract.Intent.GetTasksList -> {
                when (intent.type) {
                    1 -> {
                        viewModelScope.launch {
                            getCompletedTasks().collect { list ->
                                uiState.update {
                                    it.copy(list = list.filter { taskData ->
                                        when (intent.selectedText2) {
                                            "Day" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            "Year" -> {
                                                getCalendar(taskData.timer).get(Calendar.YEAR) == Calendar.getInstance()
                                                    .get(Calendar.YEAR)
                                            }

                                            "Week" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            "Month" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            else -> false
                                        }
                                    }
                                    )
                                }
                            }
                        }
                    }

                    2 -> {
                        viewModelScope.launch {
                            getInCompletedTasks().collect { list ->
                                uiState.update {
                                    it.copy(list = list.filter { taskData ->
                                        when (intent.selectedText2) {
                                            "Day" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            "Year" -> {
                                                getCalendar(taskData.timer).get(Calendar.YEAR) == Calendar.getInstance()
                                                    .get(Calendar.YEAR)
                                            }

                                            "Week" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            "Month" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            else -> false
                                        }
                                    }
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        viewModelScope.launch {
                            getAllList().collect { list ->
                                uiState.update {
                                    it.copy(list = list.filter { taskData ->
                                        when (intent.selectedText2) {
                                            "Day" -> {
                                                getCalendar(taskData.timer).get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.DAY_OF_YEAR)
                                            }

                                            "Year" -> {
                                                getCalendar(taskData.timer).get(Calendar.YEAR) == Calendar.getInstance()
                                                    .get(Calendar.YEAR)
                                            }

                                            "Week" -> {
                                                getCalendar(taskData.timer).get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance()
                                                    .get(Calendar.WEEK_OF_YEAR)
                                            }

                                            "Month" -> {
                                                getCalendar(taskData.timer).get(Calendar.MONTH) == Calendar.getInstance()
                                                    .get(Calendar.MONTH)
                                            }

                                            else -> false
                                        }
                                    })
                                }
                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }

    private fun getAllList(): Flow<List<TaskData>> = callbackFlow {
        viewModelScope.launch {
            repository.getAllTasks().collect {
                trySend(it)
            }
        }
        awaitClose()
    }

    private fun getCompletedTasks(): Flow<List<TaskData>> = callbackFlow {
        viewModelScope.launch {
            repository.getCompletedTasks().collect {
                trySend(it)
            }
        }
        awaitClose()
    }

    private fun getInCompletedTasks(): Flow<List<TaskData>> = callbackFlow {
        viewModelScope.launch {
            repository.getInCompletedTasks().collect {
                trySend(it)
            }
        }
        awaitClose()
    }

    private fun deleteTask(task: TaskData) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    private fun updateTask(task: TaskData) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
}