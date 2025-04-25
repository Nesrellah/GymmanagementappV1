package com.example.gymmanagement.utils

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class ImagePicker(private val context: Context) {

    // Save image to internal storage
    fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            // Create a unique filename
            val fileName = "workout_image_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            // Copy the content to internal storage
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }

            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

// Composable function to pick an image
@Composable
fun rememberImagePicker(
    onImagePicked: (Uri) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent()
) { uri: Uri? ->
    uri?.let { onImagePicked(it) }
}
