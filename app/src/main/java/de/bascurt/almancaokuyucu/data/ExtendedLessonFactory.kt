package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

internal object ExtendedLessonFactory {
    fun lesson(id: String, title: String, level: String, summary: String, texts: List<String>): ReaderLesson {
        val expandedTexts = texts + reinforcementSentences(summary)
        return ReaderLesson(
            id = id,
            title = title,
            level = level,
            summary = summary,
            sentences = expandedTexts.mapIndexed { sentenceIndex, sentence ->
                tokenizeSentence(id, sentenceIndex, sentence)
            }
        )
    }

    fun appendSentences(lesson: ReaderLesson, texts: List<String>): ReaderLesson {
        if (texts.isEmpty()) return lesson
        val start = lesson.sentences.size
        val extra = texts.mapIndexed { offset, sentence ->
            tokenizeSentence(lesson.id, start + offset, sentence)
        }
        return ReaderLesson(
            id = lesson.id,
            title = lesson.title,
            level = lesson.level,
            summary = lesson.summary,
            sentences = lesson.sentences + extra
        )
    }

    private fun tokenizeSentence(
        lessonId: String,
        sentenceIndex: Int,
        sentence: String
    ): List<ReadingToken> {
        val shownTokens = sentence.split(" ")
        val keys = shownTokens.map(::clean)
        val lexemes = keys.mapIndexed { tokenIndex, key ->
            lexemeFor(lessonId, sentenceIndex, tokenIndex, key, shownTokens[tokenIndex])
        }.toMutableList()

        applySeparableVerbs(lessonId, keys, shownTokens, lexemes)
        applyReflexiveVerbs(lessonId, keys, lexemes)
        applyFixedGroups(lessonId, keys, lexemes)

        return shownTokens.mapIndexed { index, shown -> ReadingToken(shown, lexemes[index]) }
    }

    /**
     * Aynı kelime hem edat hem ayrılan ön ek olabilir.
     * Örn: "wärmt er sich auf dem Fahrrad auf".
     * İlk "auf"tan sonra "dem Fahrrad" geldiği için edattır; sondaki "auf" ise ayrılan ön ektir.
     */
    private fun applySeparableVerbs(
        lessonId: String,
        keys: List<String>,
        shownTokens: List<String>,
        lexemes: MutableList<Lexeme>
    ) {
        storySeparableRules.forEach { rule ->
            keys.indices
                .filter { keys[it] in rule.surfaces }
                .forEach { verbIndex ->
                    val particleIndex = findSeparableParticle(
                        particle = rule.particle,
                        verbIndex = verbIndex,
                        keys = keys,
                        shownTokens = shownTokens
                    ) ?: return@forEach

                    val grouped = verbLexeme(
                        lessonId,
                        rule.seed,
                        "Ayrılabilen fiil: ‘${shownTokens[verbIndex]} … ${shownTokens[particleIndex]}’ tek fiildir. Aynı yazılan normal edatlar bu gruba alınmaz."
                    )
                    lexemes[verbIndex] = grouped
                    lexemes[particleIndex] = grouped

                    if (rule.seed.base.startsWith("sich ")) {
                        nearestIndex(keys, "sich", verbIndex, 7)?.let { lexemes[it] = grouped }
                    }
                }
        }
    }

    /**
     * Ayrılan parçacık yalnız fiilin bulunduğu cümlecik içinde aranır.
     * Böylece "Er macht das Licht an und sie schaut die Anzeige an" örneğinde
     * ilk fiil ikinci cümleciğin "an" parçacığını yanlışlıkla yakalayamaz.
     */
    private fun findSeparableParticle(
        particle: String,
        verbIndex: Int,
        keys: List<String>,
        shownTokens: List<String>
    ): Int? {
        val clauseEndExclusive = (verbIndex + 1 until keys.size)
            .firstOrNull { index -> keys[index] in clauseJoiners }
            ?: keys.size

        return (verbIndex + 1 until clauseEndExclusive)
            .filter { index ->
                keys[index] == particle && isParticlePosition(particle, index, keys, shownTokens)
            }
            .lastOrNull()
    }

