package com.birthday.ui.birthdayFeed

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.Date

@Dao
interface BirthdayDao {

  @Query("Select * from BirthdayList")
  fun getAll(): Maybe<List<BirthdayList>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insert(birthdayList: BirthdayList): Long

  @Query("Delete from BirthdayList where id is :itemId")
  fun delete(itemId: Long): Int

  @Query("Update BirthdayList set imagePath = :imagePath  where name is :name and dob is :dob ")
  fun update(imagePath: String, name: String, dob: Date): Int

  @Query("Update BirthdayList set remainderDate = :date  where name is :name and dob is :dob")
  fun updateDate(date: Date,name:String,dob: Date): Int

  @Query("Update BirthdayList set remainderTime = :time  where name is :name and dob is :dob")
  fun updateTime(time: String,name:String,dob: Date): Int
}