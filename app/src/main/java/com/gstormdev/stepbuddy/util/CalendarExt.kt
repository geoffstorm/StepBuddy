package com.gstormdev.stepbuddy.util

import java.util.Calendar

fun Calendar.startOfDay(): Calendar {
    return this.apply {
        set(Calendar.HOUR, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun Calendar.endOfDay(): Calendar {
    return this.apply {
        set(Calendar.HOUR, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }
}