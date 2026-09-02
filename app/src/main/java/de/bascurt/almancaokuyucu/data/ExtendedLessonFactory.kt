package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

internal object ExtendedLessonFactory {
    private data class SeparablePattern(val surface: String, val particle: String, val seed: ExtendedVerbSeed)
    private data class GroupPattern(val trigger: String, val required: Set<String>, val seed: ExtendedVerbSeed)

    fun lesson(id: String, title: String, level: String, summary: String, texts: List<String>): ReaderLesson {
        val expandedTexts = texts + reinforcementSentences(summary)
        return ReaderLesson(id, title, level, summary, expandedTexts.mapIndexed { si, sentence -> tokenizeSentence(id, si, sentence) })
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
        val lexemes = keys.mapIndexed { ti, key -> lexemeFor(lessonId, sentenceIndex, ti, key, shownTokens[ti]) }.toMutableList()

        // Ayrılabilen fiilde yalnız cümlecik sonunda duran gerçek ön ek seçilir.
        // Böylece "wärmt sich auf dem Fahrrad auf" içinde ilk auf edat olarak kalır,
        // yalnız sondaki auf -> aufwärmen grubuna bağlanır.
        separablePatterns.forEach { pattern ->
            keys.indices.filter { keys[it] == pattern.surface }.forEach { verbIndex ->
                val particleIndex = findSeparableParticle(pattern.particle, verbIndex, keys, shownTokens) ?: return@forEach
                val grouped = verbLexeme(
                    lessonId,
                    pattern.seed,
                    "Ayrılabilen fiil: cümlede ‘${shownTokens[verbIndex]} … ${shownTokens[particleIndex]}’ tek fiildir. Normal edat olan aynı yazılıştaki kelimeler gruba alınmaz."
                )
                lexemes[verbIndex] = grouped
                lexemes[particleIndex] = grouped
                if (pattern.seed.base.startsWith("sich ")) {
                    nearestIndex(keys, "sich", verbIndex, 6)?.let { lexemes[it] = grouped }
                }
            }
        }

        // Dönüşlü fiillerde sich + fiil birlikte seçilir.
        keys.forEachIndexed { verbIndex, key ->
            val seed = verbSeedFor(key) ?: return@forEachIndexed
            if (!seed.base.startsWith("sich ")) return@forEachIndexed
            val sichIndex = nearestIndex(keys, "sich", verbIndex, 6) ?: return@forEachIndexed
            val grouped = verbLexeme(lessonId, seed, "Dönüşlü fiil: ‘sich’ ve fiil birlikte tek kelime grubu olarak öğrenilir.")
            lexemes[verbIndex] = grouped
            lexemes[sichIndex] = grouped
        }

        // Sabit fiil + edat/zamir yapılarında yalnız tetikleyiciye en yakın gerçek parça bağlanır.
        fixedGroups.forEach { pattern ->
            keys.indices.filter { keys[it] == pattern.trigger }.forEach { triggerIndex ->
                val matched = pattern.required.mapNotNull { required ->
                    nearestIndex(keys, required, triggerIndex, 7)?.let { required to it }
                }
                if (matched.size != pattern.required.size) return@forEach
                val grouped = verbLexeme(lessonId, pattern.seed, "Sabit kelime grubu: yalnız bu yapının gerçek parçaları birlikte vurgulanır.")
                lexemes[triggerIndex] = grouped
                matched.forEach { (_, index) -> lexemes[index] = grouped }
            }
        }

        return shownTokens.mapIndexed { index, shown -> ReadingToken(shown, lexemes[index]) }
    }

    private fun findSeparableParticle(
        particle: String,
        verbIndex: Int,
        keys: List<String>,
        shownTokens: List<String>
    ): Int? = (verbIndex + 1 until keys.size)
        .filter { keys[it] == particle && isParticlePosition(it, keys, shownTokens) }
        .lastOrNull()

