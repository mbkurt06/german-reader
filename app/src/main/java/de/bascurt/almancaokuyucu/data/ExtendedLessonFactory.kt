package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

internal object ExtendedLessonFactory {
    fun lesson(id: String, title: String, level: String, summary: String, texts: List<String>): ReaderLesson {
        val expandedTexts = texts + reinforcementSentences(summary)
        return ReaderLesson(id, title, level, summary, expandedTexts.mapIndexed { si, sentence ->
            tokenizeSentence(id, si, sentence)
        })
    }

    fun appendSentences(lesson: ReaderLesson, texts: List<String>): ReaderLesson {
        if (texts.isEmpty()) return lesson
        val start = lesson.sentences.size
        val extra = texts.mapIndexed { offset, sentence -> tokenizeSentence(lesson.id, start + offset, sentence) }
        return ReaderLesson(lesson.id, lesson.title, lesson.level, lesson.summary, lesson.sentences + extra)
    }

    private fun tokenizeSentence(lessonId: String, sentenceIndex: Int, sentence: String): List<ReadingToken> {
        val shownTokens = sentence.split(" ")
        val keys = shownTokens.map(::clean)
        val lexemes = keys.mapIndexed { ti, key -> lexemeFor(lessonId, sentenceIndex, ti, key) }.toMutableList()

        // Ayrılabilen fiiller: çekimli fiil ve ayrılan ön ek aynı Lexeme kimliğini paylaşır.
        keys.forEachIndexed { verbIndex, key ->
            val particle = separableParticles[key] ?: return@forEachIndexed
            val particleIndex = keys.indexOfLast { it == particle }
            if (particleIndex > verbIndex) {
                val seed = extendedVerbLexicon[key] ?: return@forEachIndexed
                val grouped = verbLexeme(lessonId, key, seed, "Ayrılabilen fiil: cümlede ‘${shownTokens[verbIndex]} … ${shownTokens[particleIndex]}’ birlikte tek yapı olarak öğrenilir.")
                lexemes[verbIndex] = grouped
                lexemes[particleIndex] = grouped
            }
        }

        // Dönüşlü fiiller: sich ile fiil aynı Lexeme altında vurgulanır.
        keys.forEachIndexed { verbIndex, key ->
            val seed = extendedVerbLexicon[key] ?: return@forEachIndexed
            if (!seed.base.startsWith("sich ")) return@forEachIndexed
            val sichIndex = keys.indexOf("sich")
            if (sichIndex >= 0 && kotlin.math.abs(sichIndex - verbIndex) <= 5) {
                val grouped = verbLexeme(lessonId, key, seed, "Dönüşlü fiil: ‘sich’ ve fiil birlikte tek kelime grubu olarak öğrenilir.")
                lexemes[verbIndex] = grouped
                lexemes[sichIndex] = grouped
            }
        }

        return shownTokens.mapIndexed { index, shown -> ReadingToken(shown, lexemes[index]) }
    }

    private fun lexemeFor(lessonId: String, sentenceIndex: Int, tokenIndex: Int, key: String): Lexeme {
        extendedVerbLexicon[key]?.let { return verbLexeme(lessonId, key, it) }
        extendedNounLexicon[key]?.let { n ->
            return Lexeme(
                id = "$lessonId-n-$key",
                base = n.base,
                meaning = n.meaning.ifBlank { "Türkçe anlam henüz tanımlanmadı" },
                type = "Kelime",
                explanation = "Bu isim hikâyenin temasındaki önemli günlük kelimelerden biridir.",
                quizEligible = true,
                wordClass = "İsim",
                article = n.article,
                plural = n.plural
            )
        }
        commonMeanings[key]?.let { c ->
            return Lexeme(id = "$lessonId-c-$sentenceIndex-$tokenIndex", base = c.first, meaning = c.second, type = c.third, quizEligible = false, wordClass = c.third)
        }
        fallbackMeanings[key]?.let { meaning ->
            return Lexeme(id = "$lessonId-f-$key", base = key, meaning = meaning, type = "Kelime", quizEligible = false, wordClass = "Diğer")
        }
        return Lexeme(
            id = "$lessonId-x-$sentenceIndex-$tokenIndex",
            base = key,
            meaning = "Türkçe anlam henüz tanımlanmadı",
            explanation = "Bu kelimenin bağlama özel Türkçe karşılığı içerik sözlüğüne henüz eklenmedi.",
            type = "Diğer",
            quizEligible = false,
            wordClass = "Diğer"
        )
    }

    private fun verbLexeme(lessonId: String, key: String, v: ExtendedVerbSeed, note: String = "Bu fiil bu günlük yaşam temasında sık kullanılan temel bir eylemdir.") =
        Lexeme(
            id = "$lessonId-v-${v.base}",
            base = v.base,
            meaning = v.meaning.ifBlank { "Türkçe anlam henüz tanımlanmadı" },
            type = "Kelime grubu",
            explanation = note,
            quizEligible = true,
            wordClass = "Fiil",
            infinitive = v.base,
            thirdPerson = v.third,
            preterite = v.preterite,
            perfect = v.perfect
        )

