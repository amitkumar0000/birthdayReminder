package com.birthday.ui.fragment

import com.birthday.ui.birthdayFeed.BirthdayDBUpdate
import com.birthday.ui.birthdayFeed.BirthdayInteractor
import com.birthday.ui.birthdayFeed.TestUtils
import com.demo.testing.rxjavaschedulers.RxSchedulerOverrides
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class BirthdayDetailsViewModelTest {
  val birthdayInteractor: BirthdayInteractor = Mockito.mock(BirthdayInteractor::class.java)

  val viewModel = BirthdayDetailsViewModel(birthdayInteractor)

  @get:Rule
  val rxSchedulerOverrides = RxSchedulerOverrides()

  @Before
  fun setup() {

    whenever(
      birthdayInteractor.updateContent(
        "imagePath",
        "testname",
        TestUtils.getDate()
      )
    ).thenReturn(Completable.complete())

    whenever(
      birthdayInteractor.updateRemainderTimeContent(
        TestUtils.getDate(),
        "testname",
        TestUtils.getDate()
      )
    ).thenReturn(Completable.complete())

    whenever(
      birthdayInteractor.updateNotificationTimeContent(
        "10:00 AM", "tesname", TestUtils.getDate()
      )
    ).thenReturn(Completable.complete())
  }

  @Test
  fun testupdateContent() {
    viewModel.updateContent(
      "imagePath",
      "testname",
      TestUtils.getDate()
    )

    val source = viewModel.state().test()
    source.assertValue{
      it.equals(BirthdayDBUpdate.updateSuccess)
    }
  }

  @Test
  fun testupdateRemainderTimeContent() {
    viewModel.updateRemainderTimeContent(
      TestUtils.getDate(),
      "testname",
      TestUtils.getDate()
    )
    val source = viewModel.state().test()
    source.assertValue{
      it.equals(BirthdayDBUpdate.updateSuccess)
    }
  }

  @Test
  fun testupdateNotificationTimeContent() {
    viewModel.updateNotificationTimeContent(
      "10:00 AM", "tesname", TestUtils.getDate()
    )
    val source = viewModel.state().test()
    source.assertValue{
      it.equals(BirthdayDBUpdate.updateSuccess)
    }
  }
}