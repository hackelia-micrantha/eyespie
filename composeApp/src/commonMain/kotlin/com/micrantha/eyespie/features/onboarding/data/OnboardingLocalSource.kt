package com.micrantha.eyespie.features.onboarding.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.micrantha.bluebell.platform.Platform
import okio.Path.Companion.toPath

class OnboardingLocalSource(private val platform: Platform) {
    val dataStore: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { platform.dataStorePath("onboarding.preferences_pb").toPath() }
        )
    }
}
