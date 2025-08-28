/*
 * Copyright 2025, S.C.T.O
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.scto.refactor.features.git.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.room.Room
import com.github.scto.refactor.features.git.UserSettings
import com.github.scto.refactor.features.git.crypto.CryptoManager
import com.github.scto.refactor.features.git.data.local.datastore.UserSettingsSerializer
import com.github.scto.refactor.features.git.data.local.db.GitDatabase

// DataStore-Instanz als Extension Property auf Context
private val Context.userSettingsStore: DataStore<UserSettings> by
    dataStore(fileName = "user_settings.pb", serializer = UserSettingsSerializer)

interface AppContainer {
    val gitDatabase: GitDatabase
    val userSettingsStore: DataStore<UserSettings>
    val cryptoManager: CryptoManager // HINZUGEFÜGT
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val gitDatabase: GitDatabase by lazy {
        Room.databaseBuilder(context, GitDatabase::class.java, "git_database.db")
            // Nützlich für die Entwicklung: Bei Migrationsfehlern wird die DB neu erstellt.
            .fallbackToDestructiveMigration()
            .build()
    }

    override val userSettingsStore: DataStore<UserSettings> by lazy { context.userSettingsStore }

    // HINZUGEFÜGT: Stellt eine Instanz des CryptoManagers bereit.
    override val cryptoManager: CryptoManager by lazy { CryptoManager(context) }
}
