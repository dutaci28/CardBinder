package com.example.cardbinder.screens.authentication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cardbinder.screens.navigation.LoadingScreen

@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val email = viewModel.email
    val password = viewModel.password
    val repeatPasswordText = viewModel.repeatPassword
    val focusRequester = viewModel.focusRequester
    val focusRequester2 = viewModel.focusRequester2
    val auth = viewModel.auth
    val isProcessingCredentials = viewModel.isProcessingCredentials
    val isAuthenticationFailed = viewModel.isAuthenticationFailed

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
                    checkValidityFunction = ::checkPasswordValidity,
                    isHideCharacters = true,
                    hasNext = true,
                    focusRequester = focusRequester,
                    onNext = { focusRequester2.requestFocus() },
                    invalidText = "Password Complexity Too Low",
                )
                AuthTextField(
                    text = repeatPasswordText,
                    placeholder = "Repeat Password",
                    checkValidityFunction = ::checkPasswordValidity,
                    isHideCharacters = true,
                    focusRequester = focusRequester2,
                    onGo = {
                        viewModel.checkRegisterInputsAndNavigateToMain(
                            navController = navController,
                            email = email.value,
                            password = password.value,
                            repeatedPassword = repeatPasswordText.value,
                            auth = auth,
                            isProcessingCredentials = isProcessingCredentials,
                            isAuthenticationFailed = isAuthenticationFailed
                        )
                    },
                    invalidText = "Password Complexity Too Low",
                    checkMatching = true,
                    matchValue = password.value,
                    matchValue2 = repeatPasswordText.value
                )
                AuthFailedText(authenticationFailedBool = isAuthenticationFailed)
                RegisterButton(
                    viewModel = viewModel,
                    navController = navController,
                    email = email,
                    password = password,
                    repeatPasswordText = repeatPasswordText,
                    auth = auth,
                    isProcessingCredentials = isProcessingCredentials,
                    isAuthenticationFailed = isAuthenticationFailed
                )
                BackToLogInButton(navController = navController)
            }
        }
    }
}