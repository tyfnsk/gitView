package com.example.gitview.presentation.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.example.gitview.domain.model.Repo
import com.example.gitview.presentation.ui.repolist.LoadingIndicator

/*--------------------------------------------------------*/
/*  List content                                          */
/*--------------------------------------------------------*/

@Composable
fun FavoriteRepo(
    navController: NavController,
    favoriteRepos: List<Repo>,
    isLoading: Boolean
) {
    Column {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
            ) {
                items(favoriteRepos) { repo ->
                    RepoListItem(repo) {
                        navController.navigate("detail/${repo.ownerName}/${repo.name}")
                    }
                }
            }
        }
    }
}

/*--------------------------------------------------------*/
/*  Repo card                                             */
/*--------------------------------------------------------*/
@Composable
fun RepoListItem(
    repo: Repo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape   = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        modifier = Modifier
            .width(300.dp)
            .height(120.dp)
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF6F6F6))
                    )
                )
                .padding(16.dp)
        ) {
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(repo.fullName, style = MaterialTheme.typography.titleMedium)
                repo.description?.let {
                    Text(it, style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2, modifier = Modifier.padding(top = 4.dp))
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                    Spacer(Modifier.width(4.dp))
                    Text(repo.stars.toString(), style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.width(16.dp))
                    Icon(Icons.Default.CallSplit, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(repo.forks.toString(), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}