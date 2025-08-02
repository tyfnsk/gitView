package com.example.gitview.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.gitview.presentation.ui.category.CategoryGrid
import com.example.gitview.presentation.ui.home.components.FavoriteRepo

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val favoriteRepos by viewModel.favoriteRepos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEAEAEA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(0f)
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF00C6AE), Color(0xFF5BCEFA))
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = 30.dp,
                            bottomEnd = 30.dp
                        )
                    )
                    .padding(20.dp, 50.dp, 20.dp, 0.dp)
            ) {
                Text(
                    text = "Git Repos",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = {
                        navController.navigate("repolist/android")
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
                Text(
                    text = "Favorites",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.offset(y = 50.dp)
                )
            }

            Spacer(modifier = Modifier.height(80.dp)) // header altındaki boşluğu dengelemek için
            Text(
                text = "Categories",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(start = 20.dp, bottom = 2.dp)
            )

            // Category Grid
            CategoryGrid(
                categories = categories,
                onCategoryClick = { selected ->
                    val query = if (selected.languages.isNotEmpty()) {
                        selected.languages.joinToString(" ") { "language:$it" }
                    } else {
                        "stars:>1" // Others için örnek sorgu
                    }
                    viewModel.setQuery(query)
                    navController.navigate("repolist/$query")
                }
            )
        }

        // FAVORITES - header üzerine taşan
        FavoriteRepo(
            navController = navController,
            favoriteRepos = favoriteRepos,
            isLoading = isLoading,
            modifier = Modifier
                .offset(y = 150.dp)
                .fillMaxWidth()
                .zIndex(1f)
        )
    }
}

