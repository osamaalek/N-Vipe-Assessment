package com.osamaalek.n_vipeassessment.util

fun formatTravelTime(minutes: Long): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60
    return "$hours hour${if (hours != 1L) "s" else ""} and $remainingMinutes minute${if (remainingMinutes != 1L) "s" else ""}"
}