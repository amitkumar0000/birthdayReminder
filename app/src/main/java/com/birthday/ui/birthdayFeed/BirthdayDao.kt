package com.birthday.ui.birthdayFeed

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BirthdayDao {

  @Query("Select * from BirthdayList")
  fun getAll(): Maybe<List<BirthdayList>>

  @Insert
  fun insert(birthdayList: BirthdayList): Long
}