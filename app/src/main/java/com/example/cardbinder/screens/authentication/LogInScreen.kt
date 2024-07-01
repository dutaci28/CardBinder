package com.example.cardbinder.screens.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cardbinder.screens.navigation.LoadingScreen

@Composable
fun LogInScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val email = viewModel.email
    val password = viewModel.password
    val focusRequester = viewModel.focusRequester
    val coroutineScope = viewModel.coroutineScope
    val isProcessingCredentials = viewModel.isProcessingCredentials
    val isAuthenticationFailed = viewModel.isAuthenticationFailed
    val auth = viewModel.auth
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (isProcessingCredentials.value) {
            LoadingScreen()
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AuthTextField(
                    text = email,
                    placeholder = "Email",
                    checkValidityFunction = ::checkEmailValidity,
                    isHideCharacters = false,
                    hasNext = true,
                    onNext = { focusRequester.requestFocus() },
                    invalidText = "Invalid Email Address"
                )
                AuthTextField(
                    text = password,
                    placeholder = "Password",
                    isHideCharacters = true,
                    focusRequester = focusRequester,
                    onGo = {
                        checkLoginInputsAndNavigateToMain(
                            navController = navController,
                            email = email.value,
                            password = password.value,
                            auth = auth,
                            coroutineScope = coroutineScope,
                            isProcessingCredentials = isProcessingCredentials,
                            isAuthenticationFailed = isAuthenticationFailed
                        )
                    }
                )
                AuthFailedText(authenticationFailedBool = isAuthenticationFailed)
                LogInButton(
                    navController = navController,
                    email = email,
                    password = password,
                    auth = auth,
                    coroutineScope = coroutineScope,
                    isProcessingCredentials = isProcessingCredentials,
                    isAuthenticationFailed = isAuthenticationFailed
                )
                CreateAccountButton(navController = navController)
                SignInWithGoogleButton(
                    viewModel = viewModel,
                    context = context,
                    navController = navController,
                    isProcessingCredentials = isProcessingCredentials,
                    isAuthenticationFailed = isAuthenticationFailed
                )
            }
        }
    }
}