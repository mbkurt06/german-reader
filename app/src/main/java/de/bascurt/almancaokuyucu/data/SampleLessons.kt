package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.Lexeme
import de.bascurt.almancaokuyucu.model.ReaderLesson
import de.bascurt.almancaokuyucu.model.ReadingToken

object SampleLessons {
    val all: List<ReaderLesson> by lazy { listOf(a2(), b1(), b2()) }

    private fun a2(): ReaderLesson {
        val texts = listOf(
            "Morgen steht Elif sehr früh auf.",
            "Sie bereitet sich auf ihren ersten Arbeitstag in einem Hotel vor.",
            "Obwohl sie ein wenig Angst vor Fehlern hat, freut sie sich auf die neuen Kollegen.",
            "Im Hotel kümmert sie sich um die Gäste und beantwortet ihre Fragen.",
            "Am Abend ist Elif müde, aber mit ihrem ersten Tag sehr zufrieden."
        )
        val groups = mutableMapOf<Pair<Int, Int>, Lexeme>()
        fun add(indices: List<Pair<Int, Int>>, item: Lexeme) = indices.forEach { groups[it] = item }
        add(listOf(0 to 1, 0 to 5), verbGroup("a2-aufstehen", "aufstehen", "kalkmak", "Ayrılabilen fiil", "steht … auf", "„auf“ burada edat değildir; ayrılabilen aufstehen fiilinin ön ekidir.", "steht auf", "stand auf", "ist aufgestanden"))
        add(listOf(1 to 1, 1 to 2, 1 to 3, 1 to 10), verbGroup("a2-vorbereiten", "sich auf etwas vorbereiten", "bir şeye hazırlanmak", "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ vorbereiten fiilinin istediği sabit edattır. Cümlede hazırlanılan şeyi, ilk iş gününü bağlar; yer veya yön bildirmez.", "bereitet sich vor", "bereitete sich vor", "hat sich vorbereitet", "sich vorbereiten"))
        add(listOf(2 to 4, 2 to 5, 2 to 7), verbGroup("a2-angst", "Angst vor etwas haben", "bir şeyden korkmak", "İsim + fiil + edat", "vor + Dativ", "„vor“ Angst ile kurulan sabit yapının parçasıdır. Cümlede korkunun nedenini, hataları gösterir; konum bildiren vor değildir.", "hat Angst", "hatte Angst", "hat Angst gehabt", "Angst haben"))
        add(listOf(2 to 8, 2 to 10, 2 to 11), verbGroup("a2-freuen", "sich auf etwas freuen", "bir şeyi sabırsızlıkla beklemek", "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ sich freuen ile gelecekte beklenen şeyi/kişiyi bağlar; burada yeni iş arkadaşlarını gösterir.", "freut sich", "freute sich", "hat sich gefreut", "sich freuen"))
        add(listOf(3 to 2, 3 to 4, 3 to 5), verbGroup("a2-kuemmern", "sich um jemanden kümmern", "biriyle ilgilenmek", "Dönüşlü fiil + edat", "um + Akkusativ", "„um“ sich kümmern fiilinin sabit edatıdır ve ilgilenilen kişiyi bağlar; burada misafirleri gösterir.", "kümmert sich", "kümmerte sich", "hat sich gekümmert", "sich kümmern"))
        add(listOf(4 to 6, 4 to 11), Lexeme(id="a2-zufrieden", base="mit etwas zufrieden sein", meaning="bir şeyden memnun olmak", type="Sıfat + edat", grammar="mit + Dativ", explanation="„mit“ zufrieden sıfatının istediği edattır. Memnuniyetin neyle ilgili olduğunu, burada ilk iş gününü gösterir.", quizEligible=true, wordClass="Sıfat", positive="zufrieden", comparative="zufriedener", superlative="am zufriedensten"))
        return build("a2-start", "Yeni Bir Başlangıç", "A2", "Elif'in ilk iş günü", texts, groups)
    }

