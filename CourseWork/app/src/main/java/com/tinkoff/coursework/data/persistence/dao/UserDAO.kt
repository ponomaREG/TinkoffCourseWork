package com.tinkoff.coursework.data.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tinkoff.coursework.data.persistence.model.UserDB
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface UserDAO {

    @Insert
    fun insertUsers(users: List<UserDB>): Completable

    @Query("DELETE FROM user;")
    fun clearAll(): Completable

    @Query("SELECT * FROM user;")
    fun getUsers(): Single<List<UserDB>>
}