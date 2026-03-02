package org.vocaminder.root

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tools.preview.PreviewCustom
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.vocaminder.common.rememberSlotModalBottomSheetState
import org.vocaminder.crudwordset.CRUDWordSetScreen
import org.vocaminder.home.HomeScreen
import org.vocaminder.root.api.RootComponent
import org.vocaminder.theme.AppTheme
import org.vocaminder.theme.pagerIndicatorBackground


@OptIn(ExperimentalDecomposeApi::class, ExperimentalMaterial3Api::class)

@PreviewCustom
@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {

    val childSlot = component.childSlot.subscribeAsState()

    val bottomSheetState = rememberSlotModalBottomSheetState(
        child = childSlot.value.child?.instance
    ) { slotChild ->
        when (slotChild) {
            is RootComponent.SlotChild.Settings -> {
                Text(
                    text = "Settings",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }

    if (bottomSheetState.isVisible.value) {
        ModalBottomSheet(
            sheetState = bottomSheetState.sheetState,
            onDismissRequest = component::dismissSlotChild,
            content = bottomSheetState.sheetContent.value,
            containerColor = MaterialTheme.colorScheme.pagerIndicatorBackground,
            dragHandle = null
        )
    }

   Surface(Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background).navigationBarsPadding().displayCutoutPadding().statusBarsPadding()){
        Children(
            component.childStack,
            animation = predictiveBackAnimation(
                backHandler = component.backHandler,
                fallbackAnimation = stackAnimation(fade() + scale()),
                onBack = component::onBackClicked,
            ),
            modifier = Modifier,
        ){
//            Surface(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background)){
               when(val child = it.instance){
                   is RootComponent.Child.MainPage -> HomeScreen(child.component)
                   is RootComponent.Child.CreateEditSet -> CRUDWordSetScreen(child.component)
                   is RootComponent.Child.SourceSets -> Surface(modifier = Modifier.fillMaxSize()) {
                       Text(text = "Source Sets", modifier = Modifier.padding(16.dp))
                   }
               }
            }
//        }
    }


}