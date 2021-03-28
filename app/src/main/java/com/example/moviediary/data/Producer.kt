package com.example.moviediary.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "producers", foreignKeys = [ForeignKey(entity = Film::class, parentColumns = ["id"], childColumns = ["film_id"], onDelete = CASCADE)])
@Parcelize
class Producer (
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val name: String = "",
    var film_id: Int = 0
) : Parcelable {}