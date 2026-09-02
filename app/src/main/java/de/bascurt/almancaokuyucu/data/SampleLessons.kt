package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

object SampleLessons {
    val all: List<ReaderLesson> by lazy {
        listOf(
            neuerAnfang(), kueche(), arzt(), baeckerei(), supermarkt(), zuhause(), restaurant(),
            wochenmarkt(), neueStadt(), krankenhaus(), apotheke(), autowerkstatt(), digitaleBalance()
        )
    }

    private fun neuerAnfang() = lesson(
        "a2-neuer-anfang", "Ein neuer Anfang", "A2", "Arbeit • İlk iş günü ve otelde çalışma",
        listOf(
            "Elif beginnt heute ihre neue Arbeit im Hotel.",
            "Sie begrüßt die Gäste und beantwortet ihre Fragen.",
            "Am Mittag macht sie eine kurze Pause mit den Kollegen.",
            "Am Abend ist sie müde aber zufrieden."
        ),
        mapOf(
            "beginnt" to verb("a2-neuer-anfang-beginnen", "beginnen", "başlamak", "beginnt", "begann", "hat begonnen"),
            "arbeit" to noun("a2-neuer-anfang-arbeit", "Arbeit", "iş", "die", "Arbeiten"),
            "hotel" to noun("a2-neuer-anfang-hotel", "Hotel", "otel", "das", "Hotels"),
            "begrüßt" to verb("a2-neuer-anfang-begruessen", "begrüßen", "karşılamak / selamlamak", "begrüßt", "begrüßte", "hat begrüßt"),
            "gäste" to noun("a2-neuer-anfang-gast", "Gast", "misafir", "der", "Gäste"),
            "beantwortet" to verb("a2-neuer-anfang-beantworten", "beantworten", "cevaplamak", "beantwortet", "beantwortete", "hat beantwortet"),
            "fragen" to noun("a2-neuer-anfang-frage", "Frage", "soru", "die", "Fragen"),
            "pause" to noun("a2-neuer-anfang-pause", "Pause", "mola", "die", "Pausen"),
            "kollegen" to noun("a2-neuer-anfang-kollege", "Kollege", "iş arkadaşı", "der", "Kollegen", "den Kollegen (-n)"),
            "müde" to adjective("a2-neuer-anfang-muede", "müde", "yorgun", "müder", "am müdesten"),
            "zufrieden" to adjective("a2-neuer-anfang-zufrieden", "zufrieden", "memnun", "zufriedener", "am zufriedensten")
        )
    )

    private fun kueche() = lesson(
        "a2-kueche", "In der Küche", "A2", "Küche • Mutfak malzemeleri ve yemek hazırlama fiilleri",
        listOf(
            "Mina stellt den Topf auf den Herd und nimmt ein Messer.",
            "Sie schneidet die Zwiebel und die Tomaten auf dem Brett.",
            "Dann kocht sie die Suppe und rührt sie mit einem Löffel um.",
            "Zum Schluss legt sie die Teller auf den Tisch und serviert das Essen."
        ),
        mapOf(
            "stellt" to verb("a2-kueche-stellen", "stellen", "koymak / dik konumda yerleştirmek", "stellt", "stellte", "hat gestellt"),
            "topf" to noun("a2-kueche-topf", "Topf", "tencere", "der", "Töpfe"),
            "herd" to noun("a2-kueche-herd", "Herd", "ocak", "der", "Herde"),
            "nimmt" to verb("a2-kueche-nehmen", "nehmen", "almak", "nimmt", "nahm", "hat genommen"),
            "messer" to noun("a2-kueche-messer", "Messer", "bıçak", "das", "Messer"),
            "schneidet" to verb("a2-kueche-schneiden", "schneiden", "kesmek / doğramak", "schneidet", "schnitt", "hat geschnitten"),
            "zwiebel" to noun("a2-kueche-zwiebel", "Zwiebel", "soğan", "die", "Zwiebeln"),
            "tomaten" to noun("a2-kueche-tomate", "Tomate", "domates", "die", "Tomaten"),
            "brett" to noun("a2-kueche-brett", "Brett", "kesme tahtası / tahta", "das", "Bretter"),
            "kocht" to verb("a2-kueche-kochen", "kochen", "pişirmek / kaynatmak", "kocht", "kochte", "hat gekocht"),
            "suppe" to noun("a2-kueche-suppe", "Suppe", "çorba", "die", "Suppen"),
            "rührt" to verb("a2-kueche-ruehren", "rühren", "karıştırmak", "rührt", "rührte", "hat gerührt"),
            "löffel" to noun("a2-kueche-loeffel", "Löffel", "kaşık", "der", "Löffel"),
            "legt" to verb("a2-kueche-legen", "legen", "koymak / yatırarak yerleştirmek", "legt", "legte", "hat gelegt"),
            "teller" to noun("a2-kueche-teller", "Teller", "tabak", "der", "Teller"),
            "tisch" to noun("a2-kueche-tisch", "Tisch", "masa", "der", "Tische"),
            "serviert" to verb("a2-kueche-servieren", "servieren", "servis etmek", "serviert", "servierte", "hat serviert"),
            "essen" to noun("a2-kueche-essen", "Essen", "yemek", "das", "—")
        )
    )

