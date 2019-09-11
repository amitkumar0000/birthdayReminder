package com.birthday.ui.birthdayFeed

import com.birthday.TestUtils
import com.birthday.TestUtils.getBirthdayList
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class BirthdayInteractorTest{

  val repository: BirthdayRepository = Mockito.mock(BirthdayRepository::class.java)

  @Before
  fun setup(){
    whenever(repository.getAllItem()).thenReturn(Single.just(listOf(getBirthdayList())))
    whenever(repository.insertItem(getBirthdayList())).thenReturn(Completable.complete())
    whenever(repository.deleteItem(1)).thenReturn(Completable.complete())
    val list = getBirthdayList()
    whenever(repository.updateItem("list/imagePath", list.name, list.dob)).thenReturn(Completable.complete())
    whenever(repository.updateDateItem(TestUtils.getDate(), list.name, list.dob)).thenReturn(Completable.complete())
    whenever(repository.updateTimeItem("10:00 am", list.name, list.dob)).thenReturn(Completable.complete())
  }

  @Test
  fun  test_feedContent(){
    val source = repository.getAllItem().test()
    source.assertComplete()
    source.assertNoErrors()
  }

  @Test
  fun  test_saveContent(){
    val source = repository.insertItem(getBirthdayList()).test()
    source.awaitTerminalEvent()
    source.assertNoErrors()
    source.assertComplete()
  }

  @Test
  fun test_deleteId(){
    val source = repository.deleteItem(1).test()
    source.awaitTerminalEvent()
    source.assertNoErrors()
    source.assertComplete()
  }

  @Test
  fun test_updateContent(){
    val list = getBirthdayList()
    val source = repository.updateItem("list/imagePath", list.name, list.dob).test()
    source.assertNoErrors()
    source.assertComplete()
  }

  @Test
  fun test_updateRemainderTimeContent(){
    val list = getBirthdayList()
    val source = repository.updateDateItem(TestUtils.getDate(), list.name, list.dob).test()
    source.assertNoErrors()
    source.assertComplete()
  }

  @Test
  fun test_uodateNotificationTimeContent(){
    val list = getBirthdayList()
    val source = repository.updateTimeItem("10:00 am", list.name, list.dob).test()
    source.assertNoErrors()
    source.assertComplete()
  }

}