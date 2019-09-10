package com.birthday.common

import com.google.gson.Gson
import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.reflect.TypeToken

object PrefenceManager {

  private const val BIRTHDAY_SHAREDPREF = "birthdayShared"
  private const val Birthday_hashMap = "Birthday_hashMap"

  fun saveHashMap(context: Context, hashMap: HashMap<String, Long>) {
    //save in shared prefs
    val prefs = context.getSharedPreferences(BIRTHDAY_SHAREDPREF, MODE_PRIVATE)
    prefs.edit().putString(Birthday_hashMap, Gson().toJson(hashMap)).apply()
  }

  fun loadHashMap(context: Context): HashMap<String, Long> {
    //get from shared prefs
    var defaultHashMap:HashMap<String,Long> = HashMap()

    val prefs = context.getSharedPreferences(BIRTHDAY_SHAREDPREF, MODE_PRIVATE)
    val storedHashMapString = prefs.getString(Birthday_hashMap, Gson().toJson(defaultHashMap))
    return Gson().fromJson(storedHashMapString, object : TypeToken<HashMap<String, Long>>() {}.type)
  }
}