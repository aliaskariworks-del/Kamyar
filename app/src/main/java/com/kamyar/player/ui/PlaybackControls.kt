package com.kamyar.player.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Shared play / previous / next cluster used by both player surfaces. */
@Composable
fun PlaybackControls(
    isPlaying: Boolean,
    onPrevious: () -> Unit,
    onTogglePlayPause: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val sideIconSize = if (compact) 26.dp else 36.dp
    val mainButtonSize = if (compact) 46.dp else 64.dp
    val mainIconSize = if (compact) 26.dp else 34.dp

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(if (compact) 12.dp else 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPrevious) {
            Icon(
                imageVector = Icons.Filled.SkipPrevious,
                contentDescription = "آهنگ قبلی",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(sideIconSize)
            )
        }

        FilledIconButton(
            onClick = onTogglePlayPause,
            modifier = Modifier.size(mainButtonSize)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = if (isPlaying) "توقف" else "پخش",
                modifier = Modifier.size(mainIconSize)
            )
        }

        IconButton(onClick = onNext) {
            Icon(
                imageVector = Icons.Filled.SkipNext,
                contentDescription = "آهنگ بعدی",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(sideIconSize)
            )
        }
    }
}