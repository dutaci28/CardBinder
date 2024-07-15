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

package com.dutaci28.cardbinder.screens.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dutaci28.cardbinder.data.repository.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {
    private val auth = Firebase.auth
    private val repo = repository
    val getRandomCard = repo.getRandomCard()
    var startRoute by mutableStateOf(Routes.LoadingScreen.route)
    var showBottomBar by mutableStateOf(true)
    private lateinit var listener: AuthStateListener

    init {
        authStateListener()
    }

    private fun authStateListener() = viewModelScope.launch {
        listener = AuthStateListener { authState ->
            val userLoggedIn = authState.currentUser != null
            startRoute = if (!userLoggedIn)
                Routes.LogIn.route
            else
                Routes.Search.defaultRoute
        }
        auth.addAuthStateListener(listener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(listener)
        viewModelScope.cancel()
    }
}