    private fun b1(): ReaderLesson {
        val texts = listOf(
            "Amir ist nach Stuttgart gezogen und muss sich an seine neue Umgebung gewöhnen.",
            "Anfangs fällt es ihm schwer, schnellen Gesprächen zu folgen.",
            "Mit der Zeit nimmt er an einem Sprachkurs teil.",
            "Dort kommt er mit anderen Teilnehmern ins Gespräch und fühlt sich immer sicherer."
        )
        val groups = mutableMapOf<Pair<Int, Int>, Lexeme>()
        fun add(indices: List<Pair<Int, Int>>, item: Lexeme) = indices.forEach { groups[it] = item }
        add(listOf(0 to 7, 0 to 8, 0 to 12), verbGroup("b1-gewoehnen", "sich an etwas gewöhnen", "bir şeye alışmak", "Dönüşlü fiil + edat", "an + Akkusativ", "„an“ sich gewöhnen fiilinin sabit edatıdır ve alışılan şeyi, burada yeni çevreyi bağlar. Modal fiil „muss“ bu kelime grubunun parçası değildir.", "gewöhnt sich", "gewöhnte sich", "hat sich gewöhnt", "sich gewöhnen"))
        add(listOf(1 to 1, 1 to 3, 1 to 4), verbGroup("b1-schwerfallen", "jemandem schwerfallen", "birine zor gelmek", "Fiil kalıbı", "jemandem + Dativ", "„ihm“ işi zor bulan kişiyi Dativ olarak gösterir; „schwerfallen“ kalıbının anlamı bir şeyin birine zor gelmesidir.", "fällt schwer", "fiel schwer", "ist schwergefallen", "schwerfallen"))
        add(listOf(2 to 3, 2 to 5, 2 to 8), verbGroup("b1-teilnehmen", "an etwas teilnehmen", "bir şeye katılmak", "Ayrılabilen fiil + edat", "an + Dativ", "„an“ teilnehmen fiilinin istediği sabit edattır ve katılınan etkinliği, burada bir dil kursunu gösterir.", "nimmt teil", "nahm teil", "hat teilgenommen", "teilnehmen"))
        add(listOf(3 to 1, 3 to 3, 3 to 6, 3 to 7), verbGroup("b1-gespraech", "mit jemandem ins Gespräch kommen", "biriyle konuşmaya başlamak", "Sabit ifade", "mit + Dativ", "„mit“ konuşmaya girilen kişileri, burada diğer katılımcıları bağlar; ifade bütün olarak 'biriyle konuşmaya başlamak' anlamındadır.", "kommt ins Gespräch", "kam ins Gespräch", "ist ins Gespräch gekommen", "ins Gespräch kommen"))
        return build("b1-city", "Yeni Bir Şehre Alışmak", "B1", "Amir'in Stuttgart deneyimi", texts, groups)
    }

