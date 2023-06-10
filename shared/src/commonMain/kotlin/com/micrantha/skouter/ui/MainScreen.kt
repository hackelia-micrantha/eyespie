package com.micrantha.skouter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.ui.theme.Dimensions.List
import com.micrantha.skouter.ui.MainAction.Load

class SplashScreen : Screen {
    @Composable
    override fun Content() {
        SplashScreenContent()
    }
}

class MainScreen : Screen {

    @Composable
    override fun Content() {

        val model = rememberScreenModel<MainScreenModel>()

        LaunchedEffect(Unit) {
            model.dispatch(Load)
        }

        SplashScreenContent()
    }
}

@Composable
private fun SplashScreenContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
            imageVector = Skouter.defaultIcon,
            modifier = Modifier.size(List.placeholder),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
    }
}
