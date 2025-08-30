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
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

const val SETTINGS_GRAPH_ROUTE_PATTERN = "settings_graph"
const val MAIN_SETTINGS_ROUTE = "main_settings_route"
const val THEME_SETTINGS_ROUTE = "theme_settings_route"
const val DEBUG_SETTINGS_ROUTE = "debug_settings_route"
const val ABOUT_SETTINGS_ROUTE = "about_settings_route"

/*
sealed class SettingsScreen(val route: String) {
    object Main : SettingsScreen("settings_main")
	object About : SettingsScreen("settings_about")
    object Theme : SettingsScreen("settings_theme")
}
*/

fun NavController.navigateToSettingsGraph(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_GRAPH_ROUTE_PATTERN, navOptions)
}

fun NavGraphBuilder.settingsGraph(onBackClick: () -> Unit, navController: NavController) {
    navigation(route = SETTINGS_GRAPH_ROUTE_PATTERN, startDestination = MAIN_SETTINGS_ROUTE) {
        composable(route = MAIN_SETTINGS_ROUTE) {
            MainSettingsScreen(
                onBackClick = onBackClick,
                onNavigate = { route -> navController.navigate(route) },
            )
        }
		composable(route = THEME_SETTINGS_ROUTE) {
            ThemeSettingsScreen(
				onBackClick = { navController.popBackStack() }
			)
        }
		composable(route = DEBUG_SETTINGS_ROUTE) {
            DebugSettingsScreen(
				onBackClick = { navController.popBackStack() }
			)
        }
        composable(route = ABOUT_SETTINGS_ROUTE) {
            AboutSettingsScreen(
				onBackClick = { navController.popBackStack() }
			)
        }
    }
}