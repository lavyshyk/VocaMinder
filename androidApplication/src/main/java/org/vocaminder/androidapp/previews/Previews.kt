package org.vocaminder.androidapp.previews

import androidx.compose.runtime.Composable
import org.vocaminder.PreviewVocaMinder
import org.vocaminder.home.HomeScreen
import org.vocaminder.home.WordSetItem
import org.vocaminder.home.api.HomeState
import org.vocaminder.home.api.PreviewHomeComponent
import org.vocaminder.home.api.WordSet
import org.vocaminder.theme.AppTheme

@PreviewVocaMinder
@Composable
fun PreviewWordItem(){
    AppTheme(){

        val wordSet = HomeState.previewDefaultState().wordSets.first()
//        WordSetItem(item = wordSet, onClick = {})

        HomeScreen(PreviewHomeComponent)
    }


}