    private fun arzt() = lesson(
        "a2-arzt", "Beim Arzt", "A2", "Arzt • Doktor randevusu, şikâyetler ve muayene",
        listOf(
            "Leyla hat seit gestern starke Halsschmerzen und einen Termin beim Arzt.",
            "Im Wartezimmer wartet sie zwanzig Minuten.",
            "Der Arzt untersucht ihren Hals und misst ihre Temperatur.",
            "Danach schreibt er ein Rezept und empfiehlt viel Ruhe."
        ),
        mapOf(
            "halsschmerzen" to noun("a2-arzt-halsschmerzen", "Halsschmerzen", "boğaz ağrısı", "die", "—"),
            "termin" to noun("a2-arzt-termin", "Termin", "randevu", "der", "Termine"),
            "arzt" to noun("a2-arzt-arzt", "Arzt", "doktor", "der", "Ärzte"),
            "wartezimmer" to noun("a2-arzt-wartezimmer", "Wartezimmer", "bekleme odası", "das", "Wartezimmer"),
            "wartet" to verb("a2-arzt-warten", "warten", "beklemek", "wartet", "wartete", "hat gewartet"),
            "untersucht" to verb("a2-arzt-untersuchen", "untersuchen", "muayene etmek", "untersucht", "untersuchte", "hat untersucht"),
            "hals" to noun("a2-arzt-hals", "Hals", "boğaz / boyun", "der", "Hälse"),
            "misst" to verb("a2-arzt-messen", "messen", "ölçmek", "misst", "maß", "hat gemessen"),
            "temperatur" to noun("a2-arzt-temperatur", "Temperatur", "ateş / sıcaklık", "die", "Temperaturen"),
            "schreibt" to verb("a2-arzt-schreiben", "schreiben", "yazmak", "schreibt", "schrieb", "hat geschrieben"),
            "rezept" to noun("a2-arzt-rezept", "Rezept", "reçete", "das", "Rezepte"),
            "empfiehlt" to verb("a2-arzt-empfehlen", "empfehlen", "tavsiye etmek", "empfiehlt", "empfahl", "hat empfohlen"),
            "ruhe" to noun("a2-arzt-ruhe", "Ruhe", "dinlenme / sakinlik", "die", "—")
        )
    )

    private fun baeckerei() = lesson(
        "a2-baeckerei", "In der Bäckerei", "A2", "Bäckerei • Ekmek, hamur, fırın ve sipariş fiilleri",
        listOf(
            "Am Morgen bereitet der Bäcker den Teig für das Brot vor.",
            "Er formt Brötchen und schiebt das Blech in den Ofen.",
            "Später bestellt eine Kundin zwei Brötchen und ein Stück Kuchen.",
            "Der Bäcker packt alles ein und die Kundin bezahlt an der Kasse."
        ),
        mapOf(
            "bäcker" to noun("a2-baeckerei-baecker", "Bäcker", "fırıncı", "der", "Bäcker"),
            "teig" to noun("a2-baeckerei-teig", "Teig", "hamur", "der", "Teige"),
            "brot" to noun("a2-baeckerei-brot", "Brot", "ekmek", "das", "Brote"),
            "formt" to verb("a2-baeckerei-formen", "formen", "şekil vermek", "formt", "formte", "hat geformt"),
            "brötchen" to noun("a2-baeckerei-broetchen", "Brötchen", "küçük ekmek / sandviç ekmeği", "das", "Brötchen"),
            "schiebt" to verb("a2-baeckerei-schieben", "schieben", "itmek / sürmek", "schiebt", "schob", "hat geschoben"),
            "blech" to noun("a2-baeckerei-blech", "Blech", "fırın tepsisi", "das", "Bleche"),
            "ofen" to noun("a2-baeckerei-ofen", "Ofen", "fırın", "der", "Öfen"),
            "bestellt" to verb("a2-baeckerei-bestellen", "bestellen", "sipariş vermek", "bestellt", "bestellte", "hat bestellt"),
            "kundin" to noun("a2-baeckerei-kundin", "Kundin", "kadın müşteri", "die", "Kundinnen"),
            "kuchen" to noun("a2-baeckerei-kuchen", "Kuchen", "kek / pasta", "der", "Kuchen"),
            "packt" to verb("a2-baeckerei-einpacken", "einpacken", "paketlemek", "packt ein", "packte ein", "hat eingepackt"),
            "bezahlt" to verb("a2-baeckerei-bezahlen", "bezahlen", "ödemek", "bezahlt", "bezahlte", "hat bezahlt"),
            "kasse" to noun("a2-baeckerei-kasse", "Kasse", "kasa", "die", "Kassen")
        )
    )