    private fun isParticlePosition(
        particle: String,
        index: Int,
        keys: List<String>,
        shownTokens: List<String>
    ): Boolean {
        if (index == keys.lastIndex) return true

        val raw = shownTokens[index]
        if (raw.endsWith('.') || raw.endsWith(',') || raw.endsWith(';') || raw.endsWith('!') || raw.endsWith('?')) {
            return true
        }

        val next = keys[index + 1]
        if (next in clauseJoiners) return true

        // "nimmt den Müll mit nach draußen": mit ayrılan ön ektir, nach draußen ise yön tümlecidir.
        if (particle == "mit" && next in setOf("nach", "zu", "in", "auf")) return true

        // Bu parçalar edat olarak bir isim grubunu yönetmez; gerçek fiil parçaları olduklarında
        // cümlede sonda olmasalar bile güvenle eşleştirilebilirler. Yine de artikel olan "ein"
        // için yalnız cümlecik sonu kuralını kullanıyoruz.
        if (particle in nonPrepositionalParticles && particle != "ein") return true

        // auf/an/in/aus/vor/nach gibi bir kelimeden sonra artikel veya belirleyici geliyorsa
        // bu kullanım büyük olasılıkla normal edattır: "auf dem Fahrrad", "auf den Herd" vb.
        if (next in prepositionFollowers) return false

        return false
    }

    private fun applyReflexiveVerbs(
        lessonId: String,
        keys: List<String>,
        lexemes: MutableList<Lexeme>
    ) {
        keys.forEachIndexed { verbIndex, key ->
            val seed = verbSeedFor(key) ?: return@forEachIndexed
            if (!seed.base.startsWith("sich ")) return@forEachIndexed
            val sichIndex = nearestIndex(keys, "sich", verbIndex, 7) ?: return@forEachIndexed
            val grouped = verbLexeme(
                lessonId,
                seed,
                "Dönüşlü fiil: ‘sich’ ve ana fiil birlikte öğrenilir."
            )
            lexemes[verbIndex] = grouped
            lexemes[sichIndex] = grouped
        }
    }

    private fun applyFixedGroups(
        lessonId: String,
        keys: List<String>,
        lexemes: MutableList<Lexeme>
    ) {
        storyFixedGroupRules.forEach { rule ->
            keys.indices
                .filter { keys[it] in rule.surfaces }
                .forEach { verbIndex ->
                    val matched = rule.required.mapNotNull { required ->
                        nearestIndex(keys, required, verbIndex, rule.maxDistance)
                    }
                    if (matched.size != rule.required.size) return@forEach

                    val grouped = verbLexeme(
                        lessonId,
                        rule.seed,
                        "Sabit kelime grubu: yalnız bu yapıya ait parçalar birlikte vurgulanır."
                    )
                    lexemes[verbIndex] = grouped
                    matched.forEach { lexemes[it] = grouped }
                }
        }
    }

    private fun nearestIndex(
        keys: List<String>,
        target: String,
        center: Int,
        maxDistance: Int
    ): Int? = keys.indices
        .filter { keys[it] == target && kotlin.math.abs(it - center) <= maxDistance }
        .minByOrNull { kotlin.math.abs(it - center) }

    private fun lexemeFor(
        lessonId: String,
        sentenceIndex: Int,
        tokenIndex: Int,
        key: String,
        shown: String
    ): Lexeme {
        val startsUpper = shown.firstOrNull()?.isUpperCase() == true

        // Büyük yazılan kelime önce isim sözlüğünde aranır. Böylece "Morgen" = sabah,
        // "morgen" = yarın gibi bağlama duyarlı ayrımlar korunur.
        if (startsUpper) nounSeedFor(key)?.let { return nounLexeme(lessonId, it) }

        verbSeedFor(key)?.let { return verbLexeme(lessonId, it) }
        nounSeedFor(key)?.let { return nounLexeme(lessonId, it) }
        coreStorySurfaceMeanings[key]?.let {
            return surfaceLexeme(lessonId, sentenceIndex, tokenIndex, key, it)
        }
        supplementalSurfaceMeanings[key]?.let {
            return surfaceLexeme(lessonId, sentenceIndex, tokenIndex, key, it)
        }
        auditedStorySurfaceMeanings[key]?.let {
            return surfaceLexeme(lessonId, sentenceIndex, tokenIndex, key, it)
        }

        if (startsUpper && key in knownStoryNames) {
            return Lexeme(
                id = "$lessonId-name-$key",
                base = shown.trim('"', '„', '“', '.', ',', ':', ';', '!', '?'),
                meaning = "kişi adı",
                type = "Özel isim",
                quizEligible = false,
                wordClass = "Özel isim"
            )
        }

        // Bu geri dönüş yalnız kalite denetimi içindir. Mevcut hikâyelerde bu değere
        // düşen kelime bırakılmaması hedeflenir ve ContentAudit testi bunu yakalar.
        return Lexeme(
            id = "$lessonId-missing-$sentenceIndex-$tokenIndex",
            base = shown.trim('"', '„', '“', '.', ',', ':', ';', '!', '?'),
            meaning = "⚠ Türkçe anlam kaydı eksik",
            explanation = "Bu kelime merkezi sözlük kalite denetiminden geçmemiştir.",
            type = "Eksik içerik",
            quizEligible = false,
            wordClass = "Eksik içerik"
        )
    }

