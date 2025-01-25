package com.mvpsales.github.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UsercentricsDataType::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val dataTypesDao: UsercentricsDataTypeDao
}