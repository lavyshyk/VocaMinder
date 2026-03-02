package org.voceminder.core.extension

import org.voceminder.core.format.DecimalFormat

fun Double.format(): String {
    return DecimalFormat().format(
        double = this,
        minFractionDigits = 0,
        maxFractionDigits = 2
    )
}