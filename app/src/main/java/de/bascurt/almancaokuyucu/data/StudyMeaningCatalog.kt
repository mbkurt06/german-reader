package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme

object StudyMeaningCatalog {
    fun meaningFor(item: Lexeme, language: String): String {
        if (language == "tr") return item.meaning
        val keyCandidates = listOfNotNull(
            item.dictionaryForm,
            item.contextExpression,
            item.infinitive,
            item.base
        ).map(::normalize)

        val map = when (language) {
            "en" -> english
            else -> emptyMap()
        }
        return keyCandidates.firstNotNullOfOrNull { map[it] } ?: item.meaning
    }

    fun contextMeaningFor(item: Lexeme, language: String): String? {
        if (language == "tr") return item.contextMeaning
        val contextKey = item.contextExpression?.let(::normalize)
        return when (language) {
            "en" -> contextKey?.let { english[it] } ?: meaningFor(item, language)
            else -> item.contextMeaning
        }
    }

    private fun normalize(value: String): String = value
        .trim()
        .lowercase()
        .replace(Regex("\\s+"), " ")

    private val english: Map<String, String> = mapOf(
        "beginnen" to "to begin / start",
        "beginnt" to "begins / starts",
        "heute" to "today",
        "ihre" to "her",
        "neue" to "new",
        "arbeit" to "work / job",
        "ihre neue arbeit" to "her new job",
        "im" to "in the",
        "im hotel" to "at the hotel",
        "hotel" to "hotel",
        "am morgen" to "in the morning",
        "morgen" to "morning",
        "begrüßen" to "to greet / welcome",
        "begrüßt" to "greets / welcomes",
        "sie" to "she / they",
        "die" to "the",
        "kollegen" to "colleagues",
        "die kollegen" to "the colleagues",
        "und" to "and",
        "bekommt" to "gets / receives",
        "bekommen" to "to get / receive",
        "eine" to "a / an",
        "zimmerliste" to "room list",
        "eine zimmerliste" to "a room list",
        "chefin" to "manager / female boss",
        "ihre chefin" to "her manager",
        "zeigt" to "shows",
        "zeigen" to "to show",
        "ihr" to "her / to her",
        "rezeption" to "reception desk",
        "die rezeption" to "the reception desk",
        "erklären" to "to explain",
        "erklärt" to "explains",
        "wichtig" to "important",
        "wichtigsten" to "most important",
        "aufgaben" to "tasks",
        "die wichtigsten aufgaben" to "the most important tasks",
        "danach" to "after that",
        "beantworten" to "to answer",
        "beantwortet" to "answers",
        "ersten" to "first",
        "fragen" to "questions",
        "die ersten fragen" to "the first questions",
        "von" to "from / of",
        "gästen" to "guests",
        "den gästen" to "the guests",
        "die ersten fragen von den gästen" to "the first questions from the guests",
        "ein gast" to "a guest",
        "gast" to "guest",
        "nach + d fragen" to "to ask about",
        "fragen" to "to ask",
        "fragt" to "asks",
        "nach dem frühstück fragen" to "to ask about breakfast",
        "frühstück" to "breakfast",
        "dem frühstück" to "breakfast",
        "ihm" to "him / to him",
        "weg" to "way",
        "den weg" to "the way",
        "später" to "later",
        "kontrollieren" to "to check",
        "kontrolliert" to "checks",
        "die zimmerliste" to "the room list",
        "noch einmal" to "once again",
        "sorgfältig" to "carefully",
        "am mittag" to "at noon",
        "mittag" to "noon",
        "machen" to "to do / make",
        "macht" to "does / makes",
        "kurz" to "short",
        "pause" to "break",
        "eine kurze pause" to "a short break",
        "mit den kollegen" to "with the colleagues",
        "nach der pause" to "after the break",
        "der pause" to "the break",
        "helfen" to "to help",
        "hilft" to "helps",
        "wieder" to "again",
        "an der rezeption" to "at reception",
        "sortieren" to "to sort",
        "sortiert" to "sorts",
        "unterlagen" to "documents",
        "kollegin" to "female colleague",
        "eine kollegin" to "a female colleague",
        "noch" to "still / more",
        "einige" to "some",
        "wichtige" to "important",
        "sachen" to "things",
        "einige wichtige sachen" to "some important things",
        "am arbeitsplatz" to "at the workplace",
        "arbeitsplatz" to "workplace",
        "zuhören" to "to listen",
        "aufmerksam zuhören" to "to listen carefully",
        "hört" to "hears / listens",
        "aufmerksam" to "carefully / attentively",
        "zu" to "to",
        "bei bedarf" to "when necessary",
        "bedarf" to "need",
        "nachfragen" to "to ask again / clarify",
        "bei bedarf noch einmal nachfragen" to "to ask again when necessary",
        "am abend" to "in the evening",
        "abend" to "evening",
        "mit + d über + a sprechen" to "to talk with someone about something",
        "sprechen" to "to speak / talk",
        "spricht" to "speaks / talks",
        "mit ihrer chefin" to "with her manager",
        "über den ersten arbeitstag" to "about the first day at work",
        "mit ihrer chefin über den ersten arbeitstag sprechen" to "to talk with her manager about the first day at work",
        "arbeitstag" to "working day / day at work",
        "den ersten arbeitstag" to "the first day at work",
        "zum schluss" to "finally / at the end",
        "schluss" to "end",
        "aufräumen" to "to tidy up",
        "seinen platz aufräumen" to "to tidy up one's place",
        "räumt" to "tidies up",
        "platz" to "place / spot",
        "ihren platz" to "her place",
        "sich von + d verabschieden" to "to say goodbye to someone",
        "sich von den kollegen verabschieden" to "to say goodbye to the colleagues",
        "verabschiedet" to "says goodbye",
        "sich" to "oneself",
        "von den kollegen" to "from / to the colleagues",
        "ihre aufgaben" to "her tasks",
        "einer anderen person" to "another person",
        "person" to "person",
        "anderen" to "other / another",
        "den nächsten schritt" to "the next step",
        "nächsten" to "next",
        "schritt" to "step",
        "den tag" to "the day",
        "tag" to "day",
        "zufrieden" to "satisfied",
        "elįf" to "Elif"
    )
}
