@file:OptIn(ExperimentalUuidApi::class)

package org.vocaminder.crudwordset.api

import androidx.compose.runtime.staticCompositionLocalOf
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import org.vocaminder.crudwordset.api.CRUDSetStore.Message
import org.vocaminder.home.api.WordPair
import org.vocaminder.home.api.WordSet
import kotlin.uuid.ExperimentalUuidApi

interface CRUDSetStore : Store<CRUDSetStore.Intent, CRUDSetState, CRUDSetStore.Label> {

    sealed interface Intent {
        data class AddWordToSet(val wordPair: WordPair) : Intent
        data class EditWordFromSet(val wordSet: WordSet) : Intent
        data class SetEditSetName(val setName: String) : Intent
        data class DeleteWordPair(val wordPair: WordPair) : Intent
        data class EditWordPair(val wordPair: WordPair) : Intent
        data class DeleteWordSet(val wordSet: WordSet) : Intent
        data class InputWord(val word: String) : Intent
        data class InputTranslationWord(val word: String) : Intent
        data object Translate : Intent
        data object SaveSet: Intent
        data object DeleteSet: Intent

    }

    sealed interface Message {
        data class InitEditState(val wordSet: WordSet) : Message
        data class InputNameSet(val name: String) : Message
        data class InputWord(val word: String) : Message
        data class InputTranslationWord(val word: String) : Message
        data class Loading(val isLoading: Boolean) : Message
        data class AddWardPairToSet(val wordPair: WordPair) : Message
        data object DeleteWordSet : Message
        data class DeleteWordPair(val wordPair: WordPair) : Message
        data class EditWordPair(val wordPair: WordPair) : Message
    }

    sealed interface Label {
        data class ErrorDialog(val message: String) : Label
        data class SuccessDialog(val message: String) : Label
    }
}


class CRUDSetStoreFactory(
    private val editableWordSet: WordSet? = null,
    private val storeFactory: StoreFactory
) {
    fun create(): CRUDSetStore = object : CRUDSetStore,
        Store<CRUDSetStore.Intent, CRUDSetState, CRUDSetStore.Label> by storeFactory.create(
            name = "CRUDSetStore",
            initialState = CRUDSetState(wordSet = WordSet.new()),
            bootstrapper = SimpleBootstrapper(Unit),
            executorFactory = coroutineExecutorFactory {
                onAction<Unit> {
                    editableWordSet?.let { dispatch(CRUDSetStore.Message.InitEditState(it)) }
                }
                onIntent<CRUDSetStore.Intent.AddWordToSet> {
                    if (state().currentWordPair.isFullFilled) {
                        dispatch(CRUDSetStore.Message.AddWardPairToSet(state().currentWordPair))
                    } else {
                        publish(CRUDSetStore.Label.ErrorDialog("Не заполнены поля"))
                    }
                }
                onIntent<CRUDSetStore.Intent.SetEditSetName> { intent -> dispatch(CRUDSetStore.Message.InputNameSet(name = intent.setName)) }
                onIntent<CRUDSetStore.Intent.DeleteWordSet> {
                    publish(CRUDSetStore.Label.SuccessDialog("удалено успешно"))
                    dispatch(CRUDSetStore.Message.DeleteWordSet)
                }
                onIntent<CRUDSetStore.Intent.DeleteWordPair> { dispatch(CRUDSetStore.Message.DeleteWordPair(it.wordPair)) }
                onIntent<CRUDSetStore.Intent.EditWordPair> { dispatch(CRUDSetStore.Message.EditWordPair(it.wordPair)) }
                onIntent<CRUDSetStore.Intent.EditWordFromSet> { dispatch(Message.InitEditState(it.wordSet)) }
                onIntent<CRUDSetStore.Intent.Translate> {
                    // todo
                }
                onIntent<CRUDSetStore.Intent.InputWord> { dispatch(Message.InputWord(it.word))}
                onIntent<CRUDSetStore.Intent.InputTranslationWord> { dispatch(Message.InputTranslationWord(it.word))}
            },
            reducer = { message: CRUDSetStore.Message ->
                when (message) {
                    is Message.InitEditState -> copy(wordSet = message.wordSet, currentWordPair = WordPair.new())
                    is Message.InputNameSet -> copy(wordSet = wordSet.copy(setName = message.name))
                    is Message.InputTranslationWord -> copy(currentWordPair = currentWordPair.copy(translation = message.word))
                    is Message.InputWord -> copy(currentWordPair = currentWordPair.copy(word = message.word))
                    is Message.Loading -> copy(isLoading = message.isLoading)
                    is Message.AddWardPairToSet ->
                        copy(wordSet = wordSet.copy(langSet = wordSet.langSet.add(message.wordPair)), currentWordPair = WordPair.new())

                    is Message.DeleteWordSet -> {
                        copy(wordSet = WordSet.new(), currentWordPair = WordPair.new())
                    }

                    is Message.DeleteWordPair -> copy(wordSet = wordSet.copy(langSet = wordSet.langSet.remove(message.wordPair)))
                    is Message.EditWordPair -> copy(
                        wordSet = wordSet.copy(langSet = wordSet.langSet.remove(message.wordPair)),
                        currentWordPair = message.wordPair
                    )
                }

            }

        ) {}
}