package com.pinkunicorp.voicenumbers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pinkunicorp.voicenumbers.ui.screens.Screen
import com.pinkunicorp.voicenumbers.ui.screens.home.HomeScreen
import com.pinkunicorp.voicenumbers.ui.screens.training.TrainingScreen
import com.pinkunicorp.voicenumbers.ui.theme.AppTheme
import net.gotev.speech.Speech

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppTheme {
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) {
                        HomeScreen(navController)
                    }
                    composable(Screen.Training.route) {
                        TrainingScreen(navController)
                    }
                }
            }
        }
        Speech.init(this, packageName)
    }

    override fun onDestroy() {
        super.onDestroy()
        Speech.getInstance().shutdown()
    }
}