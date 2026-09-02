package de.bascurt.almancaokuyucu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.bascurt.almancaokuyucu.data.LearningStats
import de.bascurt.almancaokuyucu.data.SampleLessons
import de.bascurt.almancaokuyucu.data.SavedLexemeStore
import de.bascurt.almancaokuyucu.data.UserPreferences
import de.bascurt.almancaokuyucu.data.UserPreferencesStore
import de.bascurt.almancaokuyucu.model.*
import kotlinx.coroutines.launch

private val Turquoise = Color(0xFF1FA7A5)
private val Dark = Color(0xFF102F3C)
private val SoftBg = Color(0xFFF4F7F8)

private enum class AppPage { HOME, MY_WORDS, STUDY_MENU, STUDY_MEANING, STUDY_FILL, PROFILE, READ_STORIES, STATS, SETTINGS, ABOUT }
private data class FillBlankCase(val lexeme: Lexeme, val sentence: String, val answer: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { GermanReaderApp() }
    }
}

@Composable
private fun GermanReaderApp() {
    val context = LocalContext.current
    val savedStore = remember { SavedLexemeStore(context) }
    val userStore = remember { UserPreferencesStore(context) }
    val canonical = remember { SampleLessons.all.flatMap { it.lexemes }.associateBy { it.id } }
    var saved by remember {
        val refreshed = savedStore.load().map { canonical[it.id] ?: it }
        savedStore.save(refreshed)
        mutableStateOf(refreshed)
    }
    var prefs by remember { mutableStateOf(userStore.load()) }
    var readLessonIds by remember { mutableStateOf(userStore.readLessonIds()) }
    var stats by remember { mutableStateOf(userStore.loadStats()) }
    var currentLesson by remember { mutableStateOf<ReaderLesson?>(null) }
    var page by remember { mutableStateOf(AppPage.HOME) }
    var studyItems by remember { mutableStateOf<List<Lexeme>>(emptyList()) }

    fun savePrefs(value: UserPreferences) {
        prefs = value
        userStore.save(value)
    }
    fun toggle(item: Lexeme) {
        saved = if (saved.any { it.id == item.id }) saved.filterNot { it.id == item.id } else saved + item
        savedStore.save(saved)
    }
    fun openLesson(lesson: ReaderLesson) {
        userStore.markLessonRead(lesson.id)
        readLessonIds = userStore.readLessonIds()
        currentLesson = lesson
    }
    fun recordAnswer(correct: Boolean) {
        userStore.recordAnswer(correct)
        stats = userStore.loadStats()
    }

    val systemDark = isSystemInDarkTheme()
    val darkTheme = when (prefs.themeMode) { "dark" -> true; "light" -> false; else -> systemDark }
    val colorScheme = if (darkTheme) darkColorScheme(primary = Turquoise, secondary = Turquoise) else lightColorScheme(primary = Turquoise, secondary = Turquoise, background = SoftBg)
    val baseDensity = LocalDensity.current

    MaterialTheme(colorScheme = colorScheme) {
        CompositionLocalProvider(LocalDensity provides Density(baseDensity.density, baseDensity.fontScale * prefs.uiScale)) {
            if (currentLesson != null) {
                ReaderScreen(
                    lesson = currentLesson!!,
                    saved = saved,
                    preferences = prefs,
                    onToggleSaved = ::toggle,
                    onHome = { currentLesson = null }
                )
            } else if (page == AppPage.STUDY_MEANING) {
                MeaningStudyScreen(studyItems, { page = AppPage.STUDY_MENU }, ::recordAnswer)
            } else if (page == AppPage.STUDY_FILL) {
                FillBlankStudyScreen(studyItems, SampleLessons.all, { page = AppPage.STUDY_MENU }, ::recordAnswer)
            } else {
                MainShell(
                    page = page,
                    onPage = { target ->
                        if (target == AppPage.STUDY_MENU && studyItems.isEmpty()) studyItems = saved
                        page = target
                    },
                    preferences = prefs,
                    onPreferences = ::savePrefs,
                    lessons = SampleLessons.all,
                    saved = saved,
                    readLessonIds = readLessonIds,
                    stats = stats,
                    onLesson = ::openLesson,
                    onRemove = ::toggle,
                    studyItems = studyItems,
                    onStudyItems = { studyItems = it; userStore.recordStudySession(); stats = userStore.loadStats(); page = AppPage.STUDY_MENU },
                    onChooseStudy = { page = it },
                    onResetProgress = { userStore.resetProgress(); readLessonIds = emptySet(); stats = LearningStats() }
                )
            }
        }
    }
}

