package com.dutaci28.cardbinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.dutaci28.cardbinder.screens.navigation.MainScreen
import com.dutaci28.cardbinder.ui.theme.CardBinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CardBinderTheme {
                MainScreen(window)
            }
        }
    }
}