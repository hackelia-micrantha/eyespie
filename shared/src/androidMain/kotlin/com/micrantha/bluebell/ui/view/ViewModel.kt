package com.micrantha.bluebell.ui.view

import com.micrantha.bluebell.ui.components.ScreenVisibility
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel as AndroidXViewModel
import androidx.lifecycle.viewModelScope as androidxViewModelScope

actual abstract class ViewModel : AndroidXViewModel(), ScreenVisibility {

    actual val viewModelScope: CoroutineScope = androidxViewModelScope
}
