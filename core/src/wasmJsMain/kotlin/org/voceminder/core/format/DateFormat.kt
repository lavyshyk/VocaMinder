package org.voceminder.core.format

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.voceminder.core.date.DatePattern

actual class DateFormat {
    actual fun format(date: LocalDate, pattern: DatePattern): String {
      return  "not emplemented"
    }

    actual fun format(dateTime: LocalDateTime, pattern: DatePattern): String {
        return  "not emplemented"
    }

}