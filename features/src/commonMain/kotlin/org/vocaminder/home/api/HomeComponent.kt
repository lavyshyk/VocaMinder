package org.vocaminder.home.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.StateFlow
import org.vocaminder.home.internal.DefaultHomeComponent
import org.vocaminder.home.internal.HomeStore
import org.vocaminder.settings.api.SettingsComponent
import org.vocaminder.settings.internal.SettingStore

fun buildHomeComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    onNavigation: (HomeComponent.NavigationEvent) -> Unit

): HomeComponent =
    DefaultHomeComponent(
        componentContext,
        storeFactory,
        onNavigation
    )


@Stable
interface HomeComponent {
    val state: StateFlow<HomeState>
    val settingComponent: SettingsComponent

    val onNavigation: (NavigationEvent) -> Unit
    fun onHomeStoreIntent(intent: HomeStore.Intent)
    fun onSettingStoreIntent(intent: SettingStore.Intent)
//    fun onTopBarIntent(in)


    sealed interface NavigationEvent {
        data object CreateNewWordSet : NavigationEvent
        data class ChooseWordSet(val wordSet: WordSet) : NavigationEvent
        data class ResumeUnfinishedGame(val wordSet: WordSet) : NavigationEvent
        data class StartNewGame(val wordSet: WordSet) : NavigationEvent
        data class EditeWordSet(val wordSet: WordSet) : NavigationEvent

    }
}


