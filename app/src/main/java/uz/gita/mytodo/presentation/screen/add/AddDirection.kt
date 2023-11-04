package uz.gita.mytodo.presentation.screen.add

import uz.gita.mytodo.util.navigator.AppNavigator
import javax.inject.Inject

interface AddDirection {
    suspend fun back()
}

class AddDirectionImpl @Inject constructor(
    private val navigator: AppNavigator
):AddDirection {
    override suspend fun back() {
        navigator.back()
    }

}