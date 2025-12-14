package com.example.proyect_final.utility

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun toDateString(date: LocalDate): String{
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return date.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun toDate(dateString: String): LocalDate{
    val date = LocalDate.parse(dateString.substring(0, 10))
    return LocalDate.parse(date.toString())
}

fun encodeImage(context: Context, uri: Uri?): String {
    if (uri == null) return ""

    val bytes = context.contentResolver.openInputStream(uri)?.readBytes() ?: return ""
    return Base64.encodeToString(bytes, Base64.DEFAULT)
}

fun base64ToImageBitmap(base64: String?): androidx.compose.ui.graphics.ImageBitmap? {
    if (base64 == null) return null
    val imageBytes = Base64.decode(base64, Base64.DEFAULT)
    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    return bitmap?.asImageBitmap()
}

fun isValidDni(dni: String): Boolean {
    val regex = Regex("^[0-9]{8}[A-Za-z]\$")
    if (!regex.matches(dni)) return false

    val letters = "TRWAGMYFPDXBNJZSQVHLCKE"
    val number = dni.substring(0, 8).toInt()
    val expectedLetter = letters[number % 23]

    return dni.last().uppercaseChar() == expectedLetter
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex(
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\$",
        RegexOption.IGNORE_CASE
    )
    return emailRegex.matches(email)
}

fun isValidPhone(phone: String): Boolean {
    return phone.length == 9 && phone.all { it.isDigit() }
}

fun isImageUnder1MB(context: Context, uri: Uri): Boolean {
    val cursor = context.contentResolver.query(
        uri,
        arrayOf(OpenableColumns.SIZE),
        null,
        null,
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
            val size = it.getLong(sizeIndex)
            return size <= 1 * 1024 * 1024 // 1MB
        }
    }
    return false
}


