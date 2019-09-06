package com.birthday.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.birthday.ui.R
import com.birthday.ui.listener.NavigationManagerHolder
import com.birthday.ui.manager.NavigationFragmentManager
import timber.log.Timber

abstract class BaseActivity : AppCompatActivity(), NavigationManagerHolder {

  val navFragmentManager by lazy { NavigationFragmentManager(R.id.container, supportFragmentManager) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    Timber.tag(javaClass.simpleName)
    Timber.d("onCreate on ${javaClass.simpleName}")
  }

  override fun getNavigationFragmentManager(): NavigationFragmentManager {
    return navFragmentManager
  }

  override fun onRestart() {
    super.onRestart()
    Timber.tag(javaClass.simpleName)
    Timber.d("onRestart on ${javaClass.name}")
  }

  override fun onResume() {
    super.onResume()
    Timber.tag(javaClass.simpleName)
    Timber.d("onResume on ${javaClass.name}")
  }

  override fun onStart() {
    super.onStart()
    Timber.tag(javaClass.simpleName)
    Timber.d("onStart on ${javaClass.simpleName}")
  }

  override fun onPause() {
    super.onPause()
    Timber.tag(javaClass.simpleName)
    Timber.d("onPause on ${javaClass.name}")
  }

  override fun onStop() {
    super.onStop()
    Timber.tag(javaClass.simpleName)
    Timber.d("onStop on ${javaClass.name}")
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.tag(javaClass.simpleName)
    Timber.d("onDestroy on ${javaClass.name}")
  }
}