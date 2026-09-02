package de.bascurt.almancaokuyucu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Turquoise = Color(0xFF1FA7A5)
data class Lexeme(
    val id: String,
    val base: String,
    val meaning: String,
    val type: String = "Kelime",
    val grammar: String? = null,
    val explanation: String? = null
)
data class ReadingToken(val text: String, val lexeme: Lexeme)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = Turquoise)) { ReaderScreen() } }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun ReaderScreen() {
    var selected by remember { mutableStateOf<Lexeme?>(null) }
    val story = remember { sampleStory() }
    val context = LocalContext.current
    val preferences = remember { context.getSharedPreferences("learning_progress", android.content.Context.MODE_PRIVATE) }
    var savedIds by remember {
        mutableStateOf(preferences.getStringSet("saved_lexemes", emptySet()).orEmpty().toSet())
    }
    fun toggleSaved(lexeme: Lexeme) {
        savedIds = if (lexeme.id in savedIds) savedIds - lexeme.id else savedIds + lexeme.id
        preferences.edit().putStringSet("saved_lexemes", savedIds).apply()
    }
    Column(Modifier.fillMaxSize().background(Color.White)) {
        MeaningPanel(
            selected = selected,
            isSaved = selected?.id?.let { it in savedIds } == true,
            onToggleSaved = { selected?.let(::toggleSaved) }
        )
        ReaderTabs()
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = Color(0xFFE4F4F3), shape = RoundedCornerShape(9.dp)) {
                    Text("A2", Modifier.padding(horizontal = 12.dp, vertical = 7.dp), fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(12.dp))
                Text("Yeni Bir Başlangıç", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(22.dp))
            story.forEach { sentence ->
                FlowRow(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    sentence.forEach { token ->
                        val active = selected?.id == token.lexeme.id
                        Text(
                            token.text,
                            fontSize = 22.sp,
                            lineHeight = 32.sp,
                            color = if (active) Color.White else Color(0xFF202124),
                            modifier = Modifier
                                .background(if (active) Turquoise else Color.Transparent, RoundedCornerShape(6.dp))
                                .clickable { selected = token.lexeme }
                                .padding(horizontal = 2.dp, vertical = 2.dp)
                        )
                    }
                }
                Spacer(Modifier.height(15.dp))
            }
        }
    }
}

