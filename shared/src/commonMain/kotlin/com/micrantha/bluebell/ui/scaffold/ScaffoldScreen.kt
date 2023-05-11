package com.micrantha.bluebell.ui.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.BackHandler
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.navi.NavigationAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen(
    viewContext: ViewContext,
    state: ScaffoldState,
    content: @Composable () -> Unit
) {
    BackHandler(state.backAction?.enabled ?: false) {
        viewContext.navigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    state.title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                },
                navigationIcon = {
                    state.backAction?.let {
                        NavigationAction(
                            viewContext = viewContext,
                            navAction = it
                        )
                    }
                },
                actions = {
                    state.actions?.forEach { nav ->
                        NavigationAction(
                            modifier = Modifier.padding(start = Dimensions.content),
                            viewContext = viewContext,
                            navAction = nav
                        )
                    }
                }
            )
        }
    ) { offsets ->
        Surface(
            modifier = Modifier.padding(
                top = offsets.calculateTopPadding(),
                bottom = offsets.calculateBottomPadding()
            )
        ) {
            content()
        }
    }
}
