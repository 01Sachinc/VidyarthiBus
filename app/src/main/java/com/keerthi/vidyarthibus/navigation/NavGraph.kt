package com.keerthi.vidyarthibus.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keerthi.vidyarthibus.ui.screens.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Routes.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Routes.Notifications.route) {
            NotificationScreen(navController = navController)
        }
        composable(Routes.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Routes.BusPass.route) {
            BusPassScreen(navController = navController)
        }
        composable(Routes.Tracking.route) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
            LiveTrackingScreen(navController = navController, routeId = routeId)
        }
        composable(Routes.BusDetails.route) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
            BusDetailsScreen(navController = navController, routeId = routeId)
        }
        composable(Routes.Premium.route) {
            PremiumScreen(navController = navController)
        }
        composable(Routes.FeatureDetail.route) { backStackEntry ->
            val featureId = backStackEntry.arguments?.getString("featureId") ?: ""
            FeatureDetailScreen(navController = navController, featureId = featureId)
        }
    }
}