    private fun isParticlePosition(index: Int, keys: List<String>, shownTokens: List<String>): Boolean {
        if (index == keys.lastIndex) return true
        val raw = shownTokens[index]
        if (raw.endsWith('.') || raw.endsWith(',') || raw.endsWith(';') || raw.endsWith('!') || raw.endsWith('?')) return true
        return keys[index + 1] in clauseJoiners
    }

    private fun nearestIndex(keys: List<String>, target: String, center: Int, maxDistance: Int): Int? =
        keys.indices
            .filter { keys[it] == target && kotlin.math.abs(it - center) <= maxDistance }
            .minByOrNull { kotlin.math.abs(it - center) }

    private fun lexemeFor(lessonId: String, sentenceIndex: Int, tokenIndex: Int, key: String, shown: String): Lexeme {
        val startsUpper = shown.firstOrNull()?.isUpperCase() == true

        // Almancada isimler büyük yazılır. Nominalize edilmiş Essen/Fahren/Gehen gibi biçimlerde
        // önce isim sözlüğünü kontrol ederek bunların yanlışlıkla fiil olmasını önlüyoruz.
        if (startsUpper) nounSeedFor(key)?.let { return nounLexeme(lessonId, it) }

        verbSeedFor(key)?.let { return verbLexeme(lessonId, it) }
        nounSeedFor(key)?.let { return nounLexeme(lessonId, it) }
        baseSurfaceMeanings[key]?.let { return surfaceLexeme(lessonId, sentenceIndex, tokenIndex, key, it) }
        supplementalSurfaceMeanings[key]?.let { return surfaceLexeme(lessonId, sentenceIndex, tokenIndex, key, it) }

        if (startsUpper) {
            return Lexeme(
                id = "$lessonId-name-$sentenceIndex-$tokenIndex",
                base = shown.trim('"', '„', '“', '.', ',', ':', ';', '!', '?'),
                meaning = "özel isim / ad",
                type = "Özel isim",
                quizEligible = false,
                wordClass = "Özel isim"
            )
        }

        // Bu satıra düşen kelime içerik kalite kontrolünde eksik demektir. Hikâye kelimelerinin
        // tamamı merkezi sözlüklere eklenir; kullanıcıya anlamsız bir Almanca tekrar göstermeyiz.
        return Lexeme(
            id = "$lessonId-x-$sentenceIndex-$tokenIndex",
            base = key,
            meaning = "Türkçe anlamı içerik denetiminde tamamlanmalı",
            explanation = "Bu kelime merkezi hikâye sözlüğünde eksik bulundu. Sınav havuzuna alınmaz.",
            type = "Diğer",
            quizEligible = false,
            wordClass = "Diğer"
        )
    }

    private fun verbSeedFor(key: String): ExtendedVerbSeed? = extendedVerbLexicon[key] ?: supplementalVerbLexicon[key]
    private fun nounSeedFor(key: String): ExtendedNounSeed? = extendedNounLexicon[key] ?: supplementalNounLexicon[key] ?: genericNouns[key]

    private fun nounLexeme(lessonId: String, n: ExtendedNounSeed) = Lexeme(
        id = "$lessonId-n-${n.base.lowercase()}",
        base = n.base,
        meaning = n.meaning,
        type = "Kelime",
        explanation = "Bu isim hikâyenin bağlamındaki Türkçe anlamıyla merkezi sözlükten gelir.",
        quizEligible = true,
        wordClass = "İsim",
        article = n.article,
        plural = n.plural
    )

    private fun verbLexeme(lessonId: String, v: ExtendedVerbSeed, note: String = "Bu fiil hikâyedeki bağlamına göre merkezi fiil sözlüğünden gelir.") = Lexeme(
        id = "$lessonId-v-${v.base}",
        base = v.base,
        meaning = v.meaning,
        type = if (v.base.contains(' ')) "Kelime grubu" else "Fiil",
        explanation = note,
        quizEligible = true,
        wordClass = "Fiil",
        infinitive = v.base,
        thirdPerson = v.third,
        preterite = v.preterite,
        perfect = v.perfect
    )

