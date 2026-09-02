package de.bascurt.almancaokuyucu.data

/**
 * Ortak hikâye sözlüğünün tamamlayıcı katmanı.
 * Aynı kelime her hikâyede tekrar tanımlanmaz; bütün ExtendedLessons bu merkezi sözlüğü kullanır.
 */
internal val supplementalVerbLexicon: Map<String, ExtendedVerbSeed> = buildMap {
    fun add(base: String, meaning: String, third: String, preterite: String, perfect: String, vararg forms: String) {
        val seed = ExtendedVerbSeed(base, meaning, third, preterite, perfect)
        forms.forEach { put(it, seed) }
    }

    add("machen", "yapmak", "macht", "machte", "hat gemacht", "macht", "machen")
    add("sein", "olmak", "ist", "war", "ist gewesen", "bin", "bist", "ist", "sind", "seid", "war", "waren")
    add("haben", "sahip olmak / var olmak", "hat", "hatte", "hat gehabt", "habe", "hast", "hat", "haben", "hatte")
    add("werden", "olmak / hâline gelmek", "wird", "wurde", "ist geworden", "wird", "werden", "wurde")
    add("gehen", "gitmek / yürümek", "geht", "ging", "ist gegangen", "geht", "gehen")
    add("kommen", "gelmek", "kommt", "kam", "ist gekommen", "kommt", "kommen")
    add("bleiben", "kalmak", "bleibt", "blieb", "ist geblieben", "bleibt", "bleiben")
    add("bekommen", "almak / elde etmek", "bekommt", "bekam", "hat bekommen", "bekommt", "bekommen")
    add("finden", "bulmak", "findet", "fand", "hat gefunden", "findet", "finden")
    add("liegen", "bulunmak / yatmak", "liegt", "lag", "hat gelegen", "liegt", "liegen")
    add("stehen", "durmak / bulunmak", "steht", "stand", "hat gestanden", "steht", "stehen")
    add("sitzen", "oturmak", "sitzt", "saß", "hat gesessen", "sitzt", "sitzen")
    add("tragen", "taşımak / giymek", "trägt", "trug", "hat getragen", "trägt", "getragen")
    add("erzählen", "anlatmak", "erzählt", "erzählte", "hat erzählt", "erzählt", "erzählen")
    add("nennen", "adını söylemek / belirtmek", "nennt", "nannte", "hat genannt", "nennt", "nennen")
    add("wünschen", "dilemek", "wünscht", "wünschte", "hat gewünscht", "wünscht", "wünschen")
    add("verlassen", "ayrılmak / terk etmek", "verlässt", "verließ", "hat verlassen", "verlässt", "verlassen")
    add("kennen", "tanımak / bilmek", "kennt", "kannte", "hat gekannt", "kennt", "kannte")
    add("kennenlernen", "tanışmak / tanımaya başlamak", "lernt kennen", "lernte kennen", "hat kennengelernt", "lernte")
    add("beitreten", "katılmak / üye olmak", "tritt bei", "trat bei", "ist beigetreten", "beitreten")
    add("knüpfen", "kurmak", "knüpft", "knüpfte", "hat geknüpft", "knüpfen")
    add("bringen", "getirmek / götürmek", "bringt", "brachte", "hat gebracht", "gebracht")
    add("dauern", "sürmek", "dauert", "dauerte", "hat gedauert", "dauert", "dauern")
    add("schlafen", "uyumak", "schläft", "schlief", "hat geschlafen", "geschlafen", "schläft")
    add("ruhen", "dinlenmek", "ruht", "ruhte", "hat geruht", "ruht", "ruhen")
    add("einnehmen", "almak / kullanmak (ilaç)", "nimmt ein", "nahm ein", "hat eingenommen", "einnehmen")
    add("lesen", "okumak", "liest", "las", "hat gelesen", "gelesen")
    add("hören", "duymak / dinlemek", "hört", "hörte", "hat gehört", "hört", "hören")
    add("ersetzen", "değiştirmek / yerine yenisini takmak", "ersetzt", "ersetzte", "hat ersetzt", "ersetzt")
    add("zustimmen", "kabul etmek / onaylamak", "stimmt zu", "stimmte zu", "hat zugestimmt", "stimmt")
    add("merken", "fark etmek", "merkt", "merkte", "hat gemerkt", "merkt")
    add("verschwinden", "kaybolmak", "verschwindet", "verschwand", "ist verschwunden", "verschwunden")
    add("beeinflussen", "etkilemek", "beeinflusst", "beeinflusste", "hat beeinflusst", "beeinflusst")
    add("helfen", "yardım etmek", "hilft", "half", "hat geholfen", "helfen")
    add("genießen", "keyfini çıkarmak", "genießt", "genoss", "hat genossen", "genießt")
    add("frühstücken", "kahvaltı yapmak", "frühstückt", "frühstückte", "hat gefrühstückt", "frühstückt")
    add("teilen", "paylaşmak", "teilt", "teilte", "hat geteilt", "teilen")
    add("planen", "planlamak", "plant", "plante", "hat geplant", "planen", "plant")
    add("spielen", "oynamak", "spielt", "spielte", "hat gespielt", "spielen")
    add("essen", "yemek", "isst", "aß", "hat gegessen", "essen", "isst")
    add("fahren", "gitmek / araç kullanmak", "fährt", "fuhr", "ist gefahren", "fahren")
    add("schauen", "bakmak", "schaut", "schaute", "hat geschaut", "schaut")
    add("passen", "uymak", "passt", "passte", "hat gepasst", "passen")
    add("bestätigen", "doğrulamak / onaylamak", "bestätigt", "bestätigte", "hat bestätigt", "bestätigt")
    add("unterschreiben", "imzalamak", "unterschreibt", "unterschrieb", "hat unterschrieben", "unterschreiben")
    add("anrufen", "telefonla aramak", "ruft an", "rief an", "hat angerufen", "anrufen")
    add("aufstehen", "ayağa kalkmak", "steht auf", "stand auf", "ist aufgestanden", "aufstehen")
    add("erhalten", "almak / teslim almak", "erhält", "erhielt", "hat erhalten", "erhält")
    add("steigen", "yükselmek", "steigt", "stieg", "ist gestiegen", "steigen")
    add("halten", "durmak / tutmak", "hält", "hielt", "hat gehalten", "hält")
    add("beobachten", "izlemek / gözlemlemek", "beobachtet", "beobachtete", "hat beobachtet", "beobachten")
    add("erreichen", "ulaşmak", "erreicht", "erreichte", "hat erreicht", "erreichen")
    add("folgen", "takip etmek", "folgt", "folgte", "ist gefolgt", "folgen")
    add("dehnen", "esnetmek", "dehnt", "dehnte", "hat gedehnt", "dehnt")
    add("trainieren", "antrenman yapmak", "trainiert", "trainierte", "hat trainiert", "trainiert")
    add("schwimmen", "yüzmek", "schwimmt", "schwamm", "ist geschwommen", "schwimmt", "schwimmen")
    add("duschen", "duş almak", "duscht", "duschte", "hat geduscht", "duschen")
    add("scannen", "taratmak", "scannt", "scannte", "hat gescannt", "scannt")
    add("laufen", "yürümek / koşmak", "läuft", "lief", "ist gelaufen", "laufen")
    add("kaufen", "satın almak", "kauft", "kaufte", "hat gekauft", "gekauft")
    add("kleben", "yapıştırmak", "klebt", "klebte", "hat geklebt", "klebt")
    add("schicken", "göndermek", "schickt", "schickte", "hat geschickt", "schicken")
    add("bewerben", "başvurmak", "bewirbt sich", "bewarb sich", "hat sich beworben", "beworben")
    add("betreuen", "yönetmek / ilgilenmek", "betreut", "betreute", "hat betreut", "betreut")
    add("lösen", "çözmek", "löst", "löste", "hat gelöst", "gelöst")
    add("stattfinden", "gerçekleşmek", "findet statt", "fand statt", "hat stattgefunden", "statt")
    add("verteilen", "dağıtmak", "verteilt", "verteilte", "hat verteilt", "verteilt")
    add("berichten", "bildirmek / anlatmak", "berichtet", "berichtete", "hat berichtet", "berichtet")
    add("ankommen", "varmak", "kommt an", "kam an", "ist angekommen", "ankommen")
    add("aufwärmen", "ısınmak", "wärmt sich auf", "wärmte sich auf", "hat sich aufgewärmt", "wärmt")
    add("eintragen", "kaydetmek / yazmak", "trägt ein", "trug ein", "hat eingetragen", "trägt")
    add("fortsetzen", "devam ettirmek", "setzt fort", "setzte fort", "hat fortgesetzt", "setzt")
    add("erledigen", "halletmek / tamamlamak", "erledigt", "erledigte", "hat erledigt", "erledigt")
    add("beenden", "bitirmek", "beendet", "beendete", "hat beendet", "beendet")
}

