package com.example.labwork3.dbNotes

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity()
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val owner : String,
    val value : String,
    val creationDateTime : String
)
