package de.bascurt.almancaokuyucu.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonVocabularyAuditTest {

    @Test
    fun everyVisibleTokenHasARealMeaning() {
        val missing = SampleLessons.all.flatMap { lesson ->
            lesson.sentences.flatMapIndexed { sentenceIndex, sentence ->
                sentence.mapIndexedNotNull { tokenIndex, token ->
                    val meaning = token.lexeme.meaning.trim()
                    val invalid = meaning.isBlank() ||
                        meaning.contains("tamamlanmalı", ignoreCase = true) ||
                        meaning.contains("eksik", ignoreCase = true)
                    if (invalid) "${lesson.title} [${sentenceIndex + 1}:${tokenIndex + 1}] ${token.text} -> $meaning" else null
                }
            }
        }
        assertTrue("Eksik Türkçe anlamlar:\n${missing.joinToString("\n")}", missing.isEmpty())
    }

    @Test
    fun aufwaermenDoesNotCaptureAufDemFahrrad() {
        val lesson = SampleLessons.all.first { it.id == "b1-fitness" }
        val sentence = lesson.sentences.first { row -> row.any { it.text.startsWith("wärmt") } }
        val warmt = sentence.first { it.text.startsWith("wärmt") }
        val sich = sentence.first { it.text.trim('.', ',') == "sich" }
        val aufs = sentence.filter { it.text.trim('.', ',') == "auf" }

        assertEquals(2, aufs.size)
        assertEquals(warmt.lexeme.id, sich.lexeme.id)
        assertNotEquals(warmt.lexeme.id, aufs.first().lexeme.id)
        assertEquals(warmt.lexeme.id, aufs.last().lexeme.id)
        assertEquals("sich aufwärmen", warmt.lexeme.base)
    }

    @Test
    fun anmeldenDoesNotCaptureAnDerRezeption() {
        val lesson = SampleLessons.all.first { it.id == "a2-kinderarzt" }
        val sentence = lesson.sentences.first { row -> row.any { it.text.startsWith("meldet") } }
        val verb = sentence.first { it.text.startsWith("meldet") }
        val ans = sentence.filter { it.text.trim('.', ',') == "an" }

        assertEquals(2, ans.size)
        assertNotEquals(verb.lexeme.id, ans.first().lexeme.id)
        assertEquals(verb.lexeme.id, ans.last().lexeme.id)
    }

    @Test
    fun normalAufAfterStellenStaysAPreposition() {
        val lesson = SampleLessons.all.first { it.id == "a2-kueche" }
        val sentence = lesson.sentences.first()
        val verb = sentence.first { it.text.startsWith("stellt") }
        val auf = sentence.first { it.text == "auf" }

        assertNotEquals(verb.lexeme.id, auf.lexeme.id)
        assertEquals("Edat", auf.lexeme.wordClass)
    }
}
