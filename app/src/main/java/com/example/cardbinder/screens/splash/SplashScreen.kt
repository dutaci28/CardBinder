package com.example.cardbinder.screens.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.cardbinder.R
import com.example.cardbinder.screens.navigation.NavigationRoutes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        val auth = Firebase.auth

        DisposableEffect(auth) {
            val listener = FirebaseAuth.AuthStateListener { authState ->
                val userLoggedIn = authState.currentUser != null
                if (!userLoggedIn)
                    navController.navigate(NavigationRoutes.LogIn.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
                else
                    navController.navigate(route = "search/" + false) {
                        popUpTo(navController.graph.findStartDestination().id)
                        launchSingleTop = true
                    }
            }
            auth.addAuthStateListener(listener)
            onDispose { auth.removeAuthStateListener(listener) }
        }
        RotatingImage(imageResId = R.drawable.tsp_96_basal_sliver, modifier = Modifier.size(64.dp))
    }
}

@Composable
fun RotatingImage(imageResId: Int, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = { fraction ->
                    ((1 - kotlin.math.cos(fraction * kotlin.math.PI)) / 2).toFloat()
                }),
            repeatMode = RepeatMode.Restart
        )
    )

    Image(
        painter = painterResource(id = imageResId),
        contentDescription = "Rotating Image",
        modifier = modifier
            .rotate(angle)
    )
}