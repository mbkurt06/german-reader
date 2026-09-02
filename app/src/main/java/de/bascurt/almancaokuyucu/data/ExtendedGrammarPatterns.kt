package de.bascurt.almancaokuyucu.data

internal data class SeparableRule(
    val surfaces: Set<String>,
    val particle: String,
    val seed: ExtendedVerbSeed
)

internal data class FixedGroupRule(
    val surfaces: Set<String>,
    val required: Set<String>,
    val seed: ExtendedVerbSeed,
    val maxDistance: Int = 7
)

private fun sep(
    surfaces: String,
    particle: String,
    base: String,
    meaning: String,
    third: String,
    preterite: String,
    perfect: String
) = SeparableRule(surfaces.split('|').toSet(), particle, ExtendedVerbSeed(base, meaning, third, preterite, perfect))

internal val storySeparableRules = listOf(
    sep("macht", "an", "anmachen", "açmak / çalıştırmak", "macht an", "machte an", "hat angemacht"),
    sep("macht", "auf", "aufmachen", "açmak", "macht auf", "machte auf", "hat aufgemacht"),
    sep("trocknet", "ab", "abtrocknen", "kurulamak", "trocknet ab", "trocknete ab", "hat abgetrocknet"),
    sep("wischt", "ab", "abwischen", "silmek", "wischt ab", "wischte ab", "hat abgewischt"),
    sep("räumt|räumen", "weg", "wegräumen", "ortadan kaldırmak / yerine koymak", "räumt weg", "räumte weg", "hat weggeräumt"),
    sep("räumt|räumen", "auf", "aufräumen", "toplamak / düzenlemek", "räumt auf", "räumte auf", "hat aufgeräumt"),
    sep("räumt|räumen", "aus", "ausräumen", "boşaltmak", "räumt aus", "räumte aus", "hat ausgeräumt"),
    sep("räumt|räumen", "ein", "einräumen", "yerleştirmek / içine dizmek", "räumt ein", "räumte ein", "hat eingeräumt"),
    sep("räumt|räumen", "ab", "abräumen", "masayı toplamak", "räumt ab", "räumte ab", "hat abgeräumt"),
    sep("zieht", "an", "anziehen", "giymek", "zieht an", "zog an", "hat angezogen"),
    sep("stellt|stellen", "auf", "aufstellen", "kurmak / yerleştirmek", "stellt auf", "stellte auf", "hat aufgestellt"),
    sep("stellt", "vor", "vorstellen", "tanıtmak / sunmak", "stellt vor", "stellte vor", "hat vorgestellt"),
    sep("baut|bauen", "zusammen", "zusammenbauen", "monte etmek / kurmak", "baut zusammen", "baute zusammen", "hat zusammengebaut"),
    sep("füllt", "hinein", "hineinfüllen", "içine doldurmak", "füllt hinein", "füllte hinein", "hat hineingefüllt"),
    sep("füllt", "nach", "nachfüllen", "yeniden doldurmak", "füllt nach", "füllte nach", "hat nachgefüllt"),
    sep("füllt", "aus", "ausfüllen", "form doldurmak", "füllt aus", "füllte aus", "hat ausgefüllt"),
    sep("probiert", "an", "anprobieren", "denemek (kıyafet)", "probiert an", "probierte an", "hat anprobiert"),
    sep("probiert", "aus", "ausprobieren", "denemek / test etmek", "probiert aus", "probierte aus", "hat ausprobiert"),
    sep("meldet|meldete", "an", "sich anmelden", "kayıt olmak", "meldet sich an", "meldete sich an", "hat sich angemeldet"),
    sep("hört", "ab", "abhören", "dinleyerek muayene etmek", "hört ab", "hörte ab", "hat abgehört"),
    sep("ruft", "an", "anrufen", "telefonla aramak", "ruft an", "rief an", "hat angerufen"),
    sep("hängt|hängen", "auf", "aufhängen", "asmak", "hängt auf", "hängte auf", "hat aufgehängt"),
    sep("hängt", "zurück", "zurückhängen", "geri asmak / yerine asmak", "hängt zurück", "hängte zurück", "hat zurückgehängt"),
    sep("holt", "ab", "abholen", "gidip almak / teslim almak", "holt ab", "holte ab", "hat abgeholt"),
    sep("packt", "ein", "einpacken", "paketlemek / içine koymak", "packt ein", "packte ein", "hat eingepackt"),
    sep("packt", "aus", "auspacken", "paketten çıkarmak", "packt aus", "packte aus", "hat ausgepackt"),
    sep("kommt", "an", "ankommen", "varmak", "kommt an", "kam an", "ist angekommen"),
    sep("wärmt", "auf", "sich aufwärmen", "ısınmak", "wärmt sich auf", "wärmte sich auf", "hat sich aufgewärmt"),
    sep("trägt", "ein", "eintragen", "kaydetmek / girmek", "trägt ein", "trug ein", "hat eingetragen"),
    sep("gibt|geben", "ab", "abgeben", "teslim etmek", "gibt ab", "gab ab", "hat abgegeben"),
    sep("gibt|geben", "ein", "eingeben", "girmek / sisteme yazmak", "gibt ein", "gab ein", "hat eingegeben"),
    sep("gibt|geben", "hinein", "hineingeben", "içine koymak", "gibt hinein", "gab hinein", "hat hineingegeben"),
    sep("nimmt", "mit", "mitnehmen", "yanına almak / götürmek", "nimmt mit", "nahm mit", "hat mitgenommen"),
    sep("nimmt", "heraus", "herausnehmen", "içinden çıkarmak", "nimmt heraus", "nahm heraus", "hat herausgenommen"),
    sep("nimmt", "teil", "teilnehmen", "katılmak", "nimmt teil", "nahm teil", "hat teilgenommen"),
    sep("legt|legen", "weg", "weglegen", "bir kenara koymak", "legt weg", "legte weg", "hat weggelegt"),
    sep("legen", "ein", "einlegen", "vermek / yapmak (mola)", "legt ein", "legte ein", "hat eingelegt"),
    sep("sammelt", "ein", "einsammeln", "toplamak", "sammelt ein", "sammelte ein", "hat eingesammelt"),
    sep("fettet", "ein", "einfetten", "yağlamak", "fettet ein", "fettete ein", "hat eingefettet"),
    sep("schließt|schließen", "ab", "abschließen", "kilitlemek", "schließt ab", "schloss ab", "hat abgeschlossen"),
    sep("schließt|schließen", "ein", "einschließen", "kilitlemek / içine kilitlemek", "schließt ein", "schloss ein", "hat eingeschlossen"),
    sep("fährt", "ab", "abfahren", "hareket etmek / yola çıkmak", "fährt ab", "fuhr ab", "ist abgefahren"),
    sep("fährt", "ein", "einfahren", "istasyona girmek", "fährt ein", "fuhr ein", "ist eingefahren"),
    sep("fährt|fahren", "los", "losfahren", "yola çıkmak", "fährt los", "fuhr los", "ist losgefahren"),
    sep("fährt", "weiter", "weiterfahren", "yola devam etmek", "fährt weiter", "fuhr weiter", "ist weitergefahren"),
    sep("steigt", "ein", "einsteigen", "binmek", "steigt ein", "stieg ein", "ist eingestiegen"),
    sep("steigt", "aus", "aussteigen", "inmek", "steigt aus", "stieg aus", "ist ausgestiegen"),
    sep("liest", "ab", "ablesen", "okuyup aktarmak", "liest ab", "las ab", "hat abgelesen"),
    sep("bereitet", "vor", "vorbereiten", "hazırlamak", "bereitet vor", "bereitete vor", "hat vorbereitet"),
    sep("setzt", "fort", "fortsetzen", "devam ettirmek", "setzt fort", "setzte fort", "hat fortgesetzt"),
    sep("setzt", "hin", "sich hinsetzen", "oturmak", "setzt sich hin", "setzte sich hin", "hat sich hingesetzt"),
    sep("lernt|lernte", "kennen", "kennenlernen", "tanışmak / tanımaya başlamak", "lernt kennen", "lernte kennen", "hat kennengelernt"),
    sep("stimmt", "zu", "zustimmen", "kabul etmek / onaylamak", "stimmt zu", "stimmte zu", "hat zugestimmt"),
    sep("findet", "statt", "stattfinden", "gerçekleşmek", "findet statt", "fand statt", "hat stattgefunden")
)

