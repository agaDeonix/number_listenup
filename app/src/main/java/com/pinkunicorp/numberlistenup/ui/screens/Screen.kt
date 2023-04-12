package com.pinkunicorp.numberlistenup.ui.screens

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Training: Screen("training")
    object Settings: Screen("settings")
}
