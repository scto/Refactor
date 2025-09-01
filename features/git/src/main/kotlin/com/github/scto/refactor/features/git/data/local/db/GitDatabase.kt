package com.github.scto.refactor.features.git.data.local.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [RepositoryEntity::class],
    version = 2, // 1. Version erhöht
    autoMigrations =
        [
            AutoMigration(from = 1, to = 2) // 2. Auto-Migration deklariert
        ],
    exportSchema = true
)
abstract class GitDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}