@Composable
private fun MainShell(
    page: AppPage,
    onPage: (AppPage) -> Unit,
    preferences: UserPreferences,
    onPreferences: (UserPreferences) -> Unit,
    lessons: List<ReaderLesson>,
    saved: List<Lexeme>,
    readLessonIds: Set<String>,
    stats: LearningStats,
    onLesson: (ReaderLesson) -> Unit,
    onRemove: (Lexeme) -> Unit,
    studyItems: List<Lexeme>,
    onStudyItems: (List<Lexeme>) -> Unit,
    onChooseStudy: (AppPage) -> Unit,
    onResetProgress: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val shellPages = setOf(AppPage.HOME, AppPage.MY_WORDS, AppPage.STUDY_MENU, AppPage.PROFILE, AppPage.READ_STORIES, AppPage.STATS, AppPage.SETTINGS, AppPage.ABOUT)
    val title = when (page) {
        AppPage.HOME -> "Almanca Okuyucu"
        AppPage.MY_WORDS -> "Kelimelerim"
        AppPage.STUDY_MENU -> "Çalış"
        AppPage.PROFILE -> "Profilim"
        AppPage.READ_STORIES -> "Okuduğum Hikâyeler"
        AppPage.STATS -> "İstatistikler"
        AppPage.SETTINGS -> "Ayarlar"
        AppPage.ABOUT -> "Hakkında"
        else -> "Almanca Okuyucu"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(310.dp)) {
                DrawerHeader(preferences, saved.size, readLessonIds.size)
                HorizontalDivider()
                DrawerItem("⌂", "Ana Sayfa", page == AppPage.HOME) { onPage(AppPage.HOME); scope.launch { drawerState.close() } }
                DrawerItem("★", "Kelimelerim", page == AppPage.MY_WORDS) { onPage(AppPage.MY_WORDS); scope.launch { drawerState.close() } }
                DrawerItem("✓", "Okuduğum Hikâyeler", page == AppPage.READ_STORIES) { onPage(AppPage.READ_STORIES); scope.launch { drawerState.close() } }
                DrawerItem("▶", "Çalışmalarım", page == AppPage.STUDY_MENU) { onPage(AppPage.STUDY_MENU); scope.launch { drawerState.close() } }
                DrawerItem("▥", "İstatistikler", page == AppPage.STATS) { onPage(AppPage.STATS); scope.launch { drawerState.close() } }
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                DrawerItem("●", "Profilim", page == AppPage.PROFILE) { onPage(AppPage.PROFILE); scope.launch { drawerState.close() } }
                DrawerItem("⚙", "Ayarlar", page == AppPage.SETTINGS) { onPage(AppPage.SETTINGS); scope.launch { drawerState.close() } }
                DrawerItem("i", "Uygulama hakkında", page == AppPage.ABOUT) { onPage(AppPage.ABOUT); scope.launch { drawerState.close() } }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(title, fontWeight = FontWeight.SemiBold) },
                    navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) { Text("☰", fontSize = 25.sp) } },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
                )
            },
            bottomBar = {
                if (page in shellPages) {
                    NavigationBar {
                        NavigationBarItem(selected = page == AppPage.HOME, onClick = { onPage(AppPage.HOME) }, icon = { Text("⌂", fontSize = 21.sp) }, label = { Text("Hikâyeler") })
                        NavigationBarItem(selected = page == AppPage.MY_WORDS, onClick = { onPage(AppPage.MY_WORDS) }, icon = { Text("★", fontSize = 20.sp) }, label = { Text("Kelimelerim") })
                        NavigationBarItem(selected = page == AppPage.STUDY_MENU, onClick = { onPage(AppPage.STUDY_MENU) }, icon = { Text("▶", fontSize = 19.sp) }, label = { Text("Çalış") })
                    }
                }
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                when (page) {
                    AppPage.HOME -> ModernHomeScreen(lessons, saved, readLessonIds, preferences, onLesson, { onPage(AppPage.MY_WORDS) })
                    AppPage.MY_WORDS -> MyWordsScreen(lessons, saved, onRemove, onStudyItems)
                    AppPage.STUDY_MENU -> StudyMenuScreen(if (studyItems.isEmpty()) saved else studyItems, onChooseStudy)
                    AppPage.PROFILE -> ProfileScreen(preferences, onPreferences, saved.size, readLessonIds.size, stats)
                    AppPage.READ_STORIES -> ReadStoriesScreen(lessons, readLessonIds, saved, onLesson)
                    AppPage.STATS -> StatsScreen(saved, readLessonIds, stats)
                    AppPage.SETTINGS -> SettingsScreen(preferences, onPreferences, onResetProgress)
                    AppPage.ABOUT -> AboutScreen()
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun DrawerHeader(prefs: UserPreferences, savedCount: Int, readCount: Int) {
    Column(Modifier.fillMaxWidth().padding(22.dp)) {
        Surface(shape = CircleShape, color = Turquoise.copy(alpha = .16f), modifier = Modifier.size(58.dp)) {
            Box(contentAlignment = Alignment.Center) { Text(prefs.name.take(1).uppercase(), color = Turquoise, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(12.dp)); Text(prefs.name, fontSize = 21.sp, fontWeight = FontWeight.Bold)
        Text("Almanca ${prefs.germanLevel}  •  $savedCount kelime  •  $readCount hikâye", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
    }
}

@Composable
private fun DrawerItem(icon: String, label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Text(icon, fontSize = 20.sp) },
        selected = selected,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}

@Composable
private fun ModernHomeScreen(lessons: List<ReaderLesson>, saved: List<Lexeme>, readIds: Set<String>, prefs: UserPreferences, onLesson: (ReaderLesson) -> Unit, onMyWords: () -> Unit) {
    val continueLesson = lessons.firstOrNull { it.id in readIds } ?: lessons.firstOrNull()
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 18.dp, vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Dark), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(22.dp)) {
                Text("Merhaba, ${prefs.name}", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(5.dp)); Text("Bugün Almanca için küçük bir adım yeter.", color = Color(0xFFD4E5E9))
                Spacer(Modifier.height(18.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    StatPill("${saved.size}", "kelime")
                    StatPill("${readIds.size}", "hikâye")
                    StatPill("${prefs.dailyGoal}", "günlük hedef")
                }
            }
        }
        continueLesson?.let { lesson ->
            Text("Çalışmaya devam et", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            ElevatedCard(Modifier.fillMaxWidth().clickable { onLesson(lesson) }, shape = RoundedCornerShape(22.dp)) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    LevelBadge(lesson.level)
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) { Text(lesson.title, fontSize = 19.sp, fontWeight = FontWeight.Bold); Text(lesson.summary, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    Text("›", fontSize = 31.sp, color = Turquoise)
                }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Hikâyeler", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = onMyWords) { Text("Kelimelerim") }
        }
        lessons.forEach { lesson ->
            ElevatedCard(Modifier.fillMaxWidth().clickable { onLesson(lesson) }, shape = RoundedCornerShape(22.dp)) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    LevelBadge(lesson.level); Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text(lesson.title, fontSize = 19.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp)); Text(lesson.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (lesson.id in readIds) { Spacer(Modifier.height(6.dp)); Text("✓ Okundu", color = Turquoise, fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    }
                    Text("›", fontSize = 30.sp, color = Turquoise)
                }
            }
        }
        Spacer(Modifier.height(20.dp))
    }
}

@Composable private fun StatPill(number: String, label: String) {
    Surface(color = Color.White.copy(alpha = .10f), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(horizontal = 12.dp, vertical = 9.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(number, color = Color.White, fontWeight = FontWeight.Bold); Text(label, color = Color(0xFFD4E5E9), fontSize = 11.sp) } }
}

@Composable private fun LevelBadge(level: String) {
    Surface(color = levelColor(level), shape = RoundedCornerShape(14.dp)) { Text(level, Modifier.padding(horizontal = 13.dp, vertical = 10.dp), color = Color(0xFF203038), fontWeight = FontWeight.Bold) }
}

@Composable
private fun ReaderScreen(lesson: ReaderLesson, saved: List<Lexeme>, preferences: UserPreferences, onToggleSaved: (Lexeme) -> Unit, onHome: () -> Unit) {
    var tab by remember(lesson.id) { mutableStateOf(ReaderTab.STORY) }
    var selected by remember(lesson.id) { mutableStateOf<Lexeme?>(null) }
    val isSaved = selected?.let { item -> saved.any { it.id == item.id } } == true
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (tab == ReaderTab.STORY) MeaningPanel(selected, isSaved, preferences.detailedExplanations, { selected?.let(onToggleSaved) }, onHome)
        else SectionHeader(lesson.title, onHome)
        ReaderTabs(tab) { tab = it }
        Box(Modifier.weight(1f)) {
            when (tab) {
                ReaderTab.STORY -> StoryScreen(lesson, selected, preferences.storyTextSize, preferences.highlightEnabled) { selected = it }
                ReaderTab.QUIZ -> QuizScreen(lesson, preferences.quizQuestionCount)
                ReaderTab.WORDS -> LessonWordsScreen(lesson, saved, onToggleSaved)
                ReaderTab.GRAMMAR -> GrammarScreen(lesson)
            }
        }
    }
}

