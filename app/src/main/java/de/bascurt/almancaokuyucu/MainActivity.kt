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
private enum class AppPage { HOME, MY_WORDS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme(colorScheme = lightColorScheme(primary = Turquoise)) { GermanReaderApp() } }
    }
}

@Composable private fun GermanReaderApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val store = remember { SavedLexemeStore(context) }
    val canonical = remember { SampleLessons.all.flatMap { it.lexemes }.associateBy { it.id } }
    var saved by remember {
        val refreshed = store.load().map { canonical[it.id] ?: it }
        store.save(refreshed)
        mutableStateOf(refreshed)
    }
    var currentLesson by remember { mutableStateOf<ReaderLesson?>(null) }
    var page by remember { mutableStateOf(AppPage.HOME) }
    fun toggle(item: Lexeme) {
        saved = if (saved.any { it.id == item.id }) saved.filterNot { it.id == item.id } else saved + item
        store.save(saved)
    }
    when {
        currentLesson != null -> ReaderScreen(currentLesson!!, saved, ::toggle) { currentLesson = null }
        page == AppPage.MY_WORDS -> MyWordsScreen(SampleLessons.all, saved, ::toggle) { page = AppPage.HOME }
        else -> HomeScreen(SampleLessons.all, { currentLesson = it }) { page = AppPage.MY_WORDS }
    }
}

@Composable private fun HomeScreen(lessons: List<ReaderLesson>, onLessonClick: (ReaderLesson) -> Unit, onMyWords: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFFF7FAFA))) {
        Column(Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(Color(0xFF07171F), Dark))).padding(horizontal = 24.dp, vertical = 30.dp)) {
            Text("Almanca Okuyucu", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp)); Text("Okumak istediğin metni seç", color = Color(0xFFD5E4E8), fontSize = 17.sp)
            Spacer(Modifier.height(18.dp)); OutlinedButton(onClick = onMyWords, colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)) { Text("★  Kelimelerim") }
        }
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            lessons.forEach { lesson ->
                Card(Modifier.fillMaxWidth().clickable { onLessonClick(lesson) }, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = levelColor(lesson.level), shape = RoundedCornerShape(12.dp)) { Text(lesson.level, Modifier.padding(horizontal = 14.dp, vertical = 12.dp), fontSize = 18.sp, fontWeight = FontWeight.Bold) }
                        Spacer(Modifier.width(16.dp)); Column(Modifier.weight(1f)) { Text(lesson.title, fontSize = 21.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(5.dp)); Text(lesson.summary, color = Color(0xFF68777D), fontSize = 15.sp) }
                        Text("›", fontSize = 34.sp, color = Turquoise)
                    }
                }
            }
        }
    }
}

@Composable private fun ReaderScreen(lesson: ReaderLesson, saved: List<Lexeme>, onToggleSaved: (Lexeme) -> Unit, onHome: () -> Unit) {
    var tab by remember(lesson.id) { mutableStateOf(ReaderTab.STORY) }
    var selected by remember(lesson.id) { mutableStateOf<Lexeme?>(null) }
    val isSaved = selected?.let { item -> saved.any { it.id == item.id } } == true
    Column(Modifier.fillMaxSize().background(Color.White)) {
        if (tab == ReaderTab.STORY) MeaningPanel(selected, isSaved, { selected?.let(onToggleSaved) }, onHome) else SectionHeader(lesson.title, onHome)
        ReaderTabs(tab) { tab = it }
        Box(Modifier.weight(1f)) {
            when (tab) {
                ReaderTab.STORY -> StoryScreen(lesson, selected) { selected = it }
                ReaderTab.QUIZ -> QuizScreen(lesson)
                ReaderTab.WORDS -> LessonWordsScreen(lesson, saved, onToggleSaved)
                ReaderTab.GRAMMAR -> GrammarScreen(lesson)
            }
        }
    }
}

