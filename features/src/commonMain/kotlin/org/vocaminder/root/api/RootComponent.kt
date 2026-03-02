package org.vocaminder.root.api

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import org.vocaminder.crudwordset.api.CRUDWordSetComponent
import org.vocaminder.home.api.HomeComponent

interface RootComponent: BackHandlerOwner {

    val childStack: Value<ChildStack<*, Child>>

    val childSlot: Value<ChildSlot<*, SlotChild>>

    fun dismissSlotChild()

    sealed class SlotChild {
        class Settings(val component: ComponentContext) : SlotChild()
    }

    fun onBackClicked()


    sealed class Child {
        data class MainPage(val component: HomeComponent)  : Child()
        data class SourceSets (val component: ComponentContext) : Child()
        data class CreateEditSet (val component: CRUDWordSetComponent) : Child()
    }
}