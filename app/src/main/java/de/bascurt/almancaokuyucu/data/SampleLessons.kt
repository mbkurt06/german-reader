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
            "„auf“ bu cümlede edat değil, ayrılabilen fiilin ön ekidir."))
        add(listOf(1 to 1, 1 to 2, 1 to 3, 1 to 10), x("a2-vorbereiten", "sich auf etwas vorbereiten", "bir şeye hazırlanmak",
            "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ burada vorbereiten fiilinin istediği sabit edattır; yön bildirmez."))
        add(listOf(2 to 4, 2 to 5, 2 to 7), x("a2-angst", "Angst vor etwas haben", "bir şeyden korkmak",
            "İsim + edat", "vor + Dativ", "„vor“ bu cümlede korkunun nedenini gösterir; yer bildirmez."))
        add(listOf(2 to 8, 2 to 10, 2 to 11), x("a2-freuen", "sich auf etwas freuen", "bir şeyi sabırsızlıkla beklemek",
            "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ gelecekte beklenen yeni iş arkadaşlarını gösterir."))
        add(listOf(3 to 2, 3 to 4, 3 to 5), x("a2-kuemmern", "sich um jemanden kümmern", "biriyle ilgilenmek",
            "Dönüşlü fiil + edat", "um + Akkusativ", "„um“ ilgilenilen kişileri, yani misafirleri gösterir."))
        add(listOf(4 to 6, 4 to 11), x("a2-zufrieden", "mit etwas zufrieden sein", "bir şeyden memnun olmak",
            "Sıfat + edat", "mit + Dativ", "„mit“ memnuniyetin neyle ilgili olduğunu, ilk iş gününü gösterir."))
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
        add(listOf(0 to 6, 0 to 7, 0 to 8, 0 to 12), x("b1-gewoehnen", "sich an etwas gewöhnen", "bir şeye alışmak",
            "Dönüşlü fiil + edat", "an + Akkusativ", "„an“ alışılan yeni çevreyi gösterir."))
        add(listOf(1 to 1, 1 to 2, 1 to 4), x("b1-schwerfallen", "jemandem schwerfallen", "birine zor gelmek",
            "Fiil kalıbı", "ihm + Dativ", "Bir işi yapmakta zorlanan kişi „ihm“ ile Dativ olarak gösterilir."))
        add(listOf(2 to 3, 2 to 5, 2 to 8), x("b1-teilnehmen", "an etwas teilnehmen", "bir şeye katılmak",
            "Ayrılabilen fiil + edat", "an + Dativ", "„an“ katılınan etkinliği, burada dil kursunu gösterir."))
        add(listOf(3 to 1, 3 to 3, 3 to 6, 3 to 7), x("b1-gespraech", "mit jemandem ins Gespräch kommen", "biriyle konuşmaya başlamak",
            "Sabit ifade", "mit + Dativ", "„mit“ konuşulan kişileri gösterir."))
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
            "Dönüşlü fiil + edat", "nach + Dativ", "„nach“ arzulanan şeyi, yani sakinliği gösterir."))
        add(listOf(1 to 2, 1 to 4), x("b2-verzichten", "auf etwas verzichten", "bir şeyden vazgeçmek",
            "Fiil + edat", "auf + Akkusativ", "„auf“ vazgeçilen şeyi, yani bildirimleri gösterir."))
        add(listOf(1 to 6, 1 to 8, 1 to 11), x("b2-konzentrieren", "sich auf etwas konzentrieren", "bir şeye odaklanmak",
            "Dönüşlü fiil + edat", "auf + Akkusativ", "„auf“ odaklanılan önemli görevleri gösterir."))
        add(listOf(2 to 1, 2 to 3, 2 to 4), x("b2-ankommen", "auf etwas ankommen", "önemli/belirleyici olmak",
            "Ayrılabilen fiil + edat", "darauf … an", "„darauf“ neyin önemli olmadığını gösterir; „an“ ayrılan fiil parçasıdır."))
        return build("b2-balance", "Dijital Dünyada Denge", "B2", "Bildirimler ve dikkat üzerine", texts, groups)
    }

    private fun build(
        id: String,
        title: String,
        level: String,
        summary: String,
        texts: List<String>,
        groups: Map<Pair<Int, Int>, Lexeme>
    ): ReaderLesson {
        val special = mapOf(
            "in" to x("$id-in", "in", "içinde / -de", "Yer edatı", "Wo? + Dativ",
                "„in einem Hotel“ bu cümlede bulunulan yeri, otelin içini gösterir."),
            "im" to x("$id-im", "im (in dem)", "içinde / -de", "Yer edatı", "in + Dativ",
                "„im Hotel“ bu cümlede bulunulan yeri, otelin içini gösterir."),
            "am" to x("$id-am", "am (an dem)", "-de / sırasında", "Zaman edatı", "Wann? + Dativ",
                "„Am Abend“ bu cümlede eylemin ne zaman olduğunu gösterir."),
            "obwohl" to x("$id-obwohl", "obwohl", "-mesine rağmen", "Bağlaç", "Fiil sonda",
                "Bu cümlede beklenenin tersine gerçekleşen iki durum arasında karşıtlık kurar."),
            "aber" to x("$id-aber", "aber", "ama", "Bağlaç", null,
                "Bu cümlede iki düşünce veya durum arasında karşıtlık kurar."),
            "und" to x("$id-und", "und", "ve", "Bağlaç", null,
                "Bu cümlede aynı düzeydeki iki bilgi veya eylemi birbirine ekler."),
            "dass" to x("$id-dass", "dass", "-dığı / ki", "Bağlaç", "Fiil sonda",
                "Tekniğin günlük hayatı kolaylaştırdığı bilgisini yan cümle olarak bağlar.")
        )
        return ReaderLesson(
            id, title, level, summary,
            texts.mapIndexed { si, sentence ->
                sentence.split(" ").mapIndexed { ti, shown ->
                    val clean = shown.trimEnd('.', ',', ':').lowercase()
                    ReadingToken(
                        shown,
                        groups[si to ti] ?: special[clean]
                        ?: Lexeme("$id-$si-$ti", clean, commonMeanings[clean] ?: "Türkçe anlamı")
                    )
                }
            }
        )
    }

    private fun x(id: String, base: String, meaning: String, type: String, grammar: String?, note: String) =
        Lexeme(id, base, meaning, type, grammar, note)

    private val commonMeanings = mapOf(
        "morgen" to "yarın", "steht" to "duruyor", "sehr" to "çok", "früh" to "erken",
        "sie" to "o (kadın)", "ihren" to "onun", "ersten" to "ilk", "arbeitstag" to "iş günü",
        "einem" to "bir", "hotel" to "otel", "ein" to "bir", "wenig" to "az / biraz",
        "fehlern" to "hatalar", "die" to "belirli artikel", "neuen" to "yeni", "kollegen" to "iş arkadaşları",
        "gäste" to "misafirler", "beantwortet" to "cevaplıyor", "ihre" to "onların", "fragen" to "sorular",
        "abend" to "akşam", "ist" to "olmak", "müde" to "yorgun", "tag" to "gün",
        "amir" to "erkek adı", "nach" to "-e / sonrasında", "stuttgart" to "Stuttgart", "gezogen" to "taşındı",
        "muss" to "zorunda", "seine" to "onun", "neue" to "yeni", "umgebung" to "çevre",
        "anfangs" to "başlangıçta", "ihm" to "ona", "schnellen" to "hızlı", "gesprächen" to "konuşmalar",
        "zu" to "-mek için", "folgen" to "takip etmek", "mit" to "ile", "der" to "belirli artikel",
        "zeit" to "zaman", "nimmt" to "alıyor", "er" to "o (erkek)", "sprachkurs" to "dil kursu",
        "dort" to "orada", "anderen" to "diğer", "teilnehmern" to "katılımcılar", "fühlt" to "hissediyor",
        "immer" to "giderek / daima", "sicherer" to "daha güvende", "viele" to "birçok",
        "menschen" to "insanlar", "sind" to "olmak", "ständig" to "sürekli", "erreichbar" to "ulaşılabilir",
        "ruhe" to "sakinlik", "wer" to "kim / -en kişi", "bewusst" to "bilinçli olarak",
        "benachrichtigungen" to "bildirimler", "kann" to "-ebilir", "besser" to "daha iyi",
        "wichtige" to "önemli", "aufgaben" to "görevler", "es" to "o", "nicht" to "değil",
        "jede" to "her", "nachricht" to "mesaj", "sofort" to "hemen", "beantworten" to "cevaplamak",
        "entscheidend" to "belirleyici", "technik" to "teknoloji", "so" to "bu şekilde",
        "nutzen" to "kullanmak", "den" to "belirli artikel", "alltag" to "günlük hayat",
        "erleichtert" to "kolaylaştırır"
    )
}
