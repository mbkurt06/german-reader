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
        val lesson = SampleLessons.all.first { it.id == "b1-fitness" }
        val sentence = lesson.sentences.first { row -> row.any { it.text.startsWith("wärmt") } }

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
        val lesson = SampleLessons.all.first { it.id == "a2-kueche" }
        val sentence = lesson.sentences.first { row -> row.any { it.text.startsWith("räumt") } && row.any { it.text.startsWith("ein") } }
        val verb = sentence.first { it.text.startsWith("räumt") }
        val particle = sentence.last { it.text.trim('.', ',', ';', '!', '?') == "ein" }

        assertEquals("einräumen", verb.lexeme.base)
        assertEquals(verb.lexeme.id, particle.lexeme.id)
    }
}
