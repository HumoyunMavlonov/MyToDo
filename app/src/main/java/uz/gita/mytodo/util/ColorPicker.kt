package uz.gita.mytodo.util

import androidx.compose.ui.graphics.Color
import uz.gita.mytodo.ui.theme.*
import java.util.*

fun getColorFromCalendar(calendar: Calendar, theme: Boolean): Color {
    return if (calendar.get(Calendar.HOUR_OF_DAY) == Calendar.getInstance()
            .get(Calendar.HOUR_OF_DAY) && calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
            .get(Calendar.DAY_OF_YEAR)
    ) {

        if (theme) MyDarkCard1 else MyLightCard1

    } else if (calendar.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance()
            .get(Calendar.DAY_OF_YEAR)
    ) {

        if (theme) MyDarkCard2 else MyLightCard2

    } else if (calendar.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance()
            .get(Calendar.WEEK_OF_YEAR)
    ) {

        if (theme) MyDarkCard3 else MyLightCard3

    } else if (calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)) {
        if (theme) MyDarkCard4 else MyLightCard4
    } else {
        if (theme) MyDarkCard5 else MyLightCard5
    }
}

