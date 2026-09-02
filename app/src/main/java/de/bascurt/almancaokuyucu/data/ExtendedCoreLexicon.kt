package de.bascurt.almancaokuyucu.data

internal val coreStorySurfaceMeanings: Map<String, Triple<String, String, String>> = mapOf(
    "der" to Triple("der", "belirli artikel", "Artikel"), "die" to Triple("die", "belirli artikel", "Artikel"),
    "das" to Triple("das", "belirli artikel", "Artikel"), "den" to Triple("den", "belirli artikel (Akkusativ)", "Artikel"),
    "dem" to Triple("dem", "belirli artikel (Dativ)", "Artikel"), "des" to Triple("des", "belirli artikel (Genitiv)", "Artikel"),
    "ein" to Triple("ein", "bir", "Artikel"), "eine" to Triple("eine", "bir", "Artikel"), "einen" to Triple("einen", "bir", "Artikel"),
    "einem" to Triple("einem", "bir", "Artikel"), "einer" to Triple("einer", "bir", "Artikel"),
    "ich" to Triple("ich", "ben", "Zamir"), "du" to Triple("du", "sen", "Zamir"), "er" to Triple("er", "o (erkek)", "Zamir"),
    "sie" to Triple("sie", "o (kadın) / onlar", "Zamir"), "es" to Triple("es", "o", "Zamir"), "wir" to Triple("wir", "biz", "Zamir"),
    "ihr" to Triple("ihr", "siz / ona / onun", "Zamir"), "ihm" to Triple("ihm", "ona", "Zamir"),
    "ihre" to Triple("ihre", "onun", "Belirleyici"), "ihren" to Triple("ihren", "onun", "Belirleyici"),
    "seine" to Triple("seine", "onun", "Belirleyici"), "seinen" to Triple("seinen", "onun", "Belirleyici"),
    "sich" to Triple("sich", "kendini / kendisine", "Zamir"), "alle" to Triple("alle", "hepsi", "Belirleyici"),
    "und" to Triple("und", "ve", "Bağlaç"), "aber" to Triple("aber", "ama", "Bağlaç"), "oder" to Triple("oder", "veya", "Bağlaç"),
    "weil" to Triple("weil", "çünkü", "Bağlaç"), "dass" to Triple("dass", "-dığı / ki", "Bağlaç"),
    "wenn" to Triple("wenn", "eğer / -dığında", "Bağlaç"), "bevor" to Triple("bevor", "-meden önce", "Bağlaç"),
    "während" to Triple("während", "-iken / sırasında", "Bağlaç"), "ob" to Triple("ob", "olup olmadığını", "Bağlaç"),
    "im" to Triple("im", "-de / içinde", "Edat"), "ins" to Triple("ins", "içine / -e", "Edat"), "in" to Triple("in", "-de / içine", "Edat"),
    "am" to Triple("am", "-de / sırasında", "Edat"), "an" to Triple("an", "-de / yanında", "Edat"),
    "auf" to Triple("auf", "üzerinde / üzerine", "Edat"), "mit" to Triple("mit", "ile", "Edat"), "für" to Triple("für", "için", "Edat"),
    "nach" to Triple("nach", "sonra / -e doğru", "Edat"), "vor" to Triple("vor", "önce / önünde", "Edat"),
    "von" to Triple("von", "-den / tarafından", "Edat"), "zu" to Triple("zu", "-e / -a; bağlama göre kapalı", "Edat / Sıfat"),
    "zum" to Triple("zum", "-e / -a", "Edat"), "zur" to Triple("zur", "-e / -a", "Edat"), "aus" to Triple("aus", "-den / dışarı", "Edat"),
    "bei" to Triple("bei", "-de / yanında", "Edat"), "beim" to Triple("beim", "-de / sırasında", "Edat"),
    "über" to Triple("über", "hakkında / üzerinde", "Edat"), "unter" to Triple("unter", "altında", "Edat"),
    "neben" to Triple("neben", "yanında", "Edat"), "um" to Triple("um", "etrafında / saat belirtirken", "Edat"),
    "heute" to Triple("heute", "bugün", "Zarf"), "gestern" to Triple("gestern", "dün", "Zarf"),
    "morgen" to Triple("morgen", "yarın", "Zarf"), "morgens" to Triple("morgens", "sabahları", "Zarf"), "abends" to Triple("abends", "akşamları", "Zarf"),
    "nachts" to Triple("nachts", "geceleri / gece", "Zarf"), "dann" to Triple("dann", "sonra", "Zarf"),
    "danach" to Triple("danach", "ondan sonra", "Zarf"), "später" to Triple("später", "daha sonra", "Zarf"),
    "zuerst" to Triple("zuerst", "önce", "Zarf"), "sofort" to Triple("sofort", "hemen", "Zarf"),
    "noch" to Triple("noch", "hâlâ / daha", "Zarf"), "auch" to Triple("auch", "ayrıca / de", "Zarf"),
    "schon" to Triple("schon", "zaten / çoktan", "Zarf"), "wieder" to Triple("wieder", "tekrar", "Zarf"),
    "zusammen" to Triple("zusammen", "birlikte", "Zarf"), "sehr" to Triple("sehr", "çok", "Zarf"),
    "etwas" to Triple("etwas", "biraz / bir şey", "Zamir"), "viel" to Triple("viel", "çok", "Belirleyici"),
    "viele" to Triple("viele", "birçok", "Belirleyici"), "einige" to Triple("einige", "bazı", "Belirleyici"),
    "nicht" to Triple("nicht", "değil / -me", "Parçacık"), "kein" to Triple("kein", "hiç / yok", "Belirleyici"),
    "keine" to Triple("keine", "hiç / yok", "Belirleyici"), "keinen" to Triple("keinen", "hiç / yok", "Belirleyici"),
    "ist" to Triple("sein", "olmak", "Fiil"), "sind" to Triple("sein", "olmak", "Fiil"), "war" to Triple("sein", "idi", "Fiil"),
    "waren" to Triple("sein", "idiler / idi", "Fiil"), "hat" to Triple("haben", "sahip olmak / var", "Fiil"),
    "haben" to Triple("haben", "sahip olmak", "Fiil"), "wird" to Triple("werden", "olmak", "Fiil"), "werden" to Triple("werden", "olmak", "Fiil"),
    "kann" to Triple("können", "-ebilmek", "Fiil"), "kannte" to Triple("kennen", "tanıyordu / biliyordu", "Fiil"),
    "muss" to Triple("müssen", "zorunda olmak", "Fiil"), "müssen" to Triple("müssen", "zorunda olmak", "Fiil"),
    "soll" to Triple("sollen", "-meli / -malı", "Fiil"), "sollte" to Triple("sollen", "-meli / -malı", "Fiil"),
    "möchte" to Triple("möchten", "istemek", "Fiil"), "will" to Triple("wollen", "istemek", "Fiil"),
    "darf" to Triple("dürfen", "izinli olmak", "Fiil"), "dürfen" to Triple("dürfen", "izinli olmak", "Fiil"),
    "wie" to Triple("wie", "nasıl / ne kadar", "Zarf / Bağlaç"), "wo" to Triple("wo", "nerede", "Zarf"),
    "wann" to Triple("wann", "ne zaman", "Zarf"), "warum" to Triple("warum", "neden", "Zarf"),
    "was" to Triple("was", "ne", "Zamir"), "wer" to Triple("wer", "kim", "Zamir"),
    "hier" to Triple("hier", "burada", "Zarf"), "dort" to Triple("dort", "orada", "Zarf"),
    "jetzt" to Triple("jetzt", "şimdi", "Zarf"), "immer" to Triple("immer", "her zaman", "Zarf"),
    "oft" to Triple("oft", "sık sık", "Zarf"), "manchmal" to Triple("manchmal", "bazen", "Zarf"),
    "fast" to Triple("fast", "neredeyse", "Zarf"), "nur" to Triple("nur", "sadece", "Zarf"),
    "mehr" to Triple("mehr", "daha fazla", "Zarf"), "weniger" to Triple("weniger", "daha az", "Zarf"),
    "ganz" to Triple("ganz", "tamamen / oldukça", "Zarf"), "kurz" to Triple("kurz", "kısa / kısaca", "Sıfat / Zarf"),
    "lange" to Triple("lang", "uzun süre", "Zarf"), "direkt" to Triple("direkt", "doğrudan", "Zarf"),
    "gerne" to Triple("gerne", "memnuniyetle / severek", "Zarf"), "gleich" to Triple("gleich", "hemen / aynı", "Zarf"),
    "allem" to Triple("alle", "her şey / tümü", "Belirleyici"), "alles" to Triple("alles", "her şey", "Zamir"),
    "niemand" to Triple("niemand", "hiç kimse", "Zamir"), "jemand" to Triple("jemand", "birisi", "Zamir"),
    "etwa" to Triple("etwa", "yaklaşık", "Zarf"), "ungefähr" to Triple("ungefähr", "yaklaşık", "Zarf"),
    "einmal" to Triple("einmal", "bir kez", "Zarf"), "mehrmals" to Triple("mehrmals", "birkaç kez", "Zarf"),
    "weiter" to Triple("weiter", "devam / daha ileri", "Zarf"), "zurück" to Triple("zurück", "geri", "Zarf"),
    "hinein" to Triple("hinein", "içeri", "Zarf"), "heraus" to Triple("heraus", "dışarı", "Zarf"),
    "oben" to Triple("oben", "yukarıda", "Zarf"), "unten" to Triple("unten", "aşağıda", "Zarf"),
    "draußen" to Triple("draußen", "dışarıda", "Zarf"), "vorne" to Triple("vorne", "önde", "Zarf"),
    "dabei" to Triple("dabei", "bu sırada / bunun yanında", "Zarf"), "dazu" to Triple("dazu", "buna ek olarak / yanında", "Zarf"),
    "deshalb" to Triple("deshalb", "bu yüzden", "Zarf"), "außerdem" to Triple("außerdem", "ayrıca", "Zarf"),
    "plötzlich" to Triple("plötzlich", "aniden", "Zarf"), "schließlich" to Triple("schließlich", "sonunda", "Zarf"),
    "anschließend" to Triple("anschließend", "ardından", "Zarf"), "besonders" to Triple("besonders", "özellikle", "Zarf"),
    "wahrscheinlich" to Triple("wahrscheinlich", "muhtemelen", "Zarf"), "genug" to Triple("genug", "yeterince", "Zarf"),
    "genügend" to Triple("genügend", "yeterli", "Zarf"), "bewusst" to Triple("bewusst", "bilinçli olarak", "Zarf"),
    "ständig" to Triple("ständig", "sürekli", "Zarf"), "regelmäßig" to Triple("regelmäßig", "düzenli olarak", "Zarf"),
    "deutlich" to Triple("deutlich", "belirgin şekilde", "Zarf"), "häufig" to Triple("häufig", "sık sık", "Zarf"),
    "vorher" to Triple("vorher", "önceden", "Zarf"), "früh" to Triple("früh", "erken", "Zarf"), "spät" to Triple("spät", "geç", "Zarf"),
    "bar" to Triple("bar", "nakit", "Zarf"), "online" to Triple("online", "çevrimiçi", "Zarf"),
    "fertig" to Triple("fertig", "hazır / bitmiş", "Sıfat"), "kaputt" to Triple("kaputt", "bozuk", "Sıfat"),
    "frei" to Triple("frei", "boş / serbest", "Sıfat"), "voll" to Triple("voll", "dolu", "Sıfat"),
    "leer" to Triple("leer", "boş", "Sıfat"), "nah" to Triple("nah", "yakın", "Sıfat"),
    "weit" to Triple("weit", "uzak / geniş", "Sıfat"), "wichtig" to Triple("wichtig", "önemli", "Sıfat"),
    "wichtigen" to Triple("wichtig", "önemli", "Sıfat"), "benutzte" to Triple("benutzt", "kullanılmış", "Sıfat"),
    "benutzten" to Triple("benutzt", "kullanılmış", "Sıfat"), "nächste" to Triple("nächste", "sonraki", "Sıfat"),
    "nächsten" to Triple("nächste", "sonraki", "Sıfat"), "nächster" to Triple("nächste", "sonraki", "Sıfat"),
    "passende" to Triple("passend", "uygun", "Sıfat"), "passenden" to Triple("passend", "uygun", "Sıfat"),
    "zufrieden" to Triple("zufrieden", "memnun", "Sıfat"), "aufmerksam" to Triple("aufmerksam", "dikkatli", "Sıfat / Zarf"),
    "ruhig" to Triple("ruhig", "sakin", "Sıfat"), "ruhiger" to Triple("ruhig", "daha sakin", "Sıfat"),
    "wirklich" to Triple("wirklich", "gerçekten", "Zarf"), "erfolgreich" to Triple("erfolgreich", "başarılı", "Sıfat / Zarf"),
    "zweimal" to Triple("zweimal", "iki kez", "Zarf"), "dreimal" to Triple("dreimal", "üç kez", "Zarf")
)

