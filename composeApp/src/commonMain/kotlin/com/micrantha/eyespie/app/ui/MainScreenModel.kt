package com.micrantha.eyespie.app.ui

import com.micrantha.bluebell.app.Log
import com.micrantha.bluebell.app.d
import com.micrantha.bluebell.arch.Action
import com.micrantha.bluebell.ui.screen.ContextualScreenModel
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.app.ui.MainAction.Load
import com.micrantha.eyespie.app.ui.usecase.LoadMainUseCase
import kotlinx.coroutines.launch

class MainScreenModel(
    context: ScreenContext,
    private val loadMainUseCase: LoadMainUseCase
) : ContextualScreenModel(context) {


    override fun dispatch(action: Action) {
        dispatchScope.launch {
            send(action)
        }
    }

    override suspend fun send(action: Action) {
        when (action) {
            is Load -> loadMainUseCase()
            else -> Log.d(tag = "main", "unknown action $action")
        }
    }
}