    private fun b2(): ReaderLesson {
        val texts = listOf(
            "Viele Menschen sind ständig erreichbar, obwohl sie sich nach Ruhe sehnen.",
            "Wer bewusst auf Benachrichtigungen verzichtet, kann sich besser auf wichtige Aufgaben konzentrieren.",
            "Es kommt nicht darauf an, jede Nachricht sofort zu beantworten.",
            "Entscheidend ist, die Technik so zu nutzen, dass sie den Alltag erleichtert."
        )
        val groups = mutableMapOf<Pair<Int, Int>, Lexeme>()
        fun add(indices: List<Pair<Int, Int>>, item: Lexeme) = indices.forEach { groups[it] = item }
        add(listOf(0 to 7, 0 to 8, 0 to 9, 0 to 10), verbGroup("b2-sehnen", "sich nach etwas sehnen", "bir şeyi özlemek / arzulamak", "Dönüşlü fiil + edat", "nach + Dativ", "„nach“ sich sehnen fiilinin sabit edatıdır ve özlenen/arzulanan şeyi, burada sakinliği gösterir.", "sehnt sich", "sehnte sich", "hat sich gesehnt", "sich sehnen"))
        add(listOf(1 to 2, 1 to 4), verbGroup("b2-verzichten", "auf etwas verzichten", "bir şeyden vazgeçmek", "Fiil + edat", "auf + Akkusativ", "„auf“ verzichten fiilinin sabit edatıdır ve vazgeçilen şeyi, burada bildirimleri gösterir.", "verzichtet", "verzichtete", "hat verzichtet", "verzichten"))
        add(listOf(1 to 6, 1 to 8, 1 to 11), verbGroup("b2-konzentrieren", "sich auf etwas konzentrieren", "bir şeye odaklanmak", "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ sich konzentrieren fiilinin sabit edatıdır ve odaklanılan şeyi, burada önemli görevleri gösterir. Modal fiil „kann“ grubun parçası değildir.", "konzentriert sich", "konzentrierte sich", "hat sich konzentriert", "sich konzentrieren"))
        add(listOf(2 to 1, 2 to 3, 2 to 4), verbGroup("b2-ankommen", "auf etwas ankommen", "önemli/belirleyici olmak", "Ayrılabilen fiil + edat", "darauf … an", "„darauf“ neyin belirleyici olduğunu temsil eder; sondaki „an“ ise ayrılabilen ankommen fiilinin parçasıdır.", "kommt darauf an", "kam darauf an", "ist darauf angekommen", "auf etwas ankommen"))
        return build("b2-balance", "Dijital Dünyada Denge", "B2", "Bildirimler ve dikkat üzerine", texts, groups)
    }

    private fun build(id: String, title: String, level: String, summary: String, texts: List<String>, groups: Map<Pair<Int, Int>, Lexeme>): ReaderLesson =
        ReaderLesson(id, title, level, summary, texts.mapIndexed { si, sentence ->
            sentence.split(" ").mapIndexed { ti, shown ->
                val clean = shown.trimEnd('.', ',', ':').lowercase()
                ReadingToken(shown, groups[si to ti] ?: contextualLexeme(id, si, ti, clean) ?: word("$id-$si-$ti", clean))
            }
        })

    private fun contextualLexeme(id: String, si: Int, ti: Int, clean: String): Lexeme? {
        val key = "$id-$si-$ti"
        return when (clean) {
            "in" -> Lexeme(key, "in", "-de / içinde", "Edat", "in + Dativ", "Bu cümlede „in“, bulunulan yeri gösterir: „in einem Hotel“ = bir otelde. Hareket/yön değil, konum bildirildiği için Dativ kullanılır.", wordClass="Edat")
            "im" -> Lexeme(key, "im (in dem)", "-de / içinde", "Edat", "in + Dativ", "Bu cümlede „im“, in dem kısaltmasıdır ve bulunulan yeri gösterir: „im Hotel“ = otelde. Konum bildirildiği için Dativ kullanılır.", wordClass="Edat")
            "am" -> Lexeme(key, "am (an dem)", "-de / sırasında", "Edat", "an + Dativ", "Bu cümlede „am“ zaman bildirir ve eylemin ne zaman gerçekleştiğini gösterir: „Am Abend“ = akşamleyin/akşam.", wordClass="Edat")
            "nach" -> if (id == "b1-city" && si == 0 && ti == 2) Lexeme(key, "nach", "-e / -a", "Edat", "nach + şehir", "Bu cümlede „nach“ bir şehre doğru hareketin hedefini gösterir: „nach Stuttgart ziehen“ = Stuttgart'a taşınmak. Artikelsiz şehir adlarıyla yön bildirirken „nach“ kullanılır.", wordClass="Edat") else null
            "mit" -> Lexeme(key, "mit", "ile", "Edat", "mit + Dativ", "Bu cümlede „mit“, birlikte olma/bağlantı anlamı kurar ve kendisinden sonra Dativ ister.", wordClass="Edat")
            "obwohl" -> Lexeme(key, "obwohl", "-mesine rağmen", "Bağlaç", "Fiil sonda", "Bu cümlede beklenenin tersine gerçekleşen iki durumu birbirine bağlar; obwohl yan cümlesinde çekimli fiil sona gider.", wordClass="Bağlaç")
            "aber" -> Lexeme(key, "aber", "ama", "Bağlaç", explanation="Bu cümlede iki düşünce veya durum arasında karşıtlık kurar.", wordClass="Bağlaç")
            "und" -> Lexeme(key, "und", "ve", "Bağlaç", explanation="Bu cümlede aynı düzeydeki iki bilgi veya eylemi birbirine ekler.", wordClass="Bağlaç")
            "dass" -> Lexeme(key, "dass", "-dığı / ki", "Bağlaç", "Fiil sonda", "Bu cümlede bir bilgiyi yan cümle olarak ana cümleye bağlar; dass yan cümlesinde çekimli fiil sona gider.", wordClass="Bağlaç")
            else -> null
        }
    }

