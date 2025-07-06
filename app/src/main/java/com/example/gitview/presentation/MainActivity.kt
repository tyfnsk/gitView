package com.example.gitview.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gitview.presentation.ui.home.RepoListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GitViewApp()
        }
    }
}


@Composable
fun GitViewApp() {
    MaterialTheme {
        Surface {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") { RepoListScreen() }
            }
        }
    }
}