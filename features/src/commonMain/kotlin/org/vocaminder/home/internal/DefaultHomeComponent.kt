package org.vocaminder.home.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vocaminder.home.api.HomeComponent
import org.vocaminder.home.api.HomeState
import org.vocaminder.settings.api.SettingsComponent
import org.vocaminder.settings.api.buildSettingComponent
import org.vocaminder.settings.internal.SettingStore

class DefaultHomeComponent(
    componentContext: ComponentContext, private val storeFactory: StoreFactory,
    override val onNavigation: (HomeComponent.NavigationEvent) -> Unit
) :
    HomeComponent, ComponentContext by componentContext {

    private val homeStore = instanceKeeper.getStore { HomeStoreFactory(storeFactory).create() }

    override val state: StateFlow<HomeState>
        get() = homeStore.stateFlow(lifecycle)

    override val settingComponent: SettingsComponent
        get() = buildSettingComponent(childContext("SettingComponent"), storeFactory)

    override fun onHomeStoreIntent(intent: HomeStore.Intent) {
        homeStore.accept(intent)
    }

    override fun onSettingStoreIntent(intent: SettingStore.Intent) {
        TODO("Not yet implemented")
    }


}