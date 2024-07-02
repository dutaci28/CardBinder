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