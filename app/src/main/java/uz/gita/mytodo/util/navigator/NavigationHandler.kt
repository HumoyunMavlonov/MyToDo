package uz.gita.mytodo.util.navigator

import kotlinx.coroutines.flow.SharedFlow

interface NavigationHandler {
    val navigatorBuffer:SharedFlow<NavigationArg>
}