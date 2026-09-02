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

        // Ayrılabilen fiiller cümledeki gerçek çekimli fiil + ayrılan parçaya göre çözülür.
        separablePatterns.forEach { pattern ->
            val verbIndex = keys.indexOf(pattern.surface)
            if (verbIndex < 0) return@forEach
            val particleIndex = (verbIndex + 1 until keys.size).firstOrNull { keys[it] == pattern.particle } ?: return@forEach
            val grouped = verbLexeme(
                lessonId,
                pattern.seed,
                "Ayrılabilen fiil: cümlede ‘${shownTokens[verbIndex]} … ${shownTokens[particleIndex]}’ tek fiildir. İki parçadan birine dokunulduğunda ikisi birlikte seçilir."
            )
            lexemes[verbIndex] = grouped
            lexemes[particleIndex] = grouped
        }

        // Dönüşlü fiillerde sich + fiil birlikte seçilir.
        keys.forEachIndexed { verbIndex, key ->
            val seed = extendedVerbLexicon[key] ?: return@forEachIndexed
            if (!seed.base.startsWith("sich ")) return@forEachIndexed
            val sichIndex = keys.indices.filter { keys[it] == "sich" }.minByOrNull { kotlin.math.abs(it - verbIndex) } ?: return@forEachIndexed
            if (kotlin.math.abs(sichIndex - verbIndex) <= 6) {
                val grouped = verbLexeme(lessonId, seed, "Dönüşlü fiil: ‘sich’ ve fiil birlikte tek kelime grubu olarak öğrenilir.")
                lexemes[verbIndex] = grouped
                lexemes[sichIndex] = grouped
            }
        }

        // Günlük dilde birlikte öğrenilmesi gereken bazı sabit fiil + edat / zamir grupları.
        fixedGroups.forEach { pattern ->
            val triggerIndex = keys.indexOf(pattern.trigger)
            if (triggerIndex < 0 || !pattern.required.all { it in keys }) return@forEach
            val grouped = verbLexeme(lessonId, pattern.seed, "Sabit kelime grubu: bu parçalar cümlede birlikte öğrenilir ve birlikte vurgulanır.")
            lexemes[triggerIndex] = grouped
            pattern.required.forEach { requiredKey ->
                keys.indices.filter { keys[it] == requiredKey }.forEach { lexemes[it] = grouped }
            }
        }

        return shownTokens.mapIndexed { index, shown -> ReadingToken(shown, lexemes[index]) }
    }

    private fun lexemeFor(lessonId: String, sentenceIndex: Int, tokenIndex: Int, key: String, shown: String): Lexeme {
        extendedVerbLexicon[key]?.let { return verbLexeme(lessonId, it) }
        extendedNounLexicon[key]?.let { n ->
            return Lexeme(
                id = "$lessonId-n-${n.base.lowercase()}", base = n.base, meaning = n.meaning,
                type = "Kelime", explanation = "Bu isim hikâyenin temasındaki önemli günlük kelimelerden biridir.",
                quizEligible = true, wordClass = "İsim", article = n.article, plural = n.plural
            )
        }
        commonMeanings[key]?.let { c ->
            return Lexeme(id = "$lessonId-c-$sentenceIndex-$tokenIndex", base = c.first, meaning = c.second, type = c.third, quizEligible = false, wordClass = c.third)
        }
        surfaceMeanings[key]?.let { value ->
            return Lexeme(id = "$lessonId-f-$key", base = value.first, meaning = value.second, type = value.third, quizEligible = false, wordClass = value.third)
        }
        if (shown.firstOrNull()?.isUpperCase() == true) {
            return Lexeme(
                id = "$lessonId-name-$sentenceIndex-$tokenIndex",
                base = shown.trim('"', '„', '“', '.', ',', ':', ';', '!', '?'),
                meaning = "özel isim / ad", type = "Özel isim", quizEligible = false, wordClass = "Özel isim"
            )
        }
        return Lexeme(
            id = "$lessonId-x-$sentenceIndex-$tokenIndex", base = key,
            meaning = "Türkçe karşılığı içerik sözlüğünde eksik",
            explanation = "Bu kayıt sınav havuzuna alınmaz ve içerik kalite kontrolünde tamamlanmalıdır.",
            type = "Diğer", quizEligible = false, wordClass = "Diğer"
        )
    }

    private fun verbLexeme(lessonId: String, v: ExtendedVerbSeed, note: String = "Bu fiil bu günlük yaşam temasında sık kullanılan temel bir eylemdir.") =
        Lexeme(
            id = "$lessonId-v-${v.base}", base = v.base, meaning = v.meaning,
            type = if (v.base.contains(' ')) "Kelime grubu" else "Fiil", explanation = note,
            quizEligible = true, wordClass = "Fiil", infinitive = v.base,
            thirdPerson = v.third, preterite = v.preterite, perfect = v.perfect
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
        GroupPattern("verabschieden", setOf("sich"), ExtendedVerbSeed("sich verabschieden", "vedalaşmak", "verabschiedet sich", "verabschiedete sich", "hat sich verabschiedet")),
        GroupPattern("fühlt", setOf("sich"), ExtendedVerbSeed("sich fühlen", "hissetmek", "fühlt sich", "fühlte sich", "hat sich gefühlt"))
    )

    private val surfaceMeanings = mapOf(
        "ich" to Triple("ich", "ben", "Zamir"), "du" to Triple("du", "sen", "Zamir"), "wir" to Triple("wir", "biz", "Zamir"),
        "ihnen" to Triple("ihnen", "onlara / size", "Zamir"), "mein" to Triple("mein", "benim", "Belirleyici"), "meine" to Triple("meine", "benim", "Belirleyici"),
        "meinen" to Triple("meinen", "benim", "Belirleyici"), "seiner" to Triple("seiner", "onun", "Belirleyici"), "seinem" to Triple("seinem", "onun", "Belirleyici"),
        "ihrem" to Triple("ihrem", "onun", "Belirleyici"), "ihrer" to Triple("ihrer", "onun", "Belirleyici"), "dieses" to Triple("dieses", "bu", "Belirleyici"),
        "diese" to Triple("diese", "bu / bunlar", "Belirleyici"), "dieser" to Triple("dieser", "bu", "Belirleyici"), "welche" to Triple("welche", "hangi", "Zamir"),
        "was" to Triple("was", "ne", "Zamir"), "wer" to Triple("wer", "kim", "Zamir"), "manche" to Triple("manche", "bazı", "Belirleyici"),
        "mehrere" to Triple("mehrere", "birkaç / birden fazla", "Belirleyici"), "zwei" to Triple("zwei", "iki", "Sayı"), "drei" to Triple("drei", "üç", "Sayı"),
        "vier" to Triple("vier", "dört", "Sayı"), "fünf" to Triple("fünf", "beş", "Sayı"), "sechs" to Triple("sechs", "altı", "Sayı"),
        "sieben" to Triple("sieben", "yedi", "Sayı"), "acht" to Triple("acht", "sekiz", "Sayı"), "zehn" to Triple("zehn", "on", "Sayı"),
        "zwanzig" to Triple("zwanzig", "yirmi", "Sayı"), "vierzig" to Triple("vierzig", "kırk", "Sayı"),
        "erste" to Triple("erste", "ilk", "Sıfat"), "ersten" to Triple("erste", "ilk", "Sıfat"), "zweite" to Triple("zweite", "ikinci", "Sıfat"),
        "dritte" to Triple("dritte", "üçüncü", "Sıfat"), "neue" to Triple("neu", "yeni", "Sıfat"), "neuen" to Triple("neu", "yeni", "Sıfat"),
        "neues" to Triple("neu", "yeni", "Sıfat"), "kleine" to Triple("klein", "küçük", "Sıfat"), "kleinen" to Triple("klein", "küçük", "Sıfat"),
        "große" to Triple("groß", "büyük", "Sıfat"), "großen" to Triple("groß", "büyük", "Sıfat"), "starke" to Triple("stark", "şiddetli / güçlü", "Sıfat"),
        "schweren" to Triple("schwer", "ağır", "Sıfat"), "leichter" to Triple("leicht", "hafif", "Sıfat"), "freie" to Triple("frei", "boş / serbest", "Sıfat"),
        "freien" to Triple("frei", "boş / serbest", "Sıfat"), "frisches" to Triple("frisch", "taze", "Sıfat"), "frisch" to Triple("frisch", "taze", "Sıfat"),
        "reife" to Triple("reif", "olgun", "Sıfat"), "schmutzige" to Triple("schmutzig", "kirli", "Sıfat"), "helle" to Triple("hell", "açık renkli", "Sıfat"),
        "dunkle" to Triple("dunkel", "koyu renkli", "Sıfat"), "nasse" to Triple("nass", "ıslak", "Sıfat"), "nassen" to Triple("nass", "ıslak", "Sıfat"),
        "trockenen" to Triple("trocken", "kuru", "Sıfat"), "bequem" to Triple("bequem", "rahat", "Sıfat"), "bequeme" to Triple("bequem", "rahat", "Sıfat"),
        "lang" to Triple("lang", "uzun", "Sıfat"), "kurz" to Triple("kurz", "kısa / kısaca", "Sıfat"), "schön" to Triple("schön", "güzel", "Sıfat"),
        "schönen" to Triple("schön", "güzel", "Sıfat"), "lecker" to Triple("lecker", "lezzetli", "Sıfat"), "salzig" to Triple("salzig", "tuzlu", "Sıfat"),
        "sauber" to Triple("sauber", "temiz", "Sıfat"), "ordentlich" to Triple("ordentlich", "düzenli", "Sıfat"), "müde" to Triple("müde", "yorgun", "Sıfat"),
        "zufrieden" to Triple("zufrieden", "memnun", "Sıfat"), "allein" to Triple("allein", "yalnız", "Sıfat"), "geeignetes" to Triple("geeignet", "uygun", "Sıfat"),
        "möglichen" to Triple("möglich", "olası", "Sıfat"), "ungewöhnliches" to Triple("ungewöhnlich", "alışılmadık", "Sıfat"), "richtig" to Triple("richtig", "doğru", "Sıfat"),
        "richtige" to Triple("richtig", "doğru", "Sıfat"), "verschiedene" to Triple("verschieden", "farklı", "Sıfat"), "verschiedenen" to Triple("verschieden", "farklı", "Sıfat"),
        "ähnliches" to Triple("ähnlich", "benzer", "Sıfat"), "günstiger" to Triple("günstig", "daha uygun fiyatlı", "Sıfat"), "besser" to Triple("gut", "daha iyi", "Sıfat"),
        "schlechter" to Triple("schlecht", "daha kötü", "Sıfat"), "schlecht" to Triple("schlecht", "kötü", "Sıfat"), "langsam" to Triple("langsam", "yavaş", "Zarf"),
        "schnell" to Triple("schnell", "hızlı", "Zarf"), "direkt" to Triple("direkt", "doğrudan", "Zarf"), "gemeinsam" to Triple("gemeinsam", "birlikte", "Zarf"),
        "getrennt" to Triple("getrennt", "ayrı ayrı", "Zarf"), "vorsichtig" to Triple("vorsichtig", "dikkatlice", "Zarf"), "sorgfältig" to Triple("sorgfältig", "özenle / dikkatlice", "Zarf"),
        "wahrscheinlich" to Triple("wahrscheinlich", "muhtemelen", "Zarf"), "ungefähr" to Triple("ungefähr", "yaklaşık", "Zarf"), "besonders" to Triple("besonders", "özellikle", "Zarf"),
        "häufig" to Triple("häufig", "sık sık", "Zarf"), "manchmal" to Triple("manchmal", "bazen", "Zarf"), "oft" to Triple("oft", "sık sık", "Zarf"),
        "fast" to Triple("fast", "neredeyse", "Zarf"), "nur" to Triple("nur", "sadece", "Zarf"), "mehr" to Triple("mehr", "daha fazla", "Zarf"),
        "weniger" to Triple("weniger", "daha az", "Zarf"), "vorne" to Triple("vorne", "önde", "Zarf"), "draußen" to Triple("draußen", "dışarıda", "Zarf"),
        "oben" to Triple("oben", "yukarıda", "Zarf"), "unten" to Triple("unten", "aşağıda", "Zarf"), "dort" to Triple("dort", "orada", "Zarf"),
        "dabei" to Triple("dabei", "bu sırada / bunun yanında", "Zarf"), "deshalb" to Triple("deshalb", "bu yüzden", "Zarf"), "außerdem" to Triple("außerdem", "ayrıca", "Zarf"),
        "plötzlich" to Triple("plötzlich", "aniden", "Zarf"), "schließlich" to Triple("schließlich", "sonunda", "Zarf"), "anschließend" to Triple("anschließend", "ardından", "Zarf"),
        "ohne" to Triple("ohne", "-sız / olmadan", "Edat"), "wegen" to Triple("wegen", "nedeniyle", "Edat"), "bis" to Triple("bis", "-e kadar", "Edat"),
        "seit" to Triple("seit", "-den beri", "Edat"), "gegen" to Triple("gegen", "karşı / civarında", "Edat"), "zwischen" to Triple("zwischen", "arasında", "Edat"),
        "als" to Triple("als", "olarak / -dığında", "Bağlaç"), "obwohl" to Triple("obwohl", "-mesine rağmen", "Bağlaç"), "damit" to Triple("damit", "bununla / böylece", "Bağlaç"),
        "sondern" to Triple("sondern", "aksine", "Bağlaç"), "oder" to Triple("oder", "veya", "Bağlaç"), "keinen" to Triple("keinen", "hiç / yok", "Belirleyici"),
        "jeden" to Triple("jeden", "her", "Belirleyici"), "jede" to Triple("jede", "her", "Belirleyici"), "jeder" to Triple("jeder", "her", "Belirleyici")
    )

    private val commonMeanings = mapOf(
        "der" to Triple("der", "artikel", "Artikel"), "die" to Triple("die", "artikel", "Artikel"), "das" to Triple("das", "artikel", "Artikel"),
        "den" to Triple("den", "artikel", "Artikel"), "dem" to Triple("dem", "artikel", "Artikel"), "des" to Triple("des", "artikel", "Artikel"),
        "ein" to Triple("ein", "bir", "Artikel"), "eine" to Triple("eine", "bir", "Artikel"), "einen" to Triple("einen", "bir", "Artikel"), "einem" to Triple("einem", "bir", "Artikel"), "einer" to Triple("einer", "bir", "Artikel"),
        "und" to Triple("und", "ve", "Bağlaç"), "aber" to Triple("aber", "ama", "Bağlaç"), "weil" to Triple("weil", "çünkü", "Bağlaç"),
        "dass" to Triple("dass", "-dığı / ki", "Bağlaç"), "wenn" to Triple("wenn", "eğer / -dığında", "Bağlaç"), "bevor" to Triple("bevor", "-meden önce", "Bağlaç"),
        "während" to Triple("während", "-iken / sırasında", "Bağlaç"), "ob" to Triple("ob", "olup olmadığını", "Bağlaç"),
        "im" to Triple("im", "-de / içinde", "Edat"), "ins" to Triple("ins", "içine / -e", "Edat"), "in" to Triple("in", "-de / içine", "Edat"),
        "am" to Triple("am", "-de / sırasında", "Edat"), "an" to Triple("an", "-de / yanında", "Edat"), "auf" to Triple("auf", "üzerinde / üzerine", "Edat"),
        "mit" to Triple("mit", "ile", "Edat"), "für" to Triple("für", "için", "Edat"), "nach" to Triple("nach", "sonra / -e doğru", "Edat"),
        "vor" to Triple("vor", "önce / önünde", "Edat"), "von" to Triple("von", "-den / tarafından", "Edat"), "zu" to Triple("zu", "-e / -a", "Edat"),
        "zum" to Triple("zum", "-e / -a", "Edat"), "zur" to Triple("zur", "-e / -a", "Edat"), "aus" to Triple("aus", "-den / dışarı", "Edat"),
        "bei" to Triple("bei", "-de / yanında", "Edat"), "beim" to Triple("beim", "-de / sırasında", "Edat"), "über" to Triple("über", "hakkında / üzerinde", "Edat"),
        "unter" to Triple("unter", "altında", "Edat"), "neben" to Triple("neben", "yanında", "Edat"), "durch" to Triple("durch", "içinden / boyunca", "Edat"),
        "sie" to Triple("sie", "o / onlar", "Zamir"), "er" to Triple("er", "o", "Zamir"), "es" to Triple("es", "o", "Zamir"),
        "ihr" to Triple("ihr", "ona / onun", "Zamir"), "ihre" to Triple("ihre", "onun", "Zamir"), "ihren" to Triple("ihren", "onun", "Zamir"),
        "ihm" to Triple("ihm", "ona", "Zamir"), "seine" to Triple("seine", "onun", "Zamir"), "seinen" to Triple("seinen", "onun", "Zamir"),
        "sich" to Triple("sich", "kendini / kendisine", "Zamir"), "alle" to Triple("alle", "hepsi", "Zamir"),
        "heute" to Triple("heute", "bugün", "Zarf"), "gestern" to Triple("gestern", "dün", "Zarf"), "morgen" to Triple("morgen", "yarın", "Zarf"),
        "morgens" to Triple("morgens", "sabahları", "Zarf"), "abends" to Triple("abends", "akşamları", "Zarf"), "dann" to Triple("dann", "sonra", "Zarf"),
        "danach" to Triple("danach", "ondan sonra", "Zarf"), "später" to Triple("später", "daha sonra", "Zarf"), "zuerst" to Triple("zuerst", "önce", "Zarf"),
        "sofort" to Triple("sofort", "hemen", "Zarf"), "noch" to Triple("noch", "hala / daha", "Zarf"), "auch" to Triple("auch", "ayrıca / de", "Zarf"),
        "schon" to Triple("schon", "zaten / çoktan", "Zarf"), "wieder" to Triple("wieder", "tekrar", "Zarf"), "zusammen" to Triple("zusammen", "birlikte", "Zarf"),
        "sehr" to Triple("sehr", "çok", "Zarf"), "etwas" to Triple("etwas", "biraz / bir şey", "Zamir"), "viel" to Triple("viel", "çok", "Belirleyici"),
        "viele" to Triple("viele", "birçok", "Belirleyici"), "einige" to Triple("einige", "bazı", "Belirleyici"), "ist" to Triple("sein", "olmak", "Fiil"),
        "sind" to Triple("sein", "olmak", "Fiil"), "war" to Triple("sein", "idi", "Fiil"), "hat" to Triple("haben", "sahip olmak / var", "Fiil"),
        "haben" to Triple("haben", "sahip olmak", "Fiil"), "wird" to Triple("werden", "olmak", "Fiil"), "werden" to Triple("werden", "olmak", "Fiil"),
        "kann" to Triple("können", "-ebilmek", "Fiil"), "muss" to Triple("müssen", "zorunda olmak", "Fiil"), "müssen" to Triple("müssen", "zorunda olmak", "Fiil"),
        "soll" to Triple("sollen", "-meli / -malı", "Fiil"), "sollte" to Triple("sollen", "-meli / -malı", "Fiil"), "möchte" to Triple("möchten", "istemek", "Fiil"),
        "will" to Triple("wollen", "istemek", "Fiil"), "nicht" to Triple("nicht", "değil / -me", "Parçacık"),
        "kein" to Triple("kein", "hiç / yok", "Belirleyici"), "keine" to Triple("keine", "hiç / yok", "Belirleyici")
    )
}
