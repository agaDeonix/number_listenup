package com.pinkunicorp.voicenumbers.ui.screens

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Training: Screen("training")
}
