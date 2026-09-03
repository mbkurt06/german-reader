package de.bascurt.almancaokuyucu.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonVocabularyAuditTest {

    @Test
    fun focusedReaderUsesExactlyFiveLessons() {
        assertEquals(5, SampleLessons.all.size)
        assertEquals(
            listOf(
                "a2-neuer-anfang",
                "a2-kueche",
                "a2-arzt",
                "a2-baeckerei",
                "a2-supermarkt"
            ),
            SampleLessons.all.map { it.id }
        )
    }

    @Test
    fun everySentenceHasTranslationAndRealTokenMeanings() {
        SampleLessons.all.forEach { lesson ->
            assertEquals(
                "${lesson.title}: cümle ve çeviri sayısı aynı olmalı",
                lesson.sentences.size,
                lesson.translations.size
            )
            assertTrue("${lesson.title}: hikâye yeterince uzun olmalı", lesson.sentences.size >= 12)

            lesson.sentences.flatten().forEach { token ->
                val meaning = token.lexeme.meaning.trim()
                assertTrue("${lesson.title}: '${token.text}' için anlam boş", meaning.isNotBlank())
                assertTrue(
                    "${lesson.title}: '${token.text}' için eksik anlam kaydı: $meaning",
                    !meaning.contains("tamamlanmalı", ignoreCase = true) &&
                        !meaning.contains("eksik", ignoreCase = true)
                )
            }
        }
    }

    @Test
    fun nounPhraseSelectionKeepsIndividualWordsButLinksTheGroup() {
        val sentence = SampleLessons.all.first().sentences[6] // eine kurze Pause
        val eine = sentence.first { clean(it.text) == "eine" }
        val kurze = sentence.first { clean(it.text) == "kurze" }
        val pause = sentence.first { clean(it.text) == "pause" }

        assertTrue(eine.lexeme.id != kurze.lexeme.id)
        assertTrue(kurze.lexeme.id != pause.lexeme.id)
        assertTrue(sharedContextLink(eine.lexeme.contextLinkIds, kurze.lexeme.contextLinkIds))
        assertTrue(sharedContextLink(kurze.lexeme.contextLinkIds, pause.lexeme.contextLinkIds))
    }

    @Test
    fun fragenNachLinksVerbStronglyAndObjectWeakly() {
        val sentence = SampleLessons.all.first().sentences[4]
        val fragt = sentence.first { clean(it.text) == "fragt" }
        val nach = sentence.first { clean(it.text) == "nach" }
        val dem = sentence.first { clean(it.text) == "dem" }
        val fruehstueck = sentence.first { clean(it.text) == "frühstück" }

        assertNotNull(fragt.lexeme.strongLinkId)
        assertEquals(fragt.lexeme.strongLinkId, nach.lexeme.strongLinkId)
        assertTrue(sharedContextLink(nach.lexeme.contextLinkIds, fruehstueck.lexeme.contextLinkIds))
        assertTrue(sharedContextLink(dem.lexeme.contextLinkIds, fruehstueck.lexeme.contextLinkIds))
    }

    @Test
    fun sprechenMitUeberIsOneStrongGroupAndObjectRemainsWeakContext() {
        val sentence = SampleLessons.all.first().sentences[10]
        val spricht = sentence.first { clean(it.text) == "spricht" }
        val mit = sentence.first { clean(it.text) == "mit" }
        val ueber = sentence.first { clean(it.text) == "über" }
        val arbeitstag = sentence.first { clean(it.text) == "arbeitstag" }

        assertNotNull(spricht.lexeme.strongLinkId)
        assertEquals(spricht.lexeme.strongLinkId, mit.lexeme.strongLinkId)
        assertEquals(spricht.lexeme.strongLinkId, ueber.lexeme.strongLinkId)
        assertTrue(sharedContextLink(ueber.lexeme.contextLinkIds, arbeitstag.lexeme.contextLinkIds))
    }

    private fun sharedContextLink(first: List<String>, second: List<String>): Boolean =
        first.any { it in second }

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
