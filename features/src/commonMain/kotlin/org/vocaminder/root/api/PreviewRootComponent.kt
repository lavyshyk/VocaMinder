package org.vocaminder.root.api

import com.arkivanov.decompose.Child
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import org.vocaminder.home.api.HomeComponent
import org.voceminder.core.feature.PreviewComponentContext

//class PreviewRootComponent : RootComponent, ComponentContext by PreviewComponentContext {
//    override val childStack: Value<ChildStack<*, RootComponent.Child>>
//        get() = MutableValue(
//            ChildStack(
//                configuration = Unit,
//                instance = RootComponent.Child.MainPage(),
//            )
//        )
//    override val childSlot: Value<ChildSlot<*, RootComponent.SlotChild>>
//        get() = MutableValue(
//            ChildSlot(
//                Child.Created(
//                    configuration = Unit,
//                    RootComponent.SlotChild.Settings(this as ComponentContext)
//                )
//            )
//        )
//
//    override fun dismissSlotChild() {}
//    override fun onBackClicked() {}
//
//}