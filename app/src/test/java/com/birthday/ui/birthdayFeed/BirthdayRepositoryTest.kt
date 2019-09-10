package com.birthday.ui.birthdayFeed

import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.Calendar

class BirthdayRepositoryTest {

  var manager: BirthdayListManager = Mockito.mock(BirthdayListManager::class.java)

  var birthdayRepository: BirthdayRepository = BirthdayRepository(manager)

  @Before
  fun setup() {
    whenever(manager.loadAllItem())
      .thenReturn(Single.just(listOf(getBirthdayList())))

    whenever(manager.insertInfo(getBirthdayList())).thenReturn(1)
    whenever(manager.deleteItem(1)).thenReturn(1)
    whenever(manager.updateItem("/dev/adb/image111/png", "Android Testing", Calendar.getInstance().time)).thenReturn(1)
    whenever(
      manager.updateDateItem(
        Calendar.getInstance().time,
        "Android Testing",
        Calendar.getInstance().time
      )
    ).thenReturn(1)
    whenever(
      manager.updateTimeItem("10:00 am", "Android Testing", Calendar.getInstance().time)
    ).thenReturn(1)
  }

  @Test
  fun test_getAllItem() {
    val source = birthdayRepository.getAllItem()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertValue {
      it[0].name == getBirthdayList().name
    }
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun test_insertItem() {
    val source = birthdayRepository.insertItem(getBirthdayList())
    val testObserver = TestObserver<Int>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun test_deleteItem() {
    val source = birthdayRepository.deleteItem(1)
    val testObserver = TestObserver<Int>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun test_updateItem() {
    val source = birthdayRepository.updateItem("/dev/adb/image111/png", "Android Testing", Calendar.getInstance().time)
    val testObserver = TestObserver<Int>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun test_updateDateItem() {
    val source = birthdayRepository
      .updateTimeItem("10:00 am", "Android Testing", Calendar.getInstance().time)
    val testObserver = TestObserver<Int>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun test_updateTimeItem() {
    val source = birthdayRepository
      .updateDateItem(
        Calendar.getInstance().time,
        "Android Testing",
        Calendar.getInstance().time
      )
    val testObserver = TestObserver<Int>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  private fun getBirthdayList(): BirthdayList {
    return BirthdayList(
      name = "Android Testing",
      dob = Calendar.getInstance().time,
      imagePath = "/dev/adb/image111/png",
      remainderTime = "10:00 PM",
      remainderDate = Calendar.getInstance().time
    )
  }
}
