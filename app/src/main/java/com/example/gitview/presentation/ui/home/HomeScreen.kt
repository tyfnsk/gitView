package com.example.gitview.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gitview.domain.model.Repo
import com.example.gitview.presentation.ui.home.components.CategoryCard
import com.example.gitview.presentation.ui.home.components.RepoCard
import com.example.gitview.presentation.ui.home.components.FavoriteRepo

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Favoriler", "Son Açılan")
    val categories = listOf("Android")

    val pagingItems = viewModel.repos.collectAsLazyPagingItems()

    val favoriteRepos by viewModel.favoriteRepos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()


    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFF00C6AE), Color(0xFF5BCEFA))
                    ),
                            shape = RoundedCornerShape(
                            topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 30.dp, // Adjust this value for desired bottom-left corner radius
                    bottomEnd = 30.dp   // Adjust this value for desired bottom-right corner radius
                )
                )
                .padding(20.dp, 50.dp, 20.dp, 0.dp)
                .height(150.dp)
        ) {
            Text(
                text = "Git Repos",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = {
                    navController.navigate("repolist")
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
                modifier = Modifier.offset(y = 60.dp)
            )

        }

        Box(
            modifier = Modifier.offset(0.dp,-50.dp)
        ){
            FavoriteRepo(
                navController = navController,
                favoriteRepos = favoriteRepos,
                isLoading = isLoading
            )
        }


    }
}
