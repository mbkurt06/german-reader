package de.bascurt.almancaokuyucu.data

import android.content.Context
import de.bascurt.almancaokuyucu.model.Lexeme
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SavedLexemeStore(context: Context) {
    private val preferences = context.getSharedPreferences("learning_progress", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun load(): List<Lexeme> = runCatching {
        json.decodeFromString<List<Lexeme>>(preferences.getString(KEY, "[]") ?: "[]")
    }.getOrDefault(emptyList())

    fun save(items: List<Lexeme>) {
        preferences.edit().putString(KEY, json.encodeToString(items)).apply()
    }

    private companion object {
        const val KEY = "saved_lexemes_v2"
    }
}
