package de.bascurt.almancaokuyucu.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtendedLessonContentTest {
    @Test
    fun everyVisibleStoryTokenHasTurkishMeaning() {
        val missing = ExtendedLessonFactory.missingMeaningTokens(SampleLessons.all)
        assertTrue(
            "Türkçe anlamı eksik hikâye kelimeleri:\n${missing.joinToString("\n")}",
            missing.isEmpty()
        )
    }

    @Test
    fun aufwaermenDoesNotCaptureAufDemFahrradPreposition() {
        val sentence = auditSentence("Er wärmt sich zehn Minuten auf dem Fahrrad auf.")

        val waermt = sentence.first { it.text.startsWith("wärmt") }
        val sich = sentence.first { it.text.trim('.', ',', ';', '!', '?') == "sich" }
        val aufs = sentence.filter { it.text.trim('.', ',', ';', '!', '?') == "auf" }

        assertEquals(2, aufs.size)
        assertEquals("Edat", aufs.first().lexeme.wordClass)
        assertEquals(waermt.lexeme.id, sich.lexeme.id)
        assertEquals(waermt.lexeme.id, aufs.last().lexeme.id)
        assertTrue(aufs.first().lexeme.id != waermt.lexeme.id)
    }

    @Test
    fun kitchenEinraeumenLinksVerbAndParticle() {
        val sentence = auditSentence("Nach dem Essen räumt sie das Geschirr in die Spülmaschine ein.")
        val verb = sentence.first { it.text.startsWith("räumt") }
        val particle = sentence.last { it.text.trim('.', ',', ';', '!', '?') == "ein" }

        assertEquals("einräumen", verb.lexeme.base)
        assertEquals(verb.lexeme.id, particle.lexeme.id)
    }

    private fun auditSentence(text: String) = ExtendedLessonFactory.lesson(
        id = "content-regression",
        title = "Audit",
        level = "A2",
        summary = "test",
        texts = listOf(text)
    ).sentences.first()
}