    private fun reinforcementSentences(summary: String): List<String> {
        val category = summary.substringBefore('•').trim().lowercase()
        return when {
            category.contains("küche") || category.contains("essen") || category.contains("restaurant") || category.contains("bäck") -> listOf(
                "Danach kontrolliert die Person noch einmal alle wichtigen Sachen.",
                "Sie räumt den Arbeitsplatz auf und legt die benutzten Dinge zurück.",
                "Zum Schluss spricht sie kurz über das Ergebnis und ist zufrieden."
            )
            category.contains("arzt") || category.contains("gesund") || category.contains("kranken") || category.contains("apotheke") -> listOf(
                "Danach erklärt die Fachkraft die nächsten Schritte ganz in Ruhe.",
                "Die Person fragt noch einmal nach und hört aufmerksam zu.",
                "Zum Schluss bekommt sie einen Hinweis für die nächsten Tage."
            )
            category.contains("einkauf") || category.contains("markt") || category.contains("geschäft") -> listOf(
                "Danach vergleicht die Person noch zwei Angebote miteinander.",
                "Sie kontrolliert den Preis und legt die passenden Sachen in die Tasche.",
                "Zum Schluss bezahlt sie und nimmt den Kassenbon mit."
            )
            category.contains("verkehr") || category.contains("auto") || category.contains("reise") -> listOf(
                "Danach kontrolliert die Person noch einmal die Zeit und den Weg.",
                "Sie fragt nach einer Information und wartet einen kurzen Moment.",
                "Zum Schluss setzt sie ihre Fahrt ohne Probleme fort."
            )
            category.contains("arbeit") || category.contains("schule") -> listOf(
                "Danach kontrolliert die Person ihre Aufgaben noch einmal sorgfältig.",
                "Sie spricht kurz mit einer anderen Person über den nächsten Schritt.",
                "Zum Schluss räumt sie ihren Platz auf und beendet den Tag zufrieden."
            )
            category.contains("zuhause") || category.contains("haushalt") -> listOf(
                "Danach räumt die Person noch einige Dinge an ihren Platz zurück.",
                "Sie kontrolliert die Wohnung und öffnet kurz das Fenster.",
                "Zum Schluss macht sie eine Pause und genießt die Ruhe."
            )
            else -> listOf(
                "Danach kontrolliert die Person noch einmal alles in Ruhe.",
                "Sie fragt bei Bedarf nach und erledigt die nächsten Schritte.",
                "Zum Schluss ist die Aufgabe fertig und der Tag geht weiter."
            )
        }
    }

