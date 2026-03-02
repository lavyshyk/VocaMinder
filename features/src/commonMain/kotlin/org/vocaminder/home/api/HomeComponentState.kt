@file:OptIn(ExperimentalUuidApi::class)

package org.vocaminder.home.api

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class HomeState(
    val wordSets: PersistentList<WordSet> = persistentListOf<WordSet>(),
    val isLoading: Boolean = false,
    val unfinishedGame: WordSet? = null,
) {
    companion object {
        fun previewDefaultState() = HomeState(
            wordSets = persistentListOf(
                WordSet(
                    id = Uuid.random(),
                    setName = "Basic English-Russian Vocabulary",
                    pivotLang = "en",
                    currentWord = "Hello",
                    additionalRandomWordSet = persistentSetOf(
                        "Goodbye",
                        "Please",
                        "Thank you",
                        "Yes"
                    ),
                    langSet = persistentListOf(
                        WordPair(
                            word = "Hello",
                            translation = "Привет",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Goodbye",
                            translation = "До свидания",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Please",
                            translation = "Пожалуйста",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Thank you",
                            translation = "Спасибо",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Yes",
                            translation = "Да",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "No",
                            translation = "Нет",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Water",
                            translation = "Вода",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Food",
                            translation = "Еда",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Home",
                            translation = "Дом",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Friend",
                            translation = "Друг",
                            pivotLang = "en"
                        )
                    ),
                    lastPassAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    countMistakes = 0,
                    countAttempts = 0,
                    createAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                ),
                WordSet(
                    id = Uuid.random(),
                    setName = "Basic English-Russian Vocabulary",
                    pivotLang = "en",
                    currentWord = "Hello",
                    additionalRandomWordSet = persistentSetOf(
                        "Goodbye",
                        "Please",
                        "Thank you",
                        "Yes"
                    ),
                    langSet = persistentListOf(
                        WordPair(
                            word = "Hello",
                            translation = "Привет",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Goodbye",
                            translation = "До свидания",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Please",
                            translation = "Пожалуйста",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Thank you",
                            translation = "Спасибо",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Yes",
                            translation = "Да",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "No",
                            translation = "Нет",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Water",
                            translation = "Вода",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Food",
                            translation = "Еда",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Home",
                            translation = "Дом",
                            pivotLang = "en"
                        ),
                        WordPair(
                            word = "Friend",
                            translation = "Друг",
                            pivotLang = "en"
                        )
                    ),
                    lastPassAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    countMistakes = 0,
                    countAttempts = 0,
                    createAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )
            ),
            isLoading = false,
            unfinishedGame = WordSet(
                id = Uuid.random(),
                setName = "Basic English-Russian Vocabulary",
                pivotLang = "en",
                currentWord = "Hello",
                additionalRandomWordSet = persistentSetOf(
                    "Goodbye",
                    "Please",
                    "Thank you",
                    "Yes"
                ),
                langSet = persistentListOf(
                    WordPair(
                        word = "Hello",
                        translation = "Привет",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Goodbye",
                        translation = "До свидания",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Please",
                        translation = "Пожалуйста",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Thank you",
                        translation = "Спасибо",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Yes",
                        translation = "Да",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "No",
                        translation = "Нет",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Water",
                        translation = "Вода",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Food",
                        translation = "Еда",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Home",
                        translation = "Дом",
                        pivotLang = "en"
                    ),
                    WordPair(
                        word = "Friend",
                        translation = "Друг",
                        pivotLang = "en"
                    )
                ),
                lastPassAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                countMistakes = 0,
                countAttempts = 0,
                createAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
        )
    }
}


@Serializable
@Stable
data class WordSet(
    val id: Uuid,
    val setName: String,
    val pivotLang: String,
    val currentWord: String, //todo : currentStep
    val additionalRandomWordSet: PersistentSet<String>,
    val langSet: PersistentList<WordPair>,
    val lastPassAt: LocalDateTime?,
    val countMistakes: Int,
    val countAttempts: Int,
    val createAt: LocalDateTime,
) {
    companion object {
        fun new(): WordSet =
            WordSet(
                id = Uuid.random(),
                setName = "",
                pivotLang = "", // todo figure out how set init state
                currentWord = "",
                additionalRandomWordSet = persistentSetOf<String>(),
                langSet = persistentListOf(),
                lastPassAt = null,
                countAttempts = 0,
                countMistakes = 0,
                createAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            )
    }
}

@Serializable
@Stable
data class WordPair(
    val word: String,
    val translation: String,
    val pivotLang: String,
) {
    val isFullFilled: Boolean
        get() = word.isNotBlank() && translation.isNotBlank()

    companion object {
        fun new() = WordPair("", "", "")
    }
}