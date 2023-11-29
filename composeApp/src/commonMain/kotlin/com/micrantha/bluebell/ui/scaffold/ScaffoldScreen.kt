package com.micrantha.bluebell.ui.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.navi.NavAction
import com.micrantha.skouter.ui.navi.NavigationAction

internal fun defaultBackAction() = NavAction(
    icon = Icons.Default.KeyboardArrowLeft,
    action = { context -> context.router.navigateBack() }
)

abstract class ScaffoldScreen : Screen, Scaffolding {

    @Composable
    abstract fun Render()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val context = LocalScreenContext.current

        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.shadow(Dimensions.content),
                    title = {
                        title()?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    },
                    navigationIcon = {
                        if (showBack) {
                            backAction()?.let {
                                NavigationAction(
                                    navAction = it
                                )
                            } ?: run {
                                if ((canGoBack == null && context.router.canGoBack) || canGoBack == true) {
                                    NavigationAction(
                                        navAction = defaultBackAction()
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        actions()?.forEach { nav ->
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
                Render()
            }
        }
    }
}
