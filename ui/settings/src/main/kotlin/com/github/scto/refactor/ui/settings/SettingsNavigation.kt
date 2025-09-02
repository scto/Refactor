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
package com.github.scto.refactor.ui.settings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.hilt.navigation.compose.hiltViewModel

const val SETTINGS_ROUTE = "settings_route"
const val MAIN_SETTINGS_ROUTE = "main_settings"
const val THEME_SETTINGS_ROUTE = "theme_settings"
const val DEBUG_SETTINGS_ROUTE = "debug_settings"
const val ABOUT_SETTINGS_ROUTE = "about_settings"

fun NavController.navigateToSettings() {
    this.navigate(SETTINGS_ROUTE)
}

fun NavGraphBuilder.settingsScreen(navController: NavController) {
    navigation(
        route = SETTINGS_ROUTE,
        startDestination = MAIN_SETTINGS_ROUTE,
    ) {
        composable(MAIN_SETTINGS_ROUTE) {
            MainSettingsScreen(navController = navController)
        }
        composable(THEME_SETTINGS_ROUTE) {
            val viewModel: SettingsViewModel = hiltViewModel()
            ThemeSettingsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
        composable(DEBUG_SETTINGS_ROUTE) {
            val viewModel: SettingsViewModel = hiltViewModel()
            DebugSettingsScreen(viewModel = viewModel, onNavigateBack = { navController.popBackStack() })
        }
        composable(ABOUT_SETTINGS_ROUTE) {
            AboutSettingsScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
