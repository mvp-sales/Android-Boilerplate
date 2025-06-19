package com.mvpsales.github.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS NewsSourceEntity (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                sourceId TEXT NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL,
                url TEXT NOT NULL,
                category TEXT NOT NULL,
                language TEXT NOT NULL,
                country TEXT NOT NULL
            )
        """.trimIndent())

        database.execSQL("""
            CREATE UNIQUE INDEX IF NOT EXISTS index_NewsSourceEntity_sourceId 
            ON NewsSourceEntity(sourceId)
        """.trimIndent())
    }
}