@Composable
private fun MeaningPanel(selected: Lexeme?, isSaved: Boolean, detailed: Boolean, onSave: () -> Unit, onHome: () -> Unit) {
    Box(Modifier.fillMaxWidth().height(350.dp).background(Brush.verticalGradient(listOf(Color(0xFF07171F), Dark))).padding(horizontal = 22.dp, vertical = 16.dp)) {
        Text("‹ Hikâyeler", color = Color.White, fontSize = 17.sp, modifier = Modifier.clickable(onClick = onHome).padding(6.dp))
        Text(if (isSaved) "★" else "☆", color = if (selected == null) Color.White.copy(alpha = .35f) else Color.White, fontSize = 38.sp, modifier = Modifier.align(Alignment.TopEnd).clickable(enabled = selected != null, onClick = onSave).padding(4.dp))
        Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().heightIn(max = 275.dp).verticalScroll(rememberScrollState())) {
            if (selected == null) {
                Text("Bir kelimeye dokun", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(7.dp)); Text("Anlamı, kelime türü ve kullanım bilgisi burada görünecek.", color = Color(0xFFD5E4E8), fontSize = 16.sp)
            } else {
                Text(wordDisplayTitle(selected), color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp)); Text(selected.meaning, color = Color.White, fontSize = 19.sp)
                Spacer(Modifier.height(8.dp)); Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { DarkPill("Kelime türü: ${selected.wordClass}"); selected.grammar?.let { DarkPill(it) } }
                MorphologyDetails(selected)
                if (detailed) selected.explanation?.let { Spacer(Modifier.height(8.dp)); Text(it, color = Color(0xFFD5E4E8), fontSize = 14.sp, lineHeight = 19.sp) }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable private fun MorphologyDetails(item: Lexeme) {
    when (item.wordClass) {
        "Fiil" -> { item.infinitive?.let { DetailLine("Mastar", it) }; item.thirdPerson?.let { DetailLine("3. tekil şahıs", it) }; item.preterite?.let { DetailLine("Präteritum", it) }; item.perfect?.let { DetailLine("Perfekt", it) } }
        "İsim" -> { item.article?.let { DetailLine("Artikel", it) }; item.plural?.let { DetailLine("Çoğul", pluralNotation(item.base, it)) }; item.accusativeNote?.let { DetailLine("Akkusativ", it) } }
        "Sıfat" -> { item.positive?.let { DetailLine("Yalın hâl", it) }; item.comparative?.let { DetailLine("Komparativ", it) }; item.superlative?.let { DetailLine("Superlativ", it) } }
    }
}

@Composable private fun DetailLine(label: String, value: String) { Row(Modifier.padding(top = 4.dp)) { Text("$label: ", color = Color(0xFF9ED7D6), fontWeight = FontWeight.SemiBold, fontSize = 14.sp); Text(value, color = Color.White, fontSize = 14.sp) } }

@Composable private fun SectionHeader(title: String, onHome: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Dark).statusBarsPadding().padding(horizontal = 20.dp, vertical = 15.dp)) { Text("‹ Hikâyeler", color = Color.White, modifier = Modifier.clickable(onClick = onHome).padding(4.dp)); Spacer(Modifier.height(8.dp)); Text(title, color = Color.White, fontSize = 23.sp, fontWeight = FontWeight.Bold) }
}

