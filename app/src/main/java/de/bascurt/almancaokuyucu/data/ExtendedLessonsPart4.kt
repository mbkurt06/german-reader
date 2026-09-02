package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

internal object ExtendedLessonsPart4 {
    private val F = ExtendedLessonFactory
    val all: List<ReaderLesson> = listOf(
        F.lesson("b1-bank", "In der Bank", "B1", "Alltag • Banka işlemleri ve hesap", listOf(
            "Serkan hat einen Termin bei seiner Bank.",
            "Er möchte Informationen über ein neues Girokonto bekommen.",
            "Die Beraterin erklärt Gebühren Karten und Online-Banking.",
            "Serkan fragt besonders nach Überweisungen ins Ausland.",
            "Danach spricht die Beraterin über Sicherheitsregeln für die App.",
            "Serkan entscheidet sich noch nicht sofort für das Konto.",
            "Er nimmt die Unterlagen mit nach Hause.",
            "Dort möchte er die Bedingungen in Ruhe vergleichen."
        )),
        F.lesson("b1-rathaus", "Im Rathaus", "B1", "Alltag • Belediye işlemleri ve belge", listOf(
            "Anna ist vor kurzem in eine neue Wohnung gezogen.",
            "Deshalb muss sie ihre Adresse im Rathaus anmelden.",
            "Am Empfang zeigt sie ihren Termin und ihren Ausweis.",
            "Eine Mitarbeiterin prüft das Formular und die Wohnungsgeberbestätigung.",
            "Anna unterschreibt die Anmeldung auf einem Bildschirm.",
            "Danach bekommt sie eine schriftliche Bestätigung.",
            "Sie fragt auch nach den Öffnungszeiten des Bürgerbüros.",
            "Nach zwanzig Minuten ist alles erledigt und Anna geht wieder nach Hause."
        )),
        F.lesson("a2-schule", "In der Schule", "A2", "Schule • Ders günü ve sınıf", listOf(
            "Lina kommt morgens kurz vor acht Uhr in die Schule.",
            "Sie hängt ihre Jacke auf und geht in den Klassenraum.",
            "Die erste Stunde ist Mathematik und die Klasse schreibt eine kleine Übung.",
            "Danach haben die Schülerinnen und Schüler Deutsch.",
            "In der Pause essen viele Kinder ein Brot und trinken Wasser.",
            "Später arbeitet Lina mit einer Freundin an einem Projekt.",
            "Am Ende des Schultages schreibt die Lehrerin die Hausaufgaben an die Tafel.",
            "Lina packt ihre Bücher ein und fährt mit dem Bus nach Hause."
        )),
        F.lesson("b1-elternabend", "Beim Elternabend", "B1", "Schule • Veli toplantısı ve okul iletişimi", listOf(
            "Am Donnerstag findet in der Schule ein Elternabend statt.",
            "Die Klassenlehrerin begrüßt die Eltern und verteilt einige Informationen.",
            "Zuerst spricht sie über den Stundenplan und kommende Klassenarbeiten.",
            "Danach erklärt sie Regeln für Fehlzeiten und Entschuldigungen.",
            "Mehrere Eltern stellen Fragen zu Hausaufgaben und digitalen Geräten.",
            "Die Lehrerin berichtet auch über eine geplante Klassenfahrt.",
            "Am Ende werden wichtige Termine noch einmal gemeinsam geprüft.",
            "Die Eltern verabschieden sich und nehmen die Unterlagen mit nach Hause."
        )),
        F.lesson("a2-hotel", "Im Hotel", "A2", "Reisen • Otel giriş ve oda", listOf(
            "Laura kommt am Abend in ihrem Hotel an.",
            "An der Rezeption nennt sie ihren Namen und zeigt ihren Ausweis.",
            "Der Mitarbeiter findet die Reservierung im Computer.",
            "Er gibt Laura eine Zimmerkarte und erklärt wann es Frühstück gibt.",
            "Das Zimmer liegt im vierten Stock.",
            "Laura fährt mit dem Aufzug nach oben und öffnet die Tür.",
            "Sie stellt den Koffer neben den Schrank und schaut kurz aus dem Fenster.",
            "Danach geht sie noch einmal nach unten um etwas zu essen."
        )),
        F.lesson("a2-schwimmbad", "Im Schwimmbad", "A2", "Freizeit • Yüzme havuzu ve soyunma", listOf(
            "Am Samstag fährt Familie Braun ins Schwimmbad.",
            "Am Eingang kaufen sie Tickets für zwei Erwachsene und zwei Kinder.",
            "In der Umkleide ziehen alle ihre Badesachen an.",
            "Die Taschen schließen sie in einem Schrank ein.",
            "Die Kinder gehen zuerst in das flache Becken.",
            "Später schwimmt die ganze Familie einige Bahnen.",
            "Nach dem Schwimmen duschen alle und ziehen sich wieder an.",
            "Vor der Heimfahrt kaufen sie noch etwas zu trinken."
        )),
        F.lesson("b1-fitness", "Im Fitnessstudio", "B1", "Freizeit • Spor salonu ve egzersiz", listOf(
            "Arda trainiert dreimal pro Woche im Fitnessstudio.",
            "Vor dem Training wärmt er sich zehn Minuten auf dem Fahrrad auf.",
            "Danach macht er Übungen für Rücken Beine und Schultern.",
            "Bei schweren Gewichten achtet er besonders auf die richtige Haltung.",
            "Eine Trainerin zeigt ihm eine neue Übung für den Rücken.",
            "Arda probiert die Bewegung langsam und mit wenig Gewicht.",
            "Zum Schluss dehnt er sich und trinkt viel Wasser.",
            "Nach dem Training trägt er seine Übungen in einer App ein."
        )),
        F.lesson("a2-bibliothek", "In der Bibliothek", "A2", "Freizeit • Kütüphane ve kitap ödünç alma", listOf(
            "Mila besucht nach der Schule die Stadtbibliothek.",
            "Sie sucht ein Buch für ein Referat über Tiere.",
            "Am Computer gibt sie ein Stichwort in die Suche ein.",
            "Das gewünschte Buch steht im zweiten Stock.",
            "Mila findet es im Regal und nimmt noch einen Roman dazu.",
            "Am Automaten scannt sie ihren Bibliotheksausweis und beide Bücher.",
            "Auf dem Bildschirm sieht sie das Rückgabedatum.",
            "Zu Hause beginnt sie sofort mit den Notizen für ihr Referat."
        )),
        F.lesson("a2-kino", "Im Kino", "A2", "Freizeit • Sinema bileti ve film", listOf(
            "Am Freitagabend gehen Eren und Tom ins Kino.",
            "Sie haben die Tickets schon online gekauft.",
            "Am Eingang zeigt Eren den QR-Code auf seinem Handy.",
            "Danach kaufen sie Popcorn und zwei Getränke.",
            "Der Saal ist fast voll aber ihre Plätze sind noch frei.",
            "Vor dem Film laufen einige Werbungen und Trailer.",
            "Der Film dauert ungefähr zwei Stunden.",
            "Nach dem Ende sprechen Eren und Tom draußen über ihre Lieblingsszene."
        )),
        F.lesson("b1-ausflug", "Bei einem Ausflug", "B1", "Freizeit • Günübirlik gezi ve planlama", listOf(
            "Zwei Freunde planen einen Tagesausflug in den Schwarzwald.",
            "Am Vorabend prüfen sie das Wetter und packen ihre Rucksäcke.",
            "Am Morgen fahren sie früh mit dem Auto los.",
            "Nach einer Stunde erreichen sie einen Parkplatz am Waldrand.",
            "Von dort folgen sie einem markierten Wanderweg.",
            "Unterwegs machen sie Fotos und legen eine Pause an einem Aussichtspunkt ein.",
            "Am Nachmittag trinken sie Kaffee in einem kleinen Ort.",
            "Bevor es dunkel wird fahren sie zufrieden wieder nach Hause."
        ))
    )
}