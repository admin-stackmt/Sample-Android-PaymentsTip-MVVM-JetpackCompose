package com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_splash_screen.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.R
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun SplashScreen(navigateToNextScreen: () -> Unit = {}) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val logoAnimState = remember {
            Animatable(0f)
        }
        Image(
            painter = painterResource(id = R.drawable.logo_payments_tip),
            contentDescription = "",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .scale(logoAnimState.value)
        )
        LaunchedEffect(key1 = Unit) {
            launch {
                logoAnimState.animateTo(
                    1f, animationSpec = tween(
                        2000
                    )
                )
                navigateToNextScreen()
            }
        }
    }
}
