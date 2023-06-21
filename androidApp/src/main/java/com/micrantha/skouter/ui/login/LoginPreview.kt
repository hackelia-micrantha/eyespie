package com.micrantha.skouter.ui.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.bluebell.domain.model.UiResult.Default
import com.micrantha.skouter.ui.PreviewContext
import org.kodein.di.bind
import org.kodein.di.compose.localDI
import org.kodein.di.direct
import org.kodein.di.instance
import org.kodein.di.provider

@Preview
@Composable
fun LoginPreview() = PreviewContext(
    bindings = {
        bind(overrides = true) {
            provider {
                LoginScreenModel(
                    instance(), instance(),
                    LoginState(
                        email = "",
                        hash = "",
                        status = Default
                    )
                )
            }
        }
    }
) {
    localDI().direct.instance<LoginScreen>().Content()
}
