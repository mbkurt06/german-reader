package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

internal object ExtendedLessonFactory {
    fun lesson(id: String, title: String, level: String, summary: String, texts: List<String>): ReaderLesson =
        ReaderLesson(id, title, level, summary, texts.mapIndexed { si, sentence ->
            sentence.split(" ").mapIndexed { ti, shown ->
                val key = clean(shown)
                ReadingToken(shown, lexemeFor(id, si, ti, key))
            }
        })

    fun appendSentences(lesson: ReaderLesson, texts: List<String>): ReaderLesson {
        if (texts.isEmpty()) return lesson
        val start = lesson.sentences.size
        val extra = texts.mapIndexed { offset, sentence ->
            sentence.split(" ").mapIndexed { ti, shown ->
                val key = clean(shown)
                ReadingToken(shown, lexemeFor(lesson.id, start + offset, ti, key))
            }
        }
        return ReaderLesson(lesson.id, lesson.title, lesson.level, lesson.summary, lesson.sentences + extra)
    }

    private fun lexemeFor(lessonId: String, sentenceIndex: Int, tokenIndex: Int, key: String): Lexeme {
        extendedVerbLexicon[key]?.let { v ->
            return Lexeme(id = "$lessonId-v-$key", base = v.base, meaning = v.meaning, type = "Kelime", explanation = "Bu fiil bu günlük yaşam temasında sık kullanılan temel bir eylemdir.", quizEligible = true, wordClass = "Fiil", infinitive = v.base, thirdPerson = v.third, preterite = v.preterite, perfect = v.perfect)
        }
        extendedNounLexicon[key]?.let { n ->
            return Lexeme(id = "$lessonId-n-$key", base = n.base, meaning = n.meaning, type = "Kelime", explanation = "Bu isim hikâyenin temasındaki önemli günlük kelimelerden biridir.", quizEligible = true, wordClass = "İsim", article = n.article, plural = n.plural)
        }
        commonMeanings[key]?.let { c ->
            return Lexeme(id = "$lessonId-c-$sentenceIndex-$tokenIndex", base = c.first, meaning = c.second, type = c.third, quizEligible = false, wordClass = c.third)
        }
        return Lexeme(id = "$lessonId-x-$sentenceIndex-$tokenIndex", base = key, meaning = "Bağlama göre öğrenilecek yardımcı kelime", type = "Diğer", quizEligible = false, wordClass = "Diğer")
    }

