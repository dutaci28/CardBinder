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

package com.dutaci28.cardbinder.screens.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dutaci28.cardbinder.R
import com.dutaci28.cardbinder.screens.navigation.LoadingScreen

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
                Image(
                    painter = painterResource(id = R.drawable.cardbinder_logo),
                    modifier = Modifier.size(150.dp),
                    contentDescription = "Cardbinder Logo"
                )
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
                            passwordRepeated = repeatPasswordText.value,
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