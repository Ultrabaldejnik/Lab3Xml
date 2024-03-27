package com.example.labwork3.dbNotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: Note)

    @Query("SELECT * from note WHERE owner LIKE :owner ORDER BY creationDateTime")
    fun getNotes(owner : String) : LiveData<List<Note>>

    @Query("SELECT * from note")
    fun getAllNotes() : LiveData<List<Note>>
}