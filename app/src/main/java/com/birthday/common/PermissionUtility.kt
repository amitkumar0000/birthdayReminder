package com.birthday.common

import android.Manifest
import android.os.Build
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

object PermissionUtility {
  const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  fun checkPermission(fragment: Fragment, context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.READ_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        fragment.requestPermissions(
          arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
          MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
        )
        return false
      } else {
        return true
      }
    } else {
      return true
    }
  }
}