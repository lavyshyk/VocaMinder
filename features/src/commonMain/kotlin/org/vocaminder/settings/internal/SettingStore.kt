package org.vocaminder.settings.internal

import androidx.compose.runtime.Stable
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.coroutines.Dispatchers
import org.vocaminder.settings.internal.SettingStore.*

interface SettingStore : Store<Intent, State, Nothing> {

    sealed interface Intent {
        data object OpenSettings : Intent
        data object OpenUserSettings : Intent
        data object CloseDialog : Intent
    }

    data class State(
        val settingBottomSheet: SettingBottomSheet = SettingBottomSheet.None
    )
}
sealed interface Message {
    data class OpenDialog(val setting: SettingBottomSheet) : Message
    data class CloseDialog(val setting: SettingBottomSheet.None) : Message
}


@Stable
sealed interface SettingBottomSheet {

    data object UserSetting : SettingBottomSheet
    data object Settings : SettingBottomSheet
    data object None : SettingBottomSheet
}


internal class SettingStoreFactory(
    private val storeFactory: StoreFactory
) {
    fun create(): SettingStore = object : SettingStore, Store<Intent, State, Nothing> by storeFactory.create(
            initialState = State(),
            executorFactory = coroutineExecutorFactory(Dispatchers.Main) { },
            reducer = { message: Message ->
                when (message) {
                    is Message.CloseDialog -> copy(settingBottomSheet = SettingBottomSheet.None)
                    is Message.OpenDialog -> copy(settingBottomSheet = SettingBottomSheet.Settings)
                }
            }
        ){}
}