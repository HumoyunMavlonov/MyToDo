package uz.gita.mytodo.presentation.screen.home

import kotlinx.coroutines.flow.StateFlow
import uz.gita.mytodo.data.model.TaskData

interface HomeContract {

    interface Model {
        val uiState : StateFlow<UiState>
        fun eventDispatcher(intent: Intent)
    }

    sealed interface Intent {
        data class GetTasksList(val type: Int, val selectedText2: String):Intent
        data class UpdateTask(val task: TaskData) : Intent
        data class DeleteTask(val task: TaskData) : Intent
        data class OpenAddTaskScreen(val task: TaskData) : Intent
    }

    data class UiState(
        var list: List<TaskData> = emptyList()
    )

    interface SideEffect {
        data class ShowDialog(val message: String) : SideEffect
    }

}