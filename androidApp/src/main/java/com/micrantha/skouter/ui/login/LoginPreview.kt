package com.micrantha.skouter.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.skouter.ui.PreviewContext

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