    private fun supermarkt() = lesson(
        "a2-supermarkt", "Im Supermarkt", "A2", "Einkaufen • Süpermarkette ürün bulma ve ödeme",
        listOf(
            "Emre nimmt einen Einkaufswagen und sucht Milch und Reis.",
            "Die Milch steht im Kühlregal aber der Reis ist im nächsten Gang.",
            "Er sieht ein Angebot und legt auch Äpfel in den Wagen.",
            "An der Kasse bezahlt er mit der Karte und nimmt die Tüte."
        ),
        mapOf(
            "nimmt" to verb("a2-supermarkt-nehmen", "nehmen", "almak", "nimmt", "nahm", "hat genommen"),
            "einkaufswagen" to noun("a2-supermarkt-einkaufswagen", "Einkaufswagen", "alışveriş arabası", "der", "Einkaufswagen"),
            "sucht" to verb("a2-supermarkt-suchen", "suchen", "aramak", "sucht", "suchte", "hat gesucht"),
            "milch" to noun("a2-supermarkt-milch", "Milch", "süt", "die", "—"),
            "reis" to noun("a2-supermarkt-reis", "Reis", "pirinç", "der", "—"),
            "kühlregal" to noun("a2-supermarkt-kuehlregal", "Kühlregal", "soğutucu raf", "das", "Kühlregale"),
            "gang" to noun("a2-supermarkt-gang", "Gang", "koridor", "der", "Gänge"),
            "sieht" to verb("a2-supermarkt-sehen", "sehen", "görmek", "sieht", "sah", "hat gesehen"),
            "angebot" to noun("a2-supermarkt-angebot", "Angebot", "indirimli teklif / kampanya", "das", "Angebote"),
            "legt" to verb("a2-supermarkt-legen", "legen", "koymak", "legt", "legte", "hat gelegt"),
            "äpfel" to noun("a2-supermarkt-apfel", "Apfel", "elma", "der", "Äpfel"),
            "kasse" to noun("a2-supermarkt-kasse", "Kasse", "kasa", "die", "Kassen"),
            "bezahlt" to verb("a2-supermarkt-bezahlen", "bezahlen", "ödemek", "bezahlt", "bezahlte", "hat bezahlt"),
            "karte" to noun("a2-supermarkt-karte", "Karte", "kart", "die", "Karten"),
            "tüte" to noun("a2-supermarkt-tuete", "Tüte", "poşet", "die", "Tüten")
        )
    )

    private fun zuhause() = lesson(
        "a2-zuhause", "Ein Morgen zu Hause", "A2", "Zuhause • Sabah rutini ve ev işleri",
        listOf(
            "Nora öffnet morgens das Fenster und macht das Bett.",
            "Danach räumt sie die Spülmaschine aus und deckt den Tisch.",
            "Sie hängt die Wäsche auf und faltet die trockenen Handtücher.",
            "Bevor sie geht nimmt sie den Müll mit nach draußen."
        ),
        mapOf(
            "öffnet" to verb("a2-zuhause-oeffnen", "öffnen", "açmak", "öffnet", "öffnete", "hat geöffnet"),
            "fenster" to noun("a2-zuhause-fenster", "Fenster", "pencere", "das", "Fenster"),
            "bett" to noun("a2-zuhause-bett", "Bett", "yatak", "das", "Betten"),
            "räumt" to verb("a2-zuhause-ausraeumen", "ausräumen", "boşaltmak", "räumt aus", "räumte aus", "hat ausgeräumt"),
            "spülmaschine" to noun("a2-zuhause-spuelmaschine", "Spülmaschine", "bulaşık makinesi", "die", "Spülmaschinen"),
            "deckt" to verb("a2-zuhause-decken", "den Tisch decken", "sofrayı kurmak", "deckt", "deckte", "hat gedeckt"),
            "tisch" to noun("a2-zuhause-tisch", "Tisch", "masa", "der", "Tische"),
            "hängt" to verb("a2-zuhause-aufhaengen", "aufhängen", "asmak", "hängt auf", "hängte auf", "hat aufgehängt"),
            "wäsche" to noun("a2-zuhause-waesche", "Wäsche", "çamaşır", "die", "—"),
            "faltet" to verb("a2-zuhause-falten", "falten", "katlamak", "faltet", "faltete", "hat gefaltet"),
            "handtücher" to noun("a2-zuhause-handtuch", "Handtuch", "havlu", "das", "Handtücher"),
            "müll" to noun("a2-zuhause-muell", "Müll", "çöp", "der", "—")
        )
    )

