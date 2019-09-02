package com.birthday.ui.birthdayFeed

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.birthday.BirthdayApplication
import com.birthday.ui.R
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.birthdayfeed_fragment.birthday_container as infoContainer
import kotlinx.android.synthetic.main.birthdayfeed_fragment.birthday_info_list as birthdayList
import kotlinx.android.synthetic.main.birthdayfeed_fragment.toolbar
import java.util.Calendar
import javax.inject.Inject
import android.text.format.DateFormat
import com.birthday.common.Utility
import com.birthday.common.ui.ItemDivider
import com.birthday.ui.fragment.BaseNavigationFragment
import com.birthday.ui.fragment.BirthdayDetailsFragment
import io.reactivex.disposables.CompositeDisposable

private const val PAGE_LOADING_STATE = 0
private const val PAGE_ERROR_STATE = 1
private const val PAGE_CONTENT_STATE = 2
private const val DIALOG_PICKER = 100

class BirthdayFeedFragment : BaseNavigationFragment() {

  private val settingDrawable by lazy { resources.getDrawable(R.drawable.ic_settings_black_24dp, null) }

  private val birthdayController by lazy { BirthdayController() }

  @Inject
  lateinit var viewModelFactory: BirthdayViewModelFactory

  private val viewModel by lazy { viewModelFactory.getInstance(this) }

  private val disposable by lazy { CompositeDisposable() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    BirthdayApplication.component.inject(this)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.birthdayfeed_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    setupToolbar()

    birthdayList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = birthdayController.adapter
      addItemDecoration(ItemDivider(requireContext()))
    }

    disposable.add(
      viewModel.state()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          when (it) {
            is BirthdayListUpdate.Loading -> {
              infoContainer.displayedChild = PAGE_LOADING_STATE
            }
            is BirthdayListUpdate.Error -> {
              infoContainer.displayedChild = PAGE_ERROR_STATE
            }
            is BirthdayListUpdate.Content -> {
              val bInfoModelList = ArrayList<BirthdayInfoModel>()
              it.list?.forEach {
                bInfoModelList.add(transform(it))
              }
              birthdayController.setData(bInfoModelList)
              infoContainer.displayedChild = PAGE_CONTENT_STATE
            }
          }
        }
    )

    requestContent()
  }

  private fun transform(it: BirthdayList): BirthdayInfoModel {
    val imagePath = it.imagePath
    val profileName = it.name
    val dob = it.dob

    val difference = Calendar.getInstance().time.time - dob.time
    val secondsInMilli: Long = 1000
    val minutesInMilli = secondsInMilli * 60
    val hoursInMilli = minutesInMilli * 60
    val daysInMilli = hoursInMilli * 24
    val yearInMilli = daysInMilli * 365
    val remainingDays = (difference / daysInMilli) / 1000  //TODO fix it
    val profileDetail: String = getString(
      R.string.birthdayDetail, (difference / yearInMilli) + 1,
      DateFormat.format("dd", dob) as String,
      DateFormat.format("MMM", dob) as String
    )
    val remainingDay = getString(R.string.remainingday, remainingDays)
    return BirthdayInfoModel(
      imagePath,
      profileName,
      profileDetail,
      remainingDay,
      ::bdpLauncher
    )
  }

  private fun requestContent() {
    viewModel.loadContent()
  }

  private fun setupToolbar() {
    toolbar.apply {
      navigationIcon = settingDrawable
      setNavigationOnClickListener {
        Log.d("TAG", " Setting clikced")
      }
      menu.clear()
      inflateMenu(R.menu.birthday_menu)

      setOnMenuItemClickListener { item ->
        onOptionsItemSelected(item)
      }
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    when (requestCode) {
      DIALOG_PICKER -> {
        requestContent()
      }
    }
  }

  private fun bdpLauncher(imagePath:String,name:String,dob:String){
    val fragment = BirthdayDetailsFragment.newInstance()
    var bundle = Bundle()
    bundle.putString(Utility.IMAGE_PATH,imagePath)
    bundle.putString(Utility.NAME,name)
    bundle.putString(Utility.DOB,dob)
    fragment.arguments = bundle
    navigationManagerHolder.getNavigationFragmentManager().let {
      it.safeAddBackStack(fragment)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    disposable.dispose()
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.add -> {
        val birthdayAddFragment = BirthdayAddFragment()
        val ft = fragmentManager?.beginTransaction()
        ft?.addToBackStack(null)
        birthdayAddFragment.setTargetFragment(this, DIALOG_PICKER);
        birthdayAddFragment.show(ft, null)
        Log.d("TAG", " Add menu Item clikced")
        return true
      }
      else ->
        super.onOptionsItemSelected(item)
    }
  }

  companion object {
    @JvmStatic
    fun newInstance() =
      BirthdayFeedFragment()
  }
}
