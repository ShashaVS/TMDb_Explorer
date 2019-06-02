package com.shashavs.tmdbexplorer.util

import android.util.Log
import java.lang.NullPointerException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateTimeUtil {

    companion object {
        private val TAG = "DateTimeUtil"
        private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        fun getDate(webPublicationDate: String?): Date? {
            try {
                return dateFormat.parse(webPublicationDate)
            } catch (e: ParseException) {
                Log.e(TAG, "getDate ParseException", e)
            } catch (e: NullPointerException) {
                Log.e(TAG, "getDate NullPointerException", e)
            }
            return null
        }

        fun isCurrentYear(date: Date) : Boolean {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val currentCalendar = Calendar.getInstance()
            return currentCalendar.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
        }
    }

}