package com.mvpsales.github.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UsercentricsDataType(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dataTypeDescription: String,
    val cost: Int
)