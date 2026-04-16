package com.sensecolor.app.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sensecolor.app.data.repository.UserPreferencesRepository
import com.sensecolor.app.ui.screens.analysis.AnalysisScreen
import com.sensecolor.app.ui.screens.camera.CameraScreen
import com.sensecolor.app.ui.screens.onboarding.OnboardingScreen
import com.sensecolor.app.ui.screens.settings.SettingsScreen
import com.sensecolor.app.ui.screens.test.ColorTestScreen

sealed class Screen(val route: String) {
    data object Onboarding : Screen("onboarding")
    data object ColorTest : Screen("color_test")
    data object Camera : Screen("camera")
    data object Analysis : Screen("analysis/{photoUri}") {
        fun createRoute(photoUri: String): String =
            "analysis/${Uri.encode(photoUri)}"
    }
    data object Settings : Screen("settings")
}

@Composable
fun SenseColorNavGraph(
    navController: NavHostController,
    startDestination: String,
    preferencesRepository: UserPreferencesRepository
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                preferencesRepository = preferencesRepository,
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateToTest = {
                    navController.navigate(Screen.ColorTest.route)
                }
            )
        }

        composable(Screen.ColorTest.route) {
            ColorTestScreen(
                preferencesRepository = preferencesRepository,
                onNavigateToCamera = {
                    navController.navigate(Screen.Camera.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Camera.route) {
            CameraScreen(
                onPhotoTaken = { uri ->
                    navController.navigate(Screen.Analysis.createRoute(uri))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.Analysis.route,
            arguments = listOf(navArgument("photoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val photoUri = backStackEntry.arguments?.getString("photoUri")?.let {
                Uri.decode(it)
            } ?: ""
            AnalysisScreen(
                photoUri = photoUri,
                preferencesRepository = preferencesRepository,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                preferencesRepository = preferencesRepository,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRetakeTest = {
                    navController.navigate(Screen.ColorTest.route)
                }
            )
        }
    }
}
