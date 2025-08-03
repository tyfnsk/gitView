package com.example.gitview.presentation.ui.repolist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.gitview.domain.model.Repo
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.runtime.LaunchedEffect


@Composable
fun RepoListScreen(
    navController: NavController,
    initialQuery: String,
    viewModel: RepoListViewModel = hiltViewModel()
) {
    val query by viewModel.query.collectAsState()
    val pagingItems = viewModel.repos.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        }
    }

    Column(Modifier.fillMaxSize()) {

        val isQueryTechnical = query.contains("language:") || query.contains("stars:") || query.contains("android")
        ModernSearchBar(
            query = if (isQueryTechnical) "" else query,
            onQueryChange = viewModel::search
        )

        when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> FullScreenLoading()
            is LoadState.Error   -> {
                val err = (pagingItems.loadState.refresh as LoadState.Error).error
                ErrorView(err.localizedMessage ?: "Bir hata oluştu") { pagingItems.retry() }
            }
            else -> RepoListContent(navController, pagingItems)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernSearchBar(query: String, onQueryChange: (String) -> Unit) {

    // FocusRequester define
    val focusRequester = remember { FocusRequester() }

    // give focus when screen open
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

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
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
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

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text("Search repositories…", color = Color.White) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null,tint = Color.White) },
            textStyle = LocalTextStyle.current.copy(color = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 60.dp, 16.dp, 0.dp)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(24.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White,
                focusedIndicatorColor = Color.White,
                cursorColor = Color.White
            )
        )
    }
}

@Composable
private fun RepoListContent(
    navController: NavController,
    pagingItems: LazyPagingItems<Repo>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        items(
            count = pagingItems.itemCount,
            key   = { idx -> pagingItems[idx]?.id ?: idx }
        ) { index ->
            pagingItems[index]?.let { repo ->
                RepoListItem(repo) {
                    navController.navigate("detail/${repo.ownerName}/${repo.name}")
                }
            }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item { LoadingIndicator() }
        }
    }
}


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
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(Modifier.padding(14.dp)) {
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

@Composable
fun FullScreenLoading() = Box(
    Modifier.fillMaxSize(), contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun LoadingIndicator() = Box(
    Modifier.fillMaxWidth().padding(16.dp),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun ErrorView(msg: String, onRetry: () -> Unit) = Column(
    Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(msg)
    Spacer(Modifier.height(8.dp))
    Button(onClick = onRetry) { Text("Try Again") }
}