package com.micrantha.skouter.ui.scan.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.ui.scaffold.ScaffoldScreen
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.ui.component.ChoiceField
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.component.S
import com.micrantha.skouter.ui.navi.NavAction
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.CustomLabelChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.Init
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.LabelChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.NameChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.SaveScanEdit

class ScanEditScreen(
    private val arg: ScanEditArg,
    private val context: ScreenContext
) : ScaffoldScreen() {

    override fun title() = context.i18n.string(S.NewThing)

    override val canGoBack = true

    override fun actions() = listOf(
        NavAction(
            icon = Icons.Default.Save,
            action = {
                it.dispatcher.dispatch(SaveScanEdit)
            }
        )
    )

    @Composable
    override fun Render() {
        val viewModel: ScanEditScreenModel = rememberScreenModel()

        LaunchedEffect(Unit) {
            viewModel.dispatch(Init(arg))
        }

        LocationEnabledEffect()

        val state by viewModel.state.collectAsState()

        Render(state, viewModel::dispatch)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Render(state: ScanEditUiState, dispatch: Dispatch) {

        Box(
            modifier = Modifier.fillMaxSize().padding(Dimensions.screen),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = Dimensions.content)
                        .fillMaxWidth(),
                    value = state.name,
                    onValueChange = { dispatch(NameChanged(it)) },
                    label = { Text(text = "Name") },
                    singleLine = true,
                    maxLines = 1,
                    placeholder = { Text(text = "Please enter an identifying name for this thing") }
                )

                if (state.labels.isNotEmpty()) {
                    ChoiceField(
                        label = { Text(text = "What") },
                        choices = state.labels,
                        onValue = {
                            state.customLabel ?: it.label
                        },
                        onCustom = {
                            dispatch(CustomLabelChanged(it))
                        }
                    ) { choice ->
                        dispatch(LabelChanged(choice))
                    }
                }
            }
            Button(
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(Dimensions.content),
                onClick = { dispatch(SaveScanEdit) }
            ) {
                Text("Save")
            }
        }
    }
}
