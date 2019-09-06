package com.birthday.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.birthday.ui.listener.NavigationManagerHolder
import timber.log.Timber

abstract class BaseNavigationFragment : Fragment() {

  lateinit var navigationManagerHolder: NavigationManagerHolder

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    navigationManagerHolder = activity as NavigationManagerHolder
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