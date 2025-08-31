package com.github.scto.refactor.features.git.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepository(repository: RepositoryEntity)

    @Query("SELECT * FROM repositories") fun getAllRepositories(): Flow<List<RepositoryEntity>>
}
