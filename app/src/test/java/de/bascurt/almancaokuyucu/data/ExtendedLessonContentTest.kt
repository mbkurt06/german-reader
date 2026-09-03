package de.bascurt.almancaokuyucu.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExtendedLessonContentTest {

    @Test
    fun separableVerbDoesNotCaptureNormalPreposition() {
        val sentence = auditSentence("Er wärmt sich zehn Minuten auf dem Fahrrad auf.")
        val waermt = sentence.first { clean(it.text) == "wärmt" }
        val sich = sentence.first { clean(it.text) == "sich" }
        val aufs = sentence.filter { clean(it.text) == "auf" }

        assertEquals(2, aufs.size)
        assertEquals("Edat", aufs.first().lexeme.wordClass)
        assertEquals(waermt.lexeme.id, sich.lexeme.id)
        assertNotEquals(waermt.lexeme.id, aufs.first().lexeme.id)
        assertEquals(waermt.lexeme.id, aufs.last().lexeme.id)
    }

    @Test
    fun separableParticleBeforeConjunctionStaysInItsClause() {
        val sentence = auditSentence("Er macht das Licht an und sie schaut die Anzeige an.")
        val macht = sentence.first { clean(it.text) == "macht" }
        val ans = sentence.filter { clean(it.text) == "an" }

        assertEquals(2, ans.size)
        assertEquals("anmachen", macht.lexeme.base)
        assertEquals(macht.lexeme.id, ans.first().lexeme.id)
        assertNotEquals(macht.lexeme.id, ans.last().lexeme.id)
    }

    @Test
    fun strongLinksAreSentenceScoped() {
        val lesson = SampleLessons.all.first()
        val storySpricht = lesson.sentences[10].first { clean(it.text) == "spricht" }
        val reinforcementSpricht = lesson.sentences[13].first { clean(it.text) == "spricht" }

        assertNotNull(storySpricht.lexeme.strongLinkId)
        assertNotNull(reinforcementSpricht.lexeme.strongLinkId)
        assertNotEquals(storySpricht.lexeme.strongLinkId, reinforcementSpricht.lexeme.strongLinkId)
    }

    @Test
    fun prepositionObjectLinksAreConsistentAcrossExamples() {
        val lesson = SampleLessons.all.first()

        val breakfastSentence = lesson.sentences[4]
        val nach = breakfastSentence.first { clean(it.text) == "nach" }
        val breakfast = breakfastSentence.first { clean(it.text) == "frühstück" }
        assertTrue(sharedContextLink(nach.lexeme.contextLinkIds, breakfast.lexeme.contextLinkIds))

        val workdaySentence = lesson.sentences[10]
        val ueber = workdaySentence.first { clean(it.text) == "über" }
        val arbeitstag = workdaySentence.first { clean(it.text) == "arbeitstag" }
        assertTrue(sharedContextLink(ueber.lexeme.contextLinkIds, arbeitstag.lexeme.contextLinkIds))
    }

    private fun auditSentence(text: String) = ExtendedLessonFactory.lesson(
        id = "content-regression",
        title = "Audit",
        level = "A2",
        summary = "test",
        texts = listOf(text)
    ).sentences.first()

    private fun sharedContextLink(first: List<String>, second: List<String>): Boolean =
        first.any { it in second }

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()
}
