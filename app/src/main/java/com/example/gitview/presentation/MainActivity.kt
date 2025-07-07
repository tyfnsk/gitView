package com.example.gitview.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gitview.presentation.ui.detail.RepoDetailScreen
import com.example.gitview.presentation.ui.home.RepoListScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { GitViewApp() }
    }
}

@Composable
fun GitViewApp() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "home"
            ) {

                // HOME – repo listesi
                composable("home") {
                    RepoListScreen(navController)   // navController’ı geçiriyoruz
                }

                // DETAIL – owner ve repo adı argümanlı
                composable(
                    route = "detail/{owner}/{repo}",
                    arguments = listOf(
                        navArgument("owner") { type = NavType.StringType },
                        navArgument("repo")  { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val owner = backStackEntry.arguments!!.getString("owner")!!
                    val repo  = backStackEntry.arguments!!.getString("repo")!!
                    RepoDetailScreen(owner = owner, repo = repo)
                }
            }
        }
    }
}