package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

internal object ExtendedLessonsPart2 {
    private val F = ExtendedLessonFactory
    val all: List<ReaderLesson> = listOf(
        F.lesson("a2-kleidung", "Im Kleidungsgeschäft", "A2", "Einkaufen • Kıyafet seçme ve deneme", listOf(
            "Esra sucht eine neue Jacke für den Herbst.",
            "In einem Geschäft schaut sie zuerst nach ihrer Größe.",
            "Eine Verkäuferin zeigt ihr zwei verschiedene Modelle.",
            "Esra probiert eine blaue und eine schwarze Jacke an.",
            "Die blaue Jacke passt gut aber die Ärmel sind etwas lang.",
            "Die Verkäuferin bringt eine kleinere Größe aus dem Lager.",
            "Dieses Modell sitzt besser und gefällt Esra sofort.",
            "An der Kasse bezahlt sie und nimmt die Jacke in einer Tasche mit."
        )),
        F.lesson("a2-schuhe", "Im Schuhgeschäft", "A2", "Einkaufen • Ayakkabı deneme ve beden", listOf(
            "Deniz braucht bequeme Schuhe für die Arbeit.",
            "Im Schuhgeschäft erklärt er dem Verkäufer was er sucht.",
            "Der Verkäufer misst kurz seine Schuhgröße.",
            "Dann bringt er drei Paar Schuhe zum Anprobieren.",
            "Das erste Paar ist zu eng und das zweite zu schwer.",
            "Das dritte Paar fühlt sich bequem an und sieht gut aus.",
            "Deniz läuft damit einige Schritte durch das Geschäft.",
            "Schließlich kauft er die Schuhe und bekommt einen Kassenbon."
        )),
        F.lesson("a2-drogerie", "In der Drogerie", "A2", "Einkaufen • Kişisel bakım ve temizlik ürünleri", listOf(
            "Melis geht nach der Arbeit in die Drogerie.",
            "Sie braucht Shampoo Zahnpasta Waschmittel und Küchenpapier.",
            "Zuerst sucht sie die Pflegeprodukte im ersten Gang.",
            "Danach vergleicht sie zwei verschiedene Shampoos.",
            "Beim Waschmittel achtet sie auf den Preis und die Packungsgröße.",
            "Sie nimmt außerdem Sonnencreme für das Wochenende.",
            "An der Kasse benutzt Melis einen Rabattcoupon.",
            "Nach dem Bezahlen packt sie alle Produkte in ihre Stofftasche."
        )),
        F.lesson("a2-moebelhaus", "Im Möbelhaus", "A2", "Einkaufen • Mobilya seçimi ve teslimat", listOf(
            "Familie Arslan sucht einen neuen Esstisch.",
            "Im Möbelhaus sehen sie viele Tische und Stühle.",
            "Sie messen einen großen Holztisch und prüfen die Länge.",
            "Der Tisch gefällt ihnen aber er ist nicht sofort lieferbar.",
            "Ein Mitarbeiter zeigt ihnen ein ähnliches Modell.",
            "Dieses Modell ist etwas kleiner und günstiger.",
            "Die Familie bestellt den Tisch und vereinbart einen Liefertermin.",
            "An der Information bekommen sie die Rechnung und weitere Hinweise."
        )),
        F.lesson("a2-elektronik", "Im Elektronikmarkt", "A2", "Einkaufen • Elektronik ürünleri ve danışmanlık", listOf(
            "Kerem möchte neue Kopfhörer für sein Handy kaufen.",
            "Im Elektronikmarkt vergleicht er mehrere Modelle.",
            "Ein Mitarbeiter erklärt die Unterschiede bei Akku Klang und Verbindung.",
            "Kerem probiert zwei Kopfhörer kurz aus.",
            "Ein Modell sitzt bequemer und hat eine längere Akkulaufzeit.",
            "Er fragt ob die Kopfhörer auch mit seinem Laptop funktionieren.",
            "Der Mitarbeiter bestätigt das und zeigt ihm die Garantie.",
            "Kerem bezahlt an der Kasse und nimmt die Verpackung mit."
        )),
        F.lesson("a2-online", "Bei einer Online-Bestellung", "A2", "Einkaufen • İnternetten sipariş ve teslimat", listOf(
            "Zeynep bestellt im Internet ein neues Regal.",
            "Sie prüft zuerst die Maße und liest einige Bewertungen.",
            "Dann legt sie das Regal in den Warenkorb.",
            "Beim Bezahlen wählt sie eine Lieferung nach Hause.",
            "Am nächsten Tag bekommt sie eine Versandbestätigung per E-Mail.",
            "Zwei Tage später klingelt der Paketbote an der Tür.",
            "Zeynep kontrolliert das Paket und unterschreibt den Empfang.",
            "Am Abend baut sie das Regal mit einer Anleitung zusammen."
        )),
        F.lesson("a2-zahnarzt", "Beim Zahnarzt", "A2", "Gesundheit • Diş hekimi muayenesi", listOf(
            "Hasan hat seit zwei Tagen Zahnschmerzen.",
            "Er ruft in der Praxis an und bekommt einen Termin.",
            "Im Wartezimmer füllt er zuerst ein kurzes Formular aus.",
            "Die Zahnärztin untersucht den schmerzenden Zahn.",
            "Danach macht eine Assistentin ein Röntgenbild.",
            "Die Zahnärztin erklärt dass ein kleines Loch behandelt werden muss.",
            "Hasan bekommt eine Betäubung und die Behandlung dauert zwanzig Minuten.",
            "Zum Schluss vereinbart er einen Termin für die Kontrolle."
        )),
        F.lesson("b1-physio", "In der Physiotherapie", "B1", "Gesundheit • Fizik tedavi ve egzersiz", listOf(
            "Nach einer Knieverletzung beginnt Eva mit Physiotherapie.",
            "Der Therapeut fragt zuerst nach ihren aktuellen Beschwerden.",
            "Dann zeigt er ihr Übungen für Kraft und Beweglichkeit.",
            "Eva soll jede Bewegung langsam und kontrolliert ausführen.",
            "Bei einer Übung muss sie das Bein mehrmals anheben.",
            "Der Therapeut korrigiert ihre Haltung und erklärt worauf sie achten soll.",
            "Am Ende bekommt Eva drei Übungen für zu Hause.",
            "In der nächsten Woche wird überprüft ob die Schmerzen weniger geworden sind."
        )),
        F.lesson("a2-augenarzt", "Beim Augenarzt", "A2", "Gesundheit • Göz muayenesi ve gözlük", listOf(
            "Maria sieht seit einigen Wochen in der Ferne schlechter.",
            "Deshalb vereinbart sie einen Termin beim Augenarzt.",
            "Eine Mitarbeiterin prüft zuerst ihre Sehschärfe.",
            "Maria liest Buchstaben von einer großen Tafel ab.",
            "Danach untersucht der Arzt beide Augen mit einem Gerät.",
            "Er empfiehlt eine neue Brille für das Autofahren.",
            "Maria bekommt die Werte für die Gläser auf einem Zettel.",
            "Nach dem Termin geht sie direkt zu einem Optiker."
        )),
        F.lesson("b1-notaufnahme", "In der Notaufnahme", "B1", "Gesundheit • Acil servis ve ilk değerlendirme", listOf(
            "Nach einem Sturz hat Jonas starke Schmerzen im Fuß.",
            "Seine Freundin fährt ihn in die Notaufnahme.",
            "Am Empfang beschreibt Jonas den Unfall und zeigt seine Versichertenkarte.",
            "Eine Pflegekraft misst Blutdruck und Temperatur.",
            "Danach untersucht ein Arzt den Fuß und ordnet ein Röntgenbild an.",
            "Zum Glück ist kein Knochen gebrochen.",
            "Jonas bekommt einen Verband und soll den Fuß einige Tage schonen.",
            "Vor dem Gehen erhält er Hinweise und einen Termin zur Kontrolle."
        ))
    )
}