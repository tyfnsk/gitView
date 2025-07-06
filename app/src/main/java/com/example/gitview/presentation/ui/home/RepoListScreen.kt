package com.example.gitview.presentation.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.gitview.domain.model.Repo


@Composable
fun RepoListScreen(
    viewModel: RepoListViewModel = hiltViewModel()
) {
    val pagingItems = viewModel.repos.collectAsLazyPagingItems()

    when (pagingItems.loadState.refresh) {
        is LoadState.Loading -> FullScreenLoading()
        is LoadState.Error   -> {
            val err = (pagingItems.loadState.refresh as LoadState.Error).error
            ErrorView(err.localizedMessage ?: "Bir hata oluştu") { pagingItems.retry() }
        }
        else -> RepoListContent(pagingItems)
    }
}

@Composable
private fun RepoListContent(pagingItems: LazyPagingItems<Repo>) {
    LazyColumn(Modifier.fillMaxSize()) {
        items(
            count = pagingItems.itemCount,
            key = { idx -> pagingItems[idx]?.id ?: idx }   // daha stabil scroll için
        ) { index ->
            pagingItems[index]?.let { RepoListItem(it) }
        }

        if (pagingItems.loadState.append is LoadState.Loading) {
            item { LoadingIndicator() }
        }
    }
}

@Composable
fun FullScreenLoading() = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }

@Composable
fun ErrorView(msg: String, onRetry: () -> Unit) = Column(
    Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(msg); Spacer(Modifier.height(8.dp))
    Button(onClick = onRetry) { Text("Yeniden Dene") }
}

@Composable
fun RepoListItem(repo: Repo) {
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        AsyncImage(model = repo.ownerAvatarUrl, contentDescription = null, modifier = Modifier.size(48.dp), contentScale = ContentScale.Crop)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            androidx.compose.material3.Text(text = repo.fullName)
            repo.description?.let {
                androidx.compose.material3.Text(text = it, maxLines = 2, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
        androidx.compose.material3.CircularProgressIndicator()
    }
}