package com.sea.library.common.helper

import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.InputStream
import java.io.OutputStream

private const val TAG = "MediaStoreHelper"

private class OutputFileTaker(var file: File ? = null)

fun File.saveImageToAlbum(context: Context, fileName: String, relativePath: String ? = null): Uri ? {
    val exists = exists()
    val canRead = canRead()
    if (exists && canRead) {
        return inputStream().use { fis ->
            fis.saveImageToAlbum(context, fileName, relativePath)
        }
    }
    return null
}

fun InputStream.saveImageToAlbum(context: Context, fileName: String, relativePath: String ? = null): Uri ? {
    val resolver = context.contentResolver
    val outputFile = OutputFileTaker()
    val imageUri = resolver.insert(fileName, relativePath, Environment.DIRECTORY_PICTURES, outputFile)
    if (imageUri == null) {
        Log.w(TAG, "insert: error: uri == null")
        return null
    }
    val outputStream = imageUri.outputStream(resolver) ?: return null
    outputStream.use { output ->
        this.use { input ->
            input.copyTo(output)
            imageUri.update(context, resolver, outputFile.file)
        }
    }
    return imageUri
}

/**
 * 插入图片到媒体库
 */
@Suppress("DEPRECATION", "SimplifyBooleanWithConstants")
private fun ContentResolver.insert(fileName: String, relativePath: String ? = null, directory: String, outputFileTaker: OutputFileTaker ? = null): Uri ? {
    // 图片信息
    val contentValues = ContentValues().apply {
        val mimeType = fileName.getPictureMimeType()
        if (mimeType != null) {
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
        }
        val date = System.currentTimeMillis() / 1000
        put(MediaStore.Images.Media.DATE_ADDED, date)
        put(MediaStore.Images.Media.DATE_MODIFIED, date)
    }
    // 保存的位置
    val insertUri: Uri
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val path = if (relativePath != null) "$directory/$relativePath" else directory
        contentValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.RELATIVE_PATH, path)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        insertUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        val parent = Environment.getExternalStoragePublicDirectory(directory)
        val saveDir = if (relativePath != null) File(parent, relativePath) else parent
        if (saveDir.exists() == false && saveDir.mkdirs() == false) {
            Log.e(TAG, "save error: can't create Pictures directory")
            return null
        }
        // 文件路径查重, 重复的话在文件名后拼接数字
        var imageFile = File(saveDir, fileName)
        val fileNameWithoutExtension = imageFile.nameWithoutExtension
        val fileExtension = imageFile.extension
        var queryUri = queryMediaImageByBelow28(imageFile.absolutePath)
        var suffix = 1
        while (queryUri != null) {
            suffix++
            val newName = "$fileNameWithoutExtension($suffix).$fileExtension"
            imageFile = File(saveDir, newName)
            queryUri = queryMediaImageByBelow28(imageFile.absolutePath)
        }
        contentValues.apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.name)
            val imagePath = imageFile.absolutePath
            Log.e(TAG, "save file: $imagePath")
            put(MediaStore.Images.Media.DATA, imagePath)
        }
        // 回传文件路径, 用于设置文件大小
        if (outputFileTaker != null) {
            outputFileTaker.file = imageFile
        }
        insertUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    return insert(insertUri, contentValues)
}

@Suppress("DEPRECATION")
private fun ContentResolver.queryMediaImageByBelow28(imagePath: String): Uri ? {
    val imageFile = File(imagePath)
    val exists = imageFile.exists()
    val canRead = imageFile.canRead()
    if (canRead && exists) {
        Log.e(TAG, "image path: $imagePath exists")
        return Uri.fromFile(imageFile)
    }
    // 保存的位置
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val cursor = query(uri, arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA), "${MediaStore.Images.Media.DATA} == ?", arrayOf(imagePath), null)
    if (cursor != null) {
        cursor.use {
            val next = cursor.moveToNext()
            while (next) {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val id = it.getLong(idColumn)
                val existsUri = ContentUris.withAppendedId(uri, id)
                Log.e(TAG, "path: $imagePath exists uri: $existsUri")
                return existsUri
            }
        }
    } else {
        Log.e(TAG, "path error: cursor is null")
    }
    return null
}

@Suppress("DEPRECATION")
private fun Uri.update(context: Context, resolver: ContentResolver, outputFile: File ? = null) {
    val contentValues = ContentValues()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        if (outputFile != null) {
            val length = outputFile.length()
            contentValues.put(MediaStore.Images.Media.SIZE, length)
        }
        resolver.update(this, contentValues, null, null)
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, this)
        context.sendBroadcast(intent)
    } else {
        contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
        resolver.update(this, contentValues, null, null)
    }
}

private fun Uri.outputStream(resolver: ContentResolver): OutputStream ? {
    return try {
        resolver.openOutputStream(this)
    } catch (e: Exception) {
        Log.e(TAG, "open stream error: $e")
        null
    }
}

private fun String.getPictureMimeType(): String ? {
    val fileName = lowercase()
    return when {
        fileName.endsWith(".png") -> "image/png"
        fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") -> "image/jpeg"
        fileName.endsWith(".webp") -> "image/webp"
        fileName.endsWith(".gif") -> "image/gif"
        else -> null
    }
}