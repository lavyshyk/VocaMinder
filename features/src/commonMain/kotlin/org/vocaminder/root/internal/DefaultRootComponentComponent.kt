package org.vocaminder.root.internal

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.serialization.Serializable
import org.vocaminder.crudwordset.api.buildComponent
import org.vocaminder.home.api.HomeComponent
import org.vocaminder.home.api.WordSet
import org.vocaminder.home.api.buildHomeComponent
import org.vocaminder.root.api.RootComponent

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<StackConfig>()

    override val childStack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = StackConfig.serializer(),
        handleBackButton = true,
        initialStack = { listOf(StackConfig.MainScreen) },
        childFactory = ::child,
    )

    private val slotNavigation = SlotNavigation<SlotConfig>()

    override val childSlot: Value<ChildSlot<*, RootComponent.SlotChild>> = childSlot(
        source = slotNavigation,
        serializer = SlotConfig.serializer(),
        handleBackButton = true,
        childFactory = ::child
    )

    override fun dismissSlotChild() {
        slotNavigation.dismiss()
    }

    override fun onBackClicked() {
        navigation.pop()
    }


    private fun child(
        config: StackConfig,
        ctx: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is StackConfig.MainScreen -> RootComponent.Child.MainPage(buildHomeComponent(ctx, storeFactory, { event ->
                when (event) {
                    is HomeComponent.NavigationEvent.ChooseWordSet -> TODO()
                    is HomeComponent.NavigationEvent.CreateNewWordSet -> navigation.pushNew(StackConfig.CreateEditSetScreen(null))
                    is HomeComponent.NavigationEvent.EditeWordSet -> TODO()
                    is HomeComponent.NavigationEvent.ResumeUnfinishedGame -> TODO()
                    is HomeComponent.NavigationEvent.StartNewGame -> TODO()
                }
            }))

            is StackConfig.SetsScreen -> RootComponent.Child.SourceSets(ctx)

            is StackConfig.CreateEditSetScreen -> RootComponent.Child.CreateEditSet(
                buildComponent(ctx, storeFactory, { _ -> })
            )

        }
    }

    private fun child(
        config: SlotConfig,
        ctx: ComponentContext
    ): RootComponent.SlotChild {
        return when (config) {
            SlotConfig.Settings -> RootComponent.SlotChild.Settings(ctx)
        }
    }


    @Serializable
    private sealed interface StackConfig {
        @Serializable
        data object MainScreen : StackConfig

        @Serializable
        data object SetsScreen : StackConfig

        @Serializable
        data class CreateEditSetScreen(val wordSet: WordSet?) :
            StackConfig
    }

    @Serializable
    private sealed interface SlotConfig {
        @Serializable
        data object Settings : SlotConfig
    }

}