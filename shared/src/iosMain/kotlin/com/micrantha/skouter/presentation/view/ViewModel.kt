package com.micrantha.skouter.presentation.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

actual abstract class ViewModel {
    actual val viewModelScope: CoroutineScope = CoroutineScope(Dispatchers.Default + Job())
}
