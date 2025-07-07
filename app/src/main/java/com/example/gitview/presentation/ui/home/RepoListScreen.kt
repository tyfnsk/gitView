package com.example.gitview.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.filled.CallSplit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.gitview.domain.model.Repo


@Composable
fun RepoListScreen(viewModel: RepoListViewModel = hiltViewModel()) {
    val query by viewModel.query.collectAsState()
    val pagingItems = viewModel.repos.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        ModernSearchBar(query, onQueryChange = viewModel::search)

        when (pagingItems.loadState.refresh) {
            is LoadState.Loading -> FullScreenLoading()
            is LoadState.Error -> {
                val err = (pagingItems.loadState.refresh as LoadState.Error).error
                ErrorView(err.localizedMessage ?: "Bir hata oluştu") { pagingItems.retry() }
            }
            else -> RepoListContent(pagingItems)
        }
    }
}

// -----------------------------
// Modern SearchBar
// -----------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernSearchBar(query: String, onQueryChange: (String) -> Unit) {
    var active by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { active = false },
        active = active,
        onActiveChange = { active = it },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        placeholder = { Text("Search repositories…") },
        shape = RoundedCornerShape(24.dp),
        tonalElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {}
}

// -----------------------------
// List content
// -----------------------------
@Composable
private fun RepoListContent(pagingItems: LazyPagingItems<Repo>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp, bottom = 16.dp)
    ) {
        items(
            count = pagingItems.itemCount,
            key = { idx -> pagingItems[idx]?.id ?: idx }
        ) { index ->
            pagingItems[index]?.let { RepoListItem(it) }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item { LoadingIndicator() }
        }
    }
}

@Composable
fun RepoListItem(repo: Repo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(modifier = Modifier.padding(14.dp)) {
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = repo.fullName, style = MaterialTheme.typography.titleMedium)
                repo.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
                    Spacer(Modifier.width(4.dp))
                    Text(text = repo.stars.toString(), style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.width(16.dp))
                    Icon(Icons.Default.CallSplit, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text(text = repo.forks.toString(), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

// -----------------------------
// Loading & Error helpers
// -----------------------------
@Composable
fun FullScreenLoading() = Box(
    modifier = Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun LoadingIndicator() = Box(
    modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun ErrorView(msg: String, onRetry: () -> Unit) = Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(text = msg)
    Spacer(Modifier.height(8.dp))
    Button(onClick = onRetry) { Text("Yeniden Dene") }
}