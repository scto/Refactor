package com.github.scto.refactor.ui.settings

sealed class SettingsScreen(val route: String) {
    object Main : SettingsScreen("settings_main")
    object Theme : SettingsScreen("settings_theme")
    object About : SettingsScreen("settings_about")
}