    private fun restaurant() = lesson(
        "a2-restaurant", "Im Restaurant", "A2", "Restaurant • Sipariş verme ve masada kullanılan ifadeler",
        listOf(
            "Sara und Ali bekommen eine Speisekarte und wählen ihre Gerichte.",
            "Sara bestellt eine Suppe und Ali nimmt ein Nudelgericht.",
            "Der Kellner bringt die Getränke und später das Essen.",
            "Nach dem Essen bitten sie um die Rechnung und bezahlen zusammen."
        ),
        mapOf(
            "speisekarte" to noun("a2-restaurant-speisekarte", "Speisekarte", "menü", "die", "Speisekarten"),
            "wählen" to verb("a2-restaurant-waehlen", "wählen", "seçmek", "wählt", "wählte", "hat gewählt"),
            "gerichte" to noun("a2-restaurant-gericht", "Gericht", "yemek / yemek çeşidi", "das", "Gerichte"),
            "bestellt" to verb("a2-restaurant-bestellen", "bestellen", "sipariş vermek", "bestellt", "bestellte", "hat bestellt"),
            "suppe" to noun("a2-restaurant-suppe", "Suppe", "çorba", "die", "Suppen"),
            "nimmt" to verb("a2-restaurant-nehmen", "nehmen", "seçmek / almak", "nimmt", "nahm", "hat genommen"),
            "nudelgericht" to noun("a2-restaurant-nudelgericht", "Nudelgericht", "makarnalı yemek", "das", "Nudelgerichte"),
            "kellner" to noun("a2-restaurant-kellner", "Kellner", "garson", "der", "Kellner"),
            "bringt" to verb("a2-restaurant-bringen", "bringen", "getirmek", "bringt", "brachte", "hat gebracht"),
            "getränke" to noun("a2-restaurant-getraenk", "Getränk", "içecek", "das", "Getränke"),
            "rechnung" to noun("a2-restaurant-rechnung", "Rechnung", "hesap / fatura", "die", "Rechnungen"),
            "bezahlen" to verb("a2-restaurant-bezahlen", "bezahlen", "ödemek", "bezahlt", "bezahlte", "hat bezahlt")
        )
    )

    private fun wochenmarkt() = lesson(
        "a2-wochenmarkt", "Auf dem Wochenmarkt", "A2", "Markt • Sebze-meyve, tartma ve fiyat sorma",
        listOf(
            "Auf dem Wochenmarkt kauft Fatma frisches Gemüse und Obst.",
            "Sie wählt Tomaten Gurken und drei reife Pfirsiche.",
            "Der Verkäufer wiegt das Gemüse und nennt den Preis.",
            "Fatma bezahlt bar und legt alles in ihre Tasche."
        ),
        mapOf(
            "wochenmarkt" to noun("a2-wochenmarkt-markt", "Wochenmarkt", "semt pazarı", "der", "Wochenmärkte"),
            "kauft" to verb("a2-wochenmarkt-kaufen", "kaufen", "satın almak", "kauft", "kaufte", "hat gekauft"),
            "gemüse" to noun("a2-wochenmarkt-gemuese", "Gemüse", "sebze", "das", "—"),
            "obst" to noun("a2-wochenmarkt-obst", "Obst", "meyve", "das", "—"),
            "wählt" to verb("a2-wochenmarkt-waehlen", "wählen", "seçmek", "wählt", "wählte", "hat gewählt"),
            "tomaten" to noun("a2-wochenmarkt-tomate", "Tomate", "domates", "die", "Tomaten"),
            "gurken" to noun("a2-wochenmarkt-gurke", "Gurke", "salatalık", "die", "Gurken"),
            "pfirsiche" to noun("a2-wochenmarkt-pfirsich", "Pfirsich", "şeftali", "der", "Pfirsiche"),
            "verkäufer" to noun("a2-wochenmarkt-verkaeufer", "Verkäufer", "satıcı", "der", "Verkäufer"),
            "wiegt" to verb("a2-wochenmarkt-wiegen", "wiegen", "tartmak", "wiegt", "wog", "hat gewogen"),
            "preis" to noun("a2-wochenmarkt-preis", "Preis", "fiyat", "der", "Preise"),
            "bezahlt" to verb("a2-wochenmarkt-bezahlen", "bezahlen", "ödemek", "bezahlt", "bezahlte", "hat bezahlt"),
            "tasche" to noun("a2-wochenmarkt-tasche", "Tasche", "çanta", "die", "Taschen")
        )
    )

