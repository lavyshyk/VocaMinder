package org.vocaminder.crudwordset.impl

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.vocaminder.crudwordset.api.CRUDSetState
import org.vocaminder.crudwordset.api.CRUDSetStore
import org.vocaminder.crudwordset.api.CRUDWordSetComponent
import org.vocaminder.home.api.WordSet

class PreviewCRUDWordSetComponent() : CRUDWordSetComponent {
    override val state: StateFlow<CRUDSetState> get() = MutableStateFlow(CRUDSetState(WordSet.new()))
    override val onNavigation: (CRUDWordSetComponent.NavigationEvent) -> Unit get() = {}

    override fun onCRUDSetStoreIntent(intent: CRUDSetStore.Intent) {}

}