    private fun verbSeedFor(key: String): ExtendedVerbSeed? =
        storyBaseVerbOverrides[key]
            ?: extendedVerbLexicon[key]
            ?: supplementalVerbLexicon[key]

    private fun nounSeedFor(key: String): ExtendedNounSeed? =
        coreStoryNouns[key]
            ?: supplementalNounLexicon[key]
            ?: extendedNounLexicon[key]

    private fun nounLexeme(lessonId: String, noun: ExtendedNounSeed) = Lexeme(
        id = "$lessonId-n-${noun.base.lowercase()}",
        base = noun.base,
        meaning = noun.meaning,
        type = "Kelime",
        explanation = "Bu isim hikâyedeki bağlama uygun Türkçe anlamıyla merkezi sözlükten gelir.",
        quizEligible = true,
        wordClass = "İsim",
        article = noun.article,
        plural = noun.plural
    )

    private fun verbLexeme(
        lessonId: String,
        verb: ExtendedVerbSeed,
        note: String = "Bu fiil hikâyedeki bağlama göre merkezi fiil sözlüğünden gelir."
    ) = Lexeme(
        id = "$lessonId-v-${verb.base}",
        base = verb.base,
        meaning = verb.meaning,
        type = if (verb.base.contains(' ')) "Kelime grubu" else "Fiil",
        explanation = note,
        quizEligible = true,
        wordClass = "Fiil",
        infinitive = verb.base,
        thirdPerson = verb.third,
        preterite = verb.preterite,
        perfect = verb.perfect
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

    /** Eksik anlamları otomatik testte ve geliştirici denetiminde bulmak için. */
    fun missingMeaningTokens(lessons: List<ReaderLesson>): List<String> = lessons.flatMap { lesson ->
        lesson.sentences.flatMapIndexed { sentenceIndex, sentence ->
            sentence.filter { token -> token.lexeme.wordClass == "Eksik içerik" }
                .map { token -> "${lesson.id} / cümle ${sentenceIndex + 1}: ${token.text}" }
        }
    }

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

    private fun clean(text: String): String = text
        .trim('"', '„', '“', '.', ',', ':', ';', '!', '?', '(', ')')
        .lowercase()

    private val clauseJoiners = setOf(
        "und", "aber", "oder", "denn", "sondern", "weil", "wenn", "dass", "bevor", "während", "obwohl"
    )

    private val prepositionFollowers = setOf(
        "der", "die", "das", "den", "dem", "des", "ein", "eine", "einen", "einem", "einer",
        "mein", "meine", "meinen", "dein", "deine", "seine", "seinen", "ihre", "ihren", "dieser", "diese", "dieses"
    )

    private val nonPrepositionalParticles = setOf(
        "ab", "los", "zurück", "zusammen", "weg", "hinein", "heraus", "weiter", "teil", "fort", "hin", "kennen", "statt"
    )

    private val knownStoryNames = setOf(
        "elif", "mina", "leyla", "emre", "nora", "sara", "ali", "fatma", "amir", "yilmaz", "yılmaz",
        "meryem", "mehmet", "jana", "aylin", "kaya", "murat", "selin", "leonie", "nisa", "cem", "esra",
        "deniz", "melis", "arslan", "kerem", "zeynep", "hasan", "eva", "maria", "jonas", "emine", "paul",
        "fatih", "demir", "lea", "selma", "daniel", "can", "serkan", "anna", "lina", "laura", "braun",
        "arda", "mila", "eren", "tom"
    )
}
