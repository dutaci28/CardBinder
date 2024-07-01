package com.example.cardbinder.screens.authentication.login


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
import com.example.cardbinder.screens.authentication.common.checkLoginInputsAndNavigateToMain
import com.example.cardbinder.screens.navigation.Routes
import com.example.cardbinder.screens.loading.LoadingScreen

@Composable
fun LogInScreen(navController: NavController, viewModel: LogInViewModel = hiltViewModel()) {
    val emailText = viewModel.emailText
    val passwordText = viewModel.passwordText
    val focusRequester = viewModel.focusRequester
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
                    hideCharacters = true,
                    focusRequester = focusRequester,
                    onGo = {
                        checkLoginInputsAndNavigateToMain(
                            navController = navController,
                            email = emailText.value,
                            password = passwordText.value,
                            auth = auth,
                            coroutineScope = coroutineScope,
                            processingCredentialsBool = processingCredentials,
                            authenticationFailedBool = authenticationFailed
                        )
                    }
                )
                if (authenticationFailed.value) {
                    Text(text = "Authentication failed", color = Color.Red)
                }
                Button(
                    onClick = {
                        checkLoginInputsAndNavigateToMain(
                            navController = navController,
                            email = emailText.value,
                            password = passwordText.value,
                            auth = auth,
                            coroutineScope = coroutineScope,
                            processingCredentialsBool = processingCredentials,
                            authenticationFailedBool = authenticationFailed
                        )
                    },
                    modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(5.dp))

                ) {
                    Text(text = "Log In")
                }
                Button(
                    onClick = {
                        navController.navigate(route = Routes.Register.route) {
                            popUpTo(Routes.LogIn.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(5.dp))
                ) { Text(text = "Create an account") }
            }
        }


    }
}