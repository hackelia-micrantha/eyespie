package com.micrantha.skouter.presentation.view

import androidx.lifecycle.viewModelScope as androidxViewModelScope
import androidx.lifecycle.ViewModel as AndroidXViewModel
import kotlinx.coroutines.CoroutineScope

actual abstract class ViewModel : AndroidXViewModel() {

    actual val viewModelScope: CoroutineScope = androidxViewModelScope
}
