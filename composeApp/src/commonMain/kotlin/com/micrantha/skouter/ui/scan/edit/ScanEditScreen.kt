package com.micrantha.skouter.ui.scan.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.ui.component.ChoiceField
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.component.S
import com.micrantha.skouter.ui.navi.NavAction
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.ClearLabel
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.ColorChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.CustomLabelChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.Init
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.LabelChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.NameChanged
import com.micrantha.skouter.ui.scan.edit.ScanEditAction.SaveScanEdit

class ScanEditScreen(
    private val proof: Proof,
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
        val screenModel: ScanEditScreenModel = rememberScreenModel()

        LaunchedEffect(Unit) {
            screenModel.dispatch(Init(proof))
        }

        LocationEnabledEffect()

        val state by screenModel.state.collectAsState()

        Render(state, screenModel)
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
                state.image?.let {
                    Image(
                        modifier = Modifier.size(Dimensions.Image.placeholder)
                            .align(Alignment.CenterHorizontally),
                        painter = it,
                        contentDescription = null,
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .padding(bottom = Dimensions.content)
                        .fillMaxWidth(),
                    value = state.name,
                    onValueChange = { dispatch(NameChanged(it)) },
                    label = { Text(text = "Name") },
                    singleLine = true,
                    maxLines = 1,
                    placeholder = { Text(text = "Enter an identifying name") }
                )

                if (state.labels.isNotEmpty()) {
                    ChoiceField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "What") },
                        choices = state.labels,
                        onValue = {
                            state.customLabel ?: it.label
                        },
                        trailingIcon = {
                            IconButton(onClick = { dispatch(ClearLabel) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        },
                        onCustom = {
                            dispatch(CustomLabelChanged(it))
                        }
                    ) { choice ->
                        dispatch(LabelChanged(choice))
                    }
                }

                if (state.colors.isNotEmpty()) {
                    ChoiceField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Color") },
                        choices = state.colors,
                        trailingIcon = {
                            IconButton(onClick = { dispatch(ClearLabel) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null
                                )
                            }
                        },
                        onValue = {
                            state.customColor ?: it.label
                        },
                    ) { choice ->
                        dispatch(ColorChanged(choice))
                    }
                }
            }
            Button(
                enabled = state.enabled,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(Dimensions.content),
                onClick = { dispatch(SaveScanEdit) }
            ) {
                Text("Save")
            }
        }
    }
}