    private fun neueStadt() = lesson(
        "b1-neue-stadt", "In einer neuen Stadt", "B1", "Stadtleben • Yeni şehre alışma ve sosyal çevre",
        listOf(
            "Amir ist vor drei Monaten nach Stuttgart gezogen.",
            "Am Anfang kannte er nur wenige Menschen und fühlte sich oft allein.",
            "Dann meldete er sich bei einem Sprachkurs an und lernte neue Leute kennen.",
            "Heute kennt er die Stadt besser und fühlt sich dort zu Hause."
        ),
        mapOf(
            "gezogen" to verb("b1-neue-stadt-ziehen", "ziehen", "taşınmak", "zieht", "zog", "ist gezogen"),
            "kannte" to verb("b1-neue-stadt-kennen", "kennen", "tanımak / bilmek", "kennt", "kannte", "hat gekannt"),
            "menschen" to noun("b1-neue-stadt-mensch", "Mensch", "insan", "der", "Menschen", "den Menschen (-en)"),
            "fühlte" to verb("b1-neue-stadt-fuehlen", "sich fühlen", "hissetmek", "fühlt sich", "fühlte sich", "hat sich gefühlt"),
            "allein" to adjective("b1-neue-stadt-allein", "allein", "yalnız", null, null),
            "sprachkurs" to noun("b1-neue-stadt-sprachkurs", "Sprachkurs", "dil kursu", "der", "Sprachkurse"),
            "lernte" to verb("b1-neue-stadt-lernen", "kennenlernen", "tanışmak", "lernt kennen", "lernte kennen", "hat kennengelernt"),
            "leute" to noun("b1-neue-stadt-leute", "Leute", "insanlar", "die", "—"),
            "stadt" to noun("b1-neue-stadt-stadt", "Stadt", "şehir", "die", "Städte")
        )
    )

    private fun krankenhaus() = lesson(
        "b1-krankenhaus", "Ein Tag im Krankenhaus", "B1", "Krankenhaus • Servis, hemşire, muayene ve taburculuk",
        listOf(
            "Herr Yilmaz wird am Morgen auf die Station gebracht.",
            "Eine Pflegekraft kontrolliert seinen Blutdruck und bereitet die Untersuchung vor.",
            "Später spricht der Arzt mit ihm über die Ergebnisse.",
            "Am Nachmittag darf er das Krankenhaus verlassen und bekommt einen Kontrolltermin."
        ),
        mapOf(
            "station" to noun("b1-krankenhaus-station", "Station", "servis / bölüm", "die", "Stationen"),
            "pflegekraft" to noun("b1-krankenhaus-pflegekraft", "Pflegekraft", "sağlık bakım personeli", "die", "Pflegekräfte"),
            "kontrolliert" to verb("b1-krankenhaus-kontrollieren", "kontrollieren", "kontrol etmek", "kontrolliert", "kontrollierte", "hat kontrolliert"),
            "blutdruck" to noun("b1-krankenhaus-blutdruck", "Blutdruck", "tansiyon", "der", "—"),
            "bereitet" to verb("b1-krankenhaus-vorbereiten", "vorbereiten", "hazırlamak", "bereitet vor", "bereitete vor", "hat vorbereitet"),
            "untersuchung" to noun("b1-krankenhaus-untersuchung", "Untersuchung", "muayene / tetkik", "die", "Untersuchungen"),
            "spricht" to verb("b1-krankenhaus-sprechen", "sprechen", "konuşmak", "spricht", "sprach", "hat gesprochen"),
            "ergebnisse" to noun("b1-krankenhaus-ergebnis", "Ergebnis", "sonuç", "das", "Ergebnisse"),
            "verlassen" to verb("b1-krankenhaus-verlassen", "verlassen", "ayrılmak / terk etmek", "verlässt", "verließ", "hat verlassen"),
            "krankenhaus" to noun("b1-krankenhaus-krankenhaus", "Krankenhaus", "hastane", "das", "Krankenhäuser"),
            "kontrolltermin" to noun("b1-krankenhaus-kontrolltermin", "Kontrolltermin", "kontrol randevusu", "der", "Kontrolltermine")
        )
    )

