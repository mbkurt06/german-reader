package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import org.junit.Assert.assertEquals
import org.junit.Test

class StudyMeaningCatalogTest {
    @Test
    fun englishStudyMeaningUsesTranslationLanguage() {
        val breakfast = Lexeme(
            id = "breakfast",
            base = "Frühstück",
            meaning = "kahvaltı",
            dictionaryForm = "Frühstück"
        )
        assertEquals("breakfast", StudyMeaningCatalog.meaningFor(breakfast, "en"))
        assertEquals("kahvaltı", StudyMeaningCatalog.meaningFor(breakfast, "tr"))
    }

    @Test
    fun englishContextMeaningUsesFullPhrase() {
        val phrase = Lexeme(
            id = "questions",
            base = "Fragen",
            meaning = "sorular",
            contextExpression = "die ersten Fragen von den Gästen",
            contextMeaning = "misafirlerin ilk soruları"
        )
        assertEquals(
            "the first questions from the guests",
            StudyMeaningCatalog.contextMeaningFor(phrase, "en")
        )
    }
}
