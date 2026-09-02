package de.bascurt.almancaokuyucu.model

import kotlinx.serialization.Serializable

@Serializable
data class Lexeme(
    val id: String,
    val base: String,
    val meaning: String,
    val type: String = "Kelime",
    val grammar: String? = null,
    val explanation: String? = null
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
}

enum class ReaderTab { STORY, QUIZ, WORDS, GRAMMAR }
