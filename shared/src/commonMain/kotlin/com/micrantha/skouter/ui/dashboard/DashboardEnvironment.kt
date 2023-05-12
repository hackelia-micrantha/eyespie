package com.micrantha.skouter.ui.dashboard

import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer

class DashboardEnvironment : Reducer<DashboardState>, Effect<DashboardState> {
    override fun reduce(state: DashboardState, action: Action): DashboardState {
        return when (action) {
            else -> state
        }
    }

    override suspend fun invoke(action: Action, state: DashboardState) {

    }
}
