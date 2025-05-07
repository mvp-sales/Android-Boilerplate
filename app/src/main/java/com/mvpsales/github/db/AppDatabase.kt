package com.mvpsales.github.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ArticleNewsEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun articlesDao(): ArticlesDao
}