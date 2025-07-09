package com.example.gitview.presentation.ui.detail

import android.content.Intent
import android.net.Uri
import android.webkit.WebView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.CallSplit
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.gitview.domain.model.Repo
import dev.jeziellago.compose.markdowntext.MarkdownText
import androidx.core.net.toUri


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    navController: NavController,
    owner: String,
    repo: String,
    viewModel: RepoDetailViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(owner, repo) {
        viewModel.load(owner, repo)
    }

    // AI Summary tab selected → trigger fetch
    LaunchedEffect(selectedTabIndex) {
        if (selectedTabIndex == 1) {
            viewModel.summarizeReadmeIfNeeded()
        }
    }

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$owner/$repo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (state) {
                is DetailUiState.Loading -> DetailLoading()
                is DetailUiState.Error -> DetailError((state as DetailUiState.Error).message) {
                    viewModel.load(owner, repo)
                }

                is DetailUiState.Success -> {
                    val success = state as DetailUiState.Success
                    DetailContent(
                        repo = success.repo,
                        summary = success.summary,
                        isSummaryLoading = success.isSummaryLoading,
                        selectedTabIndex = selectedTabIndex,
                        onTabChange = { selectedTabIndex = it }
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailLoading() = Box(
    Modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
) {
    CircularProgressIndicator()
}

@Composable
private fun DetailError(msg: String, onRetry: () -> Unit) = Column(
    Modifier.fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
) {
    Text(msg)
    Spacer(Modifier.height(8.dp))
    Button(onClick = onRetry) {
        Text("Try Again")
    }
}

@Composable
private fun DetailContent(
    repo: Repo,
    summary: String?,
    isSummaryLoading: Boolean,
    selectedTabIndex: Int,
    onTabChange: (Int) -> Unit
) {
    val tabs = listOf("README", "AI Summary")
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Repo Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = repo.ownerAvatarUrl,
                contentDescription = null,
                modifier = Modifier.size(72.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(repo.fullName, style = MaterialTheme.typography.headlineSmall)
                repo.language?.let {
                    Text(it, style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(repo.description ?: "(No description)", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFD700))
            Spacer(Modifier.width(4.dp))
            Text(repo.stars.toString())
            Spacer(Modifier.width(16.dp))
            Icon(Icons.AutoMirrored.Filled.CallSplit, contentDescription = null)
            Spacer(Modifier.width(4.dp))
            Text(repo.forks.toString())
            Spacer(Modifier.width(16.dp))
            ExtendedFloatingActionButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, repo.htmlUrl.toUri())
                    context.startActivity(intent)
                },
                modifier = Modifier.defaultMinSize(minHeight = 35.dp, minWidth = 0.dp)
            ) {
                Icon(Icons.Default.Public, contentDescription = "Open in browser")
                Spacer(Modifier.width(4.dp))
                Text("Open in browser", fontSize = 13.sp)
            }
        }

        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))

        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabChange(index) },
                    text = { Text(title) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        // Tab content
        when (selectedTabIndex) {
            0 -> {
                if (repo.readme.content.isNotBlank()) {
                    MarkdownText(markdown = repo.readme.content)
                } else {
                    Text(
                        "README content not found.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                }
            }

            1 -> {
                when {
                    isSummaryLoading -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    !summary.isNullOrBlank() -> {
                        Text(summary, style = MaterialTheme.typography.bodySmall)
                    }

                    else -> {
                        Text(
                            "AI summary not available.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    }
                }
            }
        }
    }
}