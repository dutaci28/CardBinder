package com.example.cardbinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cardbinder.screens.navigation.MainScreen
import com.example.cardbinder.ui.theme.CardBinderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardBinderTheme {
                MainScreen()
            }
        }
    }
}