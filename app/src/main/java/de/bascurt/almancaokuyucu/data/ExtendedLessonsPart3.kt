package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

internal object ExtendedLessonsPart3 {
    private val F = ExtendedLessonFactory
    val all: List<ReaderLesson> = listOf(
        F.lesson("a2-kinderarzt", "Beim Kinderarzt", "A2", "Gesundheit • Çocuk doktoru ve ateş", listOf(
            "Emine bringt ihren Sohn wegen Fieber zum Kinderarzt.",
            "In der Praxis meldet sie ihn an der Rezeption an.",
            "Der Junge ist müde und möchte auf ihrem Schoß sitzen.",
            "Der Arzt hört seine Lunge ab und schaut in den Hals.",
            "Danach misst er noch einmal die Temperatur.",
            "Er sagt dass es wahrscheinlich ein leichter Infekt ist.",
            "Emine soll ihrem Sohn viel zu trinken geben.",
            "Wenn das Fieber steigt soll sie noch einmal in der Praxis anrufen."
        )),
        F.lesson("a2-bahnhof", "Am Bahnhof", "A2", "Verkehr • Tren bileti ve peron", listOf(
            "Paul fährt am Samstag mit dem Zug nach München.",
            "Am Bahnhof schaut er zuerst auf die Anzeigetafel.",
            "Sein Zug fährt von Gleis sieben ab.",
            "Paul kauft noch eine Flasche Wasser und geht zum Bahnsteig.",
            "Dort hört er eine Durchsage über eine kleine Verspätung.",
            "Nach zehn Minuten fährt der Zug ein.",
            "Paul steigt ein und sucht seinen reservierten Sitzplatz.",
            "Kurz danach kontrolliert eine Zugbegleiterin sein Ticket."
        )),
        F.lesson("a2-bus", "Mit dem Bus", "A2", "Verkehr • Otobüs yolculuğu ve duraklar", listOf(
            "Fatih fährt jeden Morgen mit dem Bus zur Arbeit.",
            "Er geht fünf Minuten zu Fuß bis zur Haltestelle.",
            "Auf der Anzeige sieht er wann der nächste Bus kommt.",
            "Heute hat der Bus wegen des Verkehrs etwas Verspätung.",
            "Fatih steigt vorne ein und zeigt sein Ticket.",
            "Nach sechs Haltestellen drückt er den Stopknopf.",
            "Der Bus hält direkt vor einem großen Bürogebäude.",
            "Fatih steigt aus und geht die letzten Meter zu Fuß."
        )),
        F.lesson("b1-flughafen", "Am Flughafen", "B1", "Reisen • Havalimanı, check-in ve bagaj", listOf(
            "Familie Demir fliegt in den Sommerferien nach Spanien.",
            "Am Flughafen geben sie zuerst ihre Koffer am Schalter ab.",
            "Die Mitarbeiterin prüft die Pässe und druckt die Bordkarten.",
            "Danach gehen alle durch die Sicherheitskontrolle.",
            "Im Wartebereich kaufen sie Wasser und beobachten die Anzeigetafel.",
            "Plötzlich ändert sich das Gate ihres Fluges.",
            "Die Familie geht schnell zum neuen Ausgang.",
            "Kurz vor dem Abflug werden die Bordkarten noch einmal kontrolliert."
        )),
        F.lesson("a2-tankstelle", "An der Tankstelle", "A2", "Verkehr • Yakıt alma ve ödeme", listOf(
            "Auf dem Heimweg sieht Murat dass der Tank fast leer ist.",
            "Er fährt an die nächste Tankstelle.",
            "Dort stellt er das Auto neben eine freie Zapfsäule.",
            "Er öffnet den Tankdeckel und tankt vierzig Liter.",
            "Danach hängt er die Zapfpistole wieder zurück.",
            "Im Shop nennt er die Nummer seiner Zapfsäule.",
            "Er bezahlt mit Karte und kauft noch eine Flasche Wasser.",
            "Bevor er weiterfährt kontrolliert er kurz den Reifendruck."
        )),
        F.lesson("b1-fahrradwerkstatt", "In der Fahrradwerkstatt", "B1", "Verkehr • Bisiklet tamiri ve bakım", listOf(
            "Lea benutzt ihr Fahrrad fast jeden Tag.",
            "Seit gestern macht die Kette ein lautes Geräusch.",
            "Sie bringt das Fahrrad am Nachmittag in eine Werkstatt.",
            "Der Mechaniker prüft Kette Bremsen und Reifen.",
            "Er erklärt dass die Kette gereinigt und gespannt werden muss.",
            "Außerdem ist der hintere Reifen schon ziemlich abgenutzt.",
            "Lea stimmt der Reparatur zu und lässt das Fahrrad dort.",
            "Am nächsten Tag holt sie es ab und fährt zufrieden nach Hause."
        )),
        F.lesson("a2-parkhaus", "Im Parkhaus", "A2", "Verkehr • Otopark ve ödeme", listOf(
            "Am Samstag fährt Selma mit dem Auto in die Innenstadt.",
            "Sie sucht ein Parkhaus in der Nähe des Einkaufszentrums.",
            "An der Einfahrt zieht sie ein Parkticket.",
            "Sie findet im dritten Stock einen freien Platz.",
            "Bevor sie geht merkt sie sich die Nummer des Bereichs.",
            "Nach dem Einkauf kommt sie zum Kassenautomaten zurück.",
            "Sie bezahlt das Ticket und steckt es an der Ausfahrt in den Automaten.",
            "Die Schranke öffnet sich und Selma fährt nach Hause."
        )),
        F.lesson("b1-buero", "Im Büro", "B1", "Arbeit • Ofis rutini ve görevler", listOf(
            "Daniel beginnt seinen Arbeitstag um acht Uhr im Büro.",
            "Zuerst liest er seine E-Mails und prüft den Kalender.",
            "Danach bespricht er mit seiner Kollegin die Aufgaben für den Tag.",
            "Um zehn Uhr nimmt er an einer kurzen Besprechung teil.",
            "Später schreibt er einen Bericht und beantwortet mehrere Anfragen.",
            "Am Nachmittag ruft ein Kunde wegen eines Problems an.",
            "Daniel notiert die wichtigsten Informationen und verspricht eine Rückmeldung.",
            "Bevor er Feierabend macht plant er die Aufgaben für morgen."
        )),
        F.lesson("b1-vorstellung", "Beim Vorstellungsgespräch", "B1", "Arbeit • İş görüşmesi ve özgeçmiş", listOf(
            "Can hat sich bei einer Firma als Systemadministrator beworben.",
            "Heute findet das Vorstellungsgespräch im Büro der Firma statt.",
            "Eine Personalreferentin begrüßt ihn und stellt zuerst das Unternehmen vor.",
            "Danach fragt sie nach seiner bisherigen Berufserfahrung.",
            "Can erklärt welche Systeme und Netzwerke er betreut hat.",
            "Er beschreibt auch ein schwieriges Problem das er erfolgreich gelöst hat.",
            "Zum Schluss stellt Can Fragen zum Team und zum Arbeitsalltag.",
            "Nach dem Gespräch bedankt er sich und wartet auf eine Rückmeldung."
        )),
        F.lesson("a2-post", "Bei der Post", "A2", "Alltag • Posta gönderme ve paket", listOf(
            "Elif möchte ein Paket an ihre Schwester schicken.",
            "Bei der Post nimmt sie eine Wartenummer.",
            "Am Schalter stellt sie das Paket auf die Waage.",
            "Der Mitarbeiter fragt nach dem Inhalt und dem Ziel.",
            "Elif wählt einen Versand mit Sendungsverfolgung.",
            "Der Mitarbeiter druckt das Etikett und klebt es auf das Paket.",
            "Elif bezahlt die Versandkosten mit Karte.",
            "Zum Schluss bekommt sie einen Beleg mit der Sendungsnummer."
        ))
    )
}