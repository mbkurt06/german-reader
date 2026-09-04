from pathlib import Path

main = Path('app/src/main/java/de/bascurt/almancaokuyucu/MainActivity.kt')
text = main.read_text()
old = '''@Composable
private fun LanguageDropdown(selected: String, appLanguage: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf(
        "tr" to "Türkçe",
        "en" to "İngilizce",
        "es" to "İspanyolca",
        "fr" to "Fransızca",
        "it" to "İtalyanca"
    )
    val selectedLabel = languages.firstOrNull { it.first == selected }?.second ?: "Türkçe"
    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
            Text(uiText(appLanguage, selectedLabel), Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Start)
            Text("▾")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(.9f)) {
            languages.forEach { (code, label) ->
                DropdownMenuItem(
                    text = { Text(uiText(appLanguage, label)) },
                    trailingIcon = { if (code == selected) Text("✓", color = Turquoise, fontWeight = FontWeight.Bold) },
                    onClick = { onSelected(code); expanded = false }
                )
            }
        }
    }
}
'''
new = '''@Composable
private fun LanguageDropdown(selected: String, appLanguage: String, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val languages = listOf(
        "tr" to "🇹🇷 Türkçe",
        "en" to "🇬🇧 English",
        "de" to "🇩🇪 Deutsch",
        "es" to "🇪🇸 Español",
        "fr" to "🇫🇷 Français",
        "it" to "🇮🇹 Italiano"
    )
    val selectedLabel = languages.firstOrNull { it.first == selected }?.second ?: "🇹🇷 Türkçe"
    Box(Modifier.fillMaxWidth()) {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
            Text(selectedLabel, Modifier.weight(1f), textAlign = androidx.compose.ui.text.style.TextAlign.Start)
            Text("▾")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth(.9f)) {
            languages.forEach { (code, label) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    trailingIcon = { if (code == selected) Text("✓", color = Turquoise, fontWeight = FontWeight.Bold) },
                    onClick = { onSelected(code); expanded = false }
                )
            }
        }
    }
}
'''
if old not in text:
    raise SystemExit('LanguageDropdown block not found')
text = text.replace(old, new, 1)
main.write_text(text)

gradle = Path('app/build.gradle.kts')
g = gradle.read_text().replace('versionCode = 35', 'versionCode = 36').replace('versionName = "0.4.1.28"', 'versionName = "0.4.1.29"')
gradle.write_text(g)
print('native language labels applied')