@Composable private fun ReaderTabs(active: ReaderTab, onSelect: (ReaderTab) -> Unit) {
    SecondaryTabRow(selectedTabIndex = ReaderTab.entries.indexOf(active)) {
        listOf(ReaderTab.STORY to "Hikâye", ReaderTab.QUIZ to "Sınav", ReaderTab.WORDS to "Kelimeler", ReaderTab.GRAMMAR to "Gramer").forEach { (tab, label) -> Tab(selected = tab == active, onClick = { onSelect(tab) }, text = { Text(label) }) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable private fun StoryScreen(lesson: ReaderLesson, selected: Lexeme?, textSize: Int, highlightEnabled: Boolean, onSelect: (Lexeme) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 110.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { LevelBadge(lesson.level); Spacer(Modifier.width(12.dp)); Text(lesson.title, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        Spacer(Modifier.height(22.dp))
        lesson.sentences.forEach { sentence ->
            FlowRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(3.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
                sentence.forEach { token ->
                    val active = highlightEnabled && selected?.id == token.lexeme.id
                    Text(token.text, fontSize = textSize.sp, lineHeight = (textSize + 10).sp, color = if (active) Color.White else MaterialTheme.colorScheme.onBackground, modifier = Modifier.background(if (active) Turquoise else Color.Transparent, RoundedCornerShape(7.dp)).clickable { onSelect(token.lexeme) }.padding(horizontal = 2.dp, vertical = 2.dp))
                }
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable private fun QuizScreen(lesson: ReaderLesson, questionCount: Int) {
    val questions = remember(lesson.id, questionCount) { lesson.quizItems.shuffled().take(questionCount.coerceAtLeast(1)) }
    var index by remember(lesson.id) { mutableIntStateOf(0) }
    var answer by remember(lesson.id, index) { mutableStateOf<String?>(null) }
    if (questions.isEmpty()) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Bu metin için henüz uygun sınav kelimesi yok.") }; return }
    val question = questions[index % questions.size]
    val optionPool = remember(lesson.id, index) { (SampleLessons.all.flatMap { it.quizItems }.filter { it.id != question.id }.shuffled().take(3) + question).shuffled() }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        LinearProgressIndicator(progress = { (index + 1).toFloat() / questions.size }, modifier = Modifier.fillMaxWidth())
        Text("Soru ${index + 1} / ${questions.size}", color = Turquoise, fontWeight = FontWeight.Bold)
        Text("“${question.meaning}” anlamına gelen Almanca ifade hangisi?", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        optionPool.forEach { option -> OutlinedButton(onClick = { answer = option.base }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) { Text(option.base, Modifier.fillMaxWidth(), fontSize = 17.sp) } }
        answer?.let { Text(if (it == question.base) "Doğru ✓" else "Doğru cevap: ${question.base}", color = if (it == question.base) Color(0xFF16845B) else MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold); Button(onClick = { index = (index + 1) % questions.size }, modifier = Modifier.align(Alignment.End)) { Text("Sonraki") } }
    }
}

@Composable private fun LessonWordsScreen(lesson: ReaderLesson, saved: List<Lexeme>, onRemove: (Lexeme) -> Unit) {
    WordCards(saved.filter { savedItem -> lesson.lexemes.any { it.id == savedItem.id } }, onRemove, "Bu hikâyeden henüz kelime kaydetmedin.")
}

@Composable
private fun MyWordsScreen(lessons: List<ReaderLesson>, saved: List<Lexeme>, onRemove: (Lexeme) -> Unit, onStudy: (List<Lexeme>) -> Unit) {
    var lessonFilters by remember { mutableStateOf(setOf<String>()) }
    var typeFilters by remember { mutableStateOf(setOf<String>()) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    val lessonByLexeme = remember(lessons) { lessons.flatMap { lesson -> lesson.lexemes.map { it.id to lesson } }.toMap() }
    val availableLessons = lessons.filter { lesson -> saved.any { lesson.lexemes.any { lx -> lx.id == it.id } } }
    val wordTypes = listOf("Fiil", "İsim", "Sıfat", "Zarf", "Edat", "Bağlaç", "Zamir", "Artikel", "Belirleyici", "Parçacık", "Özel isim", "Diğer")
    val filtered = saved.filter { item -> (lessonFilters.isEmpty() || lessonByLexeme[item.id]?.id in lessonFilters) && (typeFilters.isEmpty() || item.wordClass in typeFilters) }

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
        Text("${saved.size} kayıtlı kelime / ifade", color = MaterialTheme.colorScheme.onSurfaceVariant)
        MultiFilterDropdown("Hikâye", lessonFilters, availableLessons.map { it.id to it.title }) { lessonFilters = it }
        MultiFilterDropdown("Kelime türü", typeFilters, wordTypes.map { it to it }) { typeFilters = it }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = { selectedIds = if (filtered.isNotEmpty() && filtered.all { it.id in selectedIds }) selectedIds - filtered.map { it.id }.toSet() else selectedIds + filtered.map { it.id } }) { Text(if (filtered.isNotEmpty() && filtered.all { it.id in selectedIds }) "Seçimi kaldır" else "Tümünü seç") }
            Spacer(Modifier.weight(1f)); Text("${selectedIds.size} seçili", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Button(onClick = { onStudy(saved.filter { it.id in selectedIds }) }, enabled = selectedIds.isNotEmpty(), modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) { Text("Seçilenlerle çalış (${selectedIds.size})") }
        filtered.asReversed().forEach { item -> SelectableWordCard(item, item.id in selectedIds, { selectedIds = if (item.id in selectedIds) selectedIds - item.id else selectedIds + item.id }, { onRemove(item); selectedIds = selectedIds - item.id }, lessonByLexeme[item.id]?.title) }
        if (filtered.isEmpty()) Text("Bu filtrelerde kayıtlı kelime yok.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(20.dp))
        Spacer(Modifier.height(30.dp))
    }
}

@Composable private fun MultiFilterDropdown(label: String, selectedIds: Set<String>, options: List<Pair<String, String>>, onChange: (Set<String>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val summary = when { selectedIds.isEmpty() -> "Tümü"; selectedIds.size == 1 -> options.firstOrNull { it.first in selectedIds }?.second ?: "1 seçili"; else -> "${selectedIds.size} seçili" }
    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) { Column(Modifier.weight(1f), horizontalAlignment = Alignment.Start) { Text(label, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(summary, fontSize = 16.sp) }; Text("▾", fontSize = 20.sp) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(.92f)) {
            DropdownMenuItem(text = { Text("Tümü") }, leadingIcon = { Checkbox(checked = selectedIds.isEmpty(), onCheckedChange = null) }, onClick = { onChange(emptySet()) })
            options.forEach { (id, title) -> DropdownMenuItem(text = { Text(title) }, leadingIcon = { Checkbox(checked = id in selectedIds, onCheckedChange = null) }, onClick = { onChange(if (id in selectedIds) selectedIds - id else selectedIds + id) }) }
        }
    }
}

@Composable private fun StudyMenuScreen(items: List<Lexeme>, onChoose: (AppPage) -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(15.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = Turquoise.copy(alpha = .12f)), shape = RoundedCornerShape(20.dp)) { Column(Modifier.padding(18.dp)) { Text("Kelime Çalışması", fontSize = 24.sp, fontWeight = FontWeight.Bold); Text("${items.size} kelime / ifade hazır", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
        StudyModeCard("Türkçe ↔ Almanca", "Anlamı iki yönde soran çoktan seçmeli çalışma.") { onChoose(AppPage.STUDY_MEANING) }
        StudyModeCard("Boşluk Doldurma", "Kelime veya ifadeyi hikâyedeki gerçek cümlede tamamla.") { onChoose(AppPage.STUDY_FILL) }
        if (items.isEmpty()) Text("Önce Kelimelerim bölümünden çalışmak istediğin kelimeleri seçebilirsin.", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable private fun StudyModeCard(title: String, description: String, onClick: () -> Unit) {
    ElevatedCard(Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(22.dp)) { Column(Modifier.padding(20.dp)) { Text(title, fontSize = 21.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(6.dp)); Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(12.dp)); Text("Başla  ›", color = Turquoise, fontWeight = FontWeight.Bold) } }
}

@Composable private fun MeaningStudyScreen(items: List<Lexeme>, onBack: () -> Unit, onAnswered: (Boolean) -> Unit) {
    if (items.isEmpty()) { EmptyStudyScreen(onBack); return }
    var index by remember { mutableIntStateOf(0) }; var selectedAnswer by remember(index) { mutableStateOf<String?>(null) }; var correctCount by remember { mutableIntStateOf(0) }
    val item = items[index % items.size]; val askGerman = index % 2 == 0; val correct = if (askGerman) item.base else item.meaning
    val options = remember(index, items) { (items.filter { it.id != item.id }.shuffled().map { if (askGerman) it.base else it.meaning }.distinct().take(3) + correct).distinct().shuffled() }
    StudyHeader("Türkçe ↔ Almanca", index, items.size, correctCount, onBack) {
        Text(if (askGerman) "Bu Türkçe anlamın Almancası hangisi?" else "Bu Almanca ifade ne anlama geliyor?", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(8.dp)); Text(if (askGerman) item.meaning else wordDisplayTitle(item), fontSize = 27.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(18.dp))
        options.forEach { option -> OutlinedButton(onClick = { if (selectedAnswer == null) { selectedAnswer = option; val ok = option == correct; if (ok) correctCount++; onAnswered(ok) } }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp)) { Text(option, Modifier.fillMaxWidth(), fontSize = 17.sp) }; Spacer(Modifier.height(8.dp)) }
        selectedAnswer?.let { Text(if (it == correct) "Doğru ✓" else "Doğru cevap: $correct", color = if (it == correct) Color(0xFF16845B) else MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold); Spacer(Modifier.height(12.dp)); Button(onClick = { index = (index + 1) % items.size }, modifier = Modifier.align(Alignment.End)) { Text("Sonraki") } }
    }
}

@Composable private fun FillBlankStudyScreen(items: List<Lexeme>, lessons: List<ReaderLesson>, onBack: () -> Unit, onAnswered: (Boolean) -> Unit) {
    val cases = remember(items, lessons) { buildFillBlankCases(items, lessons) }
    if (cases.isEmpty()) { EmptyStudyScreen(onBack, "Seçilen kelimeler için hikâye içinde boşluk doldurma cümlesi bulunamadı."); return }
    var index by remember { mutableIntStateOf(0) }; var input by remember(index) { mutableStateOf("") }; var checked by remember(index) { mutableStateOf(false) }; var correctCount by remember { mutableIntStateOf(0) }
    val case = cases[index % cases.size]; val isCorrect = normalizeAnswer(input) == normalizeAnswer(case.answer)
    StudyHeader("Boşluk Doldurma", index, cases.size, correctCount, onBack) {
        Text("Boşluğa hikâyedeki doğru Almanca kelime veya ifadeyi yaz.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(14.dp)); Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), shape = RoundedCornerShape(18.dp)) { Text(case.sentence, Modifier.padding(18.dp), fontSize = 21.sp, lineHeight = 30.sp) }
        Spacer(Modifier.height(16.dp)); OutlinedTextField(value = input, onValueChange = { if (!checked) input = it }, label = { Text("Cevabın") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(12.dp))
        if (!checked) Button(onClick = { checked = true; if (isCorrect) correctCount++; onAnswered(isCorrect) }, enabled = input.isNotBlank(), modifier = Modifier.fillMaxWidth()) { Text("Kontrol et") }
        else { Text(if (isCorrect) "Doğru ✓" else "Doğru cevap: ${case.answer}", color = if (isCorrect) Color(0xFF16845B) else MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold); Spacer(Modifier.height(10.dp)); Text("${wordDisplayTitle(case.lexeme)} — ${case.lexeme.meaning}", color = MaterialTheme.colorScheme.onSurfaceVariant); Spacer(Modifier.height(12.dp)); Button(onClick = { index = (index + 1) % cases.size }, modifier = Modifier.align(Alignment.End)) { Text("Sonraki") } }
    }
}

@Composable private fun StudyHeader(title: String, index: Int, total: Int, correct: Int, onBack: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(Modifier.fillMaxWidth().background(Dark).statusBarsPadding().padding(20.dp)) { Text("‹ Çalışma seçenekleri", color = Color.White, modifier = Modifier.clickable(onClick = onBack).padding(4.dp)); Spacer(Modifier.height(10.dp)); Text(title, color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold); Text("Soru ${index + 1} / $total   •   Doğru: $correct", color = Color(0xFFD5E4E8)) }
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(20.dp), content = content)
    }
}

@Composable private fun EmptyStudyScreen(onBack: () -> Unit, message: String = "Çalışmak için kelime seçilmedi.") { Column(Modifier.fillMaxSize()) { SectionHeader("Kelime Çalışması", onBack); Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(message, modifier = Modifier.padding(24.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) } } }

@Composable
private fun ProfileScreen(prefs: UserPreferences, onSave: (UserPreferences) -> Unit, savedCount: Int, readCount: Int, stats: LearningStats) {
    var name by remember(prefs.name) { mutableStateOf(prefs.name) }
    var level by remember(prefs.germanLevel) { mutableStateOf(prefs.germanLevel) }
    var goal by remember(prefs.dailyGoal) { mutableFloatStateOf(prefs.dailyGoal.toFloat()) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) { Surface(shape = CircleShape, color = Turquoise.copy(alpha = .16f), modifier = Modifier.size(74.dp)) { Box(contentAlignment = Alignment.Center) { Text(name.take(1).uppercase(), color = Turquoise, fontSize = 30.sp, fontWeight = FontWeight.Bold) } }; Spacer(Modifier.width(16.dp)); Column { Text(name.ifBlank { "Kullanıcı" }, fontSize = 24.sp, fontWeight = FontWeight.Bold); Text("Yerel profil • veriler bu cihazda", color = MaterialTheme.colorScheme.onSurfaceVariant) } }
        OutlinedTextField(name, { name = it }, label = { Text("İsim") }, modifier = Modifier.fillMaxWidth())
        Text("Almanca seviyesi", fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { listOf("A1","A2","B1","B2","C1").forEach { lv -> FilterChip(selected = level == lv, onClick = { level = lv }, label = { Text(lv) }) } }
        Text("Günlük çalışma hedefi: ${goal.toInt()} soru", fontWeight = FontWeight.Bold)
        Slider(value = goal, onValueChange = { goal = it }, valueRange = 5f..50f, steps = 8)
        Button(onClick = { onSave(prefs.copy(name = name.ifBlank { "Kullanıcı" }, germanLevel = level, dailyGoal = goal.toInt())) }, modifier = Modifier.fillMaxWidth()) { Text("Profili kaydet") }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) { SmallStatCard("$savedCount", "Kelime", Modifier.weight(1f)); SmallStatCard("$readCount", "Hikâye", Modifier.weight(1f)); SmallStatCard("${stats.studySessions}", "Çalışma", Modifier.weight(1f)) }
    }
}

@Composable private fun SmallStatCard(value: String, label: String, modifier: Modifier = Modifier) { ElevatedCard(modifier, shape = RoundedCornerShape(17.dp)) { Column(Modifier.fillMaxWidth().padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold); Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp) } } }

@Composable
private fun ReadStoriesScreen(lessons: List<ReaderLesson>, readIds: Set<String>, saved: List<Lexeme>, onLesson: (ReaderLesson) -> Unit) {
    val read = lessons.filter { it.id in readIds }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (read.isEmpty()) { Text("Henüz okundu olarak işaretlenen hikâye yok.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        read.forEach { lesson ->
            val savedFromLesson = saved.count { item -> lesson.lexemes.any { it.id == item.id } }
            ElevatedCard(Modifier.fillMaxWidth().clickable { onLesson(lesson) }, shape = RoundedCornerShape(20.dp)) { Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) { LevelBadge(lesson.level); Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f)) { Text(lesson.title, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("$savedFromLesson kelime kaydedildi", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp) }; Text("›", fontSize = 28.sp, color = Turquoise) } }
        }
    }
}

@Composable
private fun StatsScreen(saved: List<Lexeme>, readIds: Set<String>, stats: LearningStats) {
    val accuracy = if (stats.answered == 0) 0 else stats.correct * 100 / stats.answered
    val typeCounts = saved.groupingBy { it.wordClass }.eachCount().entries.sortedByDescending { it.value }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) { SmallStatCard("${saved.size}", "Kelime", Modifier.weight(1f)); SmallStatCard("${readIds.size}", "Hikâye", Modifier.weight(1f)); SmallStatCard("$accuracy%", "Doğruluk", Modifier.weight(1f)) }
        ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp)) { Text("Çalışma özeti", fontSize = 20.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(10.dp)); Text("Toplam cevap: ${stats.answered}"); Text("Doğru cevap: ${stats.correct}"); Text("Çalışma oturumu: ${stats.studySessions}") } }
        ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp)) { Text("Kelime türleri", fontSize = 20.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(10.dp)); if (typeCounts.isEmpty()) Text("Henüz kayıtlı kelime yok.", color = MaterialTheme.colorScheme.onSurfaceVariant) else typeCounts.forEach { Text("${it.key}: ${it.value}") } } }
    }
}

