from pathlib import Path

path = Path('app/src/main/java/de/bascurt/almancaokuyucu/MainActivity.kt')
text = path.read_text()

if 'import androidx.compose.ui.graphics.luminance\n' not in text:
    text = text.replace('import androidx.compose.ui.graphics.Color\n', 'import androidx.compose.ui.graphics.Color\nimport androidx.compose.ui.graphics.luminance\n', 1)

text = text.replace(
'''                    nightMode = preferences.readerNightMode,
''',
'''                    readerThemeMode = preferences.readerThemeMode,
''',
1)
text = text.replace(
'''                    onNightModeChange = { onPreferences(preferences.copy(readerNightMode = it)) }
''',
'''                    onReaderThemeModeChange = { onPreferences(preferences.copy(readerThemeMode = it, readerNightMode = it == "dark")) }
''',
1)

text = text.replace(
'''    nightMode: Boolean,
''',
'''    readerThemeMode: String,
''',
1)
text = text.replace(
'''    onNightModeChange: (Boolean) -> Unit,
''',
'''    onReaderThemeModeChange: (String) -> Unit,
''',
1)
text = text.replace(
'''    val activity = LocalContext.current as? Activity
    val readerBackground = if (nightMode) Color(0xFF0B1014) else MaterialTheme.colorScheme.background
''',
'''    val activity = LocalContext.current as? Activity
    val appDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val nightMode = when (readerThemeMode) {
        "dark" -> true
        "light" -> false
        else -> appDark
    }
    val readerBackground = if (nightMode) Color(0xFF0B1014) else MaterialTheme.colorScheme.background
''',
1)

old = '''                Surface(
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
                            modifier = Modifier.size(54.dp).clickable { onNightModeChange(!nightMode) }
                        ) { Box(contentAlignment = Alignment.Center) { Text("☾", fontSize = 30.sp) } }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Gece modu", fontWeight = FontWeight.SemiBold)
                            Text("Okuma alanını koyu renge geçirir.", fontSize = 12.sp, color = Color.White.copy(alpha = .65f))
                        }
                        Switch(
                            checked = nightMode,
                            onCheckedChange = onNightModeChange,
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
new = '''                Surface(
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
if old not in text:
    raise SystemExit('reader theme block not found')
text = text.replace(old, new, 1)

text = text.replace(
'''                Text(uiText(lang, "Tema"), fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("system" to "Sistem", "light" to "Açık", "dark" to "Karanlık").forEach { (id, label) -> FilterChip(selected = prefs.themeMode == id, onClick = { onChange(prefs.copy(themeMode = id)); savedNotice = false }, label = { Text(uiText(lang, label)) }) }
                }
''',
'''                Text(uiText(lang, "Uygulama teması"), fontWeight = FontWeight.Bold)
                Text("Sistem seçilirse telefonun açık/koyu görünüm ayarı otomatik izlenir.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("system" to "Sistem", "light" to "Açık", "dark" to "Koyu").forEach { (id, label) -> FilterChip(selected = prefs.themeMode == id, onClick = { onChange(prefs.copy(themeMode = id)); savedNotice = false }, label = { Text(uiText(lang, label)) }) }
                }
''',
1)

path.write_text(text)
print('Theme and reader mode patch applied')