    private fun apotheke() = lesson(
        "b1-apotheke", "In der Apotheke", "B1", "Apotheke • Reçete, ilaç kullanımı ve eczacıyla konuşma",
        listOf(
            "Meryem geht mit einem Rezept in die Apotheke.",
            "Die Apothekerin erklärt wie sie das Medikament einnehmen soll.",
            "Meryem fragt auch nach möglichen Nebenwirkungen.",
            "Zum Schluss bezahlt sie und steckt das Medikament in ihre Tasche."
        ),
        mapOf(
            "rezept" to noun("b1-apotheke-rezept", "Rezept", "reçete", "das", "Rezepte"),
            "apotheke" to noun("b1-apotheke-apotheke", "Apotheke", "eczane", "die", "Apotheken"),
            "apothekerin" to noun("b1-apotheke-apothekerin", "Apothekerin", "kadın eczacı", "die", "Apothekerinnen"),
            "erklärt" to verb("b1-apotheke-erklaeren", "erklären", "açıklamak", "erklärt", "erklärte", "hat erklärt"),
            "medikament" to noun("b1-apotheke-medikament", "Medikament", "ilaç", "das", "Medikamente"),
            "einnehmen" to verb("b1-apotheke-einnehmen", "einnehmen", "ilaç kullanmak / almak", "nimmt ein", "nahm ein", "hat eingenommen"),
            "fragt" to verb("b1-apotheke-fragen", "fragen", "sormak", "fragt", "fragte", "hat gefragt"),
            "nebenwirkungen" to noun("b1-apotheke-nebenwirkung", "Nebenwirkung", "yan etki", "die", "Nebenwirkungen"),
            "bezahlt" to verb("b1-apotheke-bezahlen", "bezahlen", "ödemek", "bezahlt", "bezahlte", "hat bezahlt"),
            "steckt" to verb("b1-apotheke-stecken", "stecken", "koymak / sokmak", "steckt", "steckte", "hat gesteckt"),
            "tasche" to noun("b1-apotheke-tasche", "Tasche", "çanta", "die", "Taschen")
        )
    )

    private fun autowerkstatt() = lesson(
        "b1-autowerkstatt", "In der Autowerkstatt", "B1", "Auto • Arıza, tamir ve servis konuşmaları",
        listOf(
            "Mehmet hört beim Fahren ein ungewöhnliches Geräusch.",
            "Er bringt das Auto in die Werkstatt und beschreibt das Problem.",
            "Der Mechaniker prüft die Bremsen und entdeckt einen Defekt.",
            "Am Nachmittag ist die Reparatur fertig und Mehmet holt das Auto ab."
        ),
        mapOf(
            "fahren" to verb("b1-autowerkstatt-fahren", "fahren", "araç kullanmak / gitmek", "fährt", "fuhr", "ist gefahren"),
            "geräusch" to noun("b1-autowerkstatt-geraeusch", "Geräusch", "ses", "das", "Geräusche"),
            "bringt" to verb("b1-autowerkstatt-bringen", "bringen", "götürmek", "bringt", "brachte", "hat gebracht"),
            "auto" to noun("b1-autowerkstatt-auto", "Auto", "araba", "das", "Autos"),
            "werkstatt" to noun("b1-autowerkstatt-werkstatt", "Werkstatt", "tamirhane / servis", "die", "Werkstätten"),
            "beschreibt" to verb("b1-autowerkstatt-beschreiben", "beschreiben", "tarif etmek", "beschreibt", "beschrieb", "hat beschrieben"),
            "problem" to noun("b1-autowerkstatt-problem", "Problem", "sorun", "das", "Probleme"),
            "mechaniker" to noun("b1-autowerkstatt-mechaniker", "Mechaniker", "tamirci", "der", "Mechaniker"),
            "prüft" to verb("b1-autowerkstatt-pruefen", "prüfen", "kontrol etmek", "prüft", "prüfte", "hat geprüft"),
            "bremsen" to noun("b1-autowerkstatt-bremse", "Bremse", "fren", "die", "Bremsen"),
            "entdeckt" to verb("b1-autowerkstatt-entdecken", "entdecken", "tespit etmek / keşfetmek", "entdeckt", "entdeckte", "hat entdeckt"),
            "defekt" to noun("b1-autowerkstatt-defekt", "Defekt", "arıza", "der", "Defekte"),
            "reparatur" to noun("b1-autowerkstatt-reparatur", "Reparatur", "tamir", "die", "Reparaturen"),
            "holt" to verb("b1-autowerkstatt-abholen", "abholen", "gidip almak", "holt ab", "holte ab", "hat abgeholt")
        )
    )

