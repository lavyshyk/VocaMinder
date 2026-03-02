package org.vocaminder.settings.api

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.vocaminder.settings.internal.SettingBottomSheet
import org.voceminder.core.feature.PreviewComponentContext

class PreviewSettingComponent: SettingsComponent , ComponentContext by PreviewComponentContext {
    override val state: Flow<SettingBottomSheet>
        get() = MutableStateFlow(SettingBottomSheet.Settings)

    override fun doAction(action: SettingsComponent.Action) {}
}