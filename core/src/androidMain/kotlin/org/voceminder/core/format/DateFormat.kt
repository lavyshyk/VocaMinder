package org.voceminder.core.format

import org.voceminder.core.date.DatePattern
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAccessor
import java.util.Locale

//actual class DateFormat {
//    actual fun format(date: LocalDate, pattern: DatePattern): String {
//        return date.toJavaLocalDate().format(pattern)
//    }
//
//    actual fun format(dateTime: LocalDateTime, pattern: DatePattern): String {
//        return dateTime.toJavaLocalDateTime().format(pattern)
//    }
//
//    private fun TemporalAccessor.format(pattern: DatePattern): String {
//        val formatter = DateTimeFormatter.ofPattern(pattern.value, Locale.getDefault())
//        return formatter.format(this)
//    }
//}


//actual fun LocalDateTime.formatToString(): String {
//    // Convert kotlinx-datetime LocalDateTime to java.time.LocalDateTime
//    val jvmTime = JvmLocalDateTime.of(year, monthNumber, dayOfMonth, hour, minute)
//    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
//    return jvmTime.format(formatter)
//}