    private fun digitaleBalance() = lesson(
        "b2-digitale-balance", "Digitale Balance", "B2", "Digitaler Alltag • Bildirimler, dikkat ve dijital denge",
        listOf(
            "Viele Menschen sind ständig erreichbar und reagieren sofort auf jede Nachricht.",
            "Wer bewusst auf Benachrichtigungen verzichtet kann sich besser konzentrieren.",
            "Regelmäßige Pausen helfen dabei die Aufmerksamkeit zu schützen.",
            "Entscheidend ist die Technik bewusst zu nutzen statt sich von ihr ablenken zu lassen."
        ),
        mapOf(
            "erreichbar" to adjective("b2-digital-erreichbar", "erreichbar", "ulaşılabilir", null, null),
            "reagieren" to verb("b2-digital-reagieren", "reagieren", "tepki vermek", "reagiert", "reagierte", "hat reagiert"),
            "nachricht" to noun("b2-digital-nachricht", "Nachricht", "mesaj", "die", "Nachrichten"),
            "bewusst" to adjective("b2-digital-bewusst", "bewusst", "bilinçli", "bewusster", "am bewusstesten"),
            "benachrichtigungen" to noun("b2-digital-benachrichtigung", "Benachrichtigung", "bildirim", "die", "Benachrichtigungen"),
            "verzichtet" to verb("b2-digital-verzichten", "auf etwas verzichten", "bir şeyden vazgeçmek", "verzichtet", "verzichtete", "hat verzichtet"),
            "konzentrieren" to verb("b2-digital-konzentrieren", "sich konzentrieren", "odaklanmak", "konzentriert sich", "konzentrierte sich", "hat sich konzentriert"),
            "pausen" to noun("b2-digital-pause", "Pause", "mola", "die", "Pausen"),
            "aufmerksamkeit" to noun("b2-digital-aufmerksamkeit", "Aufmerksamkeit", "dikkat", "die", "—"),
            "schützen" to verb("b2-digital-schuetzen", "schützen", "korumak", "schützt", "schützte", "hat geschützt"),
            "technik" to noun("b2-digital-technik", "Technik", "teknoloji", "die", "Techniken"),
            "nutzen" to verb("b2-digital-nutzen", "nutzen", "kullanmak", "nutzt", "nutzte", "hat genutzt"),
            "ablenken" to verb("b2-digital-ablenken", "ablenken", "dikkatini dağıtmak", "lenkt ab", "lenkte ab", "hat abgelenkt")
        )
    )

    private fun lesson(id: String, title: String, level: String, summary: String, texts: List<String>, vocab: Map<String, Lexeme>): ReaderLesson =
        ReaderLesson(id, title, level, summary, texts.mapIndexed { si, sentence ->
            sentence.split(" ").mapIndexed { ti, shown ->
                val clean = clean(shown)
                ReadingToken(shown, vocab[clean] ?: common("$id-$si-$ti", clean))
            }
        })

    private fun clean(text: String): String = text.trim('"', '„', '“', '.', ',', ':', ';', '!', '?').lowercase()

    private fun noun(id: String, base: String, meaning: String, article: String, plural: String, accusativeNote: String? = null) =
        Lexeme(id=id, base=base, meaning=meaning, type="Kelime", explanation="Bu isim hikâyenin ana temasındaki günlük kullanım için seçildi.", quizEligible=true, wordClass="İsim", article=article, plural=plural, accusativeNote=accusativeNote)

    private fun verb(id: String, base: String, meaning: String, third: String, preterite: String, perfect: String) =
        Lexeme(id=id, base=base, meaning=meaning, type="Kelime", explanation="Bu fiil hikâyenin ana temasında sık kullanılan temel eylemlerden biridir.", quizEligible=true, wordClass="Fiil", infinitive=base, thirdPerson=third, preterite=preterite, perfect=perfect)

    private fun adjective(id: String, base: String, meaning: String, comparative: String?, superlative: String?) =
        Lexeme(id=id, base=base, meaning=meaning, type="Kelime", quizEligible=true, wordClass="Sıfat", positive=base, comparative=comparative, superlative=superlative)

    private fun common(id: String, word: String): Lexeme {
        val m = commonMeanings[word]
        return if (m != null) Lexeme(id, m.first, m.second, m.third, quizEligible=false, wordClass=m.third)
        else Lexeme(id, word, word, "Diğer", quizEligible=false, wordClass="Diğer")
    }