internal val storyFixedGroupRules = listOf(
    FixedGroupRule(setOf("fragt"), setOf("nach"), ExtendedVerbSeed("nach etwas fragen", "bir şeyi sormak", "fragt nach", "fragte nach", "hat nach etwas gefragt")),
    FixedGroupRule(setOf("achtet"), setOf("auf"), ExtendedVerbSeed("auf etwas achten", "bir şeye dikkat etmek", "achtet auf", "achtete auf", "hat auf etwas geachtet")),
    FixedGroupRule(setOf("spricht"), setOf("über"), ExtendedVerbSeed("über etwas sprechen", "bir şey hakkında konuşmak", "spricht über", "sprach über", "hat über etwas gesprochen")),
    FixedGroupRule(setOf("entscheidet"), setOf("sich", "für"), ExtendedVerbSeed("sich für etwas entscheiden", "bir şeye karar vermek", "entscheidet sich", "entschied sich", "hat sich entschieden")),
    FixedGroupRule(setOf("bedankt", "bedanken"), setOf("sich"), ExtendedVerbSeed("sich bedanken", "teşekkür etmek", "bedankt sich", "bedankte sich", "hat sich bedankt")),
    FixedGroupRule(setOf("verabschiedet", "verabschieden"), setOf("sich"), ExtendedVerbSeed("sich verabschieden", "vedalaşmak", "verabschiedet sich", "verabschiedete sich", "hat sich verabschiedet")),
    FixedGroupRule(setOf("fühlt", "fühlte"), setOf("sich"), ExtendedVerbSeed("sich fühlen", "hissetmek", "fühlt sich", "fühlte sich", "hat sich gefühlt"))
)

