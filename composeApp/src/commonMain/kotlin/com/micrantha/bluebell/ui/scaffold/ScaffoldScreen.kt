package com.micrantha.bluebell.ui.scaffold

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.ui.components.rememberConnectivityStatus
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.eyespie.ui.navi.NavAction
import com.micrantha.eyespie.ui.navi.NavigationAction

internal fun defaultBackAction() = NavAction(
    icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    action = { context -> context.router.navigateBack() }
)

abstract class ScaffoldScreen : Screen, Scaffolding {

    @Composable
    abstract fun Render()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val context = LocalScreenContext.current

        val connectivityStatus by rememberConnectivityStatus()

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
                if (connectivityStatus.not()) {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(Dimensions.screen).background(
                            Color(255, 103, 0)
                        ), text = "No network connection available"
                    )
                }
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
