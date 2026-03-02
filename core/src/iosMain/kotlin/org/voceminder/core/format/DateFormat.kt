package org.voceminder.core.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.currentLocale
import platform.Foundation.localTimeZone
import org.voceminder.core.date.DatePattern

actual class DateFormat {
    actual fun format(date: LocalDate, pattern: DatePattern): String {
        return date.toNSDateComponents().format(pattern)
    }

    actual fun format(dateTime: LocalDateTime, pattern: DatePattern): String {
        return dateTime.toNSDateComponents().format(pattern)
    }

    private fun NSDateComponents.format(pattern: DatePattern): String {
        val dateFormatter = NSDateFormatter().apply {
            timeZone = NSTimeZone.localTimeZone
            dateFormat = pattern.value
            locale = NSLocale.currentLocale
        }
        val nsDate = NSCalendar.currentCalendar.dateFromComponents(this)

        return dateFormatter.stringFromDate(requireNotNull(nsDate) { "NsDate must be not null" })
    }
}

//actual fun LocalDateTime.formatToString(): String {
//    return "%02d/%02d/%04d %02d:%02d".format(dayOfMonth, monthNumber, year, hour, minute)
//}