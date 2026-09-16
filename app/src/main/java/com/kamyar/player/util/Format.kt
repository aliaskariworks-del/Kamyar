package com.kamyar.player.util

/**
 * Formats milliseconds as m:ss, or h:mm:ss for tracks over an hour.
 * Kept free of Android imports so it is trivially unit-testable.
 */
fun formatTime(ms: Long): String {
    if (ms <= 0L) return "0:00"
    val totalSeconds = ms / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}

/**
 * MediaStore reports missing tags as "<unknown>"; replace those, and blank or
 * null values, with a label the user can actually read.
 */
fun String?.orUnknown(fallback: String): String {
    val trimmed = this?.trim().orEmpty()
    return if (trimmed.isEmpty() || trimmed == "<unknown>") fallback else trimmed
}
