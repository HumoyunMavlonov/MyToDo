package uz.gita.mytodo.util.navigator

import android.os.Parcel
import android.os.Parcelable
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavigationDispatcher @Inject constructor() : AppNavigator, NavigationHandler, Parcelable {
    override val navigatorBuffer = MutableSharedFlow<NavigationArg>()

    constructor(parcel: Parcel) : this() {
    }

    private suspend fun navigate(arg: NavigationArg) {
        navigatorBuffer.emit(arg)
    }

    override suspend fun backUntilRoot() = navigate { popUntilRoot() }

    override suspend fun backAll() = navigate { popAll() }

    override suspend fun navigateTo(screen: MyScreen) = navigate { push(screen) }

    override suspend fun replace(screen: MyScreen) = navigate { replace(screen) }
    override suspend fun replaceAll(screen: MyScreen) = navigate { replaceAll(screen) }
    override suspend fun back() = navigate { pop() }
    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NavigationDispatcher> {
        override fun createFromParcel(parcel: Parcel): NavigationDispatcher {
            return NavigationDispatcher(parcel)
        }

        override fun newArray(size: Int): Array<NavigationDispatcher?> {
            return arrayOfNulls(size)
        }
    }

}