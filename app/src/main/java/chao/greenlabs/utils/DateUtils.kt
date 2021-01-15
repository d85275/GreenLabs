package chao.greenlabs.utils

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        return format.format(date)
    }

    fun onDateChanged(year: Int, month: Int, dayOfMonth: Int): String {
        val currentDate = StringBuilder()
        currentDate.append(year).append("-").append(month + 1).append("-").append(dayOfMonth)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(format.parse(currentDate.toString())!!)
    }
}