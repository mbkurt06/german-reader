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
        add(listOf(0 to 1, 0 to 5), x("a2-aufstehen", "aufstehen", "kalkmak", "Ayrılabilen fiil", "steht … auf",
            "„auf“ burada edat değildir; ayrılabilen aufstehen fiilinin ön ekidir.", true))
        add(listOf(1 to 1, 1 to 2, 1 to 3, 1 to 10), x("a2-vorbereiten", "sich auf etwas vorbereiten", "bir şeye hazırlanmak",
            "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ vorbereiten fiilinin istediği sabit edattır. Cümlede hazırlanılan şeyi, ilk iş gününü bağlar; yer veya yön bildirmez.", true))
        add(listOf(2 to 4, 2 to 5, 2 to 7), x("a2-angst", "Angst vor etwas haben", "bir şeyden korkmak",
            "İsim + edat", "vor + Dativ", "„vor“ Angst ile kurulan sabit yapının parçasıdır. Cümlede korkunun nedenini, hataları gösterir; konum bildiren vor değildir.", true))
        add(listOf(2 to 8, 2 to 10, 2 to 11), x("a2-freuen", "sich auf etwas freuen", "bir şeyi sabırsızlıkla beklemek",
            "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ sich freuen ile gelecekte beklenen şeyi/kişiyi bağlar; burada yeni iş arkadaşlarını gösterir.", true))
        add(listOf(3 to 2, 3 to 4, 3 to 5), x("a2-kuemmern", "sich um jemanden kümmern", "biriyle ilgilenmek",
            "Dönüşlü fiil + edat", "um + Akkusativ", "„um“ sich kümmern fiilinin sabit edatıdır ve ilgilenilen kişiyi bağlar; burada misafirleri gösterir.", true))
        add(listOf(4 to 6, 4 to 11), x("a2-zufrieden", "mit etwas zufrieden sein", "bir şeyden memnun olmak",
            "Sıfat + edat", "mit + Dativ", "„mit“ zufrieden sıfatının istediği edattır. Memnuniyetin neyle ilgili olduğunu, burada ilk iş gününü gösterir.", true))
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
        add(listOf(0 to 7, 0 to 8, 0 to 12), x("b1-gewoehnen", "sich an etwas gewöhnen", "bir şeye alışmak",
            "Dönüşlü fiil + edat", "an + Akkusativ", "„an“ sich gewöhnen fiilinin sabit edatıdır ve alışılan şeyi, burada yeni çevreyi bağlar. Modal fiil „muss“ bu kelime grubunun parçası değildir.", true))
        add(listOf(1 to 1, 1 to 3, 1 to 4), x("b1-schwerfallen", "jemandem schwerfallen", "birine zor gelmek",
            "Fiil kalıbı", "jemandem + Dativ", "„ihm“ işi zor bulan kişiyi Dativ olarak gösterir; „schwerfallen“ kalıbının anlamı bir şeyin birine zor gelmesidir.", true))
        add(listOf(2 to 3, 2 to 5, 2 to 8), x("b1-teilnehmen", "an etwas teilnehmen", "bir şeye katılmak",
            "Ayrılabilen fiil + edat", "an + Dativ", "„an“ teilnehmen fiilinin istediği sabit edattır ve katılınan etkinliği, burada bir dil kursunu gösterir.", true))
        add(listOf(3 to 1, 3 to 3, 3 to 6, 3 to 7), x("b1-gespraech", "mit jemandem ins Gespräch kommen", "biriyle konuşmaya başlamak",
            "Sabit ifade", "mit + Dativ", "„mit“ konuşmaya girilen kişileri, burada diğer katılımcıları bağlar; ifade bütün olarak 'biriyle konuşmaya başlamak' anlamındadır.", true))
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
        add(listOf(0 to 7, 0 to 8, 0 to 9, 0 to 10), x("b2-sehnen", "sich nach etwas sehnen", "bir şeyi özlemek / arzulamak",
            "Dönüşlü fiil + edat", "nach + Dativ", "„nach“ sich sehnen fiilinin sabit edatıdır ve özlenen/arzulanan şeyi, burada sakinliği gösterir.", true))
        add(listOf(1 to 2, 1 to 4), x("b2-verzichten", "auf etwas verzichten", "bir şeyden vazgeçmek",
            "Fiil + edat", "auf + Akkusativ", "„auf“ verzichten fiilinin sabit edatıdır ve vazgeçilen şeyi, burada bildirimleri gösterir.", true))
        add(listOf(1 to 6, 1 to 8, 1 to 11), x("b2-konzentrieren", "sich auf etwas konzentrieren", "bir şeye odaklanmak",
            "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ sich konzentrieren fiilinin sabit edatıdır ve odaklanılan şeyi, burada önemli görevleri gösterir. Modal fiil „kann“ grubun parçası değildir.", true))
        add(listOf(2 to 1, 2 to 3, 2 to 4), x("b2-ankommen", "auf etwas ankommen", "önemli/belirleyici olmak",
            "Ayrılabilen fiil + edat", "darauf … an", "„darauf“ neyin belirleyici olduğunu temsil eder; sondaki „an“ ise ayrılabilen ankommen fiilinin parçasıdır.", true))
        return build("b2-balance", "Dijital Dünyada Denge", "B2", "Bildirimler ve dikkat üzerine", texts, groups)
    }

    private fun build(id: String, title: String, level: String, summary: String, texts: List<String>, groups: Map<Pair<Int, Int>, Lexeme>): ReaderLesson {
        return ReaderLesson(id, title, level, summary, texts.mapIndexed { si, sentence ->
            sentence.split(" ").mapIndexed { ti, shown ->
                val clean = shown.trimEnd('.', ',', ':').lowercase()
                ReadingToken(shown, groups[si to ti] ?: contextualLexeme(id, si, ti, clean, shown, texts) ?: Lexeme("$id-$si-$ti", clean, commonMeanings[clean] ?: "Türkçe anlamı"))
            }
        })
    }

    private fun contextualLexeme(id: String, si: Int, ti: Int, clean: String, shown: String, texts: List<String>): Lexeme? {
        val key = "$id-$si-$ti"
        return when (clean) {
            "in" -> x(key, "in", "-de / içinde", "Edat", "in + Dativ", "Bu cümlede „in“, bulunulan yeri gösterir: „in einem Hotel“ = bir otelde. Hareket/yön değil, konum bildirildiği için Dativ kullanılır.")
            "im" -> x(key, "im (in dem)", "-de / içinde", "Edat", "in + Dativ", "Bu cümlede „im“, in dem kısaltmasıdır ve bulunulan yeri gösterir: „im Hotel“ = otelde. Konum bildirildiği için Dativ kullanılır.")
            "am" -> x(key, "am (an dem)", "-de / sırasında", "Edat", "an + Dativ", "Bu cümlede „am“ zaman bildirir ve eylemin ne zaman gerçekleştiğini gösterir: „Am Abend“ = akşamleyin/akşam.")
            "nach" -> if (id == "b1-city" && si == 0 && ti == 2) x(key, "nach", "-e / -a", "Edat", "nach + şehir", "Bu cümlede „nach“ bir şehre doğru hareketin hedefini gösterir: „nach Stuttgart ziehen“ = Stuttgart'a taşınmak. Artikelsiz şehir adlarıyla yön bildirirken „nach“ kullanılır.") else null
            "mit" -> x(key, "mit", "ile", "Edat", "mit + Dativ", "Bu cümlede „mit“, birlikte olma/bağlantı anlamı kurar ve kendisinden sonra Dativ ister.")
            "obwohl" -> x(key, "obwohl", "-mesine rağmen", "Bağlaç", "Fiil sonda", "Bu cümlede beklenenin tersine gerçekleşen iki durumu birbirine bağlar; obwohl yan cümlesinde çekimli fiil sona gider.")
            "aber" -> x(key, "aber", "ama", "Bağlaç", null, "Bu cümlede iki düşünce veya durum arasında karşıtlık kurar.")
            "und" -> x(key, "und", "ve", "Bağlaç", null, "Bu cümlede aynı düzeydeki iki bilgi veya eylemi birbirine ekler.")
            "dass" -> x(key, "dass", "-dığı / ki", "Bağlaç", "Fiil sonda", "Bu cümlede bir bilgiyi yan cümle olarak ana cümleye bağlar; dass yan cümlesinde çekimli fiil sona gider.")
            else -> null
        }
    }

    private fun x(id: String, base: String, meaning: String, type: String, grammar: String?, note: String, quizEligible: Boolean = false) =
        Lexeme(id, base, meaning, type, grammar, note, quizEligible)

    private val commonMeanings = mapOf(
        "morgen" to "yarın", "steht" to "duruyor", "sehr" to "çok", "früh" to "erken", "sie" to "o (kadın)", "ihren" to "onun", "ersten" to "ilk", "arbeitstag" to "iş günü", "einem" to "bir", "hotel" to "otel", "ein" to "bir", "wenig" to "az / biraz", "fehlern" to "hatalar", "die" to "belirli artikel", "neuen" to "yeni", "kollegen" to "iş arkadaşları", "gäste" to "misafirler", "beantwortet" to "cevaplıyor", "ihre" to "onların", "fragen" to "sorular", "abend" to "akşam", "ist" to "olmak", "müde" to "yorgun", "tag" to "gün",
        "amir" to "erkek adı", "stuttgart" to "Stuttgart", "gezogen" to "taşındı", "muss" to "zorunda", "seine" to "onun", "neue" to "yeni", "umgebung" to "çevre", "anfangs" to "başlangıçta", "ihm" to "ona", "schnellen" to "hızlı", "gesprächen" to "konuşmalar", "zu" to "-mek için", "folgen" to "takip etmek", "der" to "belirli artikel", "zeit" to "zaman", "nimmt" to "alıyor", "er" to "o (erkek)", "sprachkurs" to "dil kursu", "dort" to "orada", "anderen" to "diğer", "teilnehmern" to "katılımcılar", "fühlt" to "hissediyor", "immer" to "giderek / daima", "sicherer" to "daha güvende",
        "viele" to "birçok", "menschen" to "insanlar", "sind" to "olmak", "ständig" to "sürekli", "erreichbar" to "ulaşılabilir", "ruhe" to "sakinlik", "wer" to "kim / -en kişi", "bewusst" to "bilinçli olarak", "benachrichtigungen" to "bildirimler", "kann" to "-ebilir", "besser" to "daha iyi", "wichtige" to "önemli", "aufgaben" to "görevler", "es" to "o", "nicht" to "değil", "jede" to "her", "nachricht" to "mesaj", "sofort" to "hemen", "beantworten" to "cevaplamak", "entscheidend" to "belirleyici", "technik" to "teknoloji", "so" to "bu şekilde", "nutzen" to "kullanmak", "den" to "belirli artikel", "alltag" to "günlük hayat", "erleichtert" to "kolaylaştırır"
    )
}
