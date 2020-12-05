package com.enigmatix.deprocrastinator

import java.util.*

data class DateTime(
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int
)

fun prettyTimeString(date: Date?): String {
    if (date == null)
        return ""
    var str1 = date.hours.toString()
    if (str1.length == 1)
        str1 = "0$str1"
    var str2 = date.minutes.toString()
    if (str2.length == 1)
        str2 = "0$str2"
    return "${date.day}-${date.month+1}-${date.year+1900} $str1:$str2"
}