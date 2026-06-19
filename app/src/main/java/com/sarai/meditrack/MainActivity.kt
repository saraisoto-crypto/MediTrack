package com.sarai.meditrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sarai.meditrack.ui.navigation.AppNavigation
import com.sarai.meditrack.ui.theme.MediTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediTrackTheme {
                AppNavigation()
            }
        }
    }
}