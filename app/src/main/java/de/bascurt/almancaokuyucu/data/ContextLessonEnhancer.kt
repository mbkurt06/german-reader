package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

internal data class ContextPhrase(
    val words: List<String>,
    val meaning: String,
    val explanation: String,
    val weakLink: Boolean = false,
    val strongLink: Boolean = false,
    val allowGaps: Boolean = false,
    val sentenceIndex: Int? = null
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
        phrases.sortedWith(compareBy<ContextPhrase> { it.strongLink }.thenByDescending { it.words.size }).forEach { phrase ->
            if (phrase.sentenceIndex != null && phrase.sentenceIndex != sentenceIndex) return@forEach
            val wanted = phrase.words.map { it.lowercase() }
            if (wanted.isEmpty() || wanted.size > keys.size) return@forEach

            val matches = mutableListOf<List<Int>>()
            if (phrase.allowGaps) {
                for (candidateStart in keys.indices) {
                    if (keys[candidateStart] != wanted.first()) continue
                    val found = mutableListOf(candidateStart)
                    var cursor = candidateStart + 1
                    var ok = true
                    for (word in wanted.drop(1)) {
                        val next = (cursor until keys.size).firstOrNull { keys[it] == word }
                        if (next == null) { ok = false; break }
                        found += next
                        cursor = next + 1
                    }
                    if (ok) matches += found
                }
            } else {
                for (candidateStart in 0..keys.size - wanted.size) {
                    if (keys.subList(candidateStart, candidateStart + wanted.size) == wanted) {
                        matches += (candidateStart until candidateStart + wanted.size).toList()
                    }
                }
            }

            matches.forEach { indices ->
                val first = indices.first()
                val linkId = "$lessonId-context-$sentenceIndex-$first-${wanted.joinToString("-")}"
                when {
                    phrase.strongLink -> indices.forEach { index ->
                        val original = result[index].lexeme
                        result[index] = result[index].copy(lexeme = original.copy(
                            id = "${original.id}-strong-$sentenceIndex-$first-$index",
                            strongLinkId = linkId,
                            contextUsage = phrase.explanation
                        ))
                    }
                    phrase.weakLink -> indices.forEach { index ->
                        val original = result[index].lexeme
                        result[index] = result[index].copy(lexeme = original.copy(
                            id = "${original.id}-ctx-$sentenceIndex-$first-$index",
                            contextLinkId = original.contextLinkId ?: linkId,
                            contextLinkIds = (original.contextLinkIds + linkId).distinct(),
                            contextUsage = phrase.explanation
                        ))
                    }
                    else -> {
                        val shown = indices.joinToString(" ") { sentence[it].text.trimEnd('.', ',', ';', '!', '?') }
                        val grouped = Lexeme(
                            id = linkId,
                            base = shown,
                            meaning = phrase.meaning,
                            type = "Kelime grubu",
                            explanation = phrase.explanation,
                            quizEligible = true,
                            wordClass = "Kelime grubu"
                        )
                        indices.forEach { index -> result[index] = result[index].copy(lexeme = grouped) }
                    }
                }
            }
        }
        return result
    }

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
