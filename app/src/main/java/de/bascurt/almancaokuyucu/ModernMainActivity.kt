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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.bascurt.almancaokuyucu.data.*
import de.bascurt.almancaokuyucu.model.*
import kotlinx.coroutines.launch

private val Accent = Color(0xFF1FA7A5)
private val Header = Color(0xFF102F3C)
private enum class Screen { HOME, WORDS, STUDY, SETTINGS, READER, MEANING, FILL }
private data class FillQuestion(val lexeme: Lexeme, val sentence: String, val correct: String, val options: List<String>)

class ModernMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ModernGermanReaderApp() }
    }
}

@Composable
private fun ModernGermanReaderApp() {
    val context = LocalContext.current
    val savedStore = remember { SavedLexemeStore(context) }
    val userStore = remember { UserPreferencesStore(context) }
    val canonical = remember { SampleLessons.all.flatMap { it.lexemes }.associateBy { it.id } }
    var saved by remember { mutableStateOf(savedStore.load().map { canonical[it.id] ?: it }) }
    var prefs by remember { mutableStateOf(userStore.load()) }
    var completed by remember { mutableStateOf(userStore.readLessonIds()) }
    var stats by remember { mutableStateOf(userStore.loadStats()) }
    var screen by remember { mutableStateOf(Screen.HOME) }
    var lesson by remember { mutableStateOf<ReaderLesson?>(null) }
    var studyItems by remember { mutableStateOf<List<Lexeme>>(emptyList()) }

    fun toggle(item: Lexeme) {
        saved = if (saved.any { it.id == item.id }) saved.filterNot { it.id == item.id } else saved + item
        savedStore.save(saved)
    }
    fun savePrefs(value: UserPreferences) { prefs = value; userStore.save(value) }
    fun record(ok: Boolean) { userStore.recordAnswer(ok); stats = userStore.loadStats() }

    val dark = when (prefs.themeMode) { "dark" -> true; "light" -> false; else -> isSystemInDarkTheme() }
    val scheme = if (dark) darkColorScheme(primary = Accent, secondary = Accent) else lightColorScheme(primary = Accent, secondary = Accent)

    MaterialTheme(colorScheme = scheme) {
        when (screen) {
            Screen.READER -> ReaderPage(lesson!!, saved, completed, prefs, ::toggle,
                onComplete = {
                    userStore.markLessonRead(lesson!!.id)
                    completed = userStore.readLessonIds()
                }, onBack = { screen = Screen.HOME })
            Screen.MEANING -> MeaningQuiz(studyItems, onBack = { screen = Screen.STUDY }, onAnswered = ::record)
            Screen.FILL -> FillQuiz(studyItems, SampleLessons.all, onBack = { screen = Screen.STUDY }, onAnswered = ::record)
            else -> MainShell(screen, { screen = it }, saved, completed, prefs, stats,
                onPrefs = ::savePrefs,
                onLesson = { lesson = it; screen = Screen.READER },
                onRemove = ::toggle,
                onStudy = { studyItems = it; userStore.recordStudySession(); stats = userStore.loadStats(); screen = Screen.STUDY },
                onChooseMeaning = { screen = Screen.MEANING },
                onChooseFill = { screen = Screen.FILL },
                onFullReset = {
                    savedStore.clear(); userStore.clearAll()
                    saved = emptyList(); completed = emptySet(); stats = LearningStats(); prefs = UserPreferences(); studyItems = emptyList(); screen = Screen.HOME
                })
        }
    }
}

