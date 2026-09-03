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
        return applyAutomaticLinks(lessonId, sentenceIndex, result)
    }

    private fun applyAutomaticLinks(
        lessonId: String,
        sentenceIndex: Int,
        sentence: MutableList<ReadingToken>
    ): List<ReadingToken> {
        val nounPrefixClasses = setOf("Artikel", "Belirleyici", "Sıfat", "Zamir")

        fun addWeakLink(indices: List<Int>, relation: String): String? {
            if (indices.size < 2) return null
            val linkId = "$lessonId-auto-$sentenceIndex-$relation-${indices.first()}-${indices.last()}"
            indices.distinct().forEach { index ->
                val original = sentence[index].lexeme
                sentence[index] = sentence[index].copy(lexeme = original.copy(
                    contextLinkId = original.contextLinkId ?: linkId,
                    contextLinkIds = (original.contextLinkIds + linkId).distinct()
                ))
            }
            return linkId
        }

        for (nounIndex in sentence.indices) {
            if (sentence[nounIndex].lexeme.wordClass != "İsim") continue
            var start = nounIndex
            var cursor = nounIndex - 1
            while (cursor >= 0 && sentence[cursor].lexeme.wordClass in nounPrefixClasses) {
                start = cursor
                cursor--
            }
            if (start < nounIndex) addWeakLink((start..nounIndex).toList(), "noun")
        }

        for (prepIndex in sentence.indices) {
            if (sentence[prepIndex].lexeme.wordClass != "Edat") continue
            val objectIndices = mutableListOf<Int>()
            var cursor = prepIndex + 1
            while (cursor < sentence.size && cursor <= prepIndex + 4) {
                val wc = sentence[cursor].lexeme.wordClass
                if (wc == "İsim") {
                    objectIndices += cursor
                    break
                }
                if (wc in nounPrefixClasses) {
                    objectIndices += cursor
                    cursor++
                    continue
                }
                break
            }
            if (objectIndices.isEmpty() || sentence[objectIndices.last()].lexeme.wordClass != "İsim") continue
            val relationIndices = mutableListOf(prepIndex).apply { addAll(objectIndices) }
            val relationLink = addWeakLink(relationIndices, "prep") ?: continue

            val strongId = sentence[prepIndex].lexeme.strongLinkId
            if (strongId != null) {
                sentence.indices.filter { sentence[it].lexeme.strongLinkId == strongId }.forEach { index ->
                    val original = sentence[index].lexeme
                    sentence[index] = sentence[index].copy(lexeme = original.copy(
                        contextLinkId = original.contextLinkId ?: relationLink,
                        contextLinkIds = (original.contextLinkIds + relationLink).distinct()
                    ))
                }
            }
        }
        return sentence
    }

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