internal val supplementalNounLexicon: Map<String, ExtendedNounSeed> = buildMap {
    fun add(base: String, meaning: String, article: String, plural: String, vararg forms: String) {
        val seed = ExtendedNounSeed(base, meaning, article, plural)
        forms.forEach { put(it, seed) }
    }

    add("Unterlage", "belge / evrak", "die", "Unterlagen", "unterlagen")
    add("Frühstück", "kahvaltı", "das", "Frühstücke", "frühstück")
    add("Weg", "yol", "der", "Wege", "weg")
    add("Zimmerliste", "oda listesi", "die", "Zimmerlisten", "zimmerliste")
    add("Chefin", "kadın yönetici / patron", "die", "Chefinnen", "chefin")
    add("Tag", "gün", "der", "Tage", "tag", "tage")
    add("Stück", "parça", "das", "Stücke", "stück", "stücke")
    add("Essen", "yemek", "das", "—", "essen")
    add("Kollegin", "kadın iş arkadaşı", "die", "Kolleginnen", "kollegin")
    add("Theke", "tezgâh", "die", "Theken", "theke")
    add("Nuss", "kuruyemiş / fındık", "die", "Nüsse", "nüsse")
    add("Abschied", "veda", "der", "Abschiede", "abschied")
    add("Saft", "meyve suyu", "der", "Säfte", "säfte", "saft")
    add("Produkt", "ürün", "das", "Produkte", "produkte", "produkt")
    add("Band", "kasa bandı", "das", "Bänder", "band")
    add("Einkauf", "alışveriş / alınan ürünler", "der", "Einkäufe", "einkäufe", "einkauf")
    add("Wohnzimmer", "oturma odası", "das", "Wohnzimmer", "wohnzimmer")
    add("Wasser", "su", "das", "—", "wasser")
    add("Katze", "kedi", "die", "Katzen", "katze")
    add("Flur", "koridor / antre", "der", "Flure", "flur")
    add("Nudelgericht", "makarnalı yemek", "das", "Nudelgerichte", "nudelgericht")
    add("Flasche", "şişe", "die", "Flaschen", "flasche")
    add("Pfirsich", "şeftali", "der", "Pfirsiche", "pfirsiche")
    add("Schale", "kap / küçük kutu", "die", "Schalen", "schale")
    add("Stand", "tezgâh / stant", "der", "Stände", "stand")
    add("Monat", "ay", "der", "Monate", "monaten", "monat")
    add("Mensch", "insan", "der", "Menschen", "menschen", "mensch")
    add("Teilnehmer", "katılımcı", "der", "Teilnehmer", "teilnehmer")
    add("Viertel", "semt / mahalle", "das", "Viertel", "viertel")
    add("Geschäft", "mağaza / dükkân", "das", "Geschäfte", "geschäfte", "geschäft")
    add("Wochenende", "hafta sonu", "das", "Wochenenden", "wochenende")
    add("Innenstadt", "şehir merkezi", "die", "Innenstädte", "innenstadt")
    add("Sportverein", "spor kulübü", "der", "Sportvereine", "sportverein")
    add("Kontakt", "iletişim / bağlantı", "der", "Kontakte", "kontakte")
    add("Infusion", "serum / infüzyon", "die", "Infusionen", "infusion")
    add("Arztbrief", "doktor raporu / taburcu mektubu", "der", "Arztbriefe", "arztbrief")
    add("Nasenspray", "burun spreyi", "das", "Nasensprays", "nasenspray")
    add("Beipackzettel", "ilaç prospektüsü", "der", "Beipackzettel", "beipackzettel")
    add("Geräusch", "ses", "das", "Geräusche", "geräusch")
    add("Defekt", "arıza", "der", "Defekte", "defekt")
    add("Ölstand", "yağ seviyesi", "der", "Ölstände", "ölstand")
    add("Teil", "parça", "das", "Teile", "teile", "teil")
    add("Kosten", "masraf / maliyet", "die", "—", "kosten")
    add("Heimweg", "eve dönüş yolu", "der", "Heimwege", "heimweg")
    add("Bildschirm", "ekran", "der", "Bildschirme", "bildschirm")
    add("Schlaf", "uyku", "der", "—", "schlaf")
    add("Smartphone", "akıllı telefon", "das", "Smartphones", "smartphone")
    add("Technik", "teknoloji", "die", "Techniken", "technik")
    add("Gespräch", "konuşma", "das", "Gespräche", "gespräche", "gespräch")
    add("Bewegung", "hareket", "die", "Bewegungen", "bewegung")
    add("Erholung", "dinlenme", "die", "—", "erholung")
    add("Licht", "ışık", "das", "Lichter", "licht")
    add("Zahn", "diş", "der", "Zähne", "zähne")
    add("Gesicht", "yüz", "das", "Gesichter", "gesicht")
    add("Haar", "saç", "das", "Haare", "haare")
    add("Creme", "krem", "die", "Cremes", "creme")
    add("Regal", "raf", "das", "Regale", "regal")
    add("Korb", "sepet", "der", "Körbe", "korb", "körbe")
    add("Zimmer", "oda", "das", "Zimmer", "zimmer", "zimmern")
    add("Kleidung", "kıyafet", "die", "—", "kleidung")
    add("Maschine", "makine", "die", "Maschinen", "maschine")
    add("Stunde", "saat", "die", "Stunden", "stunde", "stunden")
    add("Mülleimer", "çöp kutusu", "der", "Mülleimer", "mülleimer")
    add("Fenster", "pencere", "das", "Fenster", "fenster")
    add("Familie", "aile", "die", "Familien", "familie")
    add("Freitag", "cuma", "der", "Freitage", "freitag")
    add("Samstag", "cumartesi", "der", "Samstage", "samstag")
    add("Sonntag", "pazar", "der", "Sonntage", "sonntag")
    add("Freund", "arkadaş", "der", "Freunde", "freunde", "freund")
    add("Kind", "çocuk", "das", "Kinder", "kinder", "kind")
    add("Pizza", "pizza", "die", "Pizzen", "pizza")
    add("Plan", "plan", "der", "Pläne", "plänen", "plan")
    add("Tasse", "fincan", "die", "Tassen", "tasse")
    add("Zutat", "malzeme", "die", "Zutaten", "zutaten")
    add("Küchenwaage", "mutfak terazisi", "die", "Küchenwaagen", "küchenwaage")
    add("Puderzucker", "pudra şekeri", "der", "—", "puderzucker")
    add("Freundin", "kadın arkadaş", "die", "Freundinnen", "freundin")
    add("Cappuccino", "kapuçino", "der", "Cappuccinos", "cappuccino")
    add("Urlaub", "tatil", "der", "Urlaube", "urlaub")
    add("Konditorei", "pastane", "die", "Konditoreien", "konditorei")
    add("Innenstadt", "şehir merkezi", "die", "Innenstädte", "innenstadt")
    add("Gebäckstück", "hamur işi / pasta parçası", "das", "Gebäckstücke", "gebäckstücke")
    add("Geburtstagstorte", "doğum günü pastası", "die", "Geburtstagstorten", "geburtstagstorte")
    add("Schwester", "kız kardeş", "die", "Schwestern", "schwester")
    add("Schokoladentorte", "çikolatalı pasta", "die", "Schokoladentorten", "schokoladentorte")
    add("Anzahlung", "kapora / ön ödeme", "die", "Anzahlungen", "anzahlung")
    add("Imbiss", "büfe / ayaküstü yemek yeri", "der", "Imbisse", "imbiss")
    add("Hunger", "açlık", "der", "—", "hunger")
    add("Hähnchensandwich", "tavuklu sandviç", "das", "Hähnchensandwiches", "hähnchensandwich")
    add("Knoblauchsoße", "sarımsaklı sos", "die", "Knoblauchsoßen", "knoblauchsoße")
    add("Nummer", "numara", "die", "Nummern", "nummer")
    add("Picknick", "piknik", "das", "Picknicks", "picknick")
    add("Park", "park", "der", "Parks", "park")
    add("Sandwich", "sandviç", "das", "Sandwiches", "sandwiches", "sandwich")
    add("Getränk", "içecek", "das", "Getränke", "getränke", "getränk")
    add("Tasche", "çanta", "die", "Taschen", "taschen")
    add("Baum", "ağaç", "der", "Bäume", "baum")
    add("Ball", "top", "der", "Bälle", "ball")
    add("Erwachsene", "yetişkin", "der/die", "Erwachsene", "erwachsenen")
    add("Limonade", "limonata / gazlı içecek", "die", "Limonaden", "limonade")
    add("Herbst", "sonbahar", "der", "Herbste", "herbst")
    add("Modell", "model", "das", "Modelle", "modelle", "modell")
    add("Ärmel", "kol (giysi)", "der", "Ärmel", "ärmel")
    add("Lager", "depo", "das", "Lager", "lager")
    add("Schuh", "ayakkabı", "der", "Schuhe", "schuhe", "schuh")
    add("Schritt", "adım", "der", "Schritte", "schritte")
    add("Kassenbon", "kasa fişi", "der", "Kassenbons", "kassenbon")
    add("Pflegeprodukt", "bakım ürünü", "das", "Pflegeprodukte", "pflegeprodukte")
    add("Packungsgröße", "paket boyutu", "die", "Packungsgrößen", "packungsgröße")
    add("Stofftasche", "bez çanta", "die", "Stofftaschen", "stofftasche")
    add("Möbelhaus", "mobilya mağazası", "das", "Möbelhäuser", "möbelhaus")
    add("Holztisch", "ahşap masa", "der", "Holztische", "holztisch")
    add("Länge", "uzunluk", "die", "Längen", "länge")
    add("Mitarbeiter", "çalışan", "der", "Mitarbeiter", "mitarbeiter")
    add("Information", "bilgi / danışma", "die", "Informationen", "information", "informationen")
    add("Elektronikmarkt", "elektronik mağazası", "der", "Elektronikmärkte", "elektronikmarkt")
    add("Unterschied", "fark", "der", "Unterschiede", "unterschiede")
    add("Klang", "ses kalitesi", "der", "Klänge", "klang")
    add("Verbindung", "bağlantı", "die", "Verbindungen", "verbindung")
    add("Akkulaufzeit", "pil süresi", "die", "Akkulaufzeiten", "akkulaufzeit")
    add("Laptop", "dizüstü bilgisayar", "der", "Laptops", "laptop")
    add("Verpackung", "ambalaj", "die", "Verpackungen", "verpackung")
    add("Internet", "internet", "das", "—", "internet")
    add("Maß", "ölçü", "das", "Maße", "maße")
    add("Bewertung", "değerlendirme / yorum", "die", "Bewertungen", "bewertungen")
    add("Lieferung", "teslimat", "die", "Lieferungen", "lieferung")
    add("Versandbestätigung", "kargo gönderim onayı", "die", "Versandbestätigungen", "versandbestätigung")
    add("E-Mail", "e-posta", "die", "E-Mails", "e-mail")
    add("Paketbote", "kargo görevlisi", "der", "Paketboten", "paketbote")
    add("Tür", "kapı", "die", "Türen", "tür")
    add("Empfang", "teslim alma / resepsiyon", "der", "Empfänge", "empfang")
    add("Anleitung", "talimat / kullanım kılavuzu", "die", "Anleitungen", "anleitung")
    add("Praxis", "muayenehane", "die", "Praxen", "praxis")
    add("Formular", "form", "das", "Formulare", "formular")
    add("Assistentin", "kadın asistan", "die", "Assistentinnen", "assistentin")
    add("Loch", "delik / çürük", "das", "Löcher", "loch")
    add("Betäubung", "uyuşturma / anestezi", "die", "Betäubungen", "betäubung")
    add("Behandlung", "tedavi", "die", "Behandlungen", "behandlung")
    add("Kontrolle", "kontrol", "die", "Kontrollen", "kontrolle")
    add("Knieverletzung", "diz yaralanması", "die", "Knieverletzungen", "knieverletzung")
    add("Physiotherapie", "fizik tedavi", "die", "Physiotherapien", "physiotherapie")
    add("Kraft", "güç", "die", "Kräfte", "kraft")
    add("Beweglichkeit", "hareket kabiliyeti / esneklik", "die", "—", "beweglichkeit")
    add("Bein", "bacak", "das", "Beine", "bein")
    add("Haltung", "duruş", "die", "Haltungen", "haltung")
    add("Woche", "hafta", "die", "Wochen", "woche", "wochen")
    add("Ferne", "uzak", "die", "—", "ferne")
    add("Mitarbeiterin", "kadın çalışan", "die", "Mitarbeiterinnen", "mitarbeiterin")
    add("Buchstabe", "harf", "der", "Buchstaben", "buchstaben")
    add("Tafel", "tahta / pano", "die", "Tafeln", "tafel")
    add("Auge", "göz", "das", "Augen", "augen")
    add("Gerät", "cihaz", "das", "Geräte", "gerät", "geräten")
    add("Autofahren", "araba kullanma", "das", "—", "autofahren")
    add("Wert", "değer", "der", "Werte", "werte")
    add("Glas", "cam / gözlük camı", "das", "Gläser", "gläser")
    add("Zettel", "kâğıt / not", "der", "Zettel", "zettel")
    add("Optiker", "optikçi", "der", "Optiker", "optiker")
    add("Sturz", "düşme", "der", "Stürze", "sturz")
    add("Schmerz", "ağrı", "der", "Schmerzen", "schmerzen")
    add("Fuß", "ayak", "der", "Füße", "fuß")
    add("Unfall", "kaza", "der", "Unfälle", "unfall")
    add("Versichertenkarte", "sağlık sigortası kartı", "die", "Versichertenkarten", "versichertenkarte")
    add("Knochen", "kemik", "der", "Knochen", "knochen")
    add("Sohn", "oğul", "der", "Söhne", "sohn")
    add("Rezeption", "resepsiyon", "die", "Rezeptionen", "rezeption")
    add("Junge", "erkek çocuk", "der", "Jungen", "junge")
    add("Schoß", "kucak", "der", "Schöße", "schoß")
    add("Lunge", "akciğer", "die", "Lungen", "lunge")
    add("Infekt", "enfeksiyon", "der", "Infekte", "infekt")
    add("Zug", "tren", "der", "Züge", "zug")
    add("München", "Münih", "—", "—", "münchen")
    add("Durchsage", "anons", "die", "Durchsagen", "durchsage")
    add("Sitzplatz", "oturma yeri / koltuk", "der", "Sitzplätze", "sitzplatz")
    add("Zugbegleiterin", "kadın tren görevlisi", "die", "Zugbegleiterinnen", "zugbegleiterin")
    add("Anzeige", "ekran / gösterge", "die", "Anzeigen", "anzeige")
    add("Verkehr", "trafik", "der", "—", "verkehr")
    add("Haltestelle", "durak", "die", "Haltestellen", "haltestellen")
    add("Stopknopf", "durak düğmesi", "der", "Stopknöpfe", "stopknopf")
    add("Bürogebäude", "ofis binası", "das", "Bürogebäude", "bürogebäude")
    add("Meter", "metre", "der", "Meter", "meter")
    add("Sommerferien", "yaz tatili", "die", "—", "sommerferien")
    add("Spanien", "İspanya", "—", "—", "spanien")
    add("Schalter", "gişe", "der", "Schalter", "schalter")
    add("Pass", "pasaport", "der", "Pässe", "pässe")
    add("Sicherheitskontrolle", "güvenlik kontrolü", "die", "Sicherheitskontrollen", "sicherheitskontrolle")
    add("Wartebereich", "bekleme alanı", "der", "Wartebereiche", "wartebereich")
    add("Flug", "uçuş", "der", "Flüge", "fluges", "flug")
    add("Ausgang", "çıkış / kapı", "der", "Ausgänge", "ausgang")
    add("Abflug", "kalkış", "der", "Abflüge", "abflug")
    add("Tankdeckel", "yakıt deposu kapağı", "der", "Tankdeckel", "tankdeckel")
    add("Liter", "litre", "der", "Liter", "liter")
    add("Zapfpistole", "yakıt tabancası", "die", "Zapfpistolen", "zapfpistole")
    add("Shop", "mağaza", "der", "Shops", "shop")
    add("Reifendruck", "lastik basıncı", "der", "—", "reifendruck")
    add("Fahrradwerkstatt", "bisiklet tamircisi", "die", "Fahrradwerkstätten", "fahrradwerkstatt")
    add("Kette", "zincir", "die", "Ketten", "kette")
    add("Parkhaus", "kapalı otopark", "das", "Parkhäuser", "parkhaus")
    add("Nähe", "yakın", "die", "—", "nähe")
    add("Einkaufszentrum", "alışveriş merkezi", "das", "Einkaufszentren", "einkaufszentrums")
    add("Einfahrt", "giriş yolu", "die", "Einfahrten", "einfahrt")
    add("Stock", "kat", "der", "Stockwerke", "stock")
    add("Bereich", "bölüm / alan", "der", "Bereiche", "bereichs")
    add("Kassenautomat", "ödeme otomatı", "der", "Kassenautomaten", "kassenautomaten")
    add("Ausfahrt", "çıkış", "die", "Ausfahrten", "ausfahrt")
    add("Arbeitstag", "iş günü", "der", "Arbeitstage", "arbeitstag")
    add("E-Mail", "e-posta", "die", "E-Mails", "e-mails")
    add("Anfrage", "talep / soru", "die", "Anfragen", "anfragen")
    add("Problem", "sorun", "das", "Probleme", "problem")
    add("Rückmeldung", "geri dönüş", "die", "Rückmeldungen", "rückmeldung")
    add("Systemadministrator", "sistem yöneticisi", "der", "Systemadministratoren", "systemadministrator")
    add("Unternehmen", "şirket / işletme", "das", "Unternehmen", "unternehmen")
    add("System", "sistem", "das", "Systeme", "systeme")
    add("Netzwerk", "ağ", "das", "Netzwerke", "netzwerke")
    add("Team", "ekip", "das", "Teams", "team")
    add("Arbeitsalltag", "iş rutini", "der", "—", "arbeitsalltag")
    add("Wartenummer", "sıra numarası", "die", "Wartenummern", "wartenummer")
    add("Inhalt", "içerik", "der", "Inhalte", "inhalt")
    add("Ziel", "hedef / varış yeri", "das", "Ziele", "ziel")
    add("Versand", "gönderim", "der", "—", "versand")
    add("Sendungsverfolgung", "kargo takibi", "die", "—", "sendungsverfolgung")
    add("Etikett", "etiket", "das", "Etiketten", "etikett")
    add("Versandkosten", "kargo ücreti", "die", "—", "versandkosten")
    add("Beleg", "fiş / belge", "der", "Belege", "beleg")
    add("Bank", "banka", "die", "Banken", "bank")
    add("Gebühr", "ücret", "die", "Gebühren", "gebühren")
    add("Online-Banking", "internet bankacılığı", "das", "—", "online-banking")
    add("Überweisung", "havale", "die", "Überweisungen", "überweisungen")
    add("Ausland", "yurt dışı", "das", "—", "ausland")
    add("Sicherheitsregel", "güvenlik kuralı", "die", "Sicherheitsregeln", "sicherheitsregeln")
    add("App", "uygulama", "die", "Apps", "app")
    add("Konto", "hesap", "das", "Konten", "konto")
    add("Bedingung", "koşul", "die", "Bedingungen", "bedingungen")
    add("Adresse", "adres", "die", "Adressen", "adresse")
    add("Rathaus", "belediye", "das", "Rathäuser", "rathaus")
    add("Anmeldung", "kayıt", "die", "Anmeldungen", "anmeldung")
    add("Wohnungsgeberbestätigung", "ev sahibi ikamet belgesi", "die", "Wohnungsgeberbestätigungen", "wohnungsgeberbestätigung")
    add("Bestätigung", "onay", "die", "Bestätigungen", "bestätigung")
    add("Öffnungszeit", "çalışma saati", "die", "Öffnungszeiten", "öffnungszeiten")
    add("Bürgerbüro", "vatandaşlık işleri ofisi", "das", "Bürgerbüros", "bürgerbüros")
    add("Mathematik", "matematik", "die", "—", "mathematik")
    add("Klasse", "sınıf", "die", "Klassen", "klasse")
    add("Übung", "alıştırma", "die", "Übungen", "übung")
    add("Schüler", "öğrenci", "der", "Schüler", "schüler", "schülerinnen")
    add("Deutsch", "Almanca", "das", "—", "deutsch")
    add("Projekt", "proje", "das", "Projekte", "projekt")
    add("Schultag", "okul günü", "der", "Schultage", "schultages")
    add("Lehrerin", "kadın öğretmen", "die", "Lehrerinnen", "lehrerin")
    add("Donnerstag", "perşembe", "der", "Donnerstage", "donnerstag")
    add("Klassenlehrerin", "sınıf öğretmeni", "die", "Klassenlehrerinnen", "klassenlehrerin")
    add("Eltern", "ebeveynler", "die", "—", "eltern")
    add("Stundenplan", "ders programı", "der", "Stundenpläne", "stundenplan")
    add("Klassenarbeit", "yazılı sınav", "die", "Klassenarbeiten", "klassenarbeiten")
    add("Regel", "kural", "die", "Regeln", "regeln")
    add("Fehlzeit", "devamsızlık", "die", "Fehlzeiten", "fehlzeiten")
    add("Entschuldigung", "mazeret yazısı", "die", "Entschuldigungen", "entschuldigungen")
    add("Zimmerkarte", "oda kartı", "die", "Zimmerkarten", "zimmerkarte")
    add("Aufzug", "asansör", "der", "Aufzüge", "aufzug")
    add("Koffer", "valiz", "der", "Koffer", "koffer")
    add("Eingang", "giriş", "der", "Eingänge", "eingang")
    add("Ticket", "bilet", "das", "Tickets", "tickets")
    add("Erwachsener", "yetişkin", "der", "Erwachsene", "erwachsene")
    add("Umkleide", "soyunma odası", "die", "Umkleiden", "umkleide")
    add("Badesache", "yüzme kıyafeti", "die", "Badesachen", "badesachen")
    add("Bahn", "kulvar", "die", "Bahnen", "bahnen")
    add("Training", "antrenman", "das", "Trainings", "training")
    add("Rücken", "sırt", "der", "—", "rücken")
    add("Bein", "bacak", "das", "Beine", "beine")
    add("Schulter", "omuz", "die", "Schultern", "schultern")
    add("Gewicht", "ağırlık", "das", "Gewichte", "gewichten", "gewicht")
    add("Trainerin", "kadın antrenör", "die", "Trainerinnen", "trainerin")
    add("Stadtbibliothek", "şehir kütüphanesi", "die", "Stadtbibliotheken", "stadtbibliothek")
    add("Referat", "sunum / ödev", "das", "Referate", "referat")
    add("Tier", "hayvan", "das", "Tiere", "tiere")
    add("Computer", "bilgisayar", "der", "Computer", "computer")
    add("Stichwort", "anahtar kelime", "das", "Stichwörter", "stichwort")
    add("Suche", "arama", "die", "Suchen", "suche")
    add("Roman", "roman", "der", "Romane", "roman")
    add("Automat", "otomat", "der", "Automaten", "automaten")
    add("Rückgabedatum", "iade tarihi", "das", "Rückgabedaten", "rückgabedatum")
    add("Notiz", "not", "die", "Notizen", "notizen")
    add("Freitagabend", "cuma akşamı", "der", "Freitagabende", "freitagabend")
    add("QR-Code", "QR kodu", "der", "QR-Codes", "qr-code")
    add("Saal", "salon", "der", "Säle", "saal")
    add("Werbung", "reklam", "die", "Werbungen", "werbungen")
    add("Trailer", "fragman", "der", "Trailer", "trailer")
    add("Ende", "son", "das", "Enden", "ende")
    add("Lieblingsszene", "favori sahne", "die", "Lieblingsszenen", "lieblingsszene")
    add("Tagesausflug", "günübirlik gezi", "der", "Tagesausflüge", "tagesausflug")
    add("Schwarzwald", "Kara Orman", "der", "—", "schwarzwald")
    add("Vorabend", "önceki akşam", "der", "Vorabende", "vorabend")
    add("Wetter", "hava durumu", "das", "—", "wetter")
    add("Rucksack", "sırt çantası", "der", "Rucksäcke", "rucksäcke")
    add("Waldrand", "orman kenarı", "der", "Waldränder", "waldrand")
    add("Foto", "fotoğraf", "das", "Fotos", "fotos")
    add("Aussichtspunkt", "seyir noktası", "der", "Aussichtspunkte", "aussichtspunkt")
    add("Ort", "yer / kasaba", "der", "Orte", "ort")
}