@Composable
private fun MainShell(
    screen: Screen, onScreen: (Screen) -> Unit, saved: List<Lexeme>, completed: Set<String>, prefs: UserPreferences, stats: LearningStats,
    onPrefs: (UserPreferences) -> Unit, onLesson: (ReaderLesson) -> Unit, onRemove: (Lexeme) -> Unit, onStudy: (List<Lexeme>) -> Unit,
    onChooseMeaning: () -> Unit, onChooseFill: () -> Unit, onFullReset: () -> Unit
) {
    val drawer = rememberDrawerState(DrawerValue.Closed); val scope = rememberCoroutineScope()
    ModalNavigationDrawer(drawerState = drawer, drawerContent = {
        ModalDrawerSheet(Modifier.width(300.dp)) {
            Column(Modifier.padding(22.dp)) {
                Surface(shape = CircleShape, color = Accent.copy(alpha = .14f), modifier = Modifier.size(56.dp)) { Box(contentAlignment = Alignment.Center) { Text(prefs.name.take(1).uppercase(), color = Accent, fontWeight = FontWeight.Bold, fontSize = 24.sp) } }
                Spacer(Modifier.height(10.dp)); Text(prefs.name, fontSize = 21.sp, fontWeight = FontWeight.Bold); Text("${saved.size} kelime • ${completed.size} tamamlanan hikâye", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
            }
            HorizontalDivider()
            DrawerRow("⌂", "Ana Sayfa", screen == Screen.HOME) { onScreen(Screen.HOME); scope.launch { drawer.close() } }
            DrawerRow("★", "Kelimelerim", screen == Screen.WORDS) { onScreen(Screen.WORDS); scope.launch { drawer.close() } }
            DrawerRow("▶", "Çalış", screen == Screen.STUDY) { onScreen(Screen.STUDY); scope.launch { drawer.close() } }
            DrawerRow("⚙", "Ayarlar", screen == Screen.SETTINGS) { onScreen(Screen.SETTINGS); scope.launch { drawer.close() } }
        }
    }) {
        Scaffold(
            topBar = { CenterAlignedTopAppBar(title = { Text(when(screen){Screen.HOME->"Almanca Okuyucu";Screen.WORDS->"Kelimelerim";Screen.STUDY->"Kelime Çalışması";else->"Ayarlar"}) }, navigationIcon = { IconButton(onClick = { scope.launch { drawer.open() } }) { Text("☰", fontSize = 25.sp) } }) },
            bottomBar = { NavigationBar { NavigationBarItem(screen==Screen.HOME,{onScreen(Screen.HOME)},{Text("⌂")},{Text("Hikâyeler")}); NavigationBarItem(screen==Screen.WORDS,{onScreen(Screen.WORDS)},{Text("★")},{Text("Kelimelerim")}); NavigationBarItem(screen==Screen.STUDY,{onScreen(Screen.STUDY)},{Text("▶")},{Text("Çalış")}) } }
        ) { pad -> Box(Modifier.fillMaxSize().padding(pad)) {
            when(screen) {
                Screen.HOME -> HomePage(SampleLessons.all, saved, completed, onLesson)
                Screen.WORDS -> WordsPage(SampleLessons.all, saved, onRemove, onStudy)
                Screen.STUDY -> StudyPage(if (saved.isEmpty()) emptyList() else saved, onChooseMeaning, onChooseFill)
                Screen.SETTINGS -> SettingsPage(prefs, onPrefs, stats, onFullReset)
                else -> Unit
            }
        } }
    }
}

@Composable private fun DrawerRow(icon:String,label:String,selected:Boolean,onClick:()->Unit){ NavigationDrawerItem(label={Text(label)},icon={Text(icon)},selected=selected,onClick=onClick,modifier=Modifier.padding(horizontal=12.dp,vertical=2.dp)) }

@Composable
private fun HomePage(lessons: List<ReaderLesson>, saved: List<Lexeme>, completed: Set<String>, onLesson: (ReaderLesson)->Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = Header), shape = RoundedCornerShape(28.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(22.dp)) { Text("Bugün ne okuyalım?", color=Color.White,fontSize=26.sp,fontWeight=FontWeight.Bold); Text("${saved.size} kayıtlı kelime • ${completed.size} tamamlanan hikâye",color=Color(0xFFD5E4E8)); Spacer(Modifier.height(10.dp)); LinearProgressIndicator(progress={ if(lessons.isEmpty())0f else completed.size.toFloat()/lessons.size },modifier=Modifier.fillMaxWidth()) } }
        Text("Hikâyeler",fontSize=22.sp,fontWeight=FontWeight.Bold)
        lessons.forEach { item -> ElevatedCard(Modifier.fillMaxWidth().clickable{onLesson(item)},shape=RoundedCornerShape(22.dp)) { Row(Modifier.padding(18.dp),verticalAlignment=Alignment.CenterVertically){ Surface(color=Accent.copy(alpha=.14f),shape=RoundedCornerShape(14.dp)){Text(item.level,Modifier.padding(horizontal=13.dp,vertical=10.dp),fontWeight=FontWeight.Bold,color=Accent)}; Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f)){Text(item.title,fontSize=19.sp,fontWeight=FontWeight.Bold);Text(item.summary,color=MaterialTheme.colorScheme.onSurfaceVariant); if(item.id in completed) Text("✓ Tamamlandı",color=Accent,fontSize=12.sp,fontWeight=FontWeight.Bold)};Text("›",fontSize=30.sp,color=Accent)} } }
    }
}

