@file:OptIn(ExperimentalMaterial3WindowSizeClassApi::class)

package org.vocaminder.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.vocaminder.LocalWindowSizeClass
import org.vocaminder.PreviewVocaMinder
import org.vocaminder.crudwordset.CRUDWordSetScreen
import org.vocaminder.crudwordset.impl.PreviewCRUDWordSetComponent
import org.vocaminder.home.HomeScreen
import org.vocaminder.home.api.PreviewHomeComponent
import org.vocaminder.root.RootContent
import org.vocaminder.root.internal.DefaultRootComponent
import org.vocaminder.theme.AppTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val rootComponentContext = defaultComponentContext()
        val storeFactory: StoreFactory = DefaultStoreFactory()
        setContent {
            AppTheme {
                CompositionLocalProvider(
                    LocalWindowSizeClass provides calculateWindowSizeClass(this),
                ) {
                    RootContent(DefaultRootComponent(rootComponentContext, storeFactory))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun AppAndroidPreview() { PreviewRootComponent()
//}


@PreviewVocaMinder
@Composable
private fun PreviewHomeScreen() {
    AppTheme {
        CompositionLocalProvider(
            LocalWindowSizeClass provides WindowSizeClass.calculateFromSize(DpSize(1260.dp, 800.dp)) // example default
        ){
//        HomeScreen(homeComponent = PreviewHomeComponent)
        CRUDWordSetScreen(PreviewCRUDWordSetComponent())
    }}
}