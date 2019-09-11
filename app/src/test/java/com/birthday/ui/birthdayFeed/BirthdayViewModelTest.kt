package com.birthday.ui.birthdayFeed

import com.birthday.TestUtils.getBirthdayList
import com.demo.testing.rxjavaschedulers.RxSchedulerOverrides
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.junit.Rule

class BirthdayViewModelTest {

  val birthdayInteractor: BirthdayInteractor = Mockito.mock(BirthdayInteractor::class.java)

  val viewModel: BirthdayViewModel = BirthdayViewModel(birthdayInteractor)

  @get:Rule
  val rxScheduler = RxSchedulerOverrides()


  @Before
  fun setup() {

    whenever(birthdayInteractor.feedContent()).thenReturn(Single.just(listOf()))
    whenever(birthdayInteractor.saveContent(getBirthdayList())).thenReturn(Completable.complete())
    whenever(birthdayInteractor.deleteId(1)).thenReturn(Completable.complete())
  }

  @Test
  fun test_EmptyloadContent() {
    viewModel.loadContent()

    var s1 = viewModel.state().test()
    s1.assertValue{
      it.equals(BirthdayListUpdate.Error)
    }
  }

  @Test
  fun testSaveSuccess() {
    viewModel.saveContent(getBirthdayList())
    var source = viewModel.state().test()
    source.assertValue {
      it.equals(BirthdayListUpdate.InsertSuccess(getBirthdayList()))
    }
  }

  @Test
  fun test_deleteSuccess() {
    viewModel.deleteContent(1, "Name")
    var source = viewModel.state().test()
    source.assertValue {
      it.equals(BirthdayListUpdate.deleteSuccess("Name"))
    }
  }
}