package org.vocaminder.crudwordset.impl

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vocaminder.crudwordset.api.CRUDSetState
import org.vocaminder.crudwordset.api.CRUDSetStore
import org.vocaminder.crudwordset.api.CRUDSetStoreFactory
import org.vocaminder.crudwordset.api.CRUDWordSetComponent
import org.vocaminder.home.api.WordSet

class DefaultCRUDWordSetComponent(
    private val wordSet: WordSet? = null,
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory, override val onNavigation: (CRUDWordSetComponent.NavigationEvent) -> Unit,
) : CRUDWordSetComponent,
    ComponentContext by componentContext {

    val store = instanceKeeper.getStore {
        CRUDSetStoreFactory(
            editableWordSet = wordSet,
            storeFactory
        ).create()
    }

    override val state: StateFlow<CRUDSetState> get() = store.stateFlow(lifecycle)
    override fun onCRUDSetStoreIntent(intent: CRUDSetStore.Intent) = store.accept(intent)

}