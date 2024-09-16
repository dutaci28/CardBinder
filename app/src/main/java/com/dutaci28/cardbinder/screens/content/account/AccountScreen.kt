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

package com.dutaci28.cardbinder.screens.content.account

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dutaci28.cardbinder.screens.authentication.checkEmailValidity
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = viewModel.auth
    val context = LocalContext.current
    val isShowDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Logged in as:")
        Text(text = "${auth.currentUser?.email}")
        Button(onClick = {
            sendForgotPasswordEmail(
                auth = auth,
                context = context,
                email = Firebase.auth.currentUser?.email.toString()
            )
        }) { Text(text = "Reset password") }
        Button(onClick = { isShowDialog.value = true }) { Text(text = "Sign Out") }
        if (isShowDialog.value) ShowAccountDialog(
            isShowDialog = isShowDialog,
            viewModel = viewModel,
            auth = auth,
            navController = navController,
            context = context
        )
    }

}

@Composable
fun ShowAccountDialog(
    isShowDialog: MutableState<Boolean>,
    viewModel: AccountViewModel,
    auth: FirebaseAuth,
    navController: NavController,
    context: Context
) {
    AlertDialog(
        title = {
            Text(text = "Account")
        },
        text = {
            auth.currentUser?.email?.let { Text(text = "Signed in as: $it") }
        },
        onDismissRequest = {
            isShowDialog.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    isShowDialog.value = false
                    viewModel.signOut(
                        navController,
                        context
                    )
                }
            ) {
                Text("Sign Out")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    isShowDialog.value = false
                }
            ) {
                Text("Dismiss")
            }
        },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    )
}

fun sendForgotPasswordEmail(auth: FirebaseAuth, context: Context, email: String) {
    if (auth.currentUser != null) {
        var isUsingEmail = false
        auth.currentUser!!.providerData.forEach { userInfo ->
            if (userInfo.providerId == EmailAuthProvider.PROVIDER_ID) {
                isUsingEmail = true
            }
        }
        if (!isUsingEmail) {
            Toast.makeText(
                context,
                "You are not using an email/password combination.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (checkEmailValidity(email)) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        } else {
            Toast.makeText(context, "Please fill in the email field.", Toast.LENGTH_SHORT).show()
        }
    }
}