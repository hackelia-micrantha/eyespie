package com.micrantha.eyespie.features.players.domain.usecase

import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.bluebell.ui.screen.navigate
import com.micrantha.eyespie.core.data.account.model.CurrentSession
import com.micrantha.eyespie.domain.entities.Session
import com.micrantha.eyespie.features.dashboard.ui.DashboardScreen
import com.micrantha.eyespie.features.login.ui.LoginScreen
import com.micrantha.eyespie.features.players.domain.entities.Player
import com.micrantha.eyespie.features.players.domain.repository.PlayerRepository
import com.micrantha.eyespie.features.players.ui.create.NewPlayerScreen

class LoadSessionPlayerUseCase(
    private val context: ScreenContext,
    private val playerRepository: PlayerRepository,
    private val currentSession: CurrentSession
) {
    suspend operator fun invoke(session: Session): Result<Player?> {
        try {
            currentSession.update(session)
            val player = playerRepository.player(session.userId).getOrNull()
            if (player != null) {
                currentSession.update(player)
            }
            return Result.success(player)
        } catch (e: Throwable) {
            return Result.failure(e)
        }
    }

    suspend fun withNavigation(session: Session, onError: (Throwable) -> Unit = {
        context.navigate<LoginScreen>(Router.Options.Replace)
    }) {
        invoke(session).onFailure {
            onError(it)
        }.onSuccess {
            if (it == null) {
                context.navigate<NewPlayerScreen>(Router.Options.Replace)
            } else {
                context.navigate<DashboardScreen>(Router.Options.Replace)
            }
        }
    }
}
