package com.birthday.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.get
import com.birthday.common.Utility

import com.birthday.ui.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.birthday_profile_layout.bdpToolbar
import kotlinx.android.synthetic.main.birthday_profile_layout.profileDob
import kotlinx.android.synthetic.main.birthday_profile_layout.profileImage
import kotlinx.android.synthetic.main.birthday_profile_layout.profileName
import kotlinx.android.synthetic.main.birthday_share_layout.callShareId
import kotlinx.android.synthetic.main.birthday_share_layout.emailShareId
import kotlinx.android.synthetic.main.birthday_share_layout.shareId
import kotlinx.android.synthetic.main.birthday_share_layout.sharelayout
import kotlinx.android.synthetic.main.share_layout.share_text

class BirthdayDetailsFragment : BaseNavigationFragment() {

  val backButton by lazy { resources.getDrawable(R.drawable.back, null) }

  private val call by lazy { resources.getDrawable(R.drawable.call, null) }
  private val text by lazy { resources.getDrawable(R.drawable.text, null) }
  private val email by lazy { resources.getDrawable(R.drawable.mail, null) }
  private val share by lazy { resources.getDrawable(R.drawable.share, null) }


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
      profileName.text = bundle.getString(Utility.NAME)
      profileDob.text = bundle.getString(Utility.DOB)
    }

    setShareView()

  }



  fun setShareView(){
    sharelayout.get(0).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.text)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setImageDrawable(text)
        setBackgroundColor(getColor(requireContext(),android.R.color.holo_green_light))

      }
    }

    sharelayout.get(1).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.call)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setBackgroundColor(getColor(requireContext(),R.color.blue_light))
        setImageDrawable(call)
      }

    }

    sharelayout.get(2).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.email)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setImageDrawable(email)
        setBackgroundColor(getColor(requireContext(),android.R.color.holo_red_dark))
        alpha = 0.6F

      }

    }

    sharelayout.get(3).apply {
      findViewById<TextView>(R.id.share_text).text = getString(R.string.share)
      findViewById<CircleImageView>(R.id.share_image_container).apply {
        setImageDrawable(share)
        setBackgroundColor(getColor(requireContext(),android.R.color.holo_blue_dark))

      }
    }
  }

  private fun setToolbar() {
    bdpToolbar.apply {
      navigationIcon = backButton
      title = getString(R.string.birthdayTitle)
      setTitleTextColor(getColor(requireContext(), R.color.white))
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