@Composable
private fun SettingsScreen(prefs: UserPreferences, onSave: (UserPreferences) -> Unit, onResetProgress: () -> Unit) {
    var confirmReset by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        SettingCard("Görünüm") {
            Text("Tema", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { listOf("system" to "Sistem", "light" to "Açık", "dark" to "Karanlık").forEach { (id, label) -> FilterChip(selected = prefs.themeMode == id, onClick = { onSave(prefs.copy(themeMode = id)) }, label = { Text(label) }) } }
            Spacer(Modifier.height(12.dp)); Text("Arayüz yazı boyutu", fontWeight = FontWeight.Bold); Text("${(prefs.uiScale * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurfaceVariant); Slider(value = prefs.uiScale, onValueChange = { onSave(prefs.copy(uiScale = it)) }, valueRange = .85f..1.25f)
            Text("Hikâye yazı boyutu: ${prefs.storyTextSize}", fontWeight = FontWeight.Bold); Slider(value = prefs.storyTextSize.toFloat(), onValueChange = { onSave(prefs.copy(storyTextSize = it.toInt())) }, valueRange = 18f..30f, steps = 5)
        }
        SettingCard("Okuma") {
            SwitchSetting("Seçilen kelimeyi vurgula", prefs.highlightEnabled) { onSave(prefs.copy(highlightEnabled = it)) }
            SwitchSetting("Ayrıntılı açıklamaları göster", prefs.detailedExplanations) { onSave(prefs.copy(detailedExplanations = it)) }
        }
        SettingCard("Sınav") {
            Text("Hikâye sınavında soru sayısı: ${prefs.quizQuestionCount}", fontWeight = FontWeight.Bold); Slider(value = prefs.quizQuestionCount.toFloat(), onValueChange = { onSave(prefs.copy(quizQuestionCount = it.toInt())) }, valueRange = 5f..20f, steps = 14)
        }
        OutlinedButton(onClick = { confirmReset = true }, modifier = Modifier.fillMaxWidth()) { Text("Çalışma geçmişini sıfırla") }
    }
    if (confirmReset) AlertDialog(onDismissRequest = { confirmReset = false }, title = { Text("İlerleme sıfırlansın mı?") }, text = { Text("Okunan hikâyeler ve çalışma istatistikleri sıfırlanacak. Kaydettiğin kelimeler silinmeyecek.") }, confirmButton = { TextButton(onClick = { onResetProgress(); confirmReset = false }) { Text("Sıfırla") } }, dismissButton = { TextButton(onClick = { confirmReset = false }) { Text("Vazgeç") } })
}

@Composable private fun SettingCard(title: String, content: @Composable ColumnScope.() -> Unit) { ElevatedCard(shape = RoundedCornerShape(20.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(18.dp)) { Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(14.dp)); content() } } }
@Composable private fun SwitchSetting(title: String, checked: Boolean, onChecked: (Boolean) -> Unit) { Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) { Text(title, Modifier.weight(1f)); Switch(checked = checked, onCheckedChange = onChecked) } }

