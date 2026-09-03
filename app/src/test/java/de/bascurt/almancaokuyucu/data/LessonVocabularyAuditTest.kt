package de.bascurt.almancaokuyucu.data

import org.junit.Assert.assertEquals
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

        val missingWords = SampleLessons.all
            .flatMap { it.sentences }
            .flatten()
            .filter { token ->
                val meaning = token.lexeme.meaning.trim()
                meaning.isBlank() ||
                    meaning.contains("tamamlanmalı", ignoreCase = true) ||
                    meaning.contains("eksik", ignoreCase = true)
            }
            .map { token -> cleanForAudit(token.text) }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        if (missing.isNotEmpty()) {
            println("=== EKSİK HİKÂYE SÖZLÜĞÜ: TEKRARSIZ KELİMELER ===")
            println(missingWords.joinToString(", "))
            println("=== TEKRARSIZ EKSİK: ${missingWords.size} ===")
            println("=== TÜM KONUMLAR ===")
            missing.forEach(::println)
            println("=== TOPLAM EKSİK KULLANIM: ${missing.size} ===")
        }
        assertTrue("Eksik Türkçe anlamlar:\n${missing.joinToString("\n")}", missing.isEmpty())
    }

    @Test
    fun aufwaermenDoesNotCaptureAufDemFahrrad() {
        val sentence = auditSentence("Er wärmt sich zehn Minuten auf dem Fahrrad auf.")
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
        val sentence = auditSentence("Er meldet sich an der Rezeption an.")
        val verb = sentence.first { it.text.startsWith("meldet") }
        val ans = sentence.filter { it.text.trim('.', ',') == "an" }

        assertEquals(2, ans.size)
        assertNotEquals(verb.lexeme.id, ans.first().lexeme.id)
        assertEquals(verb.lexeme.id, ans.last().lexeme.id)
    }

    @Test
    fun normalAufAfterStellenStaysAPreposition() {
        val sentence = auditSentence("Er stellt den Topf auf den Herd.")
        val verb = sentence.first { it.text.startsWith("stellt") }
        val auf = sentence.first { it.text == "auf" }

        assertNotEquals(verb.lexeme.id, auf.lexeme.id)
        assertEquals("Edat", auf.lexeme.wordClass)
    }

    @Test
    fun separableParticleBeforeUndStaysInsideItsOwnClause() {
        val sentence = auditSentence("Er macht das Licht an und sie schaut die Anzeige an.")
        val macht = sentence.first { it.text == "macht" }
        val ans = sentence.filter { it.text.trim('.', ',') == "an" }

        assertEquals(2, ans.size)
        assertEquals("anmachen", macht.lexeme.base)
        assertEquals(macht.lexeme.id, ans.first().lexeme.id)
        assertNotEquals(macht.lexeme.id, ans.last().lexeme.id)
    }

    private fun auditSentence(text: String) = ExtendedLessonFactory.lesson(
        id = "audit-regression",
        title = "Audit",
        level = "A2",
        summary = "test",
        texts = listOf(text)
    ).sentences.first()

    private fun cleanForAudit(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
