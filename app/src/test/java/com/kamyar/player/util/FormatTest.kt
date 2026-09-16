package com.kamyar.player.util

import org.junit.Assert.assertEquals
import org.junit.Test

class FormatTest {

    @Test
    fun `zero and negative durations show 0_00`() {
        assertEquals("0:00", formatTime(0L))
        assertEquals("0:00", formatTime(-500L))
    }

    @Test
    fun `seconds and minutes are padded`() {
        assertEquals("0:05", formatTime(5_000L))
        assertEquals("0:59", formatTime(59_400L))
        assertEquals("3:07", formatTime(187_000L))
    }

    @Test
    fun `long tracks include hours`() {
        assertEquals("1:00:00", formatTime(3_600_000L))
        assertEquals("2:03:04", formatTime(7_384_000L))
    }

    @Test
    fun `unknown tags are replaced`() {
        assertEquals("هنرمند ناشناس", "<unknown>".orUnknown("هنرمند ناشناس"))
        assertEquals("هنرمند ناشناس", "   ".orUnknown("هنرمند ناشناس"))
        assertEquals("Ali", " Ali ".orUnknown("هنرمند ناشناس"))
    }
}
