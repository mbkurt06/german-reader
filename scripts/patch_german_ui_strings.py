from pathlib import Path

p = Path('app/src/main/java/de/bascurt/almancaokuyucu/UiText.kt')
t = p.read_text()
old = '''internal fun uiText(language: String, key: String): String {
    val values = uiStrings[key] ?: return key
    return values[language] ?: values["tr"] ?: key
}
'''
new = '''internal fun uiText(language: String, key: String): String {
    if (language == "de") return germanUiStrings[key] ?: uiStrings[key]?.get("tr") ?: key
    val values = uiStrings[key] ?: return key
    return values[language] ?: values["tr"] ?: key
}

private val germanUiStrings: Map<String, String> = mapOf(
    "Almanca Okuyucu" to "Deutsch-Leser",
    "Ana Sayfa" to "Startseite",
    "Hikâyeler" to "Geschichten",
    "Kelimelerim" to "Meine Wörter",
    "Çalış" to "Üben",
    "Çalışmalarım" to "Übungen",
    "Profilim" to "Mein Profil",
    "Tamamlanan Hikâyeler" to "Abgeschlossene Geschichten",
    "İstatistikler" to "Statistiken",
    "Ayarlar" to "Einstellungen",
    "Hakkında" to "Über",
    "Uygulama hakkında" to "Über die App",
    "Dil" to "Sprache",
    "Uygulama dili" to "App-Sprache",
    "Menüler ve uygulama arayüzü için kullanılacak dil." to "Sprache für Menüs und die Benutzeroberfläche.",
    "Hikâye çeviri dili" to "Übersetzungssprache der Geschichte",
    "Hikâyedeki Çeviri butonuna bastığında gösterilecek dil." to "Sprache, die beim Tippen auf Übersetzen in einer Geschichte angezeigt wird.",
    "Görünüm" to "Darstellung",
    "Tema" to "Design",
    "Uygulama teması" to "App-Design",
    "Sistem" to "System",
    "Açık" to "Hell",
    "Karanlık" to "Dunkel",
    "Koyu" to "Dunkel",
    "Arayüz yazı boyutu" to "Schriftgröße der Oberfläche",
    "Hikâye yazı boyutu" to "Schriftgröße der Geschichte",
    "Okuma" to "Lesen",
    "Seçilen kelimeyi vurgula" to "Ausgewähltes Wort hervorheben",
    "Ayrıntılı açıklamaları göster" to "Ausführliche Erklärungen anzeigen",
    "Sınav" to "Quiz",
    "Hikâye sınavında soru sayısı" to "Fragen im Geschichten-Quiz",
    "Değişiklikleri kaydet" to "Änderungen speichern",
    "Değişiklikler kaydedildi" to "Änderungen gespeichert",
    "Kaydedilmemiş değişiklikler" to "Nicht gespeicherte Änderungen",
    "Değişiklikler kaydedilsin mi?" to "Änderungen speichern?",
    "Kaydet ve çık" to "Speichern und verlassen",
    "Kaydetmeden çık" to "Ohne Speichern verlassen",
    "Vazgeç" to "Abbrechen",
    "Uygulamayı tamamen sıfırla" to "App vollständig zurücksetzen",
    "Uygulama sıfırlansın mı?" to "App zurücksetzen?",
    "Tamamen sıfırla" to "Vollständig zurücksetzen",
    "Çeviri" to "Übersetzen",
    "Çeviriyi Gizle" to "Übersetzung ausblenden",
    "Hikâye" to "Geschichte",
    "Kelimeler" to "Wörter",
    "Gramer" to "Grammatik",
    "Bir kelimeye dokun" to "Tippe auf ein Wort",
    "Anlamı, kelime türü ve kullanım bilgisi burada görünecek." to "Bedeutung, Wortart und Verwendung werden hier angezeigt.",
    "Kelime türü" to "Wortart",
    "Artikel" to "Artikel",
    "Çoğul" to "Plural",
    "Anlam" to "Bedeutung",
    "Bu cümlede" to "In diesem Satz",
    "Mastar" to "Infinitiv",
    "3. tekil şahıs" to "3. Person Singular",
    "Yalın hâl" to "Grundform",
    "Hikâye tamamlandı" to "Geschichte abgeschlossen",
    "Tamamlandı işaretini kaldır" to "Als abgeschlossen entfernen",
    "Hikâyeyi tamamlandı olarak işaretle" to "Geschichte als abgeschlossen markieren",
    "Başla" to "Starten",
    "Sonraki" to "Weiter",
    "Doğru" to "Richtig",
    "Doğru cevap" to "Richtige Antwort",
    "Kelime Çalışması" to "Worttraining",
    "Boşluk Doldurma" to "Lückentext",
    "Türkçe" to "Türkisch",
    "İngilizce" to "Englisch",
    "İspanyolca" to "Spanisch",
    "Fransızca" to "Französisch",
    "İtalyanca" to "Italienisch"
)
'''
if old not in t:
    raise SystemExit('uiText function block not found')
t = t.replace(old, new, 1)
p.write_text(t)

g = Path('app/build.gradle.kts')
s = g.read_text()
s = s.replace('versionCode = 37', 'versionCode = 38', 1)
s = s.replace('versionName = "0.4.1.30"', 'versionName = "0.4.1.31"', 1)
g.write_text(s)
print('German UI strings added')
