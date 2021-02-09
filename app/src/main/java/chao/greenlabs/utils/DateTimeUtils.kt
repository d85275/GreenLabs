package chao.greenlabs.utils

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
    private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val idFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private val monthFormat = SimpleDateFormat(
        "yyyy  MMM" +
                "", Locale.getDefault()
    )

    fun getCustomerId():String{
        val date = Calendar.getInstance().time
        return idFormat.format(date)
    }

    fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        return format.format(date)
    }

    fun getCurrentDate(date: String): Date? {
        return format.parse(date)
    }

    fun getDateString(year: Int, month: Int, dayOfMonth: Int): String {
        val currentDate = StringBuilder()
        currentDate.append(year).append("-").append(month + 1).append("-").append(dayOfMonth)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(format.parse(currentDate.toString())!!)
    }

    fun getDateString(date: Date): String {
        return format.format(date)
    }

    fun getMonthString(date: Date): String {
        return monthFormat.format(date)
    }

    fun getCurrentMonth(): String {
        val date = Calendar.getInstance().time
        return monthFormat.format(date)
    }

    fun getCurrentTime(): String {
        val date = Calendar.getInstance().time
        return SimpleDateFormat("HH : mm", Locale.getDefault()).format(date)
    }
}