@Composable
private fun ReaderPage(lesson: ReaderLesson, saved: List<Lexeme>, completed: Set<String>, prefs: UserPreferences, onToggle:(Lexeme)->Unit, onComplete:()->Unit, onBack:()->Unit) {
    var selected by remember(lesson.id){ mutableStateOf<Lexeme?>(null) }
    Column(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxWidth().height(230.dp).background(Header).padding(16.dp)) {
            Text("‹ Hikâyeler",color=Color.White,modifier=Modifier.clickable(onClick=onBack).padding(4.dp))
            Text(if(selected!=null && saved.any{it.id==selected!!.id})"★" else "☆",color=Color.White,fontSize=34.sp,modifier=Modifier.align(Alignment.TopEnd).clickable(enabled=selected!=null){selected?.let(onToggle)})
            Column(Modifier.align(Alignment.BottomStart).fillMaxWidth().heightIn(max=165.dp).verticalScroll(rememberScrollState())) {
                if(selected==null){Text("Bir kelimeye dokun",color=Color.White,fontSize=24.sp,fontWeight=FontWeight.Bold);Text("Anlam ve dil bilgisi burada görünür.",color=Color(0xFFD5E4E8))}
                else { Text(displayTitle(selected!!),color=Color.White,fontSize=24.sp,fontWeight=FontWeight.Bold);Text(selected!!.meaning,color=Color.White,fontSize=18.sp); Text(selected!!.wordClass,color=Color(0xFF9ED7D6),fontSize=13.sp); if(prefs.detailedExplanations) selected!!.explanation?.let{Text(it,color=Color(0xFFD5E4E8),fontSize=14.sp,modifier=Modifier.padding(top=7.dp))} }
            }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment=Alignment.CenterVertically){Text(lesson.level,color=Accent,fontWeight=FontWeight.Bold);Spacer(Modifier.width(10.dp));Text(lesson.title,fontSize=23.sp,fontWeight=FontWeight.Bold)}
            lesson.sentences.forEach { sentence -> FlowRow(horizontalArrangement=Arrangement.spacedBy(4.dp),verticalArrangement=Arrangement.spacedBy(6.dp)){ sentence.forEach { token -> val active=prefs.highlightEnabled && selected?.id==token.lexeme.id; Text(token.text,fontSize=prefs.storyTextSize.sp,lineHeight=(prefs.storyTextSize+9).sp,color=if(active)Color.White else MaterialTheme.colorScheme.onBackground,modifier=Modifier.background(if(active)Accent else Color.Transparent,RoundedCornerShape(6.dp)).clickable{selected=token.lexeme}.padding(horizontal=2.dp,vertical=2.dp)) } } }
            Button(onClick=onComplete,enabled=lesson.id !in completed,modifier=Modifier.fillMaxWidth().height(52.dp),shape=RoundedCornerShape(16.dp)){Text(if(lesson.id in completed)"✓ Hikâye tamamlandı" else "Hikâyeyi tamamlandı olarak işaretle")}
            Spacer(Modifier.height(60.dp))
        }
    }
}