    private fun surfaceLexeme(
        lessonId: String,
        sentenceIndex: Int,
        tokenIndex: Int,
        key: String,
        value: Triple<String, String, String>
    ) = Lexeme(
        id = "$lessonId-s-$sentenceIndex-$tokenIndex-$key",
        base = value.first,
        meaning = value.second,
        type = value.third,
        quizEligible = false,
        wordClass = value.third
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

    private val clauseJoiners = setOf("und", "aber", "oder", "denn", "sondern", "weil", "wenn", "dass", "bevor", "während", "obwohl")

    private val separablePatterns = listOf(
        SeparablePattern("macht", "an", ExtendedVerbSeed("anmachen", "açmak / çalıştırmak", "macht an", "machte an", "hat angemacht")),
        SeparablePattern("macht", "auf", ExtendedVerbSeed("aufmachen", "açmak", "macht auf", "machte auf", "hat aufgemacht")),
        SeparablePattern("trocknet", "ab", ExtendedVerbSeed("abtrocknen", "kurulamak", "trocknet ab", "trocknete ab", "hat abgetrocknet")),
        SeparablePattern("räumt", "weg", ExtendedVerbSeed("wegräumen", "ortadan kaldırmak / yerine koymak", "räumt weg", "räumte weg", "hat weggeräumt")),
        SeparablePattern("räumt", "auf", ExtendedVerbSeed("aufräumen", "toplamak / düzenlemek", "räumt auf", "räumte auf", "hat aufgeräumt")),
        SeparablePattern("räumt", "aus", ExtendedVerbSeed("ausräumen", "boşaltmak", "räumt aus", "räumte aus", "hat ausgeräumt")),
        SeparablePattern("räumt", "ein", ExtendedVerbSeed("einräumen", "yerleştirmek / içine dizmek", "räumt ein", "räumte ein", "hat eingeräumt")),
        SeparablePattern("räumt", "ab", ExtendedVerbSeed("abräumen", "masayı toplamak", "räumt ab", "räumte ab", "hat abgeräumt")),
        SeparablePattern("zieht", "an", ExtendedVerbSeed("anziehen", "giymek", "zieht an", "zog an", "hat angezogen")),
        SeparablePattern("stellt", "auf", ExtendedVerbSeed("aufstellen", "kurmak / dikmek / yerleştirmek", "stellt auf", "stellte auf", "hat aufgestellt")),
        SeparablePattern("stellt", "vor", ExtendedVerbSeed("vorstellen", "tanıtmak / sunmak", "stellt vor", "stellte vor", "hat vorgestellt")),
        SeparablePattern("baut", "zusammen", ExtendedVerbSeed("zusammenbauen", "monte etmek / kurmak", "baut zusammen", "baute zusammen", "hat zusammengebaut")),
        SeparablePattern("füllt", "hinein", ExtendedVerbSeed("hineinfüllen", "içine doldurmak", "füllt hinein", "füllte hinein", "hat hineingefüllt")),
        SeparablePattern("füllt", "nach", ExtendedVerbSeed("nachfüllen", "yeniden doldurmak", "füllt nach", "füllte nach", "hat nachgefüllt")),
        SeparablePattern("füllt", "aus", ExtendedVerbSeed("ausfüllen", "form doldurmak", "füllt aus", "füllte aus", "hat ausgefüllt")),
        SeparablePattern("probiert", "an", ExtendedVerbSeed("anprobieren", "denemek (kıyafet)", "probiert an", "probierte an", "hat anprobiert")),
        SeparablePattern("meldet", "an", ExtendedVerbSeed("anmelden", "kayıt yaptırmak / bildirmek", "meldet an", "meldete an", "hat angemeldet")),
        SeparablePattern("hört", "ab", ExtendedVerbSeed("abhören", "dinleyerek muayene etmek", "hört ab", "hörte ab", "hat abgehört")),
        SeparablePattern("ruft", "an", ExtendedVerbSeed("anrufen", "telefonla aramak", "ruft an", "rief an", "hat angerufen")),
        SeparablePattern("hängt", "auf", ExtendedVerbSeed("aufhängen", "asmak", "hängt auf", "hängte auf", "hat aufgehängt")),
        SeparablePattern("hängt", "zurück", ExtendedVerbSeed("zurückhängen", "geri asmak / yerine asmak", "hängt zurück", "hängte zurück", "hat zurückgehängt")),
        SeparablePattern("holt", "ab", ExtendedVerbSeed("abholen", "gidip almak / teslim almak", "holt ab", "holte ab", "hat abgeholt")),
        SeparablePattern("packt", "ein", ExtendedVerbSeed("einpacken", "paketlemek / içine koymak", "packt ein", "packte ein", "hat eingepackt")),
        SeparablePattern("packt", "aus", ExtendedVerbSeed("auspacken", "paketten çıkarmak", "packt aus", "packte aus", "hat ausgepackt")),
        SeparablePattern("kommt", "an", ExtendedVerbSeed("ankommen", "varmak", "kommt an", "kam an", "ist angekommen")),
        SeparablePattern("wärmt", "auf", ExtendedVerbSeed("sich aufwärmen", "ısınmak", "wärmt sich auf", "wärmte sich auf", "hat sich aufgewärmt")),
        SeparablePattern("trägt", "ein", ExtendedVerbSeed("eintragen", "kaydetmek / girmek", "trägt ein", "trug ein", "hat eingetragen")),
        SeparablePattern("gibt", "ab", ExtendedVerbSeed("abgeben", "teslim etmek", "gibt ab", "gab ab", "hat abgegeben")),
        SeparablePattern("gibt", "ein", ExtendedVerbSeed("eingeben", "girmek / sisteme yazmak", "gibt ein", "gab ein", "hat eingegeben")),
        SeparablePattern("gibt", "hinein", ExtendedVerbSeed("hineingeben", "içine koymak", "gibt hinein", "gab hinein", "hat hineingegeben")),
        SeparablePattern("nimmt", "mit", ExtendedVerbSeed("mitnehmen", "yanına almak / götürmek", "nimmt mit", "nahm mit", "hat mitgenommen")),
        SeparablePattern("nimmt", "heraus", ExtendedVerbSeed("herausnehmen", "içinden çıkarmak", "nimmt heraus", "nahm heraus", "hat herausgenommen")),
        SeparablePattern("legt", "weg", ExtendedVerbSeed("weglegen", "bir kenara koymak", "legt weg", "legte weg", "hat weggelegt")),
        SeparablePattern("sammelt", "ein", ExtendedVerbSeed("einsammeln", "toplamak", "sammelt ein", "sammelte ein", "hat eingesammelt")),
        SeparablePattern("fettet", "ein", ExtendedVerbSeed("einfetten", "yağlamak", "fettet ein", "fettete ein", "hat eingefettet")),
        SeparablePattern("schließt", "ab", ExtendedVerbSeed("abschließen", "kilitlemek", "schließt ab", "schloss ab", "hat abgeschlossen")),
        SeparablePattern("schließt", "ein", ExtendedVerbSeed("einschließen", "kilitlemek / içine kilitlemek", "schließt ein", "schloss ein", "hat eingeschlossen")),
        SeparablePattern("fährt", "ab", ExtendedVerbSeed("abfahren", "hareket etmek / yola çıkmak", "fährt ab", "fuhr ab", "ist abgefahren")),
        SeparablePattern("fährt", "ein", ExtendedVerbSeed("einfahren", "istasyona girmek", "fährt ein", "fuhr ein", "ist eingefahren")),
        SeparablePattern("fährt", "los", ExtendedVerbSeed("losfahren", "yola çıkmak", "fährt los", "fuhr los", "ist losgefahren")),
        SeparablePattern("fährt", "weiter", ExtendedVerbSeed("weiterfahren", "yola devam etmek", "fährt weiter", "fuhr weiter", "ist weitergefahren")),
        SeparablePattern("steigt", "ein", ExtendedVerbSeed("einsteigen", "binmek", "steigt ein", "stieg ein", "ist eingestiegen")),
        SeparablePattern("steigt", "aus", ExtendedVerbSeed("aussteigen", "inmek", "steigt aus", "stieg aus", "ist ausgestiegen")),
        SeparablePattern("liest", "ab", ExtendedVerbSeed("ablesen", "okuyup aktarmak", "liest ab", "las ab", "hat abgelesen")),
        SeparablePattern("bereitet", "vor", ExtendedVerbSeed("vorbereiten", "hazırlamak", "bereitet vor", "bereitete vor", "hat vorbereitet")),
        SeparablePattern("nimmt", "teil", ExtendedVerbSeed("teilnehmen", "katılmak", "nimmt teil", "nahm teil", "hat teilgenommen")),
        SeparablePattern("setzt", "fort", ExtendedVerbSeed("fortsetzen", "devam ettirmek", "setzt fort", "setzte fort", "hat fortgesetzt")),
        SeparablePattern("setzt", "hin", ExtendedVerbSeed("sich hinsetzen", "oturmak", "setzt sich hin", "setzte sich hin", "hat sich hingesetzt"))
    )

    private val fixedGroups = listOf(
        GroupPattern("fragt", setOf("nach"), ExtendedVerbSeed("nach etwas fragen", "bir şeyi sormak", "fragt nach", "fragte nach", "hat nach etwas gefragt")),
        GroupPattern("achtet", setOf("auf"), ExtendedVerbSeed("auf etwas achten", "bir şeye dikkat etmek", "achtet auf", "achtete auf", "hat auf etwas geachtet")),
        GroupPattern("spricht", setOf("über"), ExtendedVerbSeed("über etwas sprechen", "bir şey hakkında konuşmak", "spricht über", "sprach über", "hat über etwas gesprochen")),
        GroupPattern("entscheidet", setOf("sich", "für"), ExtendedVerbSeed("sich für etwas entscheiden", "bir şeye karar vermek", "entscheidet sich", "entschied sich", "hat sich entschieden")),
        GroupPattern("bedanken", setOf("sich"), ExtendedVerbSeed("sich bedanken", "teşekkür etmek", "bedankt sich", "bedankte sich", "hat sich bedankt")),
        GroupPattern("bedankt", setOf("sich"), ExtendedVerbSeed("sich bedanken", "teşekkür etmek", "bedankt sich", "bedankte sich", "hat sich bedankt")),
        GroupPattern("verabschieden", setOf("sich"), ExtendedVerbSeed("sich verabschieden", "vedalaşmak", "verabschiedet sich", "verabschiedete sich", "hat sich verabschiedet")),
        GroupPattern("verabschiedet", setOf("sich"), ExtendedVerbSeed("sich verabschieden", "vedalaşmak", "verabschiedet sich", "verabschiedete sich", "hat sich verabschiedet")),
        GroupPattern("fühlt", setOf("sich"), ExtendedVerbSeed("sich fühlen", "hissetmek", "fühlt sich", "fühlte sich", "hat sich gefühlt"))
    )

    private val genericNouns: Map<String, ExtendedNounSeed> = buildMap {
        fun add(base: String, meaning: String, article: String, plural: String, vararg forms: String) {
            val seed = ExtendedNounSeed(base, meaning, article, plural)
            forms.forEach { put(it, seed) }
        }
        add("Person", "kişi", "die", "Personen", "person")
        add("Fachkraft", "uzman personel", "die", "Fachkräfte", "fachkraft")
        add("Sache", "şey / eşya", "die", "Sachen", "sache", "sachen")
        add("Arbeitsplatz", "çalışma yeri", "der", "Arbeitsplätze", "arbeitsplatz")
        add("Ding", "şey / eşya", "das", "Dinge", "ding", "dinge")
        add("Ergebnis", "sonuç", "das", "Ergebnisse", "ergebnis", "ergebnisse")
        add("Schritt", "adım", "der", "Schritte", "schritt", "schritte")
        add("Aufgabe", "görev", "die", "Aufgaben", "aufgabe", "aufgaben")
        add("Moment", "an", "der", "Momente", "moment")
        add("Fahrt", "yolculuk / sürüş", "die", "Fahrten", "fahrt")
        add("Bedarf", "ihtiyaç", "der", "—", "bedarf")
        add("Fahren", "araç kullanma / sürüş", "das", "—", "fahren")
        add("Herr", "bay / bey", "der", "Herren", "herr")
    }

    private val baseSurfaceMeanings = mapOf(
        "der" to Triple("der", "belirli artikel", "Artikel"), "die" to Triple("die", "belirli artikel", "Artikel"),
        "das" to Triple("das", "belirli artikel", "Artikel"), "den" to Triple("den", "belirli artikel (Akkusativ)", "Artikel"),
        "dem" to Triple("dem", "belirli artikel (Dativ)", "Artikel"), "des" to Triple("des", "belirli artikel (Genitiv)", "Artikel"),
        "ein" to Triple("ein", "bir", "Artikel"), "eine" to Triple("eine", "bir", "Artikel"), "einen" to Triple("einen", "bir", "Artikel"),
        "einem" to Triple("einem", "bir", "Artikel"), "einer" to Triple("einer", "bir", "Artikel"),
        "ich" to Triple("ich", "ben", "Zamir"), "du" to Triple("du", "sen", "Zamir"), "er" to Triple("er", "o (erkek)", "Zamir"),
        "sie" to Triple("sie", "o (kadın) / onlar", "Zamir"), "es" to Triple("es", "o", "Zamir"), "wir" to Triple("wir", "biz", "Zamir"),
        "ihr" to Triple("ihr", "siz / ona / onun", "Zamir"), "ihm" to Triple("ihm", "ona", "Zamir"),
        "ihre" to Triple("ihre", "onun", "Belirleyici"), "ihren" to Triple("ihren", "onun", "Belirleyici"),
        "seine" to Triple("seine", "onun", "Belirleyici"), "seinen" to Triple("seinen", "onun", "Belirleyici"),
        "sich" to Triple("sich", "kendini / kendisine", "Zamir"), "alle" to Triple("alle", "hepsi", "Belirleyici"),
        "und" to Triple("und", "ve", "Bağlaç"), "aber" to Triple("aber", "ama", "Bağlaç"), "oder" to Triple("oder", "veya", "Bağlaç"),
        "weil" to Triple("weil", "çünkü", "Bağlaç"), "dass" to Triple("dass", "-dığı / ki", "Bağlaç"),
        "wenn" to Triple("wenn", "eğer / -dığında", "Bağlaç"), "bevor" to Triple("bevor", "-meden önce", "Bağlaç"),
        "während" to Triple("während", "-iken / sırasında", "Bağlaç"), "ob" to Triple("ob", "olup olmadığını", "Bağlaç"),
        "im" to Triple("im", "-de / içinde", "Edat"), "ins" to Triple("ins", "içine / -e", "Edat"), "in" to Triple("in", "-de / içine", "Edat"),
        "am" to Triple("am", "-de / sırasında", "Edat"), "an" to Triple("an", "-de / yanında", "Edat"),
        "auf" to Triple("auf", "üzerinde / üzerine", "Edat"), "mit" to Triple("mit", "ile", "Edat"), "für" to Triple("für", "için", "Edat"),
        "nach" to Triple("nach", "sonra / -e doğru", "Edat"), "vor" to Triple("vor", "önce / önünde", "Edat"),
        "von" to Triple("von", "-den / tarafından", "Edat"), "zu" to Triple("zu", "-e / -a", "Edat"), "zum" to Triple("zum", "-e / -a", "Edat"),
        "zur" to Triple("zur", "-e / -a", "Edat"), "aus" to Triple("aus", "-den / dışarı", "Edat"), "bei" to Triple("bei", "-de / yanında", "Edat"),
        "beim" to Triple("beim", "-de / sırasında", "Edat"), "über" to Triple("über", "hakkında / üzerinde", "Edat"),
        "unter" to Triple("unter", "altında", "Edat"), "neben" to Triple("neben", "yanında", "Edat"),
        "heute" to Triple("heute", "bugün", "Zarf"), "gestern" to Triple("gestern", "dün", "Zarf"), "morgen" to Triple("morgen", "yarın", "Zarf"),
        "morgens" to Triple("morgens", "sabahları", "Zarf"), "abends" to Triple("abends", "akşamları", "Zarf"),
        "dann" to Triple("dann", "sonra", "Zarf"), "danach" to Triple("danach", "ondan sonra", "Zarf"),
        "später" to Triple("später", "daha sonra", "Zarf"), "zuerst" to Triple("zuerst", "önce", "Zarf"),
        "sofort" to Triple("sofort", "hemen", "Zarf"), "noch" to Triple("noch", "hâlâ / daha", "Zarf"),
        "auch" to Triple("auch", "ayrıca / de", "Zarf"), "schon" to Triple("schon", "zaten / çoktan", "Zarf"),
        "wieder" to Triple("wieder", "tekrar", "Zarf"), "zusammen" to Triple("zusammen", "birlikte", "Zarf"),
        "sehr" to Triple("sehr", "çok", "Zarf"), "etwas" to Triple("etwas", "biraz / bir şey", "Zamir"),
        "viel" to Triple("viel", "çok", "Belirleyici"), "viele" to Triple("viele", "birçok", "Belirleyici"),
        "einige" to Triple("einige", "bazı", "Belirleyici"), "nicht" to Triple("nicht", "değil / -me", "Parçacık"),
        "kein" to Triple("kein", "hiç / yok", "Belirleyici"), "keine" to Triple("keine", "hiç / yok", "Belirleyici"),
        "ist" to Triple("sein", "olmak", "Fiil"), "sind" to Triple("sein", "olmak", "Fiil"), "war" to Triple("sein", "idi", "Fiil"),
        "hat" to Triple("haben", "sahip olmak / var", "Fiil"), "haben" to Triple("haben", "sahip olmak", "Fiil"),
        "wird" to Triple("werden", "olmak", "Fiil"), "werden" to Triple("werden", "olmak", "Fiil"),
        "kann" to Triple("können", "-ebilmek", "Fiil"), "muss" to Triple("müssen", "zorunda olmak", "Fiil"),
        "müssen" to Triple("müssen", "zorunda olmak", "Fiil"), "soll" to Triple("sollen", "-meli / -malı", "Fiil"),
        "sollte" to Triple("sollen", "-meli / -malı", "Fiil"), "möchte" to Triple("möchten", "istemek", "Fiil"),
        "will" to Triple("wollen", "istemek", "Fiil"), "darf" to Triple("dürfen", "izinli olmak", "Fiil"),
        "ein" to Triple("ein", "bir / ayrılan ön ek olarak içeri", "Artikel"), "ab" to Triple("ab", "-den itibaren / ayrılan ön ek", "Parçacık"),
        "los" to Triple("los", "başlayarak / yola", "Parçacık"), "vor" to Triple("vor", "önce / önde", "Edat")
    )
}