    private fun verbGroup(id: String, base: String, meaning: String, type: String, grammar: String, note: String, third: String, pret: String, perf: String, infinitive: String = base) =
        Lexeme(id=id, base=base, meaning=meaning, type=type, grammar=grammar, explanation=note, quizEligible=true, wordClass="Fiil", infinitive=infinitive, thirdPerson=third, preterite=pret, perfect=perf)

    private fun word(id: String, w: String): Lexeme = when (w) {
        "morgen" -> Lexeme(id,"morgen","yarın","Zarf",wordClass="Zarf")
        "elif", "amir", "stuttgart" -> Lexeme(id,w.replaceFirstChar { it.uppercase() }, if (w=="stuttgart") "Stuttgart" else "özel isim","Özel isim",wordClass="Özel isim")
        "sehr", "wenig", "anfangs", "dort", "immer", "ständig", "sofort", "so" -> Lexeme(id,w, mapOf("sehr" to "çok","wenig" to "az / biraz","anfangs" to "başlangıçta","dort" to "orada","immer" to "giderek / daima","ständig" to "sürekli","sofort" to "hemen","so" to "bu şekilde")[w]!!,"Zarf",wordClass="Zarf")
        "früh" -> adj(id,"früh","erken","früh","früher","am frühesten")
        "ersten", "erste" -> adj(id,"erste","ilk","erste",null,null)
        "neuen", "neue" -> adj(id,"neu","yeni","neu","neuer","am neuesten")
        "müde" -> adj(id,"müde","yorgun","müde","müder","am müdesten")
        "schnellen" -> adj(id,"schnell","hızlı","schnell","schneller","am schnellsten")
        "sicherer" -> adj(id,"sicher","daha güvende / emin","sicher","sicherer","am sichersten")
        "erreichbar" -> adj(id,"erreichbar","ulaşılabilir","erreichbar","erreichbarer","am erreichbarsten")
        "bewusst" -> adj(id,"bewusst","bilinçli","bewusst","bewusster","am bewusstesten")
        "besser" -> adj(id,"gut","daha iyi","gut","besser","am besten")
        "wichtige" -> adj(id,"wichtig","önemli","wichtig","wichtiger","am wichtigsten")
        "entscheidend" -> adj(id,"entscheidend","belirleyici","entscheidend","entscheidender","am entscheidendsten")
        "arbeitstag" -> noun(id,"Arbeitstag","iş günü","der","Arbeitstage")
        "hotel" -> noun(id,"Hotel","otel","das","Hotels")
        "fehlern" -> noun(id,"Fehler","hata","der","Fehler")
        "kollegen" -> noun(id,"Kollege","iş arkadaşı","der","Kollegen","Akkusativ Singular: den Kollegen (-n)")
        "gäste" -> noun(id,"Gast","misafir","der","Gäste")
        "fragen" -> noun(id,"Frage","soru","die","Fragen")
        "abend" -> noun(id,"Abend","akşam","der","Abende")
        "tag" -> noun(id,"Tag","gün","der","Tage")
        "umgebung" -> noun(id,"Umgebung","çevre","die","Umgebungen")
        "gesprächen" -> noun(id,"Gespräch","konuşma","das","Gespräche")
        "zeit" -> noun(id,"Zeit","zaman","die","Zeiten")
        "sprachkurs" -> noun(id,"Sprachkurs","dil kursu","der","Sprachkurse")
        "teilnehmern" -> noun(id,"Teilnehmer","katılımcı","der","Teilnehmer")
        "menschen" -> noun(id,"Mensch","insan","der","Menschen","Akkusativ Singular: den Menschen (-en)")
        "ruhe" -> noun(id,"Ruhe","sakinlik","die","—")
        "benachrichtigungen" -> noun(id,"Benachrichtigung","bildirim","die","Benachrichtigungen")
        "aufgaben" -> noun(id,"Aufgabe","görev","die","Aufgaben")
        "nachricht" -> noun(id,"Nachricht","mesaj","die","Nachrichten")
        "technik" -> noun(id,"Technik","teknoloji","die","Techniken")
        "alltag" -> noun(id,"Alltag","günlük hayat","der","Alltage")
        "beantwortet" -> verb(id,"beantworten","cevaplamak","beantwortet","beantwortete","hat beantwortet")
        "ist", "sind" -> verb(id,"sein","olmak","ist","war","ist gewesen")
        "gezogen" -> verb(id,"ziehen","taşınmak / çekmek","zieht","zog","ist gezogen")
        "muss" -> verb(id,"müssen","zorunda olmak","muss","musste","hat gemusst")
        "folgen" -> verb(id,"folgen","takip etmek","folgt","folgte","ist gefolgt")
        "fühlt" -> verb(id,"fühlen","hissetmek","fühlt","fühlte","hat gefühlt")
        "kann" -> verb(id,"können","-ebilmek","kann","konnte","hat gekonnt")
        "beantworten" -> verb(id,"beantworten","cevaplamak","beantwortet","beantwortete","hat beantwortet")
        "nutzen" -> verb(id,"nutzen","kullanmak","nutzt","nutzte","hat genutzt")
        "erleichtert" -> verb(id,"erleichtern","kolaylaştırmak","erleichtert","erleichterte","hat erleichtert")
        "sie", "er", "es", "ihm", "ihren", "ihre", "seine", "ihrem", "wer" -> Lexeme(id,w, mapOf("sie" to "o / onlar","er" to "o (erkek)","es" to "o","ihm" to "ona","ihren" to "onun","ihre" to "onların / onun","seine" to "onun","ihrem" to "onun","wer" to "kim / -en kişi")[w]!!,"Zamir / belirleyici",wordClass="Zamir")
        "ein", "einem", "die", "der", "den", "jede" -> Lexeme(id,w, if (w=="jede") "her" else "artikel / belirleyici","Artikel / belirleyici",wordClass="Artikel")
        "zu" -> Lexeme(id,"zu","-mek / -maya","Parçacık", explanation="Bu cümlede mastar yapısının parçasıdır.", wordClass="Parçacık")
        "nicht" -> Lexeme(id,"nicht","değil / -me","Olumsuzluk",wordClass="Parçacık")
        "viele" -> Lexeme(id,"viele","birçok","Belirleyici",wordClass="Belirleyici")
        "anderen" -> adj(id,"andere","diğer","andere",null,null)
        else -> Lexeme(id,w,"Türkçe anlamı","Kelime",wordClass="Diğer")
    }

    private fun verb(id: String, infinitive: String, meaning: String, third: String, pret: String, perf: String) =
        Lexeme(id=id, base=infinitive, meaning=meaning, type="Fiil", wordClass="Fiil", infinitive=infinitive, thirdPerson=third, preterite=pret, perfect=perf)

    private fun noun(id: String, base: String, meaning: String, article: String, plural: String, acc: String? = null) =
        Lexeme(id=id, base=base, meaning=meaning, type="İsim", wordClass="İsim", article=article, plural=plural, accusativeNote=acc)

    private fun adj(id: String, base: String, meaning: String, positive: String, comparative: String?, superlative: String?) =
        Lexeme(id=id, base=base, meaning=meaning, type="Sıfat", wordClass="Sıfat", positive=positive, comparative=comparative, superlative=superlative)
}
