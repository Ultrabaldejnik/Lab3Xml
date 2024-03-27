package com.example.lab3xml

import android.app.Application
import com.example.lab3xml.sharedPref.DataStore
import com.example.labwork3.dbNotes.NoteDB
import com.example.labwork3.dbUser.UserDB

class App : Application() {
    val dbUser by lazy { UserDB.getDatabase(this) }
    val dbNotes by lazy { NoteDB.getDatabase(this) }
    val dbDataStore by lazy{ DataStore(this) }
}