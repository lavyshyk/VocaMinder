package org.vocaminder.crudwordset

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SplitButtonDefaults
import androidx.compose.material3.SplitButtonLayout
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.painterResource
import org.vocaminder.LocalWindowSizeClass
import org.vocaminder.crudwordset.api.CRUDSetState
import org.vocaminder.crudwordset.api.CRUDSetStore
import org.vocaminder.crudwordset.api.CRUDSetStore.Intent
import org.vocaminder.crudwordset.api.CRUDWordSetComponent
import org.vocaminder.generated.resources.Res
import org.vocaminder.generated.resources.ic_delete_24dp
import org.vocaminder.generated.resources.ic_edit_24dp
import org.vocaminder.generated.resources.ic_more_up_24dp
import org.vocaminder.generated.resources.ic_translate_24dp
import org.vocaminder.home.api.WordPair


@Composable
fun CRUDWordSetScreen(component: CRUDWordSetComponent) {

    val state = component.state.collectAsStateWithLifecycle()

    CRUDWordSetContent(
        state = state,
        onNavigationEvent = { component.onNavigation(it) },
        onIntent = component::onCRUDSetStoreIntent,
        modifier = Modifier.fillMaxSize()
    )

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CRUDWordSetContent(
    state: State<CRUDSetState>,
    onNavigationEvent: (CRUDWordSetComponent.NavigationEvent) -> Unit,
    onIntent: (CRUDSetStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            SplitButtonLayout(
                leadingButton = {
                    SplitButtonDefaults.LeadingButton(
                        onClick = { onIntent(Intent.SaveSet) }
                    ) {
                        Text("Save")
                    }
                },
                trailingButton = {
                    Box {
                        SplitButtonDefaults.TrailingButton(
                            checked = menuExpanded,
                            onCheckedChange = { menuExpanded = it },
                        ) {
                            Image(
                                painterResource(Res.drawable.ic_more_up_24dp),
                                contentDescription = "more options",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Delete word set") },
                                onClick = {
                                    menuExpanded = false
                                    onIntent(Intent.DeleteWordSet(state.value.wordSet))
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = state.value.wordSet.setName,
                onValueChange = { onIntent(CRUDSetStore.Intent.SetEditSetName(it)) },
                label = { Text("Set name") },
                modifier = Modifier.fillMaxWidth()
            )
            InputTranslateBlock(
                modifier = Modifier.fillMaxWidth(),
                state = state.value,
                onIntent = onIntent
            )
            Button(
                onClick = { onIntent(Intent.AddWordToSet(state.value.currentWordPair)) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add word")
            }
            wordSetContainer(
                wordSets = state.value.wordSet.langSet,
                onNavigationEvent = onNavigationEvent,
                onIntent = onIntent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun InputTranslateBlock(
    modifier: Modifier,
    state: CRUDSetState,
    onIntent: (CRUDSetStore.Intent) -> Unit
) {
    when (LocalWindowSizeClass.current.widthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InputBlock(
                    modifier = Modifier.weight(1f),
                    word = state.currentWordPair.word.orEmpty(),
                    lable = "enter new word",
                    onChangeInput = { onIntent(CRUDSetStore.Intent.InputWord(it)) }
                )
                TranslateButton(modifier = Modifier.size(48.dp)) { onIntent(CRUDSetStore.Intent.Translate) }
                InputBlock(
                    modifier = Modifier.weight(1f),
                    word = state.currentWordPair.translation.orEmpty(),
                    lable = "translation",
                    onChangeInput = { onIntent(CRUDSetStore.Intent.InputTranslationWord(it)) }
                )

            }
        }

        else -> {
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                InputBlock(
                    modifier = Modifier,
                    word = state.currentWordPair.word.orEmpty(),
                    lable = "enter new word",
                    onChangeInput = { onIntent(CRUDSetStore.Intent.InputWord(it)) }
                )
                TranslateButton { onIntent(CRUDSetStore.Intent.Translate) }
                InputBlock(
                    modifier = Modifier,
                    word = state.currentWordPair.translation.orEmpty(),
                    lable = "translation",
                    onChangeInput = { onIntent(CRUDSetStore.Intent.InputTranslationWord(it)) }
                )
            }
        }
    }
}


@Composable
fun InputBlock(
    modifier: Modifier = Modifier,
    word: String,
    lable: String,
    onChangeInput: (String) -> Unit
) {
    OutlinedCard(modifier = modifier) {
        TextField(
            label = { Text(text = lable) },
            value = word,
            onValueChange = onChangeInput,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun TranslateButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Image(painterResource(Res.drawable.ic_translate_24dp), contentDescription = "to translate")
    }
}

@Composable
fun wordSetContainer(
    wordSets: PersistentList<WordPair>,
    onNavigationEvent: (CRUDWordSetComponent.NavigationEvent) -> Unit,
    onIntent: (CRUDSetStore.Intent) -> Unit,
    modifier: Modifier = Modifier,
) {

    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.padding(16.dp).fillMaxWidth().verticalScroll(scrollState),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            wordSets.forEach { wordPair ->
                key(wordPair) {
                    WordPairItem(
                        modifier = Modifier.fillMaxWidth(),
                        onIntent = onIntent,
                        wordPair = wordPair
                    )
                }
            }
        }
    }
}

@Composable
fun WordPairItem(modifier: Modifier = Modifier, onIntent: (CRUDSetStore.Intent) -> Unit, wordPair: WordPair) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${wordPair.word} - ${wordPair.translation}")
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painterResource(Res.drawable.ic_edit_24dp),
            modifier = Modifier
                .clickable {
                    onIntent(Intent.EditWordPair(wordPair))
                }
                .padding(16.dp),
            contentDescription = "edit word")
        Image(
            painterResource(Res.drawable.ic_delete_24dp), modifier = Modifier.clickable {
                onIntent(Intent.DeleteWordPair(wordPair))
            }.padding(16.dp),
            contentDescription = "word delete"
        )

    }

}

