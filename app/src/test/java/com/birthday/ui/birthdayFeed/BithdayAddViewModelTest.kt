package com.birthday.ui.birthdayFeed

import com.birthday.ui.birthdayFeed.TestUtils.getBirthdayList
import com.demo.testing.rxjavaschedulers.RxSchedulerOverrides
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class BithdayAddViewModelTest{

  val birthdayInteractor: BirthdayInteractor = Mockito.mock(BirthdayInteractor::class.java)

  val viewModel = BithdayAddViewModel(birthdayInteractor)

  @get:Rule
  val rxSchedulerOverrides = RxSchedulerOverrides()

  @Before
  fun setup(){
    whenever(birthdayInteractor.saveContent(getBirthdayList())).thenReturn(Completable.complete())
  }

  @Test
  fun testSaveContentSuccess(){
    viewModel.saveContent(getBirthdayList())
    val source = viewModel.state().test()
    source.assertValue{
      it.equals(BirthdayDBUpdate.InsertSuccess(getBirthdayList()))
    }
  }

}