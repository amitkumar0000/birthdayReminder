package com.birthday.ui.birthdayFeed

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.junit.runner.RunWith
import androidx.test.runner.AndroidJUnit4
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Calendar

@RunWith(AndroidJUnit4::class)
class BirthdayDatabaseTest {

  lateinit var db: BirthdayDatabase

  @Before
  @Throws(Exception::class)
  fun setup() {
    db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), BirthdayDatabase::class.java)
      .build()
  }

  @Test
  fun getAll_should_return_empty_row_sucess() {
    val source = db.birthdayDao().getAll()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun getAll_return_single_row_success() {
    db.birthdayDao().insert(getBirthdayList())
    val source = db.birthdayDao().getAll()

    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertValue {
      it[0].name == getBirthdayList().name
    }
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun insert_success() {
    db.birthdayDao().insert(getBirthdayList())
    val source = db.birthdayDao().getAll()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertValue {
      it[0].name == getBirthdayList().name
      it[0].dob == getBirthdayList().dob
      it[0].imagePath == getBirthdayList().imagePath
    }
  }

  @Test
  fun delete_success() {
    db.birthdayDao().insert(getBirthdayList())
    val source = db.birthdayDao().getAll()
      .flatMap {
        Completable.fromAction {
          db.birthdayDao().delete(it[0].id)
        }.toMaybe<Int>()
      }
    val teObserver = TestObserver<Int>()
    source.subscribe(teObserver)

    val source1 = db.birthdayDao().getAll()
    val testObserver = TestObserver<List<BirthdayList>>()
    source1.subscribe(testObserver)
    testObserver.assertValue {
      it.size == 0
    }
    testObserver.assertNoErrors()
    testObserver.assertComplete()
  }

  @Test
  fun update_success() {
    db.birthdayDao().insert(getBirthdayList())
    db.birthdayDao().update("updatedImagePath", getBirthdayList().name, getBirthdayList().dob)
    val source = db.birthdayDao().getAll()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertValue {
      it[0].imagePath == "updatedImagePath"
    }
  }

  @Test
  fun updateDate_success() {
    db.birthdayDao().insert(getBirthdayList())
    db.birthdayDao()
      .updateDate(SimpleDateFormat("dd/MM/yyyy").parse("19/05/1985"), getBirthdayList().name, getBirthdayList().dob)
    val source = db.birthdayDao().getAll()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertValue {
      it[0].remainderDate == SimpleDateFormat("dd/MM/yyyy").parse("19/05/1985")
    }
  }

  @Test
  fun updateTime_success() {
    db.birthdayDao().insert(getBirthdayList())
    db.birthdayDao().updateTime("10:04 am", getBirthdayList().name, getBirthdayList().dob)
    val source = db.birthdayDao().getAll()
    val testObserver = TestObserver<List<BirthdayList>>()
    source.subscribe(testObserver)
    testObserver.assertValue {
      it[0].remainderTime == "10:04 am"
    }
  }

  fun getBirthdayList(): BirthdayList {
    return BirthdayList(
      name = "Android Testing",
      dob = Calendar.getInstance().time,
      imagePath = "/dev/adb/image111/png",
      remainderTime = "10:00 PM",
      remainderDate = Calendar.getInstance().time
    )
  }

  @After
  @Throws(java.lang.Exception::class)
  fun close(){
    db.close()
  }
}