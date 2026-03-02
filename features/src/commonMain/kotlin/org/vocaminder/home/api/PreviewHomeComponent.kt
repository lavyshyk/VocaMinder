package org.vocaminder.home.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vocaminder.home.internal.HomeStore
import org.vocaminder.settings.api.PreviewSettingsComponent
import org.vocaminder.settings.api.SettingsComponent
import org.vocaminder.settings.internal.SettingStore
import org.voceminder.core.feature.PreviewComponentContext

object PreviewHomeComponent : HomeComponent , ComponentContext by PreviewComponentContext {
    override val state: StateFlow<HomeState>
        get() = MutableStateFlow(HomeState.previewDefaultState())
    override val settingComponent: SettingsComponent
        get() = PreviewSettingsComponent
    override val onNavigation: (HomeComponent.NavigationEvent) -> Unit
        get() = {}

    override fun onHomeStoreIntent(intent: HomeStore.Intent) {}

    override fun onSettingStoreIntent(intent: SettingStore.Intent) {}
}