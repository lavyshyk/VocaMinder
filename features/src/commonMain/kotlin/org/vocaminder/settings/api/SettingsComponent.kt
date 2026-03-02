package org.vocaminder.settings.api

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.compose.resources.DrawableResource
import org.vocaminder.settings.internal.DefaultSettingsComponent
import org.vocaminder.settings.internal.SettingBottomSheet
import org.vocaminder.settings.internal.SettingStore

fun buildSettingComponent(componentContext: ComponentContext, storeFactory: StoreFactory): SettingsComponent =
    DefaultSettingsComponent(componentContext, storeFactory)

interface SettingsComponent {

    val state: Flow<SettingBottomSheet>

    fun doAction(action: Action)

    sealed interface Action{
        data class OpensSetting(val icon: DrawableResource): Action
    }
}

object PreviewSettingsComponent : SettingsComponent {
    override val state: Flow<SettingBottomSheet>
        get() = flow { SettingStore.State() }

    override fun doAction(action: SettingsComponent.Action) {

    }
}