@Composable private fun MeaningPanel(selected: Lexeme?, isSaved: Boolean, onSave: () -> Unit, onHome: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(350.dp).background(Brush.verticalGradient(listOf(Color(0xFF07171F), Dark))).padding(horizontal = 22.dp, vertical = 16.dp)) {
        Text("‹ Metinler", color = Color.White, fontSize = 17.sp, modifier = Modifier.clickable(onClick = onHome).padding(6.dp))
        Text(if (isSaved) "★" else "☆", color = if (selected == null) Color.White.copy(alpha = .35f) else Color.White, fontSize = 38.sp, modifier = Modifier.align(Alignment.TopEnd).clickable(enabled = selected != null, onClick = onSave).padding(4.dp))
        Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().heightIn(max = 275.dp).verticalScroll(rememberScrollState())) {
            if (selected == null) {
                Text("Bir kelimeye dokun", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(7.dp)); Text("Anlamı ve bu cümledeki görevi burada görünecek.", color = Color(0xFFD5E4E8), fontSize = 16.sp)
            } else {
                Text(selected.base, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp)); Text(selected.meaning, color = Color.White, fontSize = 19.sp)
                Spacer(Modifier.height(8.dp)); Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { DarkPill("Kelime türü: ${selected.wordClass}"); selected.grammar?.let { DarkPill(it) } }
                MorphologyDetails(selected)
                selected.explanation?.let { Spacer(Modifier.height(8.dp)); Text(it, color = Color(0xFFD5E4E8), fontSize = 14.sp, lineHeight = 19.sp) }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable private fun MorphologyDetails(item: Lexeme) {
    when (item.wordClass) {
        "Fiil" -> {
            item.infinitive?.let { DetailLine("Mastar", it) }
            item.thirdPerson?.let { DetailLine("3. tekil şahıs", it) }
            item.preterite?.let { DetailLine("Präteritum", it) }
            item.perfect?.let { DetailLine("Perfekt", it) }
        }
        "İsim" -> {
            item.article?.let { DetailLine("Artikel", it) }
            item.plural?.let { DetailLine("Çoğul", it) }
            item.accusativeNote?.let { DetailLine("Akkusativ", it) }
        }
        "Sıfat" -> {
            item.positive?.let { DetailLine("Yalın hâl", it) }
            item.comparative?.let { DetailLine("Komparativ", it) }
            item.superlative?.let { DetailLine("Superlativ", it) }
        }
    }
}

@Composable private fun DetailLine(label: String, value: String) {
    Row(Modifier.padding(top = 4.dp)) { Text("$label: ", color = Color(0xFF9ED7D6), fontWeight = FontWeight.SemiBold, fontSize = 14.sp); Text(value, color = Color.White, fontSize = 14.sp) }
}

@Composable private fun SectionHeader(title: String, onHome: () -> Unit) {
    Column(Modifier.fillMaxWidth().height(112.dp).background(Dark).padding(horizontal = 22.dp, vertical = 16.dp), verticalArrangement = Arrangement.SpaceBetween) {
        Text("‹ Metinler", color = Color.White, fontSize = 17.sp, modifier = Modifier.clickable(onClick = onHome).padding(4.dp)); Text(title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable private fun ReaderTabs(active: ReaderTab, onSelect: (ReaderTab) -> Unit) {
    val tabs = listOf(ReaderTab.STORY to "Hikâye", ReaderTab.QUIZ to "Sınav", ReaderTab.WORDS to "Kelimeler", ReaderTab.GRAMMAR to "Gramer")
    Row(Modifier.fillMaxWidth().padding(horizontal = 7.dp, vertical = 9.dp), horizontalArrangement = Arrangement.SpaceAround) {
        tabs.forEach { (tab, label) -> Surface(color = if (tab == active) Turquoise else Color.Transparent, shape = RoundedCornerShape(20.dp), modifier = Modifier.clickable { onSelect(tab) }) { Text(label, Modifier.padding(horizontal = 11.dp, vertical = 7.dp), color = if (tab == active) Color.White else Color.Black, fontSize = 17.sp) } }
    }
    HorizontalDivider(color = Color(0xFFE8E8E8))
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun StoryScreen(lesson: ReaderLesson, selected: Lexeme?, onSelect: (Lexeme) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(start = 20.dp, end = 20.dp, top = 17.dp, bottom = 96.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { Surface(color = levelColor(lesson.level), shape = RoundedCornerShape(9.dp)) { Text(lesson.level, Modifier.padding(horizontal = 12.dp, vertical = 7.dp), fontWeight = FontWeight.Bold) }; Spacer(Modifier.width(12.dp)); Text(lesson.title, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(21.dp))
        lesson.sentences.forEach { sentence ->
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                sentence.forEach { token -> val active = selected?.id == token.lexeme.id; Text(token.text, fontSize = 22.sp, lineHeight = 32.sp, color = if (active) Color.White else Color(0xFF202124), modifier = Modifier.background(if (active) Turquoise else Color.Transparent, RoundedCornerShape(6.dp)).clickable { onSelect(token.lexeme) }.padding(horizontal = 2.dp, vertical = 2.dp)) }
            }
            Spacer(Modifier.height(15.dp))
        }
    }
}

@Composable private fun QuizScreen(lesson: ReaderLesson) {
    val questions = remember(lesson.id) { lesson.quizItems }; var index by remember(lesson.id) { mutableIntStateOf(0) }; var answer by remember(lesson.id, index) { mutableStateOf<String?>(null) }
    if (questions.isEmpty()) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Bu metin için henüz uygun sınav kelimesi yok.") }; return }
    val question = questions[index % questions.size]
    val optionPool = remember(lesson.id, index) { (SampleLessons.all.flatMap { it.quizItems }.filter { it.id != question.id }.shuffled().take(3) + question).shuffled() }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Soru ${index + 1} / ${questions.size}", color = Turquoise, fontWeight = FontWeight.Bold); Text("“${question.meaning}” anlamına gelen Almanca ifade hangisi?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        optionPool.forEach { option -> OutlinedButton(onClick = { answer = option.base }, modifier = Modifier.fillMaxWidth()) { Text(option.base, Modifier.fillMaxWidth(), fontSize = 17.sp) } }
        answer?.let { Text(if (it == question.base) "Doğru ✓" else "Doğru cevap: ${question.base}", color = if (it == question.base) Color(0xFF16845B) else Color(0xFFC34232), fontWeight = FontWeight.Bold); Button(onClick = { index = (index + 1) % questions.size }, modifier = Modifier.align(Alignment.End)) { Text("Sonraki") } }
    }
}

@Composable private fun LessonWordsScreen(lesson: ReaderLesson, saved: List<Lexeme>, onRemove: (Lexeme) -> Unit) {
    val items = saved.filter { savedItem -> lesson.lexemes.any { it.id == savedItem.id } }
    WordCards(items, onRemove, "Bu hikâyeden henüz kelime kaydetmedin.")
}

@Composable private fun MyWordsScreen(lessons: List<ReaderLesson>, saved: List<Lexeme>, onRemove: (Lexeme) -> Unit, onBack: () -> Unit) {
    var lessonFilter by remember { mutableStateOf<String?>(null) }; var typeFilter by remember { mutableStateOf<String?>(null) }; var selectedIds by remember { mutableStateOf(setOf<String>()) }
    val lessonByLexeme = remember(lessons) { lessons.flatMap { lesson -> lesson.lexemes.map { it.id to lesson } }.toMap() }
    val savedLessonIds = saved.mapNotNull { lessonByLexeme[it.id]?.id }.toSet()
    val availableLessons = lessons.filter { it.id in savedLessonIds }
    val wordTypes = listOf("Fiil", "İsim", "Sıfat", "Zarf", "Edat", "Bağlaç", "Zamir", "Artikel", "Belirleyici", "Parçacık", "Özel isim", "Diğer")
    val filtered = saved.filter { item -> (lessonFilter == null || lessonByLexeme[item.id]?.id == lessonFilter) && (typeFilter == null || item.wordClass == typeFilter) }
    Column(Modifier.fillMaxSize().background(Color(0xFFF7FAFA))) {
        Column(Modifier.fillMaxWidth().background(Dark).padding(20.dp)) { Text("‹ Ana sayfa", color = Color.White, modifier = Modifier.clickable(onClick = onBack).padding(4.dp)); Spacer(Modifier.height(12.dp)); Text("Kelimelerim", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold); Text("${saved.size} kayıtlı kelime / ifade", color = Color(0xFFD5E4E8)) }
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            FilterDropdown("Hikâye", lessonFilter, availableLessons.map { it.id to it.title }) { lessonFilter = it }
            FilterDropdown("Kelime türü", typeFilter, wordTypes.map { it to it }) { typeFilter = it }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = { selectedIds = if (filtered.isNotEmpty() && filtered.all { it.id in selectedIds }) selectedIds - filtered.map { it.id }.toSet() else selectedIds + filtered.map { it.id } }) { Text(if (filtered.isNotEmpty() && filtered.all { it.id in selectedIds }) "Seçimi kaldır" else "Tümünü seç") }
                Spacer(Modifier.weight(1f)); Text("${selectedIds.size} seçili")
            }
            Button(onClick = {}, enabled = selectedIds.isNotEmpty(), modifier = Modifier.fillMaxWidth()) { Text("Seçilenlerle çalış (${selectedIds.size})") }
            filtered.asReversed().forEach { item -> SelectableWordCard(item, item.id in selectedIds, { selectedIds = if (item.id in selectedIds) selectedIds - item.id else selectedIds + item.id }, { onRemove(item); selectedIds = selectedIds - item.id }, lessonByLexeme[item.id]?.title) }
            if (filtered.isEmpty()) Text("Bu filtrelerde kayıtlı kelime yok.", color = Color.Gray, modifier = Modifier.padding(20.dp))
            Spacer(Modifier.height(40.dp))
        }
    }
}

@Composable private fun FilterDropdown(label: String, selectedId: String?, options: List<Pair<String, String>>, onSelect: (String?) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.first == selectedId }?.second ?: "Tümü"
    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start) { Text(label, fontSize = 12.sp, color = Color.Gray); Text(selectedLabel, fontSize = 16.sp) }
            Text("▾", fontSize = 20.sp)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(0.9f)) {
            DropdownMenuItem(text = { Text("Tümü") }, leadingContent = { Checkbox(checked = selectedId == null, onCheckedChange = null) }, onClick = { onSelect(null); expanded = false })
            options.forEach { (id, title) -> DropdownMenuItem(text = { Text(title) }, leadingContent = { Checkbox(checked = selectedId == id, onCheckedChange = null) }, onClick = { onSelect(id); expanded = false }) }
        }
    }
}

