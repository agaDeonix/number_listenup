package com.pinkunicorp.numberlistenup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinkunicorp.numberlistenup.other.tts.TTSService
import com.pinkunicorp.numberlistenup.ui.screens.Screen
import com.pinkunicorp.numberlistenup.ui.screens.home.HomeScreen
import com.pinkunicorp.numberlistenup.ui.screens.settings.SettingsScreen
import com.pinkunicorp.numberlistenup.ui.screens.training.TrainingScreen
import com.pinkunicorp.numberlistenup.ui.theme.AppTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val ttsService: TTSService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme(darkTheme = false) {
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Screen.Training.route) {
                        TrainingScreen(navController)
                    }
                    composable(Screen.Settings.route) {
                        SettingsScreen(navController)
                    }
                }
            }
        }
        ttsService.init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsService.shutdown()
    }
}