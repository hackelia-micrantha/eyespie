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
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.rememberStore
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.navi.NavigationAction

@Composable
fun rememberScaffoldStore(): Lazy<Store<ScaffoldState>> {
    val store = rememberStore(ScaffoldState())
    return lazy { store.addReducer(::scaffoldReducer) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen(
    state: ScaffoldState,
    content: @Composable () -> Unit
) {
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
                            navAction = it
                        )
                    }
                },
                actions = {
                    state.actions?.forEach { nav ->
                        NavigationAction(
                            modifier = Modifier.padding(start = Dimensions.content),
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
