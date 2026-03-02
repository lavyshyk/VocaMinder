package org.vocaminder.settings.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.states
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.vocaminder.settings.api.SettingsComponent

class DefaultSettingsComponent(componentContext: ComponentContext, storeFactory: StoreFactory) : SettingsComponent,
    ComponentContext by componentContext {

    private val settingStore = instanceKeeper.getStore { SettingStoreFactory(storeFactory).create() }

    override val state: Flow<SettingBottomSheet>
        get() = settingStore.states.map { it.settingBottomSheet }


    init {
        this.backHandler.register(BackCallback {
            if (settingStore.state.settingBottomSheet != SettingBottomSheet.None) {
                settingStore.accept(SettingStore.Intent.CloseDialog)
            }
        })
    }


    override fun doAction(action: SettingsComponent.Action) {
        when(action){
            is SettingsComponent.Action.OpensSetting -> settingStore.accept(SettingStore.Intent.OpenSettings)
        }
    }


}