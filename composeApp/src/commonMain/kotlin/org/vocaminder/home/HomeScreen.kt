package org.vocaminder.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tools.preview.PreviewCustom
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.vocaminder.generated.resources.Res
import org.vocaminder.generated.resources.add_icon_24dp
import org.vocaminder.home.api.HomeComponent
import org.vocaminder.home.api.WordSet
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)

@PreviewCustom
@Composable
fun HomeScreen(homeComponent: HomeComponent) {

    val homeState = homeComponent.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            IconButton(
                onClick = { homeComponent.onNavigation(HomeComponent.NavigationEvent.CreateNewWordSet) },
                content = {
                    Image(
                        painter = painterResource(Res.drawable.add_icon_24dp),
                        contentDescription = "add word set",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )
                })
        }

    ) { innerPaddings ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPaddings).verticalScroll(scrollState)
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            homeState.value.wordSets.forEach { wordSet ->
                key(wordSet.id) {
                    WordSetItem(wordSet, onClick = {
                        homeComponent.onNavigation(
                            HomeComponent.NavigationEvent.ChooseWordSet(
                                wordSet
                            )
                        )
                    })
                }
            }
        }
    }

}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WordSetItem(item: WordSet, onClick: () -> Unit, modifier: Modifier = Modifier) {

    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.elevatedCardElevation(disabledElevation = 8.dp),
    ) {

        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.setName,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
//                Text( modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.End ,text = "Last pass ${ item.lastPassAt.toDisplayString() }")
            }
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                val words = remember {
                    mutableStateOf(item.langSet.joinToString(",") { it.word })
                }
                Text(
                    text = words.value,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic
                )

            }

        }
    }
}


@Composable
fun BottomNavePanel(
    onEvent: (HomeComponent.NavigationEvent) -> Unit,
    modifier: Modifier = Modifier,
    unfinishedGame: WordSet? = null,
) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

        Text(
            "Create new set",
            modifier = Modifier.weight(1f).background(color = Color.Green)
                .clickable { onEvent(HomeComponent.NavigationEvent.CreateNewWordSet) })
        VerticalDivider(
            modifier = Modifier.height(36.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        if (unfinishedGame != null) Text(
            "Resume current game ${unfinishedGame.setName}",
            modifier = Modifier.weight(1f).background(color = Color.Green)
                .clickable {
                    onEvent(
                        HomeComponent.NavigationEvent.ResumeUnfinishedGame(
                            unfinishedGame
                        )
                    )
                })

    }

}


