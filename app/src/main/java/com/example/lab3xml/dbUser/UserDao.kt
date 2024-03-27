package com.example.labwork3.dbUser

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun addUser(user : User)
    @Query("SELECT * FROM user WHERE login LIKE :login")
    suspend fun getUser(login: String): User

    @Query("SELECT COUNT(*) FROM user WHERE login = :login")
    suspend fun checkedUser(login : String): Int
}