@Composable private fun AboutScreen() { Column(Modifier.fillMaxSize().padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { Text("Almanca Okuyucu", fontSize = 26.sp, fontWeight = FontWeight.Bold); Text("Hikâyeler üzerinden bağlama uygun kelime, ifade ve gramer çalışmak için hazırlanmış kişisel öğrenme uygulaması.", color = MaterialTheme.colorScheme.onSurfaceVariant); Text("Sürüm 0.4.1.4", color = Turquoise, fontWeight = FontWeight.Bold); Text("Profil, ayarlar, okuma ilerlemesi ve çalışma istatistikleri cihazda yerel olarak saklanır.") } }

@Composable private fun WordCards(items: List<Lexeme>, onRemove: (Lexeme) -> Unit, emptyText: String) {
    if (items.isEmpty()) { Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(emptyText, color = MaterialTheme.colorScheme.onSurfaceVariant) }; return }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { items.asReversed().forEach { item -> ElevatedCard(shape = RoundedCornerShape(18.dp)) { Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.Top) { Column(Modifier.weight(1f)) { Text(wordDisplayTitle(item), fontSize = 19.sp, fontWeight = FontWeight.Bold); Text(item.meaning, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(item.wordClass, color = Turquoise, fontSize = 13.sp) }; Text("★", color = Turquoise, fontSize = 27.sp, modifier = Modifier.clickable { onRemove(item) }.padding(5.dp)) } } } }
}

