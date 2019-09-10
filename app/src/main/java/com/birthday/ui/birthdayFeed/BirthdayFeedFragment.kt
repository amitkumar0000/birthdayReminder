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
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.birthday.common.DateUtils
import com.birthday.common.PrefenceManager
import com.birthday.common.Utils
import com.birthday.common.ui.ItemDivider
import com.birthday.ui.fragment.BaseNavigationFragment
import com.birthday.ui.fragment.BirthdayDetailsFragment
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.birthdayfeed_fragment.searchView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

private const val PAGE_LOADING_STATE = 0
private const val PAGE_ERROR_STATE = 1
private const val PAGE_CONTENT_STATE = 2
private const val DIALOG_PICKER = 100
private const val EMAIL = "email"

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

  var backData: List<BirthdayInfoModel>? = null

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    setupFBlogin()

    setupToolbar()

    birthdayList.apply {
      layoutManager = LinearLayoutManager(requireContext())
      adapter = birthdayController.adapter
      addItemDecoration(ItemDivider(requireContext()))
    }

    setupSwipeToDelete()

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
              backData = bInfoModelList
              infoContainer.displayedChild = PAGE_CONTENT_STATE
            }
            is BirthdayListUpdate.InsertSuccess -> {
              requestContent()
            }
            is BirthdayListUpdate.deleteSuccess -> {
              Completable.fromAction {
                var hashMap = PrefenceManager.loadHashMap(requireContext())
                hashMap.remove(it.name)
                PrefenceManager.saveHashMap(requireContext(), hashMap)
              }.subscribeOn(Schedulers.io())
                .subscribe()
              requestContent()
            }
          }
        }
    )

    setSearchView()

    requestContent()
  }

  private fun setSearchView() {

    disposable.add(fromview(searchView).debounce(300, TimeUnit.MILLISECONDS)
      .filter { text -> !text.isEmpty() && text.length >= 3 }
      .map { text -> text.toLowerCase().trim() }
      .distinctUntilChanged()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { text ->
        var filterdata = arrayListOf<BirthdayInfoModel>()
        backData?.forEach {
          if (it.profileName.contains(text, true))
            filterdata.add(it)
        }
        if (filterdata.size > 0)
          birthdayController.setData(filterdata)
      }
    )

    searchView.setOnCloseListener {
      birthdayController.setData(backData)
      subject.onNext("dummy")
      true
    }
  }

  var subject = PublishSubject.create<String>();

  private fun fromview(searchView: SearchView): Observable<String> {

    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        searchView.clearFocus()
        return false
      }

      override fun onQueryTextChange(newText: String): Boolean {
        subject.onNext(newText)
        return false
      }
    })
    return subject;
  }

  private fun setupSwipeToDelete() {
    var itemTouchHelperCallback =
      object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder,
          target: RecyclerView.ViewHolder
        ): Boolean {
          Timber.d("Item onMove")
          return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
          val content = birthdayController.currentData?.get(viewHolder.adapterPosition)

          Timber.d("Item Swiped ")
          content?.let { viewModel.deleteContent(it.id, it.profileName) }
        }
      }
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(birthdayList)
  }

  private fun transform(it: BirthdayList): BirthdayInfoModel {
    val imagePath = it.imagePath
    val profileName = it.name
    val dob: Date = it.dob
    val ct = Calendar.getInstance().time
    val monthRem = (ct.month - dob.month)
    val dayRem = ct.date - dob.date
    var age = Calendar.getInstance().time.year - dob.year
    if (monthRem > 0 || (monthRem == 0 && dayRem > 0)) {
      age += 1
    }
    val profileDetail: String = getString(
      R.string.birthdayDetail, age,
      DateFormat.format("dd", dob) as String,
      DateFormat.format("MMM", dob) as String
    )
    val remainingDay = getString(R.string.remainingday, DateUtils.getRemainingDays(dob, Calendar.getInstance().time))
    return BirthdayInfoModel(
      id =it.id,
      imagePath = imagePath,
      profileName = profileName,
      profileDetail = profileDetail,
      remainingDate = remainingDay,
      dob = it.dob,
      remainderDate = it.remainderDate,
      remainderTime = it.remainderTime,
      launchFunction = ::bdpLauncher
    )
  }


  private fun requestContent() {
    viewModel.loadContent()
  }

  private fun setupToolbar() {
    toolbar.apply {
      /*  navigationIcon = settingDrawable
          setNavigationOnClickListener {
          }*/
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

  private fun bdpLauncher(imagePath: String,
    name: String, details: String,
    dob: Date,
    remainderDate:Date,
    remainderTime:String

  ) {
    val fragment = BirthdayDetailsFragment.newInstance()
    var bundle = Bundle()
    bundle.putString(Utils.IMAGE_PATH, imagePath)
    bundle.putString(Utils.NAME, name)
    bundle.putString(Utils.DOB, SimpleDateFormat("dd MMMM yyyy").format(dob))
    bundle.putString(Utils.REMAINDER_DATE, SimpleDateFormat("dd/MM/yyyy").format(remainderDate))
    bundle.putString(Utils.REMAINDER_TIME, remainderTime)
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
