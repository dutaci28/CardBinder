package com.dutaci28.cardbinder.screens.content.account

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dutaci28.cardbinder.screens.navigation.Routes
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {
    val auth = Firebase.auth

    fun signOut(navController: NavController, context: Context) {
        val credentialManager = CredentialManager.create(context)
        auth.signOut()
        viewModelScope.launch {
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
        }
        navController.navigate(route = Routes.LogIn.route) {
            popUpTo(route = Routes.LogIn.route)
            launchSingleTop = true
        }
    }
}