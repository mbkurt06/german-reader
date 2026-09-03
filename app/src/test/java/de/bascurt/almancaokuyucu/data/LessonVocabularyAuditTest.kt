package de.bascurt.almancaokuyucu.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LessonVocabularyAuditTest {

    @Test
    fun focusedReaderUsesOnlyEinNeuerAnfang() {
        assertEquals(listOf("a2-neuer-anfang"), SampleLessons.all.map { it.id })
    }

    @Test
    fun focusedStoryProvidesCompactDictionaryMetadata() {
        val lesson = SampleLessons.all.single()

        val fragt = lesson.sentences[4].first { clean(it.text) == "fragt" }.lexeme
        assertEquals("nach + D fragen", fragt.dictionaryForm)
        assertEquals("nach dem Frühstück fragen", fragt.contextExpression)
        assertEquals("kahvaltıyı / kahvaltı hakkında sormak", fragt.contextMeaning)

        val hotel = lesson.sentences[0].first { clean(it.text) == "hotel" }.lexeme
        assertEquals("das", hotel.article)
        assertEquals("Hotels", hotel.plural)
        assertEquals("im Hotel", hotel.contextExpression)

        val wichtig = lesson.sentences[2].first { clean(it.text) == "wichtigsten" }.lexeme
        assertEquals("wichtig", wichtig.positive)
        assertEquals("wichtiger", wichtig.comparative)
        assertEquals("am wichtigsten", wichtig.superlative)
        assertEquals("die wichtigsten Aufgaben", wichtig.contextExpression)
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
    fun everyNounHasArticleAndPluralMetadata() {
        SampleLessons.all.forEach { lesson ->
            lesson.sentences.flatten()
                .filter { it.lexeme.wordClass == "İsim" }
                .forEach { token ->
                    assertTrue(
                        "${lesson.title}: '${token.text}' isminin artikeli eksik",
                        !token.lexeme.article.isNullOrBlank()
                    )
                    assertTrue(
                        "${lesson.title}: '${token.text}' isminin çoğul bilgisi eksik",
                        !token.lexeme.plural.isNullOrBlank()
                    )
                }
        }
    }

    @Test
    fun nounPhraseSelectionKeepsIndividualWordsButLinksTheGroup() {
        val sentence = SampleLessons.all.first().sentences[6]
        val eine = sentence.first { clean(it.text) == "eine" }
        val kurze = sentence.first { clean(it.text) == "kurze" }
        val pause = sentence.first { clean(it.text) == "pause" }

        assertTrue(eine.lexeme.id != kurze.lexeme.id)
        assertTrue(kurze.lexeme.id != pause.lexeme.id)
        assertTrue(sharedContextLink(eine.lexeme.contextLinkIds, kurze.lexeme.contextLinkIds))
        assertTrue(sharedContextLink(kurze.lexeme.contextLinkIds, pause.lexeme.contextLinkIds))
    }

    @Test
    fun fragenNachOnlyLinksVerbAndPrepositionStrongly() {
        val sentence = SampleLessons.all.first().sentences[4]
        val fragt = sentence.first { clean(it.text) == "fragt" }
        val nach = sentence.first { clean(it.text) == "nach" }
        val dem = sentence.first { clean(it.text) == "dem" }
        val fruehstueck = sentence.first { clean(it.text) == "frühstück" }

        assertNotNull(fragt.lexeme.strongLinkId)
        assertEquals(fragt.lexeme.strongLinkId, nach.lexeme.strongLinkId)
        assertFalse(sharedContextLink(nach.lexeme.contextLinkIds, dem.lexeme.contextLinkIds))
        assertFalse(sharedContextLink(nach.lexeme.contextLinkIds, fruehstueck.lexeme.contextLinkIds))
        assertTrue(sharedContextLink(dem.lexeme.contextLinkIds, fruehstueck.lexeme.contextLinkIds))
    }

    @Test
    fun sprechenMitUeberStrongGroupDoesNotCaptureItsObjects() {
        val sentence = SampleLessons.all.first().sentences[10]
        val spricht = sentence.first { clean(it.text) == "spricht" }
        val mit = sentence.first { clean(it.text) == "mit" }
        val ueber = sentence.first { clean(it.text) == "über" }
        val ihrer = sentence.first { clean(it.text) == "ihrer" }
        val chefin = sentence.first { clean(it.text) == "chefin" }
        val den = sentence.first { clean(it.text) == "den" }
        val ersten = sentence.first { clean(it.text) == "ersten" }
        val arbeitstag = sentence.first { clean(it.text) == "arbeitstag" }

        assertNotNull(spricht.lexeme.strongLinkId)
        assertEquals(spricht.lexeme.strongLinkId, mit.lexeme.strongLinkId)
        assertEquals(spricht.lexeme.strongLinkId, ueber.lexeme.strongLinkId)

        assertFalse(sharedContextLink(mit.lexeme.contextLinkIds, chefin.lexeme.contextLinkIds))
        assertFalse(sharedContextLink(ueber.lexeme.contextLinkIds, arbeitstag.lexeme.contextLinkIds))

        assertTrue(sharedContextLink(ihrer.lexeme.contextLinkIds, chefin.lexeme.contextLinkIds))
        assertTrue(sharedContextLink(den.lexeme.contextLinkIds, ersten.lexeme.contextLinkIds))
        assertTrue(sharedContextLink(ersten.lexeme.contextLinkIds, arbeitstag.lexeme.contextLinkIds))
    }

    private fun sharedContextLink(first: List<String>, second: List<String>): Boolean =
        first.any { it in second }

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