@Composable private fun SelectableWordCard(item: Lexeme, selected: Boolean, onSelect: () -> Unit, onRemove: () -> Unit, lessonTitle: String?) {
    ElevatedCard(colors = CardDefaults.elevatedCardColors(containerColor = if (selected) Turquoise.copy(alpha = .12f) else MaterialTheme.colorScheme.surface), modifier = Modifier.fillMaxWidth().clickable(onClick = onSelect), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.Top) { Checkbox(checked = selected, onCheckedChange = { onSelect() }); Column(Modifier.weight(1f).padding(start = 6.dp)) { Text(wordDisplayTitle(item), fontSize = 18.sp, fontWeight = FontWeight.Bold); Text(item.meaning, color = MaterialTheme.colorScheme.onSurfaceVariant); Text(listOfNotNull(lessonTitle, item.wordClass).joinToString(" • "), color = Turquoise, fontSize = 12.sp) }; Text("★", color = Turquoise, fontSize = 26.sp, modifier = Modifier.clickable(onClick = onRemove).padding(4.dp)) }
    }
}

@Composable private fun GrammarScreen(lesson: ReaderLesson) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) { lesson.grammarItems.forEach { item -> ElevatedCard(shape = RoundedCornerShape(18.dp)) { Column(Modifier.fillMaxWidth().padding(15.dp)) { Text(item.base, fontSize = 18.sp, fontWeight = FontWeight.Bold); Text(item.meaning, color = MaterialTheme.colorScheme.onSurfaceVariant); Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(7.dp)) { LightPill(item.wordClass); item.grammar?.let { LightPill(it) } }; item.explanation?.let { Text(it, Modifier.padding(top = 8.dp), fontSize = 14.sp) } } } } }
}

