package com.github.scto.refactor.features.git.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repositories")
data class RepositoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val repoUrl: String,
    val localPath: String,
    val branchName: String,
    // HINZUGEFÜGT: Neue Spalte mit einem Standardwert
    val lastUsed: Long = System.currentTimeMillis()
)