    private fun clean(text: String): String = text.trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')').lowercase()

    private val separableParticles = mapOf(
        "hängt" to "auf",
        "räumt" to "aus",
        "holt" to "ab",
        "meldet" to "an",
        "steigt" to "ein",
        "ordnet" to "an",
        "packt" to "ein",
        "baut" to "zusammen",
        "zieht" to "an",
        "trocknet" to "ab",
        "nimmt" to "mit",
        "stellt" to "vor",
        "macht" to "auf",
        "schaltet" to "ein",
        "ruft" to "an",
        "gibt" to "ab",
        "kommt" to "zurück",
        "fährt" to "los"
    )

    private val fallbackMeanings = mapOf(
        "person" to "kişi", "fachkraft" to "uzman personel", "sachen" to "şeyler / eşyalar",
        "arbeitsplatz" to "çalışma yeri", "dinge" to "şeyler / eşyalar", "ergebnis" to "sonuç",
        "ergebnisse" to "sonuçlar", "schritte" to "adımlar", "hinweis" to "uyarı / bilgi",
        "tage" to "günler", "angebote" to "teklifler / kampanyalar", "preis" to "fiyat",
        "tasche" to "çanta", "kassenbon" to "kasa fişi", "zeit" to "zaman", "weg" to "yol",
        "information" to "bilgi", "moment" to "an", "fahrt" to "yolculuk / sürüş", "probleme" to "sorunlar",
        "aufgaben" to "görevler", "platz" to "yer", "wohnung" to "ev / daire", "fenster" to "pencere",
        "pause" to "mola", "ruhe" to "sakinlik", "bedarf" to "ihtiyaç", "aufgabe" to "görev", "tag" to "gün",
        "wichtig" to "önemli", "wichtigen" to "önemli", "benutzten" to "kullanılmış", "nächsten" to "sonraki",
        "passenden" to "uygun", "kurzen" to "kısa", "sorgfältig" to "dikkatlice", "zufrieden" to "memnun",
        "ganz" to "tamamen / oldukça", "kurz" to "kısa / kısaca", "aufmerksam" to "dikkatli",
        "noch" to "daha / hâlâ", "einmal" to "bir kez", "zurück" to "geri", "miteinander" to "birbiriyle",
        "ohne" to "-sız / olmadan", "weiter" to "devam", "fertig" to "hazır / bitmiş"
    )

    private val commonMeanings = mapOf(
        "der" to Triple("der", "artikel", "Artikel"), "die" to Triple("die", "artikel", "Artikel"), "das" to Triple("das", "artikel", "Artikel"),
        "den" to Triple("den", "artikel", "Artikel"), "dem" to Triple("dem", "artikel", "Artikel"), "ein" to Triple("ein", "bir", "Artikel"),
        "eine" to Triple("eine", "bir", "Artikel"), "einen" to Triple("einen", "bir", "Artikel"), "einem" to Triple("einem", "bir", "Artikel"), "einer" to Triple("einer", "bir", "Artikel"),
        "und" to Triple("und", "ve", "Bağlaç"), "aber" to Triple("aber", "ama", "Bağlaç"), "weil" to Triple("weil", "çünkü", "Bağlaç"),
        "dass" to Triple("dass", "-dığı / ki", "Bağlaç"), "wenn" to Triple("wenn", "eğer / -dığında", "Bağlaç"), "bevor" to Triple("bevor", "-meden önce", "Bağlaç"),
        "während" to Triple("während", "-iken / sırasında", "Bağlaç"), "ob" to Triple("ob", "olup olmadığını", "Bağlaç"),
        "im" to Triple("im", "-de / içinde", "Edat"), "in" to Triple("in", "-de / içine", "Edat"), "am" to Triple("am", "-de / sırasında", "Edat"),
        "an" to Triple("an", "-de / yanında", "Edat"), "auf" to Triple("auf", "üzerinde / üzerine", "Edat"), "mit" to Triple("mit", "ile", "Edat"),
        "für" to Triple("für", "için", "Edat"), "nach" to Triple("nach", "sonra / -e doğru", "Edat"), "vor" to Triple("vor", "önce / önünde", "Edat"),
        "von" to Triple("von", "-den / tarafından", "Edat"), "zu" to Triple("zu", "-e / -a", "Edat"), "zum" to Triple("zum", "-e / -a", "Edat"),
        "zur" to Triple("zur", "-e / -a", "Edat"), "aus" to Triple("aus", "-den / dışarı", "Edat"), "bei" to Triple("bei", "-de / yanında", "Edat"),
        "über" to Triple("über", "hakkında / üzerinde", "Edat"), "unter" to Triple("unter", "altında", "Edat"), "neben" to Triple("neben", "yanında", "Edat"),
        "durch" to Triple("durch", "içinden / boyunca", "Edat"), "sie" to Triple("sie", "o / onlar", "Zamir"), "er" to Triple("er", "o", "Zamir"),
        "es" to Triple("es", "o", "Zamir"), "ihr" to Triple("ihr", "ona / onun", "Zamir"), "ihre" to Triple("ihre", "onun", "Zamir"),
        "ihren" to Triple("ihren", "onun", "Zamir"), "ihm" to Triple("ihm", "ona", "Zamir"), "seine" to Triple("seine", "onun", "Zamir"),
        "seinen" to Triple("seinen", "onun", "Zamir"), "sich" to Triple("sich", "kendini / kendisine", "Zamir"), "alle" to Triple("alle", "hepsi", "Zamir"),
        "heute" to Triple("heute", "bugün", "Zarf"), "gestern" to Triple("gestern", "dün", "Zarf"), "morgen" to Triple("morgen", "yarın", "Zarf"),
        "morgens" to Triple("morgens", "sabahları", "Zarf"), "abends" to Triple("abends", "akşamları", "Zarf"), "dann" to Triple("dann", "sonra", "Zarf"),
        "danach" to Triple("danach", "ondan sonra", "Zarf"), "später" to Triple("später", "daha sonra", "Zarf"), "zuerst" to Triple("zuerst", "önce", "Zarf"),
        "sofort" to Triple("sofort", "hemen", "Zarf"), "noch" to Triple("noch", "hala / daha", "Zarf"), "auch" to Triple("auch", "ayrıca / de", "Zarf"),
        "schon" to Triple("schon", "zaten / çoktan", "Zarf"), "wieder" to Triple("wieder", "tekrar", "Zarf"), "zusammen" to Triple("zusammen", "birlikte", "Zarf"),
        "sehr" to Triple("sehr", "çok", "Zarf"), "etwas" to Triple("etwas", "biraz / bir şey", "Zamir"), "viel" to Triple("viel", "çok", "Belirleyici"),
        "viele" to Triple("viele", "birçok", "Belirleyici"), "einige" to Triple("einige", "bazı", "Belirleyici"), "ist" to Triple("sein", "olmak", "Fiil"),
        "sind" to Triple("sein", "olmak", "Fiil"), "hat" to Triple("haben", "sahip olmak / var", "Fiil"), "haben" to Triple("haben", "sahip olmak", "Fiil"),
        "wird" to Triple("werden", "olmak", "Fiil"), "kann" to Triple("können", "-ebilmek", "Fiil"), "muss" to Triple("müssen", "zorunda olmak", "Fiil"),
        "soll" to Triple("sollen", "-meli / -malı", "Fiil"), "möchte" to Triple("möchten", "istemek", "Fiil"), "will" to Triple("wollen", "istemek", "Fiil"),
        "nicht" to Triple("nicht", "değil / -me", "Parçacık"), "kein" to Triple("kein", "hiç / yok", "Belirleyici"), "keine" to Triple("keine", "hiç / yok", "Belirleyici")
    )
}