    private val commonMeanings = mapOf(
        "der" to Triple("der", "artikel", "Artikel"), "die" to Triple("die", "artikel", "Artikel"), "das" to Triple("das", "artikel", "Artikel"),
        "den" to Triple("den", "artikel", "Artikel"), "dem" to Triple("dem", "artikel", "Artikel"), "ein" to Triple("ein", "bir", "Artikel"),
        "eine" to Triple("eine", "bir", "Artikel"), "einen" to Triple("einen", "bir", "Artikel"), "einem" to Triple("einem", "bir", "Artikel"), "einer" to Triple("einer", "bir", "Artikel"),
        "sie" to Triple("sie", "o / onlar", "Zamir"), "er" to Triple("er", "o", "Zamir"), "sich" to Triple("sich", "kendini / kendisine", "Zamir"),
        "ihre" to Triple("ihre", "onun", "Zamir"), "ihren" to Triple("ihren", "onun", "Zamir"), "ihm" to Triple("ihm", "ona", "Zamir"), "ihr" to Triple("ihr", "ona / onun", "Zamir"), "seinen" to Triple("seinen", "onun", "Zamir"),
        "und" to Triple("und", "ve", "Bağlaç"), "aber" to Triple("aber", "ama", "Bağlaç"), "bevor" to Triple("bevor", "-meden önce", "Bağlaç"), "wie" to Triple("wie", "nasıl", "Bağlaç"),
        "dann" to Triple("dann", "sonra", "Zarf"), "danach" to Triple("danach", "ondan sonra", "Zarf"), "später" to Triple("später", "daha sonra", "Zarf"), "heute" to Triple("heute", "bugün", "Zarf"),
        "gestern" to Triple("gestern", "dün", "Zarf"), "morgens" to Triple("morgens", "sabahları", "Zarf"), "oft" to Triple("oft", "sık sık", "Zarf"), "nur" to Triple("nur", "sadece", "Zarf"),
        "auch" to Triple("auch", "ayrıca / de", "Zarf"), "sehr" to Triple("sehr", "çok", "Zarf"), "sofort" to Triple("sofort", "hemen", "Zarf"), "ständig" to Triple("ständig", "sürekli", "Zarf"),
        "mit" to Triple("mit", "ile", "Edat"), "für" to Triple("für", "için", "Edat"), "beim" to Triple("beim", "-de / yanında", "Edat"), "auf" to Triple("auf", "üzerinde / üzerine", "Edat"),
        "in" to Triple("in", "içinde / -de", "Edat"), "im" to Triple("im", "içinde / -de", "Edat"), "an" to Triple("an", "-de / yanında", "Edat"), "am" to Triple("am", "-de / sırasında", "Edat"),
        "nach" to Triple("nach", "sonra / -e doğru", "Edat"), "vor" to Triple("vor", "önce / önünde", "Edat"), "seit" to Triple("seit", "-den beri", "Edat"), "über" to Triple("über", "hakkında", "Edat"),
        "um" to Triple("um", "için / çevresinde", "Edat"), "von" to Triple("von", "-den / tarafından", "Edat"), "zum" to Triple("zum", "-e / -a", "Edat"), "aus" to Triple("aus", "-den / dışarı", "Edat"),
        "ist" to Triple("sein", "olmak", "Fiil"), "sind" to Triple("sein", "olmak", "Fiil"), "hat" to Triple("haben", "sahip olmak / var", "Fiil"), "wird" to Triple("werden", "olmak", "Fiil"),
        "kann" to Triple("können", "-ebilmek", "Fiil"), "darf" to Triple("dürfen", "izinli olmak", "Fiil"), "soll" to Triple("sollen", "-meli / -malı", "Fiil"), "geht" to Triple("gehen", "gitmek", "Fiil"),
        "macht" to Triple("machen", "yapmak", "Fiil"), "steht" to Triple("stehen", "durmak", "Fiil"), "bekommen" to Triple("bekommen", "almak", "Fiil"), "bekommt" to Triple("bekommen", "almak", "Fiil"),
        "hört" to Triple("hören", "duymak", "Fiil"), "kennt" to Triple("kennen", "tanımak / bilmek", "Fiil"), "hilft" to Triple("helfen", "yardım etmek", "Fiil"), "helfen" to Triple("helfen", "yardım etmek", "Fiil"),
        "starke" to Triple("stark", "şiddetli", "Sıfat"), "kurze" to Triple("kurz", "kısa", "Sıfat"), "neue" to Triple("neu", "yeni", "Sıfat"), "neuen" to Triple("neu", "yeni", "Sıfat"),
        "frisches" to Triple("frisch", "taze", "Sıfat"), "reife" to Triple("reif", "olgun", "Sıfat"), "ungewöhnliches" to Triple("ungewöhnlich", "alışılmadık", "Sıfat"), "nächsten" to Triple("nächst", "bir sonraki", "Sıfat"),
        "trockenen" to Triple("trocken", "kuru", "Sıfat"), "fertig" to Triple("fertig", "hazır / bitmiş", "Sıfat"), "besser" to Triple("gut", "daha iyi", "Sıfat"), "möglichen" to Triple("möglich", "olası", "Sıfat")
    )
}