internal val supplementalSurfaceMeanings: Map<String, Triple<String, String, String>> = mapOf(
    "seit" to Triple("seit", "-den beri", "Edat"), "vor" to Triple("vor", "önce / önünde", "Edat"),
    "nach" to Triple("nach", "sonra / -e doğru", "Edat"), "gegen" to Triple("gegen", "karşı / civarında", "Edat"),
    "zwischen" to Triple("zwischen", "arasında", "Edat"), "ohne" to Triple("ohne", "-sız / olmadan", "Edat"),
    "wegen" to Triple("wegen", "nedeniyle", "Edat"), "bis" to Triple("bis", "-e kadar", "Edat"),
    "durch" to Triple("durch", "içinden / aracılığıyla", "Edat"), "per" to Triple("per", "ile / yoluyla", "Edat"),
    "als" to Triple("als", "olarak / -dığında", "Bağlaç"), "obwohl" to Triple("obwohl", "-mesine rağmen", "Bağlaç"),
    "denn" to Triple("denn", "çünkü", "Bağlaç"), "sondern" to Triple("sondern", "aksine", "Bağlaç"),
    "oder" to Triple("oder", "veya", "Bağlaç"), "damit" to Triple("damit", "bununla / böylece", "Bağlaç"),
    "wer" to Triple("wer", "kim", "Zamir"), "was" to Triple("was", "ne", "Zamir"), "welche" to Triple("welche", "hangi", "Zamir"),
    "welcher" to Triple("welcher", "hangi", "Zamir"), "welches" to Triple("welches", "hangi", "Zamir"),
    "ihnen" to Triple("ihnen", "onlara / size", "Zamir"), "ihn" to Triple("ihn", "onu", "Zamir"),
    "ihres" to Triple("ihres", "onun", "Belirleyici"), "ihrem" to Triple("ihrem", "onun", "Belirleyici"), "ihrer" to Triple("ihrer", "onun", "Belirleyici"),
    "seiner" to Triple("seiner", "onun", "Belirleyici"), "seinem" to Triple("seinem", "onun", "Belirleyici"),
    "sein" to Triple("sein", "onun", "Belirleyici"), "ihr" to Triple("ihr", "onun", "Belirleyici"),
    "dieses" to Triple("dieses", "bu", "Belirleyici"), "diesem" to Triple("diesem", "bu", "Belirleyici"), "dieser" to Triple("dieser", "bu", "Belirleyici"),
    "dieser" to Triple("dieser", "bu", "Belirleyici"), "diese" to Triple("diese", "bu / bunlar", "Belirleyici"),
    "jeden" to Triple("jeden", "her", "Belirleyici"), "jede" to Triple("jede", "her", "Belirleyici"), "jeder" to Triple("jeder", "her", "Belirleyici"),
    "beide" to Triple("beide", "her ikisi", "Belirleyici"), "beiden" to Triple("beiden", "her ikisine / her ikisinin", "Belirleyici"),
    "mehrere" to Triple("mehrere", "birkaç / birden fazla", "Belirleyici"), "manche" to Triple("manche", "bazı", "Belirleyici"),
    "anderen" to Triple("ander", "diğer", "Sıfat"), "anderer" to Triple("ander", "diğer", "Sıfat"), "andere" to Triple("ander", "diğer", "Sıfat"),
    "neue" to Triple("neu", "yeni", "Sıfat"), "neuen" to Triple("neu", "yeni", "Sıfat"), "neues" to Triple("neu", "yeni", "Sıfat"),
    "kleine" to Triple("klein", "küçük", "Sıfat"), "kleinen" to Triple("klein", "küçük", "Sıfat"), "kleiner" to Triple("klein", "daha küçük", "Sıfat"),
    "große" to Triple("groß", "büyük", "Sıfat"), "großen" to Triple("groß", "büyük", "Sıfat"), "großer" to Triple("groß", "büyük", "Sıfat"),
    "starke" to Triple("stark", "şiddetli / güçlü", "Sıfat"), "starken" to Triple("stark", "şiddetli / güçlü", "Sıfat"),
    "kurze" to Triple("kurz", "kısa", "Sıfat"), "kurzen" to Triple("kurz", "kısa", "Sıfat"),
    "frische" to Triple("frisch", "taze", "Sıfat"), "frisches" to Triple("frisch", "taze", "Sıfat"), "frisch" to Triple("frisch", "taze", "Sıfat"),
    "reife" to Triple("reif", "olgun", "Sıfat"), "schmutzige" to Triple("schmutzig", "kirli", "Sıfat"),
    "helle" to Triple("hell", "açık renkli", "Sıfat"), "dunkle" to Triple("dunkel", "koyu renkli", "Sıfat"),
    "nasse" to Triple("nass", "ıslak", "Sıfat"), "nassen" to Triple("nass", "ıslak", "Sıfat"), "trockenen" to Triple("trocken", "kuru", "Sıfat"),
    "schweren" to Triple("schwer", "ağır", "Sıfat"), "schwer" to Triple("schwer", "ağır / zor", "Sıfat"), "leicht" to Triple("leicht", "hafif / kolay", "Sıfat"),
    "leichter" to Triple("leicht", "hafif", "Sıfat"), "bequem" to Triple("bequem", "rahat", "Sıfat"), "bequeme" to Triple("bequem", "rahat", "Sıfat"),
    "eng" to Triple("eng", "dar", "Sıfat"), "lang" to Triple("lang", "uzun", "Sıfat"), "längere" to Triple("lang", "daha uzun", "Sıfat"),
    "schön" to Triple("schön", "güzel", "Sıfat"), "schönen" to Triple("schön", "güzel", "Sıfat"),
    "lecker" to Triple("lecker", "lezzetli", "Sıfat"), "salzig" to Triple("salzig", "tuzlu", "Sıfat"),
    "sauber" to Triple("sauber", "temiz", "Sıfat"), "ordentlich" to Triple("ordentlich", "düzenli", "Sıfat"),
    "müde" to Triple("müde", "yorgun", "Sıfat"), "zufrieden" to Triple("zufrieden", "memnun", "Sıfat"),
    "allein" to Triple("allein", "yalnız", "Sıfat"), "geeignetes" to Triple("geeignet", "uygun", "Sıfat"),
    "möglichen" to Triple("möglich", "olası", "Sıfat"), "ungewöhnliches" to Triple("ungewöhnlich", "alışılmadık", "Sıfat"),
    "verschiedene" to Triple("verschieden", "farklı", "Sıfat"), "verschiedenen" to Triple("verschieden", "farklı", "Sıfat"),
    "ähnliches" to Triple("ähnlich", "benzer", "Sıfat"), "günstiger" to Triple("günstig", "daha uygun fiyatlı", "Sıfat"),
    "besser" to Triple("gut", "daha iyi", "Sıfat"), "schlechter" to Triple("schlecht", "daha kötü", "Sıfat"), "schlecht" to Triple("schlecht", "kötü", "Sıfat"),
    "richtig" to Triple("richtig", "doğru", "Sıfat"), "richtige" to Triple("richtig", "doğru", "Sıfat"), "hintere" to Triple("hinter", "arka", "Sıfat"),
    "abgenutzt" to Triple("abgenutzt", "aşınmış", "Sıfat"), "markierten" to Triple("markiert", "işaretlenmiş", "Sıfat"),
    "schriftliche" to Triple("schriftlich", "yazılı", "Sıfat"), "digitalen" to Triple("digital", "dijital", "Sıfat"),
    "geplante" to Triple("geplant", "planlanmış", "Sıfat"), "flache" to Triple("flach", "sığ", "Sıfat"),
    "gewünschte" to Triple("gewünscht", "istenen", "Sıfat"), "freie" to Triple("frei", "boş / serbest", "Sıfat"), "freien" to Triple("frei", "boş / serbest", "Sıfat"),
    "langsam" to Triple("langsam", "yavaşça", "Zarf"), "schnell" to Triple("schnell", "hızlıca", "Zarf"),
    "direkt" to Triple("direkt", "doğrudan", "Zarf"), "gemeinsam" to Triple("gemeinsam", "birlikte", "Zarf"),
    "getrennt" to Triple("getrennt", "ayrı ayrı", "Zarf"), "vorsichtig" to Triple("vorsichtig", "dikkatlice", "Zarf"),
    "sorgfältig" to Triple("sorgfältig", "özenle / dikkatlice", "Zarf"), "wahrscheinlich" to Triple("wahrscheinlich", "muhtemelen", "Zarf"),
    "ungefähr" to Triple("ungefähr", "yaklaşık", "Zarf"), "besonders" to Triple("besonders", "özellikle", "Zarf"),
    "häufig" to Triple("häufig", "sık sık", "Zarf"), "manchmal" to Triple("manchmal", "bazen", "Zarf"), "oft" to Triple("oft", "sık sık", "Zarf"),
    "fast" to Triple("fast", "neredeyse", "Zarf"), "nur" to Triple("nur", "sadece", "Zarf"), "mehr" to Triple("mehr", "daha fazla", "Zarf"),
    "weniger" to Triple("weniger", "daha az", "Zarf"), "vorne" to Triple("vorne", "önde", "Zarf"), "draußen" to Triple("draußen", "dışarıda", "Zarf"),
    "oben" to Triple("oben", "yukarıda", "Zarf"), "unten" to Triple("unten", "aşağıda", "Zarf"), "dort" to Triple("dort", "orada", "Zarf"),
    "dabei" to Triple("dabei", "bu sırada / bunun yanında", "Zarf"), "deshalb" to Triple("deshalb", "bu yüzden", "Zarf"),
    "außerdem" to Triple("außerdem", "ayrıca", "Zarf"), "plötzlich" to Triple("plötzlich", "aniden", "Zarf"),
    "schließlich" to Triple("schließlich", "sonunda", "Zarf"), "anschließend" to Triple("anschließend", "ardından", "Zarf"),
    "zunächst" to Triple("zunächst", "öncelikle", "Zarf"), "bald" to Triple("bald", "yakında", "Zarf"),
    "dort" to Triple("dort", "orada", "Zarf"), "dazu" to Triple("dazu", "buna ek olarak / yanında", "Zarf"),
    "damit" to Triple("damit", "bununla", "Zarf"), "weiter" to Triple("weiter", "devam / daha ileri", "Zarf"),
    "zurück" to Triple("zurück", "geri", "Zarf"), "hinein" to Triple("hinein", "içeri", "Zarf"), "heraus" to Triple("heraus", "dışarı", "Zarf"),
    "weg" to Triple("weg", "uzak / bir kenara", "Parçacık"), "teil" to Triple("teil", "parça / teilnehmen yapısında ön ek", "Parçacık"),
    "fort" to Triple("fort", "devam", "Parçacık"), "hin" to Triple("hin", "oraya / hinsetzen yapısında ön ek", "Parçacık"),
    "bar" to Triple("bar", "nakit", "Zarf"), "online" to Triple("online", "çevrimiçi", "Zarf"),
    "pro" to Triple("pro", "başına", "Edat"), "mal" to Triple("mal", "kez / bir kere", "Parçacık"),
    "ungewöhnlich" to Triple("ungewöhnlich", "alışılmadık", "Sıfat"), "genügend" to Triple("genügend", "yeterli", "Zarf"),
    "bewusst" to Triple("bewusst", "bilinçli olarak", "Zarf"), "ständig" to Triple("ständig", "sürekli", "Zarf"),
    "regelmäßig" to Triple("regelmäßig", "düzenli olarak", "Zarf"), "deutlich" to Triple("deutlich", "belirgin şekilde", "Zarf"),
    "häufig" to Triple("häufig", "sık sık", "Zarf"), "vorher" to Triple("vorher", "önceden", "Zarf"),
    "statt" to Triple("statt", "yerine", "Edat"), "echt" to Triple("echt", "gerçek", "Sıfat"),
    "früh" to Triple("früh", "erken", "Zarf"), "spät" to Triple("spät", "geç", "Zarf"),
    "wenige" to Triple("wenig", "az sayıda", "Belirleyici"), "wenigen" to Triple("wenig", "az sayıda", "Belirleyici"),
    "halb" to Triple("halb", "yarım", "Sıfat"), "direkt" to Triple("direkt", "doğrudan", "Zarf"),
    "zwei" to Triple("zwei", "iki", "Sayı"), "drei" to Triple("drei", "üç", "Sayı"), "vier" to Triple("vier", "dört", "Sayı"),
    "fünf" to Triple("fünf", "beş", "Sayı"), "sechs" to Triple("sechs", "altı", "Sayı"), "sieben" to Triple("sieben", "yedi", "Sayı"),
    "acht" to Triple("acht", "sekiz", "Sayı"), "zehn" to Triple("zehn", "on", "Sayı"), "zwanzig" to Triple("zwanzig", "yirmi", "Sayı"),
    "vierzig" to Triple("vierzig", "kırk", "Sayı"), "ersten" to Triple("erste", "ilk", "Sıfat"), "erste" to Triple("erste", "ilk", "Sıfat"),
    "zweite" to Triple("zweite", "ikinci", "Sıfat"), "dritte" to Triple("dritte", "üçüncü", "Sıfat"), "vierten" to Triple("vierte", "dördüncü", "Sıfat")
)
