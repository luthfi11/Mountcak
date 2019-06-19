package com.wysiwyg.mountcak.util

import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateUtils
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun datePicker(editText: EditText, context: Context, minDate: Long) {
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH)
        val year = c.get(Calendar.YEAR)

        val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener {
                _, years, monthOfYear, dayOfMonth ->

            c.timeInMillis = 0
            c.set(years, monthOfYear, dayOfMonth, 0, 0, 0)
            val chosenDate = c.time
            val local = Locale("in", "ID")
            val dateFormat = SimpleDateFormat("dd/MM/yy", local)
            val date = dateFormat.format(chosenDate)
            editText.setText(date)

        }, year, month, day)

        dpd.datePicker.minDate = minDate
        dpd.show()
    }

    fun dateFormat(date: String?, pattern: String): String {
        val locale = Locale("in", "ID")
        val format = SimpleDateFormat("dd/MM/yy", locale)
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())

        return sdf.format(format.parse(date))
    }

    fun dateToLong(date: String): Long {
        val locale = Locale("in", "ID")
        val format = SimpleDateFormat("dd/MM/yy", locale)
        val mDate = format.parse(date)
        return mDate.time
    }

    fun timeFormat(format: String, millis: Long): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun minAgo(millis: String): String {
        return DateUtils.getRelativeTimeSpanString(
            millis.toLong(),
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }

    fun dayAgo(millis: Long): String {
        return DateUtils.getRelativeTimeSpanString(
            millis,
            System.currentTimeMillis(),
            DateUtils.DAY_IN_MILLIS
        ).toString()
    }

    fun isYesterday(millis: Long): Boolean {
        return DateUtils.isToday(millis + DateUtils.DAY_IN_MILLIS)
    }
}