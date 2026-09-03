package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

internal data class ContextPhrase(
    val words: List<String>,
    val meaning: String,
    val explanation: String
)

/**
 * Anlamı tek bir genel sözlükten zorlamak yerine, yalnız ilgili hikâyede ve ilgili
 * cümlede gerçekten geçen kelime grubunu bağlama göre tek öğrenme birimi yapar.
 */
internal object ContextLessonEnhancer {
    fun apply(
        lesson: ReaderLesson,
        translations: List<String>,
        phrases: List<ContextPhrase>
    ): ReaderLesson {
        require(translations.size == lesson.sentences.size) {
            "${lesson.id}: ${lesson.sentences.size} cümle için ${translations.size} çeviri verildi."
        }
        val contextualSentences = lesson.sentences.mapIndexed { sentenceIndex, sentence ->
            applyPhrases(lesson.id, sentenceIndex, sentence, phrases)
        }
        return lesson.copy(sentences = contextualSentences, translations = translations)
    }

    private fun applyPhrases(
        lessonId: String,
        sentenceIndex: Int,
        sentence: List<ReadingToken>,
        phrases: List<ContextPhrase>
    ): List<ReadingToken> {
        val result = sentence.toMutableList()
        val keys = sentence.map { clean(it.text) }
        phrases.sortedByDescending { it.words.size }.forEach { phrase ->
            val wanted = phrase.words.map { it.lowercase() }
            if (wanted.isEmpty() || wanted.size > keys.size) return@forEach
            for (start in 0..keys.size - wanted.size) {
                if (keys.subList(start, start + wanted.size) != wanted) continue
                val shown = sentence.subList(start, start + wanted.size).joinToString(" ") { it.text.trimEnd('.', ',', ';', '!', '?') }
                val grouped = Lexeme(
                    id = "$lessonId-context-$sentenceIndex-$start-${wanted.joinToString("-")}",
                    base = shown,
                    meaning = phrase.meaning,
                    type = "Kelime grubu",
                    explanation = phrase.explanation,
                    quizEligible = true,
                    wordClass = "Kelime grubu"
                )
                for (index in start until start + wanted.size) {
                    result[index] = result[index].copy(lexeme = grouped)
                }
            }
        }
        return result
    }

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
