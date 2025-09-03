package com.github.scto.refactor.ui.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.github.scto.refactor.ui.settings.R
import com.github.scto.refactor.ui.settings.AboutSettingsScreen
import com.github.scto.refactor.ui.settings.DebugSettingsScreen
import com.github.scto.refactor.ui.settings.ThemeSettingsScreen
import com.github.scto.refactor.ui.settings.SettingsViewModel

@Composable
fun SettingsScreen(
    // Dieser ViewModel wird zwischen allen Settings-Screens geteilt
    viewModel: SettingsViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SettingsScreen.Main.route) {
        composable(SettingsScreen.Main.route) {
			MainSettingsScreen(
				navController = navController
			)
		}
        composable(SettingsScreen.Theme.route) {
            ThemeSettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
            )
        }
		composable(SettingsScreen.Debug.route) {
            DebugSettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(SettingsScreen.About.route) {
            AboutSettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}