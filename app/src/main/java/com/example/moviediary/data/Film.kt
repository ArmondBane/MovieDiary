package com.example.moviediary.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "films")
@Parcelize
data class Film (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val genre: String = "",
    val year_of_issue: Long = System.currentTimeMillis(),
    val poster: String = ""
) : Parcelable {}