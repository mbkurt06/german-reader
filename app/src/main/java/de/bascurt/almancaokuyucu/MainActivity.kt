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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.bascurt.almancaokuyucu.data.SampleLessons
import de.bascurt.almancaokuyucu.data.SavedLexemeStore
import de.bascurt.almancaokuyucu.model.*

private val Turquoise = Color(0xFF1FA7A5)
private val Dark = Color(0xFF102F3C)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = Turquoise)) { GermanReaderApp() } }
    }
}

@Composable private fun GermanReaderApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val store = remember { SavedLexemeStore(context) }
    var saved by remember { mutableStateOf(store.load()) }
    var currentLesson by remember { mutableStateOf<ReaderLesson?>(null) }
    fun toggle(item: Lexeme) {
        saved = if (saved.any { it.id == item.id }) saved.filterNot { it.id == item.id } else saved + item
        store.save(saved)
    }
    currentLesson?.let {
        ReaderScreen(it, saved, ::toggle) { currentLesson = null }
    } ?: HomeScreen(SampleLessons.all) { currentLesson = it }
}

@Composable private fun HomeScreen(lessons: List<ReaderLesson>, onLessonClick: (ReaderLesson) -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF7FAFA))) {
        Column(
            Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF07171F), Dark)))
                .padding(horizontal = 24.dp, vertical = 34.dp)
        ) {
            Text("Almanca Okuyucu", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Okumak istediğin metni seç", color = Color(0xFFD5E4E8), fontSize = 17.sp)
        }
        Column(
            Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            lessons.forEach { lesson ->
                Card(
                    Modifier.fillMaxWidth().clickable { onLessonClick(lesson) },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = levelColor(lesson.level), shape = RoundedCornerShape(12.dp)) {
                            Text(lesson.level, Modifier.padding(horizontal = 14.dp, vertical = 12.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(16.dp))
                        Column(Modifier.weight(1f)) {
                            Text(lesson.title, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(5.dp))
                            Text(lesson.summary, color = Color(0xFF68777D), fontSize = 15.sp)
                        }
                        Text("›", fontSize = 34.sp, color = Turquoise)
                    }
                }
            }
        }
    }
}

@Composable private fun ReaderScreen(
    lesson: ReaderLesson,
    saved: List<Lexeme>,
    onToggleSaved: (Lexeme) -> Unit,
    onHome: () -> Unit
) {
    var tab by remember(lesson.id) { mutableStateOf(ReaderTab.STORY) }
    var selected by remember(lesson.id) { mutableStateOf<Lexeme?>(null) }
    val isSaved = selected?.let { item -> saved.any { it.id == item.id } } == true
    Column(Modifier.fillMaxSize().background(Color.White)) {
        if (tab == ReaderTab.STORY) {
            MeaningPanel(selected, isSaved, { selected?.let(onToggleSaved) }, onHome)
        } else SectionHeader(lesson.title, onHome)
        ReaderTabs(tab) { tab = it }
        when (tab) {
            ReaderTab.STORY -> StoryScreen(lesson, selected) { selected = it }
            ReaderTab.QUIZ -> QuizScreen(lesson)
            ReaderTab.WORDS -> SavedWordsScreen(saved, onToggleSaved)
            ReaderTab.GRAMMAR -> GrammarScreen(lesson)
        }
    }
}

@Composable private fun MeaningPanel(selected: Lexeme?, isSaved: Boolean, onSave: () -> Unit, onHome: () -> Unit) {
    Box(
        Modifier.fillMaxWidth().height(278.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFF07171F), Dark)))
            .padding(horizontal = 22.dp, vertical = 18.dp)
    ) {
        Text("‹ Metinler", color = Color.White, fontSize = 17.sp, modifier = Modifier.clickable(onClick = onHome).padding(6.dp))
        Text(
            if (isSaved) "★" else "☆",
            color = if (selected == null) Color.White.copy(alpha = .35f) else Color.White,
            fontSize = 38.sp,
            modifier = Modifier.align(Alignment.TopEnd).clickable(enabled = selected != null, onClick = onSave).padding(4.dp)
        )
        Column(Modifier.align(Alignment.BottomStart)) {
            if (selected == null) {
                Text("Bir kelimeye dokun", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(7.dp))
                Text("Anlamı ve bu cümledeki görevi burada görünecek.", color = Color(0xFFD5E4E8), fontSize = 16.sp)
            } else {
                Text(selected.base, color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(5.dp))
                Text(selected.meaning, color = Color.White, fontSize = 20.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                    DarkPill(selected.type)
                    selected.grammar?.let { DarkPill(it) }
                }
                selected.explanation?.let {
                    Spacer(Modifier.height(9.dp))
                    Text(it, color = Color(0xFFD5E4E8), fontSize = 15.sp, lineHeight = 20.sp)
                }
            }
        }
    }
}

