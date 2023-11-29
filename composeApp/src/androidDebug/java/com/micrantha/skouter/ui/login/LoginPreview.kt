package com.micrantha.skouter.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.bluebell.domain.model.Ready
import com.micrantha.skouter.ui.PreviewContext

@Preview
@Composable
fun LoginPreview() = PreviewContext(
    LoginUiState(
        email = "account@example.com",
        password = "P@ssw0rd123",
        status = Ready()
    )
) {
    LoginScreen()
}
