package com.example.cardbinder.screens.authentication.register

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : ViewModel() {
    val emailText = mutableStateOf("")
    val passwordText =  mutableStateOf("")
    val repeatPasswordText =  mutableStateOf("")
    val focusRequester = FocusRequester()
    val focusRequester2 = FocusRequester()
    val auth = Firebase.auth
    val coroutineScope = viewModelScope
    val processingCredentials =  mutableStateOf(false)
    val authenticationFailed =  mutableStateOf(false)
}
