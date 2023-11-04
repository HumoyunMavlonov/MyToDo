package uz.gita.mytodo.presentation.screen.home

import uz.gita.mytodo.data.model.TaskData
import uz.gita.mytodo.presentation.screen.add.AddScreen
import uz.gita.mytodo.util.navigator.AppNavigator
import javax.inject.Inject

interface HomeDirection {
    suspend fun navigateToAddScreen(taskData: TaskData)

}

class HomeDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
):HomeDirection {
    override suspend fun navigateToAddScreen(taskData: TaskData) {
        navigator.navigateTo(AddScreen(taskData))
    }

}