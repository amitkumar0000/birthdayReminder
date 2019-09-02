package com.birthday.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.birthday.ui.listener.NavigationManagerHolder

abstract class BaseNavigationFragment : Fragment() {

  lateinit var navigationManagerHolder: NavigationManagerHolder

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    navigationManagerHolder = activity as NavigationManagerHolder
  }

}