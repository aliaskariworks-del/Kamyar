package com.kamyar.player.data

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.kamyar.player.util.orUnknown

/** A single audio file found on the device. */
data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val album: String,
    val durationMs: Long,
    val uri: Uri
)

/**
 * Reads the device's MediaStore audio table.
 *
 * Scoped to [MediaStore.Audio.Media.IS_MUSIC] so ringtones, notifications and
 * voice recordings do not show up in the library.
 */
object MediaStoreSource {

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.DURATION
    )

    fun querySongs(context: Context): List<Song> {
        val songs = mutableListOf<Song>()
        val collection = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        context.contentResolver.query(
            collection,
            projection,
            "${MediaStore.Audio.Media.IS_MUSIC} != 0",
            null,
            "${MediaStore.Audio.Media.TITLE} COLLATE NOCASE ASC"
        )?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idCol)
                songs += Song(
                    id = id,
                    title = cursor.getString(titleCol).orUnknown("بدون نام"),
                    artist = cursor.getString(artistCol).orUnknown("هنرمند ناشناس"),
                    album = cursor.getString(albumCol).orUnknown("آلبوم ناشناس"),
                    durationMs = cursor.getLong(durationCol),
                    uri = Uri.withAppendedPath(collection, id.toString())
                )
            }
        }
        return songs
    }
}
