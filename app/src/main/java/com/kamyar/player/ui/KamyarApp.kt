package com.kamyar.player.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kamyar.player.data.MediaStoreSource
import com.kamyar.player.data.Song
import com.kamyar.player.playback.PlayerViewModel

private val audioPermissions: Array<String> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.READ_MEDIA_AUDIO)
    } else {
        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

@Composable
fun KamyarApp() {
    val context = LocalContext.current
    val player: PlayerViewModel = viewModel()

    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var granted by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var reloadKey by remember { mutableStateOf(0) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        granted = result.values.any { it }
    }

    LaunchedEffect(Unit) {
        launcher.launch(audioPermissions)
        player.connect(context)
    }

    LaunchedEffect(granted, reloadKey) {
        if (granted) songs = MediaStoreSource.querySongs(context)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            !granted -> PermissionScreen(
                onRequest = { launcher.launch(audioPermissions) }
            )

            expanded && player.currentSong != null -> NowPlayingScreen(
                player = player,
                onCollapse = { expanded = false }
            )

            else -> LibraryScreen(
                songs = songs,
                player = player,
                onRefresh = { reloadKey++ },
                onOpenPlayer = { expanded = true }
            )
        }
    }
}

@Composable
private fun PermissionScreen(onRequest: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "کامیار",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "برای پیدا کردن آهنگ‌های روی گوشی، اجازه دسترسی به فایل‌های صوتی لازم است.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Button(
            onClick = onRequest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("اجازه دسترسی")
        }
    }
}
