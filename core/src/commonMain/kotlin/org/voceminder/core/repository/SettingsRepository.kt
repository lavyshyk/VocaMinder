package org.voceminder.core.repository

import kotlinx.coroutines.flow.StateFlow
import org.voceminder.core.model.RocketSettings

interface SettingsRepository {
    val currentSettings: RocketSettings

    fun updateSettings(settings: RocketSettings)
    fun observeRocketSettings(): StateFlow<RocketSettings>
}