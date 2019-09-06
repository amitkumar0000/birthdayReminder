package com.birthday.ui.birthdayFeed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface BirthdayDao {

  @Query("Select * from BirthdayList")
  fun getAll(): Maybe<List<BirthdayList>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(birthdayList: BirthdayList): Long

  @Query("Delete from BirthdayList where id is :itemId" )
  fun delete(itemId:Long): Int
}