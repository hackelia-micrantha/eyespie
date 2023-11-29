package com.micrantha.bluebell.ui.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun TabPager(
    vararg tabs: String,
    content: @Composable (Int, String) -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    TabRow(selectedTabIndex = currentIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(
                currentIndex == index,
                onClick = { currentIndex = index },
                text = { Text(title) }
            )
        }
    }

    content(currentIndex, tabs[currentIndex])
}
