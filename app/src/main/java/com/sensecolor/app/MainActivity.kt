package com.sensecolor.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.sensecolor.app.data.datastore.dataStore
import com.sensecolor.app.data.repository.UserPreferencesRepository
import com.sensecolor.app.ui.navigation.Screen
import com.sensecolor.app.ui.navigation.SenseColorNavGraph
import com.sensecolor.app.ui.theme.SenseColorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferencesRepository = UserPreferencesRepository(dataStore)
        enableEdgeToEdge()
        setContent {
            SenseColorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userPreferences by preferencesRepository.userPreferencesFlow
                        .collectAsState(initial = null)

                    if (userPreferences != null) {
                        val prefs = userPreferences!!
                        val navController = rememberNavController()
                        val startDestination = if (prefs.hasCompletedOnboarding) {
                            Screen.Camera.route
                        } else {
                            Screen.Onboarding.route
                        }
                        SenseColorNavGraph(
                            navController = navController,
                            startDestination = startDestination,
                            preferencesRepository = preferencesRepository
                        )
                    } else {
                        // Loading state while preferences are being read
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            androidx.compose.material3.Text(
                                text = "Loading...",
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.align(Alignment.BottomCenter)
                                    .then(Modifier.padding(bottom = 120.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}
