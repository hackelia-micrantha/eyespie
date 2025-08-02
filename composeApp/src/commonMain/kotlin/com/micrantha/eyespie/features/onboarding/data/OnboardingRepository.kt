package com.micrantha.eyespie.features.onboarding.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first

class OnboardingRepository(
    private val localSource: OnboardingLocalSource
) {
    val hasRunOnce = booleanPreferencesKey("has_run_once")

    suspend fun setHasRunOnce() {
        localSource.dataStore.edit { prefs ->
            prefs[hasRunOnce] = true
        }
    }

    suspend fun hasRunOnce(): Boolean {
        val prefs = localSource.dataStore.data.first()
        return prefs[hasRunOnce] ?: false
    }
}