@Composable
private fun WordsPage(lessons:List<ReaderLesson>,saved:List<Lexeme>,onRemove:(Lexeme)->Unit,onStudy:(List<Lexeme>)->Unit){
    var selected by remember{ mutableStateOf(setOf<String>()) }
    val lessonById=remember(lessons){lessons.flatMap{l->l.lexemes.map{it.id to l.title}}.toMap()}
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),verticalArrangement=Arrangement.spacedBy(10.dp)){
        Text("${saved.size} kayıtlı kelime / ifade",color=MaterialTheme.colorScheme.onSurfaceVariant)
        Row(Modifier.fillMaxWidth(),verticalAlignment=Alignment.CenterVertically){TextButton(onClick={selected=if(selected.size==saved.size) emptySet() else saved.map{it.id}.toSet()}){Text(if(selected.size==saved.size&&saved.isNotEmpty())"Seçimi kaldır" else "Tümünü seç")};Spacer(Modifier.weight(1f));Text("${selected.size} seçili")}
        Button(onClick={onStudy(saved.filter{it.id in selected})},enabled=selected.isNotEmpty(),modifier=Modifier.fillMaxWidth().height(52.dp),shape=RoundedCornerShape(16.dp)){Text("Seçilenlerle çalış (${selected.size})")}
        saved.asReversed().forEach{item-> ElevatedCard(Modifier.fillMaxWidth().clickable{selected=if(item.id in selected)selected-item.id else selected+item.id},shape=RoundedCornerShape(18.dp),colors=CardDefaults.elevatedCardColors(containerColor=if(item.id in selected)Accent.copy(alpha=.12f) else MaterialTheme.colorScheme.surface)){Row(Modifier.padding(14.dp),verticalAlignment=Alignment.CenterVertically){Checkbox(item.id in selected,{selected=if(item.id in selected)selected-item.id else selected+item.id});Column(Modifier.weight(1f)){Text(displayTitle(item),fontWeight=FontWeight.Bold,fontSize=18.sp);Text(item.meaning,color=MaterialTheme.colorScheme.onSurfaceVariant);Text(listOfNotNull(lessonById[item.id],item.wordClass).joinToString(" • "),color=Accent,fontSize=12.sp)};Text("★",color=Accent,fontSize=25.sp,modifier=Modifier.clickable{onRemove(item)}.padding(6.dp))}}}
    }
}

@Composable
private fun StudyPage(items:List<Lexeme>,onMeaning:()->Unit,onFill:()->Unit){ Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(14.dp)){ Card(colors=CardDefaults.cardColors(containerColor=Accent.copy(alpha=.12f)),shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(18.dp)){Text("Kelime Çalışması",fontSize=24.sp,fontWeight=FontWeight.Bold);Text("${items.size} kelime hazır",color=MaterialTheme.colorScheme.onSurfaceVariant)}}; StudyModeCard("1. Anlam Testi","Almanca → Türkçe ve Türkçe → Almanca karışık çoktan seçmeli test.",onMeaning); StudyModeCard("2. Boşluk Doldurma","Hikâyedeki gerçek cümlede eksik kelimeyi seçeneklerden bul.",onFill) } }
@Composable private fun StudyModeCard(title:String,desc:String,onClick:()->Unit){ElevatedCard(Modifier.fillMaxWidth().clickable(onClick=onClick),shape=RoundedCornerShape(22.dp)){Column(Modifier.padding(20.dp)){Text(title,fontSize=21.sp,fontWeight=FontWeight.Bold);Spacer(Modifier.height(5.dp));Text(desc,color=MaterialTheme.colorScheme.onSurfaceVariant);Spacer(Modifier.height(14.dp));Button(onClick=onClick,modifier=Modifier.fillMaxWidth().height(50.dp),shape=RoundedCornerShape(15.dp)){Text("Başla")}}}}

