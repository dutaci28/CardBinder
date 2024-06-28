package com.example.cardbinder.screens.authentication.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cardbinder.screens.authentication.common.AuthTextField
import com.example.cardbinder.screens.authentication.common.checkEmailValidity
import com.example.cardbinder.screens.authentication.common.checkPasswordValidity
import com.example.cardbinder.screens.authentication.common.checkRegisterInputsAndNavigateToMain
import com.example.cardbinder.screens.navigation.NavigationRoutes

@Composable
fun RegisterScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val emailText = remember { mutableStateOf("") }
        val passwordText = remember { mutableStateOf("") }
        val repeatPasswordText = remember { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val focusRequester2 = remember { FocusRequester() }

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
                        repeatedPassword = repeatPasswordText.value
                    )
                },
                invalidText = "Password Complexity Too Low",
                checkMatching = true,
                matchValue = passwordText.value,
                matchValue2 = repeatPasswordText.value
            )
            Button(
                onClick = {
                    checkRegisterInputsAndNavigateToMain(
                        navController = navController,
                        email = emailText.value,
                        password = passwordText.value,
                        repeatedPassword = repeatPasswordText.value
                    )
                },
                modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(5.dp))
            ) { Text(text = "Register") }
            Button(
                onClick = {
                    navController.navigate(route = NavigationRoutes.LogIn.route) {
                        popUpTo(NavigationRoutes.Register.route) {
                            inclusive = true
                        }
                    }
                },
                modifier = Modifier.shadow(10.dp, shape = RoundedCornerShape(5.dp))
            ) { Text(text = "Go back to Log In") }
        }
    }
}