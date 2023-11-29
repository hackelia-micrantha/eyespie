package com.micrantha.skouter.ui.dashboard.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.domain.i18n.stringResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> DashboardTabContent(
    data: List<T>,
    headerStr: LocalizedString? = null,
    onEmpty: @Composable () -> Unit = {},
    hasMore: Boolean,
    onHasMore: () -> Unit = {},
    onItem: @Composable (T) -> Unit
) {
    when {
        data.isEmpty() -> onEmpty()
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            headerStr?.let { str ->
                stickyHeader {
                    ListHeader(label = stringResource(str))
                }
            }
            items(data) { thing ->
                onItem(thing)
            }
            if (hasMore) {
                item {
                    HasMoreFooter(onClick = onHasMore)
                }
            }
        }
    }
}
