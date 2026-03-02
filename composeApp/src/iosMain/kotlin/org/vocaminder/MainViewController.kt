package org.vocaminder

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.ApplicationLifecycle
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.vocaminder.root.RootContent
import org.vocaminder.root.internal.DefaultRootComponent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController() = ComposeUIViewController {

    val rootComponent = DefaultRootComponent(
        componentContext = DefaultComponentContext(
            lifecycle = ApplicationLifecycle()
        ), DefaultStoreFactory()
    )

    CompositionLocalProvider(
        LocalWindowSizeClass provides calculateWindowSizeClass()
    ) {
        RootContent(rootComponent)
    }
}