@Composable private fun WordCards(items: List<Lexeme>, onRemove: (Lexeme) -> Unit, emptyText: String) {
    if (items.isEmpty()) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(emptyText, color = Color.Gray) }; return }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.asReversed().forEach { item -> Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F8F8))) { Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.Top) { Column(Modifier.weight(1f)) { Text(item.base, fontSize = 19.sp, fontWeight = FontWeight.Bold); Text(item.meaning, color = Color(0xFF53666D)); Text(item.wordClass, color = Turquoise, fontSize = 13.sp); item.explanation?.let { Text(it, Modifier.padding(top = 6.dp), fontSize = 14.sp) } }; Text("★", color = Turquoise, fontSize = 28.sp, modifier = Modifier.clickable { onRemove(item) }.padding(5.dp)) } } }
    }
}

@Composable private fun SelectableWordCard(item: Lexeme, selected: Boolean, onSelect: () -> Unit, onRemove: () -> Unit, lessonTitle: String?) {
    Card(colors = CardDefaults.cardColors(containerColor = if (selected) Color(0xFFE1F4F3) else Color.White), modifier = Modifier.fillMaxWidth().clickable(onClick = onSelect)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) { Checkbox(checked = selected, onCheckedChange = { onSelect() }); Column(Modifier.weight(1f).padding(start = 6.dp)) { Text(item.base, fontSize = 18.sp, fontWeight = FontWeight.Bold); Text(item.meaning, color = Color(0xFF53666D)); Text(listOfNotNull(lessonTitle, item.wordClass).joinToString(" • "), color = Turquoise, fontSize = 12.sp) }; Text("★", color = Turquoise, fontSize = 26.sp, modifier = Modifier.clickable(onClick = onRemove).padding(4.dp)) }
    }
}

@Composable private fun GrammarScreen(lesson: ReaderLesson) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        lesson.grammarItems.forEach { item -> Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))) { Column(Modifier.fillMaxWidth().padding(15.dp)) { Text(item.base, fontSize = 18.sp, fontWeight = FontWeight.Bold); Text(item.meaning, color = Color(0xFF53666D)); Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) { LightPill(item.wordClass); item.grammar?.let { LightPill(it) } }; item.explanation?.let { Text(it, Modifier.padding(top = 8.dp), fontSize = 14.sp) } } } }
    }
}

@Composable private fun DarkPill(text: String) { Surface(color = Color.White.copy(alpha = .14f), shape = RoundedCornerShape(20.dp)) { Text(text, Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = Color(0xFFE6F5F5), fontSize = 13.sp) } }
@Composable private fun LightPill(text: String) { Surface(color = Color(0xFFDCEEEE), shape = RoundedCornerShape(20.dp)) { Text(text, Modifier.padding(horizontal = 9.dp, vertical = 5.dp), color = Color(0xFF275A5B), fontSize = 12.sp) } }
private fun levelColor(level: String) = when (level) { "A2" -> Color(0xFFE2F4E6); "B1" -> Color(0xFFE2F0FA); else -> Color(0xFFF1E6FA) }
