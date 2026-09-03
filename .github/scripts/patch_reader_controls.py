from pathlib import Path

# Completion state must be reversible.
p = Path('app/src/main/java/de/bascurt/almancaokuyucu/data/UserPreferencesStore.kt')
s = p.read_text()
old = '''    fun markLessonRead(id: String) {
        val updated = readLessonIds() + id
        prefs.edit().putStringSet("read_lessons", updated).apply()
    }
'''
if 'fun toggleLessonRead(id: String)' not in s:
    if old not in s:
        raise SystemExit('markLessonRead block missing')
    s = s.replace(old, old + '''
    fun toggleLessonRead(id: String) {
        val current = readLessonIds()
        val updated = if (id in current) current - id else current + id
        prefs.edit().putStringSet("read_lessons", updated).apply()
    }
''', 1)
    p.write_text(s)

# Reader UI: completion toggle + fixed translation control.
p = Path('app/src/main/java/de/bascurt/almancaokuyucu/MainActivity.kt')
s = p.read_text()
s = s.replace('''    fun completeLesson(lesson: ReaderLesson) {
        userStore.markLessonRead(lesson.id)
        completedLessonIds = userStore.readLessonIds()
    }
''', '''    fun completeLesson(lesson: ReaderLesson) {
        userStore.toggleLessonRead(lesson.id)
        completedLessonIds = userStore.readLessonIds()
    }
''', 1)

old_start = '''    var showTranslations by remember(lesson.id) { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).navigationBarsPadding().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 110.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            LevelBadge(lesson.level)
            Spacer(Modifier.width(12.dp))
            Text(lesson.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        OutlinedButton(
            onClick = { showTranslations = !showTranslations },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(if (showTranslations) "Çeviriyi gizle" else "Çevirisi", fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(18.dp))
        lesson.sentences.forEachIndexed { sentenceIndex, sentence ->
'''
new_start = '''    var showTranslations by remember(lesson.id) { mutableStateOf(false) }
    Column(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, top = 18.dp, bottom = 10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LevelBadge(lesson.level)
                Spacer(Modifier.width(12.dp))
                Text(lesson.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = { showTranslations = !showTranslations },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(if (showTranslations) "Çeviriyi gizle" else "Çevirisi", fontWeight = FontWeight.SemiBold)
            }
        }
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).navigationBarsPadding().padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 110.dp)) {
            lesson.sentences.forEachIndexed { sentenceIndex, sentence ->
'''
if old_start not in s:
    raise SystemExit('StoryScreen start block missing')
s = s.replace(old_start, new_start, 1)

old_end = '''        if (isCompleted) {
            Surface(color = Turquoise.copy(alpha = .12f), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text("✓", color = Turquoise, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(10.dp))
                    Column { Text("Hikâye tamamlandı", fontWeight = FontWeight.Bold); Text("Bu hikâye tamamlananlar listesinde.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp) }
                }
            }
        } else {
            Button(onClick = onComplete, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(17.dp)) {
                Text("Hikâyeyi tamamlandı olarak işaretle", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
'''
new_end = '''        if (isCompleted) {
            Surface(color = Turquoise.copy(alpha = .12f), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("✓", color = Turquoise, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(10.dp))
                        Column { Text("Hikâye tamamlandı", fontWeight = FontWeight.Bold); Text("Bu hikâye tamamlananlar listesinde.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp) }
                    }
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(onClick = onComplete, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(15.dp)) {
                        Text("Tamamlandı işaretini kaldır")
                    }
                }
            }
        } else {
            Button(onClick = onComplete, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(17.dp)) {
                Text("Hikâyeyi tamamlandı olarak işaretle", fontWeight = FontWeight.SemiBold)
            }
        }
        }
    }
}
'''
if old_end not in s:
    raise SystemExit('StoryScreen completion block missing')
s = s.replace(old_end, new_end, 1)
p.write_text(s)

# Remaining reinforcement noun phrase.
p = Path('app/src/main/java/de/bascurt/almancaokuyucu/data/FocusedLessons.kt')
s = p.read_text()
if 'linkAt(12, "ihre Aufgaben"' not in s:
    anchor = '                linkAt(11, "den Kollegen", "çalışma arkadaşları", "den Kollegen → çalışma arkadaşları. Dativ çoğul artikel + isim aynı isim grubudur."),\n'
    addition = '                linkAt(12, "ihre Aufgaben", "görevlerini", "ihre Aufgaben → onun görevleri / görevlerini. İyelik belirleyicisi + isim aynı isim grubudur; seçilen koyu, diğer kelime açık vurgulanır."),\n'
    if anchor not in s:
        raise SystemExit('FocusedLessons noun anchor missing')
    p.write_text(s.replace(anchor, anchor + addition, 1))
