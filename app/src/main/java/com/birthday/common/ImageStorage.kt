package com.birthday.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import java.io.File
import java.io.FileInputStream

object ImageStorageManager {
  fun saveToInternalStorage(context: Context, bitmapImage: Bitmap, imageFileName: String): Single<String> {
    context.openFileOutput(imageFileName, Context.MODE_PRIVATE).use { fos ->
      bitmapImage.compress(Bitmap.CompressFormat.PNG, 25, fos)
    }
    return Single.just(context.filesDir.absolutePath + "/" + imageFileName)
  }

  fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap {
    val directory = context.filesDir
    val file = File(directory, imageFileName)
    return BitmapFactory.decodeStream(FileInputStream(file))
  }

  fun deleteImageFromInternalStorage(context: Context, imageFileName: String): Boolean {
    val dir = context.filesDir
    val file = File(dir, imageFileName)
    return file.delete()
  }
}