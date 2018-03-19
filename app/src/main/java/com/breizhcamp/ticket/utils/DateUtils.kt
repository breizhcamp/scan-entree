package com.breizhcamp.ticket.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat


@SuppressLint("SimpleDateFormat")
fun daysOfWeek(days: Array<String>?): String {

    var daysOfWeek = StringBuilder()

    days?.forEach { day: String? ->

        val dateFormat = SimpleDateFormat("yyyy-MM-dd").parse(day)

        daysOfWeek.append(SimpleDateFormat("EEEE").format(dateFormat))
        daysOfWeek.append(", ")
    }

    return daysOfWeek.toString().dropLast(2)
}