package uz.gita.mytodo.presentation.screen.add

import org.orbitmvi.orbit.ContainerHost
import uz.gita.mytodo.data.model.TaskData

interface AddContract {
    sealed interface Model:ContainerHost<UiState, SideEffect>{
        fun eventDispatcher(intent: Intent)
    }
    sealed interface UiState{
        object Welcome:UiState
    }
    sealed interface SideEffect{
        data class ShowToast(val message:String): SideEffect
    }
    sealed interface Intent{
        data class AddTask(val task: TaskData): Intent
        data class UpdateTask(val task: TaskData): Intent
        object Back: Intent
    }
}