    private fun clean(text: String): String = text.trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')').lowercase()

    private val commonMeanings = mapOf(
        "der" to Triple("der", "artikel", "Artikel"),
        "die" to Triple("die", "artikel", "Artikel"),
        "das" to Triple("das", "artikel", "Artikel"),
        "den" to Triple("den", "artikel", "Artikel"),
        "dem" to Triple("dem", "artikel", "Artikel"),
        "ein" to Triple("ein", "bir", "Artikel"),
        "eine" to Triple("eine", "bir", "Artikel"),
        "einen" to Triple("einen", "bir", "Artikel"),
        "einem" to Triple("einem", "bir", "Artikel"),
        "einer" to Triple("einer", "bir", "Artikel"),
        "und" to Triple("und", "ve", "Bağlaç"),
        "aber" to Triple("aber", "ama", "Bağlaç"),
        "weil" to Triple("weil", "çünkü", "Bağlaç"),
        "dass" to Triple("dass", "-dığı / ki", "Bağlaç"),
        "wenn" to Triple("wenn", "eğer / -dığında", "Bağlaç"),
        "bevor" to Triple("bevor", "-meden önce", "Bağlaç"),
        "während" to Triple("während", "-iken / sırasında", "Bağlaç"),
        "ob" to Triple("ob", "olup olmadığını", "Bağlaç"),
        "im" to Triple("im", "-de / içinde", "Edat"),
        "in" to Triple("in", "-de / içine", "Edat"),
        "am" to Triple("am", "-de / sırasında", "Edat"),
        "an" to Triple("an", "-de / yanında", "Edat"),
        "auf" to Triple("auf", "üzerinde / üzerine", "Edat"),
        "mit" to Triple("mit", "ile", "Edat"),
        "für" to Triple("für", "için", "Edat"),
        "nach" to Triple("nach", "sonra / -e doğru", "Edat"),
        "vor" to Triple("vor", "önce / önünde", "Edat"),
        "von" to Triple("von", "-den / tarafından", "Edat"),
        "zu" to Triple("zu", "-e / -a", "Edat"),
        "zum" to Triple("zum", "-e / -a", "Edat"),
        "zur" to Triple("zur", "-e / -a", "Edat"),
        "aus" to Triple("aus", "-den / dışarı", "Edat"),
        "bei" to Triple("bei", "-de / yanında", "Edat"),
        "über" to Triple("über", "hakkında / üzerinde", "Edat"),
        "unter" to Triple("unter", "altında", "Edat"),
        "neben" to Triple("neben", "yanında", "Edat"),
        "durch" to Triple("durch", "içinden / boyunca", "Edat"),
        "sie" to Triple("sie", "o / onlar", "Zamir"),
        "er" to Triple("er", "o", "Zamir"),
        "es" to Triple("es", "o", "Zamir"),
        "ihr" to Triple("ihr", "ona / onun", "Zamir"),
        "ihre" to Triple("ihre", "onun", "Zamir"),
        "ihren" to Triple("ihren", "onun", "Zamir"),
        "ihm" to Triple("ihm", "ona", "Zamir"),
        "seine" to Triple("seine", "onun", "Zamir"),
        "seinen" to Triple("seinen", "onun", "Zamir"),
        "sich" to Triple("sich", "kendini / kendisine", "Zamir"),
        "alle" to Triple("alle", "hepsi", "Zamir"),
        "heute" to Triple("heute", "bugün", "Zarf"),
        "gestern" to Triple("gestern", "dün", "Zarf"),
        "morgen" to Triple("morgen", "yarın", "Zarf"),
        "morgens" to Triple("morgens", "sabahları", "Zarf"),
        "abends" to Triple("abends", "akşamları", "Zarf"),
        "dann" to Triple("dann", "sonra", "Zarf"),
        "danach" to Triple("danach", "ondan sonra", "Zarf"),
        "später" to Triple("später", "daha sonra", "Zarf"),
        "zuerst" to Triple("zuerst", "önce", "Zarf"),
        "sofort" to Triple("sofort", "hemen", "Zarf"),
        "noch" to Triple("noch", "hala / daha", "Zarf"),
        "auch" to Triple("auch", "ayrıca / de", "Zarf"),
        "schon" to Triple("schon", "zaten / çoktan", "Zarf"),
        "wieder" to Triple("wieder", "tekrar", "Zarf"),
        "zusammen" to Triple("zusammen", "birlikte", "Zarf"),
        "sehr" to Triple("sehr", "çok", "Zarf"),
        "etwas" to Triple("etwas", "biraz / bir şey", "Zamir"),
        "viel" to Triple("viel", "çok", "Belirleyici"),
        "viele" to Triple("viele", "birçok", "Belirleyici"),
        "einige" to Triple("einige", "bazı", "Belirleyici"),
        "ist" to Triple("sein", "olmak", "Fiil"),
        "sind" to Triple("sein", "olmak", "Fiil"),
        "hat" to Triple("haben", "sahip olmak / var", "Fiil"),
        "haben" to Triple("haben", "sahip olmak", "Fiil"),
        "wird" to Triple("werden", "olmak", "Fiil"),
        "kann" to Triple("können", "-ebilmek", "Fiil"),
        "muss" to Triple("müssen", "zorunda olmak", "Fiil"),
        "soll" to Triple("sollen", "-meli / -malı", "Fiil"),
        "möchte" to Triple("möchten", "istemek", "Fiil"),
        "will" to Triple("wollen", "istemek", "Fiil"),
        "nicht" to Triple("nicht", "değil / -me", "Parçacık"),
        "kein" to Triple("kein", "hiç / yok", "Belirleyici"),
        "keine" to Triple("keine", "hiç / yok", "Belirleyici")
    )
}