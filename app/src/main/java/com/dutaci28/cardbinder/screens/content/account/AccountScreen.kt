package com.dutaci28.cardbinder.screens.content.account

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel(),
    navController: NavController
) {
    val auth = viewModel.auth
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val isShowDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(bottom = 140.dp)
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { isShowDialog.value = true }) { Text(text = "Account") }
            if (isShowDialog.value) ShowAccountDialog(
                isShowDialog = isShowDialog,
                viewModel = viewModel,
                auth = auth,
                navController = navController,
                context = context
            )
        }
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