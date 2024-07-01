package com.dutaci28.cardbinder.screens.authentication

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.dutaci28.cardbinder.R
import com.dutaci28.cardbinder.screens.navigation.Routes
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    val email = mutableStateOf("")
    val password = mutableStateOf("")
    val focusRequester = FocusRequester()
    val auth = Firebase.auth
    val coroutineScope = viewModelScope
    val isProcessingCredentials = mutableStateOf(false)
    val isAuthenticationFailed = mutableStateOf(false)

    val repeatPassword = mutableStateOf("")
    val focusRequester2 = FocusRequester()

    fun authenticateWithGoogle(
        context: Context,
        navController: NavController,
        processingCredentialsBool: MutableState<Boolean>,
        authenticationFailedBool: MutableState<Boolean>
    ) {
        //TODO ADD NONCE
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption =
            GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(context.getString(R.string.FIREBASE_AUTH_WEB_CLIENT_ID))
                .build()
        val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(context = context, request = request)
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val googleIdToken = googleIdTokenCredential.idToken
                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                auth.signInWithCredential(firebaseCredential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(route = "search/" + false) {
                            popUpTo(Routes.LogIn.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        authenticationFailedBool.value = true
                        processingCredentialsBool.value = false
                        Log.d("LOGIN FAILED", task.exception?.message.toString())
                    }
                }
            } catch (e: Exception) {
                authenticationFailedBool.value = true
                processingCredentialsBool.value = false
                Log.d("LOGIN FAILED", e.message.toString())
                e.printStackTrace()
            }
        }
    }

    fun checkRegisterInputsAndNavigateToMain(
        navController: NavController,
        email: String,
        password: String,
        repeatedPassword: String,
        auth: FirebaseAuth,
        isProcessingCredentials: MutableState<Boolean>,
        isAuthenticationFailed: MutableState<Boolean>
    ) {
        isProcessingCredentials.value = true
        isAuthenticationFailed.value = false
        if (checkEmailValidity(email) && checkPasswordValidity(password) && password == repeatedPassword)
            viewModelScope.launch {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(route = "search/" + false) {
                            popUpTo(Routes.LogIn.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        isAuthenticationFailed.value = true
                        isProcessingCredentials.value = false
                        Log.d("REGISTER FAILED", task.exception?.message.toString())
                    }
                }
            } else {
            isAuthenticationFailed.value = true
            isProcessingCredentials.value = false
            Log.d("REGISTER FAILED", "Email or password invalid or passwords dont match")
        }
    }
}