package com.example.cardbinder.screens.authentication.login

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LogInScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val emailText = remember { mutableStateOf("") }
        val passwordText = remember { mutableStateOf("") }

        Column() {
            AuthTextField(text = emailText, checkValidityFunction = ::checkEmailValidity)
            AuthTextField(text = passwordText, checkValidityFunction = ::checkPasswordValidity)
            Button(onClick = { Log.d("CARDS", "Inputs valid") }) { Text(text = "Login") }
        }
    }
}

@Composable
fun AuthTextField(text: MutableState<String>, checkValidityFunction: (String) -> Boolean) {
    var isTextValid by remember { mutableStateOf(true) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .height(80.dp)
            .graphicsLayer {
                shadowElevation = 50.dp.toPx()
                shape = RoundedCornerShape(15.dp)
                clip = true
            }
            .padding(10.dp)
            .background(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Gray,
            unfocusedTextColor = Color.Gray,
            cursorColor = Color.Gray,
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.Transparent,
            focusedSupportingTextColor = Color.Gray,
            unfocusedSupportingTextColor = Color.Gray
        ),
        value = text.value,
        singleLine = true,
        onValueChange = {
            text.value = it
            isTextValid = checkValidityFunction(it)
        },
        textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        placeholder = {
            Text(
                text = "Email",
                color = Color.Gray,
                style = TextStyle.Default.copy(fontSize = 20.sp)
            )
        },
        trailingIcon = {
            if (text.value.isNotEmpty())
                IconButton(onClick = {
                    text.value = ""
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Empty Text Icon",
                        tint = Color.Gray
                    )
                }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                //TODO redirect to password textfield
            }
        )
    )
    if (!isTextValid) {
        Text(
            text = "Invalid email address",
            color = Color.Red
        )
    }
}

fun checkEmailValidity(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun checkPasswordValidity(password: String): Boolean {
    val hasMinimumLength = password.length >= 8
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasLowerCase = password.any { it.isLowerCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialCharacter = password.any { !it.isLetterOrDigit() }

    return hasMinimumLength && hasUpperCase && hasLowerCase && hasDigit && hasSpecialCharacter
}