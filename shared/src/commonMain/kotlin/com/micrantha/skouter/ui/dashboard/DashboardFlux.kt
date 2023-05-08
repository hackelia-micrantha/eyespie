package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer

sealed class DashboardActions {
    object Load
}

class DashboardReducer : Reducer<DashboardState> {
    override fun invoke(state: DashboardState, action: Action): DashboardState {
        return when (action) {
            else -> state
        }
    }
}

class DashboardEffects : Effect<DashboardState> {
    override suspend fun invoke(action: Action, state: DashboardState) {

    }
}
