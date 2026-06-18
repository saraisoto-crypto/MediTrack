package com.sarai.meditrack.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dose: String,
    val frequency: String,
    val time: String,
    val category: String,
    val notes: String = ""
)