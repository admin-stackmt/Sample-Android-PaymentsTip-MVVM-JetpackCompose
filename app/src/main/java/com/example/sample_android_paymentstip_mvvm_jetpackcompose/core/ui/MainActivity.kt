package com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.AppTheme
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.ui.theme.colorAppBg
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.core.utils.ui.NavigationConstants
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.addpayment.AddPaymentScreen
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_home_screen.presentation.playmentslist.PaymentsListScreen
import com.example.sample_android_paymentstip_mvvm_jetpackcompose.feature_splash_screen.presentation.SplashScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ApplicationScreen()
            }
        }
    }

    @Preview
    @Composable
    fun ApplicationScreen() {
        val navigationController = rememberNavController()
        AppNavigation(navigationController)
    }

    @Composable
    fun AppNavigation(navigationController: NavHostController) {
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState by remember { mutableStateOf(SnackbarHostState()) }
        Scaffold(modifier = Modifier.background(colorAppBg), snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }) { paddingValues ->
            NavHost(
                navController = navigationController,
                startDestination = NavigationConstants.ROUTE_LANDING,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorAppBg)
            ) {
                LandingPageGraph(navController = navigationController)
                MainGraph(navController = navigationController, snackbarHostState, coroutineScope)
            }
        }
    }

    fun NavGraphBuilder.LandingPageGraph(navController: NavController) {
        navigation(
            startDestination = NavigationConstants.DESTINATION_SCREEN_SPLASH,
            route = NavigationConstants.ROUTE_LANDING
        ) {
            composable(NavigationConstants.DESTINATION_SCREEN_SPLASH) {
                SplashScreen {
                    navController.navigate(NavigationConstants.ROUTE_MAIN) {
                        popUpTo(NavigationConstants.ROUTE_LANDING) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            }
        }
    }

    fun NavGraphBuilder.MainGraph(
        navController: NavController,
        snackbarHostState: SnackbarHostState,
        coroutineScope: CoroutineScope
    ) {
        navigation(
            startDestination = NavigationConstants.DESTINATION_ADD_PAYMENT,
            route = NavigationConstants.ROUTE_MAIN
        ) {
            composable(NavigationConstants.DESTINATION_ADD_PAYMENT) {
                AddPaymentScreen(
                    navigateToSavedPayments = {
                        navController.navigate(NavigationConstants.DESTINATION_SAVED_PAYMENTS)
                    }) {
                    launchSnackBar(snackbarHostState, coroutineScope, message = it)
                }
            }
            composable(NavigationConstants.DESTINATION_SAVED_PAYMENTS) {
                PaymentsListScreen({
                    navController.popBackStack()
                }) {
                    launchSnackBar(snackbarHostState, coroutineScope, message = it)
                }
            }
        }
    }

    fun launchSnackBar(
        snackbarHostState: SnackbarHostState,
        coroutineScope: CoroutineScope,
        message: String
    ) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }
}

