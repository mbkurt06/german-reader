package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

internal object ExtendedLessonsPart1 {
    private val F = ExtendedLessonFactory
    val all: List<ReaderLesson> = listOf(
        F.lesson("a2-badezimmer", "Im Badezimmer", "A2", "Zuhause • Banyo rutini ve kişisel bakım", listOf(
            "Jana geht morgens ins Badezimmer und macht das Licht an.",
            "Sie putzt zuerst ihre Zähne und wäscht ihr Gesicht.",
            "Danach nimmt sie eine Dusche und trocknet sich mit einem Handtuch ab.",
            "Sie kämmt ihre Haare und benutzt etwas Creme.",
            "Auf dem Regal stehen Shampoo Seife und Zahnpasta.",
            "Jana räumt die nassen Sachen weg und öffnet kurz das Fenster.",
            "Dann zieht sie sich an und legt die benutzte Wäsche in den Korb.",
            "Bevor sie geht kontrolliert sie noch einmal ob der Wasserhahn zu ist."
        )),
        F.lesson("a2-waesche", "Beim Wäschewaschen", "A2", "Zuhause • Çamaşır yıkama ve asma", listOf(
            "Mehmet sammelt die schmutzige Wäsche aus den Zimmern.",
            "Er sortiert helle und dunkle Kleidung in zwei Körbe.",
            "Dann füllt er die Waschmaschine und gibt Waschmittel hinein.",
            "Er wählt ein Programm und startet die Maschine.",
            "Nach einer Stunde nimmt er die nasse Wäsche heraus.",
            "Einige Sachen hängt er auf den Wäscheständer.",
            "Die Handtücher kommen später in den Trockner.",
            "Am Abend faltet Mehmet alles und legt die Kleidung in den Schrank."
        )),
        F.lesson("a2-putzen", "Beim Wohnungsputz", "A2", "Zuhause • Ev temizliği ve temizlik malzemeleri", listOf(
            "Aylin möchte am Samstag die ganze Wohnung putzen.",
            "Zuerst räumt sie die Sachen im Wohnzimmer auf.",
            "Dann saugt sie den Teppich und wischt den Boden.",
            "Im Bad reinigt sie das Waschbecken und die Dusche.",
            "In der Küche leert sie den Mülleimer und wischt die Arbeitsfläche.",
            "Für die Fenster benutzt sie einen Lappen und Glasreiniger.",
            "Nach zwei Stunden ist fast alles sauber und ordentlich.",
            "Zum Schluss öffnet Aylin die Fenster und macht eine kurze Pause."
        )),
        F.lesson("a2-umzug", "Beim Umzug", "A2", "Zuhause • Taşınma ve ev eşyaları", listOf(
            "Familie Kaya zieht am Wochenende in eine neue Wohnung.",
            "Am Freitag packen alle Bücher Kleidung und Geschirr in Kartons.",
            "Die schweren Möbel werden am Samstag von Freunden getragen.",
            "Im neuen Haus stellen sie zuerst den Tisch und die Stühle auf.",
            "Danach bauen sie das Bett und den Schrank zusammen.",
            "Die Kinder bringen ihre Kisten direkt in ihre Zimmer.",
            "Am Abend bestellen alle Pizza weil niemand mehr kochen möchte.",
            "Am Sonntag räumen sie weiter ein und hängen die ersten Bilder auf."
        )),
        F.lesson("a2-fruehstueck", "Beim Frühstück", "A2", "Essen • Kahvaltı hazırlama ve masa düzeni", listOf(
            "Am Sonntag frühstückt die Familie gemeinsam in der Küche.",
            "Murat schneidet Brot und stellt Butter Käse und Marmelade auf den Tisch.",
            "Seine Tochter kocht Eier und macht Tee.",
            "Der Sohn gießt Orangensaft in die Gläser.",
            "Alle setzen sich hin und erzählen von ihren Plänen für den Tag.",
            "Murat möchte noch Kaffee und füllt die Tasse nach.",
            "Nach dem Frühstück sammelt die Familie das Geschirr ein.",
            "Zum Schluss räumen sie den Tisch ab und stellen alles in die Spülmaschine."
        )),
        F.lesson("a2-backen", "Beim Backen zu Hause", "A2", "Essen • Evde kek ve hamur işi hazırlama", listOf(
            "Selin möchte für ihre Freunde einen Kuchen backen.",
            "Sie stellt Mehl Zucker Eier und Butter auf die Arbeitsfläche.",
            "Zuerst wiegt sie die Zutaten mit einer Küchenwaage.",
            "Dann mischt sie alles in einer großen Schüssel.",
            "Sie fettet die Backform ein und füllt den Teig hinein.",
            "Der Kuchen bleibt vierzig Minuten im Ofen.",
            "Nach dem Abkühlen streut Selin etwas Puderzucker darüber.",
            "Am Nachmittag schneidet sie den Kuchen und serviert ihn mit Tee."
        )),
        F.lesson("a2-cafe", "Im Café", "A2", "Freizeit • Kafede sipariş ve ödeme", listOf(
            "Leonie trifft ihre Freundin am Nachmittag in einem kleinen Café.",
            "Sie suchen einen freien Tisch am Fenster.",
            "Ein Kellner bringt die Karte und fragt nach den Getränken.",
            "Leonie bestellt einen Cappuccino und ihre Freundin einen Tee.",
            "Dazu teilen sie sich ein Stück Apfelkuchen.",
            "Sie sprechen lange über Arbeit Familie und Urlaub.",
            "Später bestellen sie noch ein Glas Wasser.",
            "Zum Schluss bezahlen sie getrennt und verabschieden sich vor dem Café."
        )),
        F.lesson("a2-konditorei", "In der Konditorei", "A2", "Essen • Pastane ürünleri ve sipariş", listOf(
            "Am Samstag besucht Nisa eine Konditorei in der Innenstadt.",
            "In der Vitrine sieht sie Torten Kuchen und kleine Gebäckstücke.",
            "Sie braucht eine Geburtstagstorte für ihre Schwester.",
            "Die Verkäuferin zeigt ihr verschiedene Größen und Sorten.",
            "Nisa entscheidet sich für eine Schokoladentorte mit Erdbeeren.",
            "Sie lässt einen kurzen Namen auf die Torte schreiben.",
            "Danach bezahlt sie eine Anzahlung und bekommt einen Abholschein.",
            "Am nächsten Tag holt sie die fertige Torte vorsichtig ab."
        )),
        F.lesson("a2-imbiss", "Am Imbiss", "A2", "Essen • Hızlı yemek siparişi", listOf(
            "Nach der Arbeit hat Cem Hunger und geht zu einem Imbiss.",
            "Vor der Theke liest er die Speisekarte.",
            "Er bestellt eine Portion Pommes und ein Hähnchensandwich.",
            "Die Mitarbeiterin fragt welche Soße er möchte.",
            "Cem nimmt Knoblauchsoße und bestellt noch ein Wasser.",
            "Während er wartet setzt er sich an einen kleinen Tisch.",
            "Nach wenigen Minuten wird seine Nummer aufgerufen.",
            "Cem holt das Essen ab und bezahlt direkt an der Kasse."
        )),
        F.lesson("a2-picknick", "Beim Picknick", "A2", "Freizeit • Piknik hazırlığı ve açık hava", listOf(
            "Zwei Familien planen am Sonntag ein Picknick im Park.",
            "Am Morgen bereiten sie Sandwiches Obst und Getränke vor.",
            "Sie packen Decken Teller Becher und Servietten in große Taschen.",
            "Im Park suchen sie einen schattigen Platz unter einem Baum.",
            "Die Kinder spielen Ball während die Erwachsenen den Tisch vorbereiten.",
            "Später essen alle zusammen und trinken kalte Limonade.",
            "Nach dem Essen sammeln sie den Müll in einer Tüte.",
            "Bevor sie nach Hause fahren kontrollieren sie den Platz noch einmal."
        ))
    )
}