package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

internal object ExtendedSampleLessons {
    val all: List<ReaderLesson> by lazy {
        ExtendedLessonsPart1.all + ExtendedLessonsPart2.all + ExtendedLessonsPart3.all + ExtendedLessonsPart4.all
    }

    fun expandExisting(existing: List<ReaderLesson>): List<ReaderLesson> = existing.map { lesson ->
        ExtendedLessonFactory.appendSentences(lesson, extraSentences[lesson.id].orEmpty())
    }

    private val extraSentences: Map<String, List<String>> = mapOf(
        "a2-neuer-anfang" to listOf(
            "Ein Gast fragt nach dem Frühstück und Elif erklärt den Weg.",
            "Später kontrolliert sie noch einmal die Zimmerliste.",
            "Am Abend spricht sie kurz mit ihrer Chefin über den Tag.",
            "Elif ist müde aber zufrieden und freut sich auf morgen."
        ),
        "a2-kueche" to listOf(
            "Danach würzt sie die Suppe mit Salz und Pfeffer.",
            "Zum Schluss legt sie die Teller auf den Tisch und serviert das Essen.",
            "Nach dem Essen räumt sie das Geschirr in die Spülmaschine.",
            "Sie wischt die Arbeitsfläche ab und macht die Küche sauber."
        ),
        "a2-arzt" to listOf(
            "Leyla erzählt dass sie nachts schlecht geschlafen hat.",
            "Danach schreibt der Arzt ein Rezept und empfiehlt viel Ruhe.",
            "Er erklärt wie oft Leyla das Medikament nehmen soll.",
            "Zu Hause trinkt Leyla Tee und ruht sich auf dem Sofa aus."
        ),
        "a2-baeckerei" to listOf(
            "Sie fragt auch nach einem Brot ohne Nüsse.",
            "Der Bäcker packt alles ein und nennt den Preis.",
            "Die Kundin bezahlt an der Kasse mit ihrer Karte.",
            "Zum Abschied wünscht der Bäcker ihr einen schönen Tag."
        ),
        "a2-supermarkt" to listOf(
            "Er kauft außerdem Brot Eier und etwas Käse.",
            "An der Kasse legt er alle Produkte auf das Band.",
            "Er bezahlt mit der Karte und nimmt die Tüte.",
            "Zu Hause kontrolliert er den Kassenbon und räumt die Einkäufe ein."
        ),
        "a2-zuhause" to listOf(
            "Sie gießt die Pflanzen und bringt frisches Wasser für die Katze.",
            "Bevor sie geht nimmt sie den Müll mit nach draußen.",
            "Im Flur zieht sie ihre Jacke und ihre Schuhe an.",
            "Dann schließt sie die Wohnung ab und fährt zur Arbeit."
        ),
        "a2-restaurant" to listOf(
            "Sara findet die Suppe lecker aber ein wenig salzig.",
            "Nach dem Essen bestellen beide noch einen Kaffee.",
            "Danach bitten sie um die Rechnung und bezahlen zusammen.",
            "Sie bedanken sich beim Kellner und verlassen das Restaurant."
        ),
        "a2-wochenmarkt" to listOf(
            "Sie probiert eine Erdbeere und nimmt noch eine kleine Schale.",
            "An einem anderen Stand kauft sie Käse und frisches Brot.",
            "Fatma bezahlt bar und legt alles in ihre Tasche.",
            "Zu Hause wäscht sie das Gemüse und bereitet einen Salat vor."
        ),
        "b1-neue-stadt" to listOf(
            "Amir entdeckt dabei neue Viertel und interessante Geschäfte.",
            "Am Wochenende fährt er mit Freunden in die Innenstadt.",
            "Heute kennt er die Stadt besser und fühlt sich dort zu Hause.",
            "Er möchte bald einem Sportverein beitreten und weitere Kontakte knüpfen."
        ),
        "b1-krankenhaus" to listOf(
            "Die Pflegekraft erklärt wann er wieder aufstehen darf.",
            "Am Nachmittag darf er das Krankenhaus verlassen.",
            "Er bekommt einen Kontrolltermin und einen kurzen Arztbrief.",
            "Zu Hause soll er sich schonen und bei starken Beschwerden sofort anrufen."
        ),
        "b1-apotheke" to listOf(
            "Sie erklärt dass Meryem genügend Wasser trinken sollte.",
            "Meryem entscheidet sich nur für das verschriebene Medikament.",
            "Zum Schluss bezahlt sie und steckt das Medikament in ihre Tasche.",
            "Zu Hause liest sie noch einmal sorgfältig den Beipackzettel."
        ),
        "b1-autowerkstatt" to listOf(
            "Er ruft Mehmet an und erklärt welche Teile ersetzt werden müssen.",
            "Mehmet stimmt der Reparatur zu und fragt nach den Kosten.",
            "Am Nachmittag ist die Reparatur fertig und Mehmet holt das Auto ab.",
            "Auf dem Heimweg merkt er sofort dass das Geräusch verschwunden ist."
        ),
        "b2-digitale-balance" to listOf(
            "Besonders abends beeinflusst der Bildschirm häufig den Schlaf.",
            "Deshalb legen manche Menschen das Handy schon eine Stunde vorher weg.",
            "Entscheidend ist die Technik bewusst zu nutzen statt sich von ihr ablenken zu lassen.",
            "So bleibt mehr Zeit für Gespräche Bewegung und echte Erholung."
        )
    )
}