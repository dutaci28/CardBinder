package com.example.cardbinder.screens.authentication.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cardbinder.screens.authentication.common.AuthTextField
import com.example.cardbinder.screens.authentication.common.checkEmailValidity
import com.example.cardbinder.screens.authentication.common.checkPasswordValidity
import com.example.cardbinder.screens.authentication.common.checkRegisterInputsAndNavigateToMain
import com.example.cardbinder.screens.navigation.LoadingScreen
import com.example.cardbinder.screens.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val emailText = viewModel.emailText
    val passwordText = viewModel.passwordText
    val repeatPasswordText = viewModel.repeatPasswordText
    val focusRequester = viewModel.focusRequester
    val focusRequester2 = viewModel.focusRequester2
    val auth = viewModel.auth
    val coroutineScope = viewModel.coroutineScope
    val processingCredentials = viewModel.processingCredentials
    val authenticationFailed = viewModel.authenticationFailed

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (processingCredentials.value) {
            LoadingScreen()
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AuthTextField(
                    text = emailText,
                    placeholder = "Email",
                    checkValidityFunction = ::checkEmailValidity,
                    hideCharacters = false,
                    hasNext = true,
                    onNext = { focusRequester.requestFocus() },
                    invalidText = "Invalid Email Address"
                )
                AuthTextField(
                    text = passwordText,
                    placeholder = "Password",
                    checkValidityFunction = ::checkPasswordValidity,
                    hideCharacters = true,
                    hasNext = true,
                    focusRequester = focusRequester,
                    onNext = { focusRequester2.requestFocus() },
                    invalidText = "Password Complexity Too Low",
                )
                AuthTextField(
                    text = repeatPasswordText,
                    placeholder = "Repeat Password",
                    checkValidityFunction = ::checkPasswordValidity,
                    hideCharacters = true,
                    focusRequester = focusRequester2,
                    onGo = {
                        checkRegisterInputsAndNavigateToMain(
                            navController = navController,
                            email = emailText.value,
                            password = passwordText.value,
                            repeatedPassword = repeatPasswordText.value,
                            auth = auth,
                            coroutineScope = coroutineScope,
                            processingCredentialsBool = processingCredentials,
                            authenticationFailedBool = authenticationFailed
                        )
                    },
                    invalidText = "Password Complexity Too Low",
                    checkMatching = true,
                    matchValue = passwordText.value,
                    matchValue2 = repeatPasswordText.value
                )
                if (authenticationFailed.value) {
                    Text(text = "Authentication failed", color = Color.Red)
                }
                Button(
                    onClick = {
                        checkRegisterInputsAndNavigateToMain(
                            navController = navController,
                            email = emailText.value,
                            password = passwordText.value,
                            repeatedPassword = repeatPasswordText.value,
                            auth = auth,
                            coroutineScope = coroutineScope,
                            processingCredentialsBool = processingCredentials,
                            authenticationFailedBool = authenticationFailed
                        )
                    },
                    modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(5.dp))
                ) { Text(text = "Register") }
                Button(
                    onClick = {
                        navController.navigate(route = Routes.LogIn.route) {
                            popUpTo(Routes.Register.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(5.dp))
                ) { Text(text = "Go back to Log In") }
            }
        }
    }
}