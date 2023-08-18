package com.micrantha.skouter.ui.scan.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.micrantha.skouter.ui.PreviewContext

@Preview(showBackground = true, backgroundColor = 0xFF)
@Composable
fun ScanPreview() = PreviewContext(
    ScanUiState(
        clues = listOf(),
        overlays = listOf(),
        enabled = true,
        capture = null
    )
) {
    ScanScreen()
}
