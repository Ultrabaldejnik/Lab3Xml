package com.example.labwork3.dbNotes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class], version = 1,exportSchema = false)
abstract class NoteDB : RoomDatabase() {
    abstract val dao: NoteDao

    companion object {
        @Volatile
        private var Instance: NoteDB? = null


        fun getDatabase(context: Context): NoteDB {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, NoteDB::class.java, "note_database")

                    .build()
                    .also { Instance = it }
            }
        }
    }
}