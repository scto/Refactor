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
package com.github.scto.refactor.data.local

import android.content.Context

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext

import javax.inject.Inject
import javax.inject.Singleton

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for handling user preferences stored in DataStore.
 *
 * This class provides methods to get and set user-specific settings like API keys, selected themes,
 * etc. It uses Dagger Hilt for dependency injection to get the application context.
 *
 * @property context The application context provided by Hilt.
 */
@Singleton
class UserPreferencesRepository
@Inject
constructor(@ApplicationContext private val context: Context) {

    /** Flow representing the user's selected theme. */
    val theme: Flow<String?> =
        context.dataStore.data // KORRIGIERT: context. hinzugefügt
            .map { preferences -> preferences[THEME] }

    val dynamicColor: Flow<String?> =
        context.dataStore.data // KORRIGIERT: context. hinzugefügt
            .map { preferences -> preferences[DYNAMIC_COLOR] }

    /** Flow representing the user's stored API key. Emits the latest value whenever it changes. */
    val apiKey: Flow<String?> =
        context.dataStore.data // KORRIGIERT: context. hinzugefügt
            .map { preferences -> preferences[API_KEY] }

    val debug: Flow<String?> =
        context.dataStore.data // KORRIGIERT: context. hinzugefügt
            .map { preferences -> preferences[DEBUG] }
			
    val appVersion: Flow<String?> =
        context.dataStore.data // KORRIGIERT: context. hinzugefügt
            .map { preferences -> preferences[APP_VERSION] }

    /**
     * Suspended function to update the theme in DataStore.
     *
     * @param theme The new theme name to be stored.
     */
    suspend fun saveTheme(theme: String) {
        context.dataStore.edit { preferences -> // KORRIGIERT: context. hinzugefügt
            preferences[THEME] = theme
        }
    }

    /**
     * Suspended function to update the dynamic color in DataStore.
     *
     * @param useDynamicColor The new valid if dynamic colors to be stored.
     */
    suspend fun saveDynamicColor(dynamicColor: Boolean) {
        context.dataStore.edit { preferences -> // KORRIGIERT: context. hinzugefügt
            preferences[DYNAMIC_COLOR] = dynamicColor
        }
    }
	
	/**
     * Suspended function to update the API key in DataStore.
     *
     * @param apiKey The new API key to be stored.
     */
    suspend fun saveApiKey(apiKey: String) {
        context.dataStore.edit { preferences -> // KORRIGIERT: context. hinzugefügt
            preferences[API_KEY] = apiKey
        }
    }
	
    /**
     * Suspended function to update the theme in DataStore.
     *
     * @param theme The new theme name to be stored.
     */
    suspend fun saveDebug(debug: Boolean) {
        context.dataStore.edit { preferences -> // KORRIGIERT: context. hinzugefügt
            preferences[DEBUG] = debug
        }
    }
	
	/**
     * Suspended function to update the theme in DataStore.
     *
     * @param theme The new theme name to be stored.
     */
    suspend fun saveAppVersion(appVersion: String) {
        context.dataStore.edit { preferences -> // KORRIGIERT: context. hinzugefügt
            preferences[APP_VERSION] = appVersion
        }
    }
	
    private companion object {
        // Define keys for DataStore
        private val THEME = stringPreferencesKey("theme")
		private val DYNAMOC_COLOR = booleanPreferencesKey("dynamicColor")
		private val API_KEY = stringPreferencesKey("api_key")
		private val DEBUG = booleanPreferencesKey("debug")
		private val APP_VERSION = stringPreferencesKey("app_version")
    }
}

// Top-level property to create a single instance of DataStore for the application.
// This is the recommended way to instantiate DataStore.
private val Context.dataStore: DataStore<Preferences> by
    preferencesDataStore(name = "user_preferences")
