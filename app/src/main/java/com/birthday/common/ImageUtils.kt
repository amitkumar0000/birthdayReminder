package com.birthday.common

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment

const val REQUEST_CAMERA = 1
const val SELECT_FILE = 2

object ImageUtils {
  var userChoosenTask: String = ""

  fun selectImage(fragment: Fragment, context: Context) {
    val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
    val builder = AlertDialog.Builder(context)
    builder.setTitle("Add Photo!")
    builder.setItems(items) { dialog, item ->
      val result = PermissionUtility.checkPermission(fragment)
      if (items[item] == "Take Photo") {
        userChoosenTask = "Take Photo"
        if (result)
          cameraIntent(fragment)
      } else if (items[item] == "Choose from Library") {
        userChoosenTask = "Choose from Library"
        if (result)
          galleryIntent(fragment)
      } else if (items[item] == "Cancel") {
        dialog.dismiss()
      }
    }
    builder.show()
  }

  fun cameraIntent(fragment: Fragment) {
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    fragment.startActivityForResult(intent, REQUEST_CAMERA)
  }

  fun galleryIntent(fragment: Fragment) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_GET_CONTENT//
    fragment.startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
  }
}