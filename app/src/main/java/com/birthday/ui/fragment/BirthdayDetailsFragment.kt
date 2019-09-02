package com.birthday.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import com.birthday.common.Utility

import com.birthday.ui.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_birthday_details.bdpToolbar
import kotlinx.android.synthetic.main.fragment_birthday_details.profileDob
import kotlinx.android.synthetic.main.fragment_birthday_details.profileImage
import kotlinx.android.synthetic.main.fragment_birthday_details.profileName

class BirthdayDetailsFragment : BaseNavigationFragment() {

  val backButton by lazy { resources.getDrawable(R.drawable.back,null) }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.fragment_birthday_details, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setToolbar()
    val bundle = arguments
    bundle?.let {
      Glide.with(requireContext()).load(bundle.getString(Utility.IMAGE_PATH)).into(profileImage)
      profileName.text=bundle.getString(Utility.NAME)
      profileDob.text=bundle.getString(Utility.DOB)
    }
  }

  private fun setToolbar() {
    bdpToolbar.apply {
      navigationIcon = backButton
      title = getString(R.string.birthdayTitle)
      setTitleTextColor(getColor(requireContext(),R.color.white))
      setNavigationOnClickListener {
        fragmentManager?.popBackStack()
      }
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      BirthdayDetailsFragment()
  }
}