@Composable private fun SectionHeader(title: String, onHome: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().height(112.dp).background(Dark).padding(horizontal = 22.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("‹ Metinler", color = Color.White, fontSize = 17.sp, modifier = Modifier.clickable(onClick = onHome).padding(4.dp))
        Text(title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable private fun ReaderTabs(active: ReaderTab, onSelect: (ReaderTab) -> Unit) {
    val tabs = listOf(ReaderTab.STORY to "Hikâye", ReaderTab.QUIZ to "Sınav", ReaderTab.WORDS to "Kelimeler", ReaderTab.GRAMMAR to "Gramer")
    Row(Modifier.fillMaxWidth().padding(horizontal = 7.dp, vertical = 9.dp), horizontalArrangement = Arrangement.SpaceAround) {
        tabs.forEach { (tab, label) ->
            Surface(
                color = if (tab == active) Turquoise else Color.Transparent,
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.clickable { onSelect(tab) }
            ) {
                Text(label, Modifier.padding(horizontal = 11.dp, vertical = 7.dp), color = if (tab == active) Color.White else Color.Black, fontSize = 17.sp)
            }
        }
    }
    HorizontalDivider(color = Color(0xFFE8E8E8))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun StoryScreen(lesson: ReaderLesson, selected: Lexeme?, onSelect: (Lexeme) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 17.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(color = levelColor(lesson.level), shape = RoundedCornerShape(9.dp)) {
                Text(lesson.level, Modifier.padding(horizontal = 12.dp, vertical = 7.dp), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Text(lesson.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(21.dp))
        lesson.sentences.forEach { sentence ->
            FlowRow(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                sentence.forEach { token ->
                    val active = selected?.id == token.lexeme.id
                    Text(
                        token.text, fontSize = 22.sp, lineHeight = 32.sp,
                        color = if (active) Color.White else Color(0xFF202124),
                        modifier = Modifier.background(if (active) Turquoise else Color.Transparent, RoundedCornerShape(6.dp))
                            .clickable { onSelect(token.lexeme) }.padding(horizontal = 2.dp, vertical = 2.dp)
                    )
                }
            }
            Spacer(Modifier.height(15.dp))
        }
    }
}

@Composable private fun QuizScreen(lesson: ReaderLesson) {
    val questions = remember(lesson.id) { lesson.lexemes.filter { it.meaning != "Türkçe anlamı" }.take(12) }
    var index by remember(lesson.id) { mutableIntStateOf(0) }
    var answer by remember(lesson.id, index) { mutableStateOf<String?>(null) }
    if (questions.isEmpty()) return
    val question = questions[index % questions.size]
    val options = remember(index, lesson.id) {
        (listOf(question) + questions.filter { it.id != question.id }).take(4).map { it.base }
    }
    Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Soru " + (index + 1) + " / " + questions.size, color = Turquoise, fontWeight = FontWeight.Bold)
        Text("“" + question.meaning + "” anlamına gelen Almanca ifade hangisi?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        options.forEach { option ->
            OutlinedButton(onClick = { answer = option }, modifier = Modifier.fillMaxWidth()) {
                Text(option, Modifier.fillMaxWidth(), fontSize = 17.sp)
            }
        }
        answer?.let {
            Text(
                if (it == question.base) "Doğru ✓" else "Doğru cevap: " + question.base,
                color = if (it == question.base) Color(0xFF16845B) else Color(0xFFC34232),
                fontWeight = FontWeight.Bold
            )
            Button(onClick = { index = (index + 1) % questions.size }, modifier = Modifier.align(Alignment.End)) { Text("Sonraki") }
        }
    }
}

@Composable private fun SavedWordsScreen(saved: List<Lexeme>, onRemove: (Lexeme) -> Unit) {
    if (saved.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Henüz kaydedilmiş kelime veya ifade yok.", color = Color.Gray)
        }
        return
    }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        saved.asReversed().forEach { item ->
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F8F8))) {
                Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.Top) {
                    Column(Modifier.weight(1f)) {
                        Text(item.base, fontSize = 19.sp, fontWeight = FontWeight.Bold)
                        Text(item.meaning, color = Color(0xFF53666D))
                        Text(item.type, color = Turquoise, fontSize = 13.sp)
                        item.explanation?.let { Text(it, Modifier.padding(top = 6.dp), fontSize = 14.sp) }
                    }
                    Text("★", color = Turquoise, fontSize = 28.sp, modifier = Modifier.clickable { onRemove(item) }.padding(5.dp))
                }
            }
        }
    }
}

@Composable private fun GrammarScreen(lesson: ReaderLesson) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        lesson.grammarItems.forEach { item ->
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))) {
                Column(Modifier.fillMaxWidth().padding(15.dp)) {
                    Text(item.base, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(item.meaning, color = Color(0xFF53666D))
                    Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                        LightPill(item.type)
                        item.grammar?.let { LightPill(it) }
                    }
                    item.explanation?.let { Text(it, Modifier.padding(top = 8.dp), fontSize = 14.sp) }
                }
            }
        }
    }
}

@Composable private fun DarkPill(text: String) {
    Surface(color = Color.White.copy(alpha = .14f), shape = RoundedCornerShape(20.dp)) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = Color(0xFFE6F5F5), fontSize = 13.sp)
    }
}

@Composable private fun LightPill(text: String) {
    Surface(color = Color(0xFFDCEEEE), shape = RoundedCornerShape(20.dp)) {
        Text(text, Modifier.padding(horizontal = 9.dp, vertical = 5.dp), color = Color(0xFF275A5B), fontSize = 12.sp)
    }
}

private fun levelColor(level: String) = when (level) {
    "A2" -> Color(0xFFE2F4E6)
    "B1" -> Color(0xFFE2F0FA)
    else -> Color(0xFFF1E6FA)
}
