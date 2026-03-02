package org.vocaminder.home.internal

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.vocaminder.home.api.HomeState
import org.vocaminder.home.api.WordSet
import org.vocaminder.home.internal.HomeStore.Intent

interface HomeStore : Store<Intent, HomeState, Nothing> {

    sealed interface Intent {
        data class ChooseWordSet(val wordSet: WordSet) : Intent
        data class ResumeUnfinishedGame(val wordSet: WordSet) : Intent
        data class StartNewGame(val wordSet: WordSet) : Intent
        object CreateNewWordSet : Intent
        data class SearchWordSetByWord(val word: String) : Intent
        data class SearchWordSetByTranslation(val translation: String) : Intent
        data class SearchWordSetByPivotLang(val pivotLang: String) : Intent
        data class DeleteWordSet(val wordSet: WordSet) : Intent
    }

//    data class State(
//        val wordSets: List<WordSet> = listOf<WordSet>(),
//        val isLoading: Boolean = false,
//        val unfinishedGame: WordSet? = null,
//    )

}

class HomeStoreFactory(private val storeFactory: StoreFactory) {
    fun create(): HomeStore =
        object : HomeStore, Store<Intent, HomeState, Nothing> by storeFactory.create(
            name = "HomeStore",
            initialState = HomeState.previewDefaultState(),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory {
                onAction<Unit> {
                }
                onIntent<Intent.DeleteWordSet>{}
                onIntent<Intent>{}
            }

        ) {}
}