private fun buildFillBlankCases(items: List<Lexeme>, lessons: List<ReaderLesson>): List<FillBlankCase> {
    val ids = items.map { it.id }.toSet(); val itemById = items.associateBy { it.id }
    return lessons.flatMap { lesson -> lesson.sentences.mapNotNull { sentence -> val targetId = sentence.firstOrNull { it.lexeme.id in ids }?.lexeme?.id ?: return@mapNotNull null; val target = itemById[targetId] ?: return@mapNotNull null; val answerTokens = sentence.filter { it.lexeme.id == targetId }.map { it.text.trimEnd('.', ',', ':', ';', '!', '?') }; if (answerTokens.isEmpty()) return@mapNotNull null; val masked = sentence.joinToString(" ") { token -> if (token.lexeme.id == targetId) "_____" else token.text }.replace("_____ _____", "_____"); FillBlankCase(target, masked, answerTokens.joinToString(" … ")) } }.distinctBy { it.lexeme.id }
}

private fun normalizeAnswer(text: String): String = text.lowercase().replace("…", " ").replace("...", " ").replace(Regex("[.,:;!?]"), "").replace(Regex("\\s+"), " ").trim()

private fun wordDisplayTitle(item: Lexeme): String {
    if (item.wordClass != "İsim") return item.base
    val article = item.article?.let { "$it " } ?: ""
    val plural = item.plural?.let { " — Pl.: ${pluralNotation(item.base, it)}" } ?: ""
    return "$article${item.base}$plural"
}

private fun pluralNotation(base: String, plural: String): String {
    val b = base.trim(); val p = plural.trim()
    if (p.equals(b, ignoreCase = true) || p == "-") return "-"
    return if (p.startsWith(b, ignoreCase = false)) p.removePrefix(b).ifBlank { "-" } else p
}

@Composable private fun DarkPill(text: String) { Surface(color = Color.White.copy(alpha = .14f), shape = RoundedCornerShape(20.dp)) { Text(text, Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = Color(0xFFE6F5F5), fontSize = 13.sp) } }
@Composable private fun LightPill(text: String) { Surface(color = Turquoise.copy(alpha = .12f), shape = RoundedCornerShape(20.dp)) { Text(text, Modifier.padding(horizontal = 9.dp, vertical = 5.dp), color = Turquoise, fontSize = 12.sp) } }
private fun levelColor(level: String) = when (level) { "A2" -> Color(0xFFE2F4E6); "B1" -> Color(0xFFE2F0FA); else -> Color(0xFFF1E6FA) }
