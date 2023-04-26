package com.micrantha.skouter.ui

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
import androidx.compose.ui.graphics.Color
import com.micrantha.bluebell.ui.navi.NavigationScreen
import com.micrantha.bluebell.ui.view.ViewContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainState,
    viewContext: ViewContext
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    state.title?.let { MainTitle(it) }
                },
                navigationIcon = {
                    if (state.showBack) {
                        MainBackButton(state) {
                            viewContext.router.back()
                        }
                    }
                }
            )
        }
    ) {
        NavigationScreen(
            viewContext = viewContext
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

