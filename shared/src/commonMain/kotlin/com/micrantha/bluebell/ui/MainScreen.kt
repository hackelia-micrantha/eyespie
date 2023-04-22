package com.micrantha.bluebell.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.micrantha.bluebell.ui.navi.NavigationRoutes
import com.micrantha.bluebell.ui.navi.NavigationScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    routes: NavigationRoutes
) {
    val state by viewModel.state().collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    state.title?.let { MainTitle(it) }
                },
                navigationIcon = {
                    if (state.showBack) {
                        MainBackButton(state) {
                            viewModel.router().back()
                        }
                    }
                }
            )
        }
    ) {
        NavigationScreen(
            routes = routes,
            viewContext = viewModel.viewContext
        )
    }
}

@Composable
private fun MainTitle(title: String) =
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            color = Color.White
        )
    )

@Composable
private fun MainBackButton(state: MainState, onBack: () -> Unit) =
    IconButton(onClick = {
        state.onBack?.let { it() } ?: onBack()
    }) {
        Icon(
            Icons.Default.KeyboardArrowLeft,
            contentDescription = null,
            tint = Color.White
        )
    }

