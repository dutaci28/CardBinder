package com.example.cardbinder.screens.authentication.common

import android.util.Log
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cardbinder.screens.navigation.NavigationRoutes
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AuthTextField(
    text: MutableState<String>,
    placeholder: String,
    checkValidityFunction: (String) -> Boolean = { true },
    checkMatching: Boolean = false,
    matchValue: String = "",
    matchValue2: String = "",
    hideCharacters: Boolean,
    focusRequester: FocusRequester = FocusRequester(),
    hasNext: Boolean = false,
    onNext: () -> Unit = {},
    onGo: () -> Unit = {},
    invalidText: String = ""
) {
    var isTextValid by remember { mutableStateOf(true) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .graphicsLayer {
                shadowElevation = 50.dp.toPx()
            }
            .padding(20.dp)
            .background(
                color = Color.White.copy(alpha = 0.9f),
                shape = RoundedCornerShape(8.dp)
            )
            .focusRequester(focusRequester),
        visualTransformation = if (hideCharacters) PasswordVisualTransformation() else VisualTransformation.None,
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
                text = placeholder,
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
            imeAction = if (hasNext) ImeAction.Next else ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onNext()
            },
            onGo = {
                onGo()
            }
        )
    )
    if (!isTextValid) {
        Text(
            text = invalidText,
            color = Color.Red
        )
        if (checkMatching && !checkPasswordsMatching(matchValue, matchValue2))
            Text(
                text = "Values Are Not Matching",
                color = Color.Red
            )
    }
}

fun checkLoginInputsAndNavigateToMain(
    navController: NavController,
    email: String,
    password: String,
    auth: FirebaseAuth,
    coroutineScope: CoroutineScope,
    processingCredentialsBool: MutableState<Boolean>,
    authenticationFailedBool: MutableState<Boolean>
) {
    processingCredentialsBool.value = true
    if (checkEmailValidity(email) && checkPasswordValidity(password))
        coroutineScope.launch {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(route = "search/" + false) {
                        popUpTo(NavigationRoutes.LogIn.route) {
                            inclusive = true
                        }
                    }
                } else {
                    authenticationFailedBool.value = true
                    processingCredentialsBool.value = false
                    Log.d("LOGIN FAILED", task.exception?.message.toString())
                }
            }
        }
    else {
        authenticationFailedBool.value = true
        processingCredentialsBool.value = false
        Log.d("LOGIN FAILED", "Email or password invalid")
    }
}

fun checkRegisterInputsAndNavigateToMain(
    navController: NavController,
    email: String,
    password: String,
    repeatedPassword: String,
    auth: FirebaseAuth,
    coroutineScope: CoroutineScope,
    processingCredentialsBool: MutableState<Boolean>,
    authenticationFailedBool: MutableState<Boolean>
) {
    processingCredentialsBool.value = true
    authenticationFailedBool.value = false
    if (checkEmailValidity(email) && checkPasswordValidity(password) && password == repeatedPassword)
        coroutineScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(route = "search/" + false) {
                        popUpTo(NavigationRoutes.LogIn.route) {
                            inclusive = true
                        }
                    }
                } else {
                    authenticationFailedBool.value = true
                    processingCredentialsBool.value = false
                    Log.d("REGISTER FAILED", task.exception?.message.toString())
                }
            }
        } else {
        authenticationFailedBool.value = true
        processingCredentialsBool.value = false
        Log.d("REGISTER FAILED", "Email or password invalid or passwords dont match")
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

fun checkPasswordsMatching(password: String, repeatedPassword: String): Boolean =
    password == repeatedPassword