package uz.gita.mytodo.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

const val DUE_DATE_FORMAT = "dd MMM.yyyy HH:mm"

fun formatCalendar(calendar: Calendar, dateTimeFormat: String? = DUE_DATE_FORMAT): String {
    val simpleDateFormat = SimpleDateFormat(dateTimeFormat, Locale.getDefault())
    return simpleDateFormat.format(calendar.time)
}

fun getCalendar(dateTime: String): Calendar {
    val simpleDateFormat = SimpleDateFormat(DUE_DATE_FORMAT, Locale.getDefault())
    val calendar = Calendar.getInstance()
    try {
        simpleDateFormat.parse(dateTime)?.let {
            calendar.time = it
        }
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return calendar
}