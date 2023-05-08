package com.micrantha.skouter.ui.login

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.bluebell.Platform
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.arch.StoreFactory
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.model.ResultStatus
import com.micrantha.bluebell.domain.model.ResultStatus.Default
import com.micrantha.bluebell.ui.navi.LocalRouter
import com.micrantha.bluebell.ui.navi.Route
import com.micrantha.bluebell.ui.navi.RouteContext
import com.micrantha.bluebell.ui.navi.RouteRenderer
import com.micrantha.bluebell.ui.navi.Router
import com.micrantha.bluebell.ui.view.LocalViewContext
import com.micrantha.bluebell.ui.view.ViewContext
import com.micrantha.skouter.ui.PreviewContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Preview
@Composable
fun LoginPreview() = PreviewContext {
    LoginContent(
        state = LoginUiState(
            email = "",
            password = "",
            isLoggedIn = false,
            status = Default
        )
    ) {}
}
