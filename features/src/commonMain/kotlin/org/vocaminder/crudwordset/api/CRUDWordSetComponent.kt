package org.vocaminder.crudwordset.api

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.flow.StateFlow
import org.vocaminder.crudwordset.impl.DefaultCRUDWordSetComponent
import org.vocaminder.home.api.WordPair
import org.vocaminder.home.api.WordSet
import org.vocaminder.home.internal.DefaultHomeComponent


fun buildComponent(componentContext: ComponentContext, storeFactory: StoreFactory, onNavigation:(CRUDWordSetComponent.NavigationEvent) -> Unit): CRUDWordSetComponent = DefaultCRUDWordSetComponent(
    componentContext = componentContext, storeFactory = storeFactory, onNavigation = onNavigation
)

interface CRUDWordSetComponent {

    val state: StateFlow<CRUDSetState>
    val onNavigation: (CRUDWordSetComponent.NavigationEvent) -> Unit
    fun onCRUDSetStoreIntent(intent: CRUDSetStore.Intent)

    interface NavigationEvent{
        data object OnBack : NavigationEvent
    }

}


@Stable
data class CRUDSetState(
    val wordSet: WordSet,
    val isLoading: Boolean = false,
    val currentWordPair: WordPair = WordPair("","","")
) {

}