@Composable
private fun MeaningQuiz(items:List<Lexeme>,onBack:()->Unit,onAnswered:(Boolean)->Unit){ if(items.isEmpty()){EmptyStudy(onBack);return}; var index by remember{mutableIntStateOf(0)};var answer by remember(index){mutableStateOf<String?>(null)};var score by remember{mutableIntStateOf(0)};val item=items[index%items.size];val askGerman=index%2==0;val correct=if(askGerman)displayTitle(item) else item.meaning;val options=remember(index,items){(items.filter{it.id!=item.id}.shuffled().map{if(askGerman)displayTitle(it) else it.meaning}.distinct().take(3)+correct).distinct().shuffled()};QuizScaffold("Anlam Testi",index,items.size,score,onBack){Text(if(askGerman)"Bu Türkçe anlamın Almancası hangisi?" else "Bu Almanca kelimenin Türkçesi hangisi?",color=MaterialTheme.colorScheme.onSurfaceVariant);Text(if(askGerman)item.meaning else displayTitle(item),fontSize=28.sp,fontWeight=FontWeight.Bold,modifier=Modifier.padding(vertical=12.dp));options.forEach{opt->AnswerButton(opt,answer,opt==correct){if(answer==null){answer=opt;val ok=opt==correct;if(ok)score++;onAnswered(ok)}}};answer?.let{Text(if(it==correct)"Doğru ✓" else "Doğru cevap: $correct",color=if(it==correct)Accent else MaterialTheme.colorScheme.error,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=8.dp));Button(onClick={index=(index+1)%items.size},modifier=Modifier.fillMaxWidth().height(50.dp),shape=RoundedCornerShape(15.dp)){Text("Sonraki soru")}}}}

@Composable
private fun FillQuiz(items:List<Lexeme>,lessons:List<ReaderLesson>,onBack:()->Unit,onAnswered:(Boolean)->Unit){val questions=remember(items,lessons){buildFillQuestions(items,lessons)};if(questions.isEmpty()){EmptyStudy(onBack);return};var index by remember{mutableIntStateOf(0)};var answer by remember(index){mutableStateOf<String?>(null)};var score by remember{mutableIntStateOf(0)};val q=questions[index%questions.size];QuizScaffold("Boşluk Doldurma",index,questions.size,score,onBack){Text("Boşluğa uygun kelime / ifadeyi seç.",color=MaterialTheme.colorScheme.onSurfaceVariant);Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.surfaceVariant),shape=RoundedCornerShape(18.dp),modifier=Modifier.fillMaxWidth().padding(vertical=12.dp)){Text(q.sentence,Modifier.padding(18.dp),fontSize=21.sp,lineHeight=30.sp)};q.options.forEach{opt->AnswerButton(opt,answer,opt==q.correct){if(answer==null){answer=opt;val ok=opt==q.correct;if(ok)score++;onAnswered(ok)}}};answer?.let{Text(if(it==q.correct)"Doğru ✓" else "Doğru cevap: ${q.correct}",color=if(it==q.correct)Accent else MaterialTheme.colorScheme.error,fontWeight=FontWeight.Bold,modifier=Modifier.padding(top=8.dp));Button(onClick={index=(index+1)%questions.size},modifier=Modifier.fillMaxWidth().height(50.dp),shape=RoundedCornerShape(15.dp)){Text("Sonraki soru")}}}}

@Composable private fun QuizScaffold(title:String,index:Int,total:Int,score:Int,onBack:()->Unit,content:@Composable ColumnScope.()->Unit){Column(Modifier.fillMaxSize()){Column(Modifier.fillMaxWidth().background(Header).statusBarsPadding().padding(18.dp)){Text("‹ Çalışma seçenekleri",color=Color.White,modifier=Modifier.clickable(onClick=onBack));Spacer(Modifier.height(10.dp));Text(title,color=Color.White,fontSize=25.sp,fontWeight=FontWeight.Bold);Text("Soru ${index+1} / $total • Doğru: $score",color=Color(0xFFD5E4E8));Spacer(Modifier.height(8.dp));LinearProgressIndicator(progress={(index+1).toFloat()/total},modifier=Modifier.fillMaxWidth())};Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),content=content)}}
@Composable private fun AnswerButton(text:String,selected:String?,correct:Boolean,onClick:()->Unit){val chosen=selected==text;val container=when{selected==null->MaterialTheme.colorScheme.surface;chosen&&correct->Accent.copy(alpha=.18f);chosen&&!correct->MaterialTheme.colorScheme.errorContainer;else->MaterialTheme.colorScheme.surface};OutlinedButton(onClick=onClick,enabled=selected==null,modifier=Modifier.fillMaxWidth().heightIn(min=52.dp).padding(vertical=4.dp),shape=RoundedCornerShape(15.dp),colors=ButtonDefaults.outlinedButtonColors(containerColor=container)){Text(text,Modifier.fillMaxWidth(),fontSize=17.sp,color=MaterialTheme.colorScheme.onSurface)}}
@Composable private fun EmptyStudy(onBack:()->Unit){Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center){Column(horizontalAlignment=Alignment.CenterHorizontally){Text("Çalışmak için önce kelime seç.");TextButton(onClick=onBack){Text("Geri dön")}}}}