@Composable private fun MeaningPanel(
    selected: Lexeme?,
    isSaved: Boolean,
    onToggleSaved: () -> Unit
) {
    Box(
        Modifier.fillMaxWidth().height(275.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFF07171F), Color(0xFF153B49))))
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Text(
            text = if (isSaved) "★" else "☆",
            color = if (selected == null) Color.White.copy(alpha = 0.35f) else Color.White,
            fontSize = 38.sp,
            modifier = Modifier.align(Alignment.TopEnd)
                .clickable(enabled = selected != null, onClick = onToggleSaved)
                .padding(4.dp)
        )
        Column(Modifier.align(Alignment.BottomStart)) {
            if (selected == null) {
                Text("Bir kelimeye dokun", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Text("Anlamı ve ait olduğu yapı burada görünecek.", color = Color(0xFFD5E4E8), fontSize = 17.sp)
            } else {
                Text(selected.base, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(7.dp))
                Text(selected.meaning, color = Color.White, fontSize = 21.sp)
                Spacer(Modifier.height(9.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoPill(selected.type)
                    selected.grammar?.let { InfoPill(it) }
                }
                selected.explanation?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(it, color = Color(0xFFD5E4E8), fontSize = 15.sp, lineHeight = 20.sp)
                }
                if (isSaved) {
                    Spacer(Modifier.height(5.dp))
                    Text("★ Kaydedildi", color = Color(0xFF8FE0DD), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable private fun InfoPill(text: String) {
    Surface(color = Color.White.copy(alpha = 0.14f), shape = RoundedCornerShape(20.dp)) {
        Text(text, Modifier.padding(horizontal = 11.dp, vertical = 6.dp), color = Color(0xFFE6F5F5), fontSize = 13.sp)
    }
}

@Composable private fun ReaderTabs() {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = Turquoise, shape = RoundedCornerShape(20.dp)) {
            Text("Hikâye", Modifier.padding(horizontal = 14.dp, vertical = 7.dp), color = Color.White, fontSize = 18.sp)
        }
        Text("Sınav", fontSize = 18.sp)
        Text("Kelimeler", fontSize = 18.sp)
        Text("Gramer", fontSize = 18.sp)
    }
    HorizontalDivider(color = Color(0xFFE8E8E8))
}

private fun sampleStory(): List<List<ReadingToken>> {
    val sentences = listOf(
        "Morgen steht Elif sehr früh auf.",
        "Sie bereitet sich auf ihren ersten Arbeitstag in einem Hotel vor.",
        "Obwohl sie ein wenig Angst vor Fehlern hat, freut sie sich auf die neuen Kollegen.",
        "Im Hotel kümmert sie sich um die Gäste und beantwortet ihre Fragen.",
        "Am Abend ist Elif müde, aber mit ihrem ersten Tag sehr zufrieden.",
        "Sie merkt: Ein neuer Anfang kann schwer sein, aber er bringt auch neue Möglichkeiten."
    )
    fun p(id: String, base: String, tr: String, type: String, grammar: String, note: String) =
        Lexeme(id, base, tr, type, grammar, note)
    val aufstehen = p(
        "aufstehen", "aufstehen", "kalkmak", "Ayrılabilen fiil", "steht … auf",
        "Buradaki „auf“ bir edat değil, ayrılabilen fiilin ön ekidir. Cümlede fiilden ayrı ve sonda durur."
    )
    val vorbereiten = p(
        "vorbereiten", "sich auf etwas vorbereiten", "bir şeye hazırlanmak", "Dönüşlü fiil + edat", "auf + Akkusativ",
        "„auf“ burada yön veya bir şeyin üstü anlamında değildir; vorbereiten fiilinin istediği sabit edattır."
    )
    val angst = p(
        "angst", "Angst vor etwas haben", "bir şeyden korkmak", "İsim + edat", "vor + Dativ",
        "„vor“ burada mekânsal olarak „önünde“ değil, korkunun sebebini/hedefini gösterir: hatalardan korkmak."
    )
    val freuen = p(
        "freuen", "sich auf etwas freuen", "bir şeyi sabırsızlıkla beklemek", "Dönüşlü fiil + edat", "auf + Akkusativ",
        "„auf“ gelecekte olacak bir şeyi sevinçle beklemeyi anlatır. Burada kelime kelime „üzerine“ diye çevrilmez."
    )
    val kuemmern = p(
        "kuemmern", "sich um jemanden kümmern", "biriyle ilgilenmek", "Dönüşlü fiil + edat", "um + Akkusativ",
        "„um“ burada „etrafında“ anlamı taşımaz; kümmern fiiliyle birlikte ilgilenilen kişiyi gösterir."
    )
    val zufrieden = p(
        "zufrieden", "mit etwas zufrieden sein", "bir şeyden memnun olmak", "Sıfat + edat", "mit + Dativ",
        "„mit“ memnuniyetin hangi kişi veya şeyle ilgili olduğunu gösterir. Cümlede öğeler ayrı dursa da tek yapıdır."
    )
    val groups = buildMap<Pair<Int, Int>, Lexeme> {
        listOf(1, 5).forEach { put(0 to it, aufstehen) }
        listOf(1, 2, 3, 10).forEach { put(1 to it, vorbereiten) }
        listOf(4, 5, 7).forEach { put(2 to it, angst) }
        listOf(8, 10, 11).forEach { put(2 to it, freuen) }
        listOf(2, 4, 5).forEach { put(3 to it, kuemmern) }
        listOf(6, 11).forEach { put(4 to it, zufrieden) }
    }
    val meanings = mapOf(
        "morgen" to "yarın", "elif" to "kadın adı", "sehr" to "çok", "früh" to "erken",
        "sie" to "o (kadın)", "ihren" to "onun", "ersten" to "ilk", "arbeitstag" to "iş günü",
        "in" to "içinde / -de", "einem" to "bir", "hotel" to "otel", "obwohl" to "-mesine rağmen",
        "ein" to "bir", "wenig" to "az / biraz", "fehlern" to "hatalar", "die" to "belirli artikel",
        "neuen" to "yeni", "kollegen" to "iş arkadaşları", "im" to "-de / içinde", "gäste" to "misafirler",
        "und" to "ve", "beantwortet" to "cevaplıyor", "ihre" to "onların", "fragen" to "sorular",
        "am" to "-de / sırasında", "abend" to "akşam", "ist" to "olmak", "müde" to "yorgun",
        "aber" to "ama", "ihrem" to "onun", "tag" to "gün", "merkt" to "fark ediyor",
        "neuer" to "yeni", "anfang" to "başlangıç", "kann" to "-ebilir", "schwer" to "zor",
        "sein" to "olmak", "er" to "o (erkek)", "bringt" to "getiriyor", "auch" to "ayrıca / de",
        "neue" to "yeni", "möglichkeiten" to "imkânlar, olasılıklar"
    )
    val explanations = mapOf(
        "in" to "„in einem Hotel“: Wo? sorusuna cevap verir. Bir binanın içinde bulunmayı anlatır ve Dativ kullanılır. Yönelme olsaydı „in den Supermarkt“ denirdi: Wohin? + Akkusativ. „drinnen“ içeride, „draußen“ dışarıda demektir.",
        "im" to "„im“ = „in dem“. Burada Wo? sorusuna cevap verir: otelin/binanın içinde bulunma. Bu nedenle Dativ kullanılır.",
        "am" to "„am“ = „an dem“. Burada yer değil zaman bildirir: „Am Abend“ = akşamleyin. Wann? sorusuna cevap verir.",
        "obwohl" to "Yan cümle bağlacıdır. Beklenenin tersine gerçekleşen durumu anlatır: korkmasına rağmen seviniyor. Çekimli fiil yan cümlenin sonuna gider.",
        "aber" to "İki düşünce arasında karşıtlık kurar: yorgun ama memnun. „obwohl“dan farklı olarak normal ana cümle söz dizimini değiştirmez.",
        "und" to "Aynı düzeydeki iki eylemi veya bilgiyi birbirine ekler: ilgileniyor ve cevaplıyor."
    )
    val specialTypes = mapOf(
        "in" to "Yer edatı", "im" to "Yer edatı", "am" to "Zaman edatı",
        "obwohl" to "Bağlaç", "aber" to "Bağlaç", "und" to "Bağlaç"
    )
    val specialGrammar = mapOf(
        "in" to "Wo? + Dativ", "im" to "in dem + Dativ", "am" to "an dem + Dativ"
    )
    return sentences.mapIndexed { si, sentence ->
        sentence.split(" ").mapIndexed { ti, shown ->
            val clean = shown.trimEnd('.', ',', ':').lowercase()
            ReadingToken(
                shown,
                groups[si to ti] ?: Lexeme(
                    id = "$si-$ti",
                    base = clean,
                    meaning = meanings[clean] ?: "anlam eklenecek",
                    type = specialTypes[clean] ?: "Kelime",
                    grammar = specialGrammar[clean],
                    explanation = explanations[clean]
                )
            )
        }
    }
}

