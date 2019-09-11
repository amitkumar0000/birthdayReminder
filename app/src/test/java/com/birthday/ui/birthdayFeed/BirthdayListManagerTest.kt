package com.birthday.ui.birthdayFeed

import com.birthday.TestUtils
import com.birthday.TestUtils.getBirthdayList
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BirthdayListManagerTest{


  val dao = Mockito.mock(BirthdayDao::class.java)
  private val birthdayListManager = BirthdayListManager(dao)


  @Before
  fun setup(){
    whenever(dao.getAll()).thenReturn(Maybe.just(listOf(getBirthdayList())))
    whenever(dao.insert(getBirthdayList())).thenReturn(1L)
    whenever(dao.delete(1)).thenReturn(1)
    val list = getBirthdayList()
    whenever(dao.update("list/imagePath", list.name, TestUtils.getDate())).thenReturn(1)
    whenever(dao.updateDate(TestUtils.getDate(), list.name, list.dob)).thenReturn(1)
    whenever(dao.updateTime("10:00 am", list.name, list.dob)).thenReturn(1)
  }

  @Test
  fun test_loadAllItem(){
    val source = birthdayListManager.loadAllItem()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun test_insertInfo(){
    val listId = birthdayListManager.insertInfo(getBirthdayList())
    assert(listId == 1L)
  }

  @Test
  fun test_deleteItem(){
    val listId = birthdayListManager.deleteItem(1)
    assert(listId == 1)
  }

  @Test
  fun test_updateItem(){
    val list = getBirthdayList()
    val listId = birthdayListManager.updateItem("list/imagePath", list.name, TestUtils.getDate())
    assert(listId == 1)
  }

  @Test
  fun  test_updateDateItem(){
    val list = getBirthdayList()
    val listid = birthdayListManager.updateDateItem(TestUtils.getDate(), list.name, list.dob)
    assert(listid == 1)

  }

  @Test
  fun test_updateTimeItem(){
    val list = getBirthdayList()
    val listid = birthdayListManager.updateTimeItem("10:00 am", list.name, list.dob)
    assert(listid == 1)
  }

}