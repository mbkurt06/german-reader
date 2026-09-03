from pathlib import Path

main_path = Path('app/src/main/java/de/bascurt/almancaokuyucu/MainActivity.kt')
text = main_path.read_text()

# Drawer: 60% of screen width.
text = text.replace('ModalDrawerSheet(modifier = Modifier.width(310.dp)) {', 'ModalDrawerSheet(modifier = Modifier.fillMaxWidth(.60f)) {', 1)

# Reader theme: deterministic local night toggle only.
text = text.replace('''    val appDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val nightMode = when (readerThemeMode) {
        "dark" -> true
        "light" -> false
        else -> appDark
    }
''', '''    val nightMode = readerThemeMode == "dark"
''', 1)

old_theme = '''                Surface(
                    color = Color.White.copy(alpha = .07f),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
                        Text("Okuma teması", fontWeight = FontWeight.SemiBold)
                        Text("Sadece hikâye okuma ekranını etkiler; uygulamanın genel temasını değiştirmez.", fontSize = 12.sp, color = Color.White.copy(alpha = .65f))
                        Spacer(Modifier.height(10.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                            listOf("app" to "Uygulama", "light" to "Açık", "dark" to "Koyu").forEach { (id, label) ->
                                FilterChip(
                                    selected = readerThemeMode == id,
                                    onClick = { onReaderThemeModeChange(id) },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }
                }
'''
new_theme = '''                Surface(
                    color = Color.White.copy(alpha = .07f),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = .08f),
                            contentColor = Color.White,
                            shape = CircleShape,
                            modifier = Modifier.size(54.dp).clickable { onReaderThemeModeChange(if (nightMode) "light" else "dark") }
                        ) { Box(contentAlignment = Alignment.Center) { Text("☾", fontSize = 30.sp) } }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Gece modu", fontWeight = FontWeight.SemiBold)
                            Text("Yalnızca hikâye okuma alanını koyu yapar.", fontSize = 12.sp, color = Color.White.copy(alpha = .65f))
                        }
                        Switch(
                            checked = nightMode,
                            onCheckedChange = { enabled -> onReaderThemeModeChange(if (enabled) "dark" else "light") },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Turquoise,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.White.copy(alpha = .18f),
                                uncheckedBorderColor = Color.White.copy(alpha = .38f)
                            )
                        )
                    }
                }
'''
if old_theme not in text:
    raise SystemExit('reader theme block not found')
text = text.replace(old_theme, new_theme, 1)

# Live preview of settings theme within settings page; cancel naturally returns to saved outer theme.
old_settings_case = '''                    AppPage.SETTINGS -> SettingsScreen(
                        prefs = settingsDraft,
                        savedPrefs = preferences,
                        onChange = { settingsDraft = it },
                        onSave = { onPreferences(settingsDraft) },
                        onFullReset = onFullReset
                    )
'''
new_settings_case = '''                    AppPage.SETTINGS -> {
                        val previewDark = when (settingsDraft.themeMode) {
                            "dark" -> true
                            "light" -> false
                            else -> isSystemInDarkTheme()
                        }
                        val previewScheme = if (previewDark) {
                            darkColorScheme(primary = Turquoise, secondary = Turquoise)
                        } else {
                            lightColorScheme(primary = Turquoise, secondary = Turquoise, background = SoftBg)
                        }
                        MaterialTheme(colorScheme = previewScheme) {
                            SettingsScreen(
                                prefs = settingsDraft,
                                savedPrefs = preferences,
                                onChange = { settingsDraft = it },
                                onSave = { onPreferences(settingsDraft) },
                                onFullReset = onFullReset
                            )
                        }
                    }
'''
if old_settings_case not in text:
    raise SystemExit('settings case not found')
text = text.replace(old_settings_case, new_settings_case, 1)

# Replace settings sliders with +/- controls styled like reader brightness.
old_ui = '''                Text(uiText(lang, "Arayüz yazı boyutu"), fontWeight = FontWeight.Bold)
                Text("${(prefs.uiScale * 100).toInt()}%", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Slider(value = prefs.uiScale, onValueChange = { onChange(prefs.copy(uiScale = it)); savedNotice = false }, valueRange = .85f..1.25f)
'''
new_ui = '''                Text(uiText(lang, "Arayüz yazı boyutu"), fontWeight = FontWeight.Bold)
                StepValueControl(
                    valueText = "${(prefs.uiScale * 100).toInt()}%",
                    canDecrease = prefs.uiScale > .85f,
                    canIncrease = prefs.uiScale < 1.25f,
                    onDecrease = { onChange(prefs.copy(uiScale = (prefs.uiScale - .05f).coerceAtLeast(.85f))); savedNotice = false },
                    onIncrease = { onChange(prefs.copy(uiScale = (prefs.uiScale + .05f).coerceAtMost(1.25f))); savedNotice = false },
                    decreaseLabel = "A⁻",
                    increaseLabel = "A⁺"
                )
'''
if old_ui not in text:
    raise SystemExit('ui scale slider not found')