internal val coreStoryNouns: Map<String, ExtendedNounSeed> = buildMap {
    fun add(base: String, meaning: String, article: String, plural: String, vararg forms: String) {
        val seed = ExtendedNounSeed(base, meaning, article, plural)
        forms.forEach { put(it, seed) }
    }
    add("Morgen", "sabah", "der", "Morgen", "morgen")
    add("Mittag", "öğle", "der", "Mittage", "mittag")
    add("Mittagessen", "öğle yemeği", "das", "—", "mittagessen")
    add("Nachmittag", "öğleden sonra", "der", "Nachmittage", "nachmittag")
    add("Abend", "akşam", "der", "Abende", "abend")
    add("Nacht", "gece", "die", "Nächte", "nacht")
    add("Schluss", "son / bitiş", "der", "Schlüsse", "schluss")
    add("Sofa", "kanepe", "das", "Sofas", "sofa")
    add("Zeit", "zaman", "die", "Zeiten", "zeit")
    add("Ruhe", "dinlenme / sakinlik", "die", "—", "ruhe")
    add("Hinweis", "uyarı / bilgi", "der", "Hinweise", "hinweis", "hinweise")
    add("Problem", "sorun", "das", "Probleme", "probleme")
    add("Platz", "yer", "der", "Plätze", "platz")
    add("Woche", "hafta", "die", "Wochen", "woche", "wochen")
}
