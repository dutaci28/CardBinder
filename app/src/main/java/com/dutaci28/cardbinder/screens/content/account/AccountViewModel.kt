//CardBinder - a Magic: The Gathering collector's app.
//Copyright (C) 2024 Catalin Duta
//
//This program is free software: you can redistribute it and/or modify
//it under the terms of the GNU General Public License as published by
//the Free Software Foundation, either version 3 of the License, or
//(at your option) any later version.
//
//This program is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//GNU General Public License for more details.
//
//You should have received a copy of the GNU General Public License
//along with this program.(https://github.com/dutaci28/CardBinder?tab=GPL-3.0-1-ov-file#readme).
//If not, see <https://www.gnu.org/licenses/>.

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