text = text.replace(old_ui, new_ui, 1)

old_quiz = '''                Text("${uiText(lang, "Hikâye sınavında soru sayısı")}: ${prefs.quizQuestionCount}", fontWeight = FontWeight.Bold)
                Slider(value = prefs.quizQuestionCount.toFloat(), onValueChange = { onChange(prefs.copy(quizQuestionCount = it.toInt())); savedNotice = false }, valueRange = 5f..20f, steps = 14)
'''
new_quiz = '''                Text(uiText(lang, "Hikâye sınavında soru sayısı"), fontWeight = FontWeight.Bold)
                StepValueControl(
                    valueText = "${prefs.quizQuestionCount}",
                    canDecrease = prefs.quizQuestionCount > 5,
                    canIncrease = prefs.quizQuestionCount < 20,
                    onDecrease = { onChange(prefs.copy(quizQuestionCount = (prefs.quizQuestionCount - 1).coerceAtLeast(5))); savedNotice = false },
                    onIncrease = { onChange(prefs.copy(quizQuestionCount = (prefs.quizQuestionCount + 1).coerceAtMost(20))); savedNotice = false },
                    decreaseLabel = "−",
                    increaseLabel = "+"
                )
'''
if old_quiz not in text:
    raise SystemExit('quiz slider not found')
text = text.replace(old_quiz, new_quiz, 1)

# Add shared compact step control before SettingCard.
marker = '@Composable private fun SettingCard(title: String, content: @Composable ColumnScope.() -> Unit) {'
helper = '''@Composable
private fun StepValueControl(
    valueText: String,
    canDecrease: Boolean,
    canIncrease: Boolean,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    decreaseLabel: String = "−",
    increaseLabel: String = "+"
) {
    Spacer(Modifier.height(8.dp))
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = CircleShape,
            modifier = Modifier.size(54.dp).clickable(enabled = canDecrease, onClick = onDecrease)
        ) { Box(contentAlignment = Alignment.Center) { Text(decreaseLabel, fontSize = 22.sp, fontWeight = FontWeight.SemiBold) } }
        Spacer(Modifier.weight(1f))
        Surface(color = Turquoise.copy(alpha = .13f), shape = RoundedCornerShape(11.dp)) {
            Text(valueText, Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = Turquoise, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.weight(1f))
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
            shape = CircleShape,
            modifier = Modifier.size(54.dp).clickable(enabled = canIncrease, onClick = onIncrease)
        ) { Box(contentAlignment = Alignment.Center) { Text(increaseLabel, fontSize = 24.sp, fontWeight = FontWeight.SemiBold) } }
    }
}

'''
if marker not in text:
    raise SystemExit('SettingCard marker missing')
text = text.replace(marker, helper + marker, 1)

main_path.write_text(text)

# Profile daily goal gets the same +/- visual language.
profile_path = Path('app/src/main/java/de/bascurt/almancaokuyucu/ProfileSetupActivity.kt')
p = profile_path.read_text()
old_goal = '''        Text("Günlük hedef: ${goal.toInt()} soru", fontWeight = FontWeight.Bold)
        Slider(
            value = goal,
            onValueChange = { goal = it },
            valueRange = 5f..50f,
            steps = 8
        )
'''
new_goal = '''        Text("Günlük hedef", fontWeight = FontWeight.Bold)
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
                modifier = Modifier.size(54.dp).clickable(enabled = goal > 5f) { goal = (goal - 5f).coerceAtLeast(5f) }
            ) { Box(contentAlignment = Alignment.Center) { Text("−", fontSize = 24.sp, fontWeight = FontWeight.SemiBold) } }
            Spacer(Modifier.weight(1f))
            Surface(color = ProfileTurquoise.copy(alpha = .13f), shape = RoundedCornerShape(11.dp)) {
                Text("${goal.toInt()} soru", Modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = ProfileTurquoise, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.weight(1f))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface,
                shape = CircleShape,
                modifier = Modifier.size(54.dp).clickable(enabled = goal < 50f) { goal = (goal + 5f).coerceAtMost(50f) }
            ) { Box(contentAlignment = Alignment.Center) { Text("+", fontSize = 24.sp, fontWeight = FontWeight.SemiBold) } }
        }
'''
if old_goal not in p:
    raise SystemExit('profile goal slider not found')
p = p.replace(old_goal, new_goal, 1)
profile_path.write_text(p)

print('UI controls round 2 patch applied')