/**
 * Ayrılabilen fiil öncesinde ilk anlamlandırmada kullanılacak yalın fiiller.
 * Prefix, cümlede gerçekten ayrılmış olarak bulunursa ExtendedLessonFactory daha sonra
 * bu yalın Lexeme'i bütün ayrılabilen fiil grubuyla değiştirir.
 */
internal val storyBaseVerbOverrides: Map<String, ExtendedVerbSeed> = mapOf(
    "hängt" to ExtendedVerbSeed("hängen", "asmak / asılı durmak", "hängt", "hing", "hat gehangen"),
    "hängen" to ExtendedVerbSeed("hängen", "asmak / asılı durmak", "hängt", "hing", "hat gehangen"),
    "zieht" to ExtendedVerbSeed("ziehen", "çekmek / taşınmak", "zieht", "zog", "hat gezogen"),
    "räumt" to ExtendedVerbSeed("räumen", "toplamak / boşaltmak", "räumt", "räumte", "hat geräumt"),
    "räumen" to ExtendedVerbSeed("räumen", "toplamak / boşaltmak", "räumt", "räumte", "hat geräumt"),
    "packt" to ExtendedVerbSeed("packen", "paketlemek / koymak", "packt", "packte", "hat gepackt"),
    "stellt" to ExtendedVerbSeed("stellen", "koymak / dik yerleştirmek", "stellt", "stellte", "hat gestellt"),
    "stellen" to ExtendedVerbSeed("stellen", "koymak / dik yerleştirmek", "stellt", "stellte", "hat gestellt"),
    "baut" to ExtendedVerbSeed("bauen", "inşa etmek / kurmak", "baut", "baute", "hat gebaut"),
    "bauen" to ExtendedVerbSeed("bauen", "inşa etmek / kurmak", "baut", "baute", "hat gebaut"),
    "füllt" to ExtendedVerbSeed("füllen", "doldurmak", "füllt", "füllte", "hat gefüllt"),
    "probiert" to ExtendedVerbSeed("probieren", "denemek", "probiert", "probierte", "hat probiert"),
    "meldet" to ExtendedVerbSeed("melden", "bildirmek", "meldet", "meldete", "hat gemeldet"),
    "meldete" to ExtendedVerbSeed("melden", "bildirmek", "meldet", "meldete", "hat gemeldet"),
    "hört" to ExtendedVerbSeed("hören", "duymak / dinlemek", "hört", "hörte", "hat gehört"),
    "holt" to ExtendedVerbSeed("holen", "getirmek / gidip almak", "holt", "holte", "hat geholt"),
    "kommt" to ExtendedVerbSeed("kommen", "gelmek", "kommt", "kam", "ist gekommen"),
    "wärmt" to ExtendedVerbSeed("wärmen", "ısıtmak", "wärmt", "wärmte", "hat gewärmt"),
    "trägt" to ExtendedVerbSeed("tragen", "taşımak / giymek", "trägt", "trug", "hat getragen"),
    "gibt" to ExtendedVerbSeed("geben", "vermek", "gibt", "gab", "hat gegeben"),
    "geben" to ExtendedVerbSeed("geben", "vermek", "gibt", "gab", "hat gegeben"),
    "nimmt" to ExtendedVerbSeed("nehmen", "almak", "nimmt", "nahm", "hat genommen"),
    "legt" to ExtendedVerbSeed("legen", "koymak / yatırarak yerleştirmek", "legt", "legte", "hat gelegt"),
    "legen" to ExtendedVerbSeed("legen", "koymak / yatırarak yerleştirmek", "legt", "legte", "hat gelegt"),
    "sammelt" to ExtendedVerbSeed("sammeln", "toplamak", "sammelt", "sammelte", "hat gesammelt"),
    "schließt" to ExtendedVerbSeed("schließen", "kapatmak", "schließt", "schloss", "hat geschlossen"),
    "schließen" to ExtendedVerbSeed("schließen", "kapatmak", "schließt", "schloss", "hat geschlossen"),
    "fährt" to ExtendedVerbSeed("fahren", "gitmek / araç kullanmak", "fährt", "fuhr", "ist gefahren"),
    "fahren" to ExtendedVerbSeed("fahren", "gitmek / araç kullanmak", "fährt", "fuhr", "ist gefahren"),
    "steigt" to ExtendedVerbSeed("steigen", "çıkmak / yükselmek", "steigt", "stieg", "ist gestiegen"),
    "liest" to ExtendedVerbSeed("lesen", "okumak", "liest", "las", "hat gelesen"),
    "bereitet" to ExtendedVerbSeed("bereiten", "hazırlamak", "bereitet", "bereitete", "hat bereitet"),
    "setzt" to ExtendedVerbSeed("setzen", "oturtmak / koymak", "setzt", "setzte", "hat gesetzt"),
    "lernt" to ExtendedVerbSeed("lernen", "öğrenmek", "lernt", "lernte", "hat gelernt"),
    "lernte" to ExtendedVerbSeed("lernen", "öğrenmek", "lernt", "lernte", "hat gelernt"),
    "stimmt" to ExtendedVerbSeed("stimmen", "doğru olmak / uyuşmak", "stimmt", "stimmte", "hat gestimmt"),
    "findet" to ExtendedVerbSeed("finden", "bulmak", "findet", "fand", "hat gefunden"),
    "wischt" to ExtendedVerbSeed("wischen", "silmek", "wischt", "wischte", "hat gewischt")
)