@Composable
private fun SettingsPage(prefs:UserPreferences,onPrefs:(UserPreferences)->Unit,stats:LearningStats,onReset:()->Unit){var confirm by remember{mutableStateOf(false)};Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(18.dp),verticalArrangement=Arrangement.spacedBy(16.dp)){ElevatedCard(shape=RoundedCornerShape(20.dp)){Column(Modifier.padding(18.dp)){Text("Tema",fontSize=20.sp,fontWeight=FontWeight.Bold);Row(horizontalArrangement=Arrangement.spacedBy(6.dp)){listOf("system" to "Sistem","light" to "Açık","dark" to "Karanlık").forEach{(id,label)->FilterChip(prefs.themeMode==id,{onPrefs(prefs.copy(themeMode=id))},{Text(label)})}};Spacer(Modifier.height(12.dp));Text("Hikâye yazı boyutu: ${prefs.storyTextSize}");Slider(prefs.storyTextSize.toFloat(),{onPrefs(prefs.copy(storyTextSize=it.toInt()))},valueRange=18f..30f,steps=5)}};ElevatedCard(shape=RoundedCornerShape(20.dp)){Column(Modifier.padding(18.dp)){Text("Çalışma",fontSize=20.sp,fontWeight=FontWeight.Bold);Text("Toplam cevap: ${stats.answered}");Text("Doğru cevap: ${stats.correct}");Text("Oturum: ${stats.studySessions}")}};OutlinedButton(onClick={confirm=true},modifier=Modifier.fillMaxWidth().height(52.dp),shape=RoundedCornerShape(16.dp)){Text("Uygulamayı tamamen sıfırla")}};if(confirm)AlertDialog(onDismissRequest={confirm=false},title={Text("Tüm veriler silinsin mi?")},text={Text("Kaydedilen kelimeler, tamamlanan hikâyeler, çalışma geçmişi, profil ve ayarlar cihazdan silinecek.")},confirmButton={TextButton(onClick={onReset();confirm=false}){Text("Tamamen sıfırla")}},dismissButton={TextButton(onClick={confirm=false}){Text("Vazgeç")}})}

private fun buildFillQuestions(items:List<Lexeme>,lessons:List<ReaderLesson>):List<FillQuestion>{val ids=items.map{it.id}.toSet();val byId=items.associateBy{it.id};return lessons.flatMap{lesson->lesson.sentences.mapNotNull{sentence->val id=sentence.firstOrNull{it.lexeme.id in ids}?.lexeme?.id?:return@mapNotNull null;val target=byId[id]?:return@mapNotNull null;val tokens=sentence.filter{it.lexeme.id==id}.map{it.text.trimEnd('.',',',':',';','!','?')};if(tokens.isEmpty())return@mapNotNull null;val answer=tokens.joinToString(" … ");val masked=sentence.joinToString(" "){if(it.lexeme.id==id)"_____" else it.text}.replace("_____ _____","_____");val distractors=items.filter{it.id!=id}.shuffled().map{displayTitle(it)}.distinct().take(3);FillQuestion(target,masked,answer,(distractors+answer).distinct().shuffled())}}.distinctBy{it.lexeme.id}}
private fun displayTitle(item:Lexeme):String{if(item.wordClass!="İsim")return item.base;val art=item.article?.let{"$it "}?:"";val pl=item.plural?.let{" — Pl.: ${pluralShort(item.base,it)}"}?:"";return "$art${item.base}$pl"}
private fun pluralShort(base:String,plural:String):String{val b=base.trim();val p=plural.trim();if(p.equals(b,true)||p=="-")return "-";return if(p.startsWith(b))p.removePrefix(b).ifBlank{"-"}else p}
