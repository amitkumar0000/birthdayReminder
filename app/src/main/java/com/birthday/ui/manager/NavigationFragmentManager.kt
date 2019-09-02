package com.birthday.ui.manager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class NavigationFragmentManager(private val containerId:Int, private val fragmentManager:FragmentManager){

  fun addAsBaseFragment(fragment: Fragment){
    addAsBaseFragment(fragment,getDefaultFragmentTag(fragment))
  }

  fun addAsBaseFragment(fragment:Fragment,tag:String){
    fragmentManager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE)
    fragmentManager.beginTransaction().replace(containerId,fragment,tag).commit()
    fragmentManager.executePendingTransactions()
  }

  private fun getDefaultFragmentTag(fragment: Fragment) =
    (fragment as Object).javaClass.name

}