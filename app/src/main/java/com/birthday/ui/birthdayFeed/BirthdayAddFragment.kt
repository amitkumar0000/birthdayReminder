package com.birthday.ui.birthdayFeed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.birthday.ui.R
import kotlinx.android.synthetic.main.birthday_add_fragment.addDone
import kotlinx.android.synthetic.main.birthday_add_fragment.profile_image as profileImage
import androidx.appcompat.app.AlertDialog
import com.birthday.common.PermissionUtility
import android.provider.MediaStore
import android.content.Intent
import android.app.Activity
import android.graphics.Bitmap
import java.io.IOException
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.core.app.ActivityCompat
import com.birthday.BirthdayApplication
import com.birthday.common.ImageStorageManager
import com.birthday.common.ImageUtils
import com.birthday.common.ImageUtils.userChoosenTask
import com.birthday.common.PickerUtils
import com.birthday.common.REQUEST_CAMERA
import com.birthday.common.SELECT_FILE
import com.birthday.scheduler.AlarmManagerScheduler
import com.birthday.scheduler.BirthdayWorkManager
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.birthday_add_fragment.dob_input
import kotlinx.android.synthetic.main.birthday_add_fragment.nameInput
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val RECEIPT_REGEX = "\\d{4}-\\d{4}-\\d{4}-\\d{4}-\\d"

class BirthdayAddFragment : DialogFragment()
  , ActivityCompat.OnRequestPermissionsResultCallback {

  @Inject
  lateinit var viewModelFactory: BirthdayAddViewModelFactory

  @Inject
  lateinit var alarmManagerScheduler: AlarmManagerScheduler

  private val viewModel by lazy { viewModelFactory.getInstance(this) }

  private lateinit var imagePath:String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    BirthdayApplication.component.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.birthday_add_fragment, container, false)
  }

  fun getURLForResource ( resourceId:Int)
    =  Uri.parse("android.resource://" + requireContext().packageName + "/" + resourceId).toString();


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    imagePath  =  getURLForResource(R.drawable.profile_icon)
    addDone.setOnClickListener {
      val profileName = nameInput.text?.toString()
      var dob: Date? = null

      if (dob_input.text?.toString()?.matches(Regex(".*[dmyDMY].*")) == false) {
        dob = SimpleDateFormat("dd/MM/yyyy").parse(dob_input.text.toString())
      }
      if (!profileName.isNullOrEmpty()  && dob != null) {
        viewModel.saveContent(BirthdayList(name = profileName, dob = dob, imagePath = imagePath))
      } else {
        dialog.dismiss()
        Toast.makeText(requireContext(),getString(R.string.notSaved),Toast.LENGTH_SHORT).show()
      }
    }

    viewModel.state()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe {
        when (it) {
          is BirthdayDBUpdate.InsertSuccess -> {

            BirthdayWorkManager().startOneTimeWork(it.item.name,it.item.dob.time)
              .subscribeOn(Schedulers.io())
              .subscribe()

            Toast.makeText(requireContext(), "Birthday Added", Toast.LENGTH_SHORT).show()
            targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, null);
            dialog.dismiss();
          }
        }
      }

    profileImage.setOnClickListener {
      ImageUtils.selectImage(fragment = this@BirthdayAddFragment,context = requireContext())
    }

    RxView.clicks(dob_input)
      .debounce(300,TimeUnit.MILLISECONDS)
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        PickerUtils.showDatePicker(requireContext(),::datePickerText)
      },{
        Timber.d("Error $it")
      })

  }

  private fun datePickerText(text:String){
    dob_input.setText(text)
  }

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    when (requestCode) {
      PermissionUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE ->
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          if (ImageUtils.userChoosenTask.equals("Take Photo"))
            ImageUtils.cameraIntent(this@BirthdayAddFragment);
          else if (userChoosenTask.equals("Choose from Library"))
            ImageUtils.galleryIntent(this@BirthdayAddFragment);
        } else {
          Toast.makeText(requireContext(), "Permission is denied", Toast.LENGTH_SHORT).show()
        }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == SELECT_FILE)
        onSelectFromGalleryResult(data)
      else if (requestCode == REQUEST_CAMERA)
        onCaptureImageResult(data)
    }
  }

  private fun onSelectFromGalleryResult(data: Intent?) {
    var bm: Bitmap? = null
    if (data != null) {
      try {
        val uri = data.data
        bm = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
        saveImageToInternalStorage(bm, nameInput.text.toString() + System.currentTimeMillis())
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
  }

  private fun saveImageToInternalStorage(bm: Bitmap, fileName: String) {
    ImageStorageManager.saveToInternalStorage(requireContext(), bm, fileName)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe({
        imagePath = it
        Glide.with(requireContext()).load(it).into(profileImage)
      }, {
        Log.d("TAG", it.toString())
      })
  }

  private fun onCaptureImageResult(data: Intent?) {
    val thumbnail = data?.extras?.get("data") as Bitmap?
    thumbnail?.let {
      saveImageToInternalStorage(thumbnail, nameInput.text.toString() + System.currentTimeMillis())
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      BirthdayAddFragment()
  }
}
