from pathlib import Path

main = Path('app/src/main/java/de/bascurt/almancaokuyucu/MainActivity.kt')
text = main.read_text()

old = '    val lang = preferences.appLanguage\n'
new = '    val lang = if (page == AppPage.SETTINGS) settingsDraft.appLanguage else preferences.appLanguage\n'
if old not in text:
    raise SystemExit('MainShell language line not found')
text = text.replace(old, new, 1)

old_settings = '    val lang = savedPrefs.appLanguage\n'
new_settings = '    val lang = prefs.appLanguage\n'
if old_settings not in text:
    raise SystemExit('SettingsScreen language line not found')
text = text.replace(old_settings, new_settings, 1)

main.write_text(text)

gradle = Path('app/build.gradle.kts')
g = gradle.read_text()
g = g.replace('versionCode = 36', 'versionCode = 37', 1)
g = g.replace('versionName = "0.4.1.29"', 'versionName = "0.4.1.30"', 1)
gradle.write_text(g)

print('live language preview applied')
