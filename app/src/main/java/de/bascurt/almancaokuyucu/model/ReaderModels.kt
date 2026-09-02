package de.bascurt.almancaokuyucu.model

import kotlinx.serialization.Serializable

@Serializable
data class Lexeme(
    val id: String,
    val base: String,
    val meaning: String,
    val type: String = "Kelime",
    val grammar: String? = null,
    val explanation: String? = null,
    val quizEligible: Boolean = false,
    val wordClass: String = "Diğer",
    val infinitive: String? = null,
    val thirdPerson: String? = null,
    val preterite: String? = null,
    val perfect: String? = null,
    val article: String? = null,
    val plural: String? = null,
    val accusativeNote: String? = null,
    val positive: String? = null,
    val comparative: String? = null,
    val superlative: String? = null
)

data class ReadingToken(val text: String, val lexeme: Lexeme)

data class ReaderLesson(
    val id: String,
    val title: String,
    val level: String,
    val summary: String,
    val sentences: List<List<ReadingToken>>
) {
    val lexemes: List<Lexeme>
        get() = sentences.flatten().map { it.lexeme }.distinctBy { it.id }

    val grammarItems: List<Lexeme>
        get() = lexemes.filter { it.type != "Kelime" && it.type != "Artikel" }

    val quizItems: List<Lexeme>
        get() = lexemes.filter { it.quizEligible }
}

enum class ReaderTab { STORY, QUIZ, WORDS, GRAMMAR }
