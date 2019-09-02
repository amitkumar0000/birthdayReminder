package com.birthday.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.birthday.ui.R
import com.birthday.ui.listener.NavigationManagerHolder
import com.birthday.ui.manager.NavigationFragmentManager

abstract class BaseActivity : AppCompatActivity(), NavigationManagerHolder{

  val navFragmentManager by lazy { NavigationFragmentManager(R.id.container,supportFragmentManager) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun getNavigationFragmentManager(): NavigationFragmentManager {
    return navFragmentManager
  }

}