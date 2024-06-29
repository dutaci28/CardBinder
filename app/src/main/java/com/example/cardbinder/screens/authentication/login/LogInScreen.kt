package com.example.cardbinder.screens.authentication.login


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cardbinder.screens.authentication.common.AuthTextField
import com.example.cardbinder.screens.authentication.common.checkEmailValidity
import com.example.cardbinder.screens.authentication.common.checkLoginInputsAndNavigateToMain
import com.example.cardbinder.screens.authentication.common.checkPasswordValidity
import com.example.cardbinder.screens.navigation.NavigationRoutes
import com.example.cardbinder.screens.loading.LoadingScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun LogInScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val emailText = remember { mutableStateOf("") }
        val passwordText = remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val auth = Firebase.auth
        val coroutineScope = rememberCoroutineScope()
        val processingCredentials = remember { mutableStateOf(false) }
        val authenticationFailed = remember { mutableStateOf(false) }
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
                    }, invalidText = "Password complexity too low"
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
                        navController.navigate(route = NavigationRoutes.Register.route) {
                            popUpTo(NavigationRoutes.LogIn.route) {
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