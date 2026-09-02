# Almanca Okuyucu — Yeni Metin Hazırlama Ana Promptu

Bu dosya, uygulamaya eklenecek her yeni Almanca metin için kullanılacak yaşayan kurallar bütünüdür.

## Temel kurallar
- Hikâye başlığı tamamen Almanca olmalı.
- Her hikâye belirgin bir günlük yaşam temasına bağlı olmalı: Küche, Gesundheit, Krankenhaus, Bäckerei, Supermarkt, Restaurant, Apotheke, Zuhause, Arbeit, Schule, Verkehr, Reisen vb.
- Tema içindeki isimler ve fiiller doğal bir olay akışında birlikte öğretilmeli.
- Yeni hikâyelerin hedef uzunluğu **en az yaklaşık 11 anlamlı cümle** olmalı. Gereksiz tekrarlarla yapay uzatma yapılmamalı.
- Metindeki her görünen kelime dokunulabilir olmalı.

## Kelime anlamları
- Kullanıcı herhangi bir kelimeye dokunduğunda Türkçe anlam alanı **asla boş bırakılmamalı**.
- Her görünen kelimenin o cümledeki bağlama uygun Türkçe karşılığı tanımlanmalı.
- `Türkçe anlamı`, boş string, yalnız Almanca kelimenin tekrar edilmesi veya anlamsız placeholder kabul edilmez.
- İçerik kalite kontrolünde sözlükte karşılığı olmayan tokenlar ayrıca bulunup tamamlanmalı.

## Kelime türleri ve çekimler
- Her kelimenin gerçek türü belirtilmeli: Fiil, İsim, Sıfat, Zarf, Edat, Bağlaç, Zamir, Artikel/Belirleyici, Parçacık, Özel isim vb.
- Fiillerde mastar, Präsens 3. tekil, Präteritum ve Perfekt hazırlanmalı.
- İsimlerde artikel ve çoğul hazırlanmalı; gerekiyorsa Akkusativ -n/-en notu eklenmeli.
- Sıfatlarda uygun olduğunda Positiv, Komparativ ve Superlativ hazırlanmalı.

## Kelime grupları
- Birlikte öğrenilmesi gereken yapı **tek Lexeme** olmalı.
- Ayrılabilen fiillerde çekimli fiil ile ayrılan ön ek aynı Lexeme kimliğine bağlanmalı. Örn. `hängt ... auf`, `holt ... ab`, `meldet ... an`, `steigt ... ein/aus`, `räumt ... auf/aus`.
- Kullanıcı bu yapılardan herhangi bir parçaya dokunduğunda grubun tüm parçaları birlikte vurgulanmalı ve üst anlam alanında bütün fiilin mastarı ve Türkçe anlamı görünmeli.
- Dönüşlü fiillerde `sich` ile ana fiil gerektiğinde aynı Lexeme grubuna bağlanmalı.
- Sabit fiil + edat ve diğer kalıplarda da yalnız gerçek yapının parçaları aynı Lexeme olmalı; modal fiiller yanlışlıkla gruba dahil edilmemeli.

## Edatlar ve bağlaçlar
- Önemli edatların açıklaması o cümledeki görevine göre yazılmalı; hâl bilgisi (Akkusativ/Dativ) gerektiğinde belirtilmeli.
- Bağlaçların cümlede kurduğu ilişki ve gerekiyorsa fiil konumu açıklanmalı.

## Sınav ve adaptif çalışma
- Yalnız öğrenme değeri yüksek Lexeme'ler `quizEligible = true` olmalı.
- Kişi/şehir adı, artikel, basit zamir ve anlamsız tokenlar sınav hedefi olmamalı.
- Tematik isim ve fiiller dengeli seçilmeli.
- Kaydedilen ve otomatik çalışma havuzundaki kelimeler güvenilir Lexeme kimlikleri taşımalı.

## Kalite kontrol
Yeni veya değiştirilmiş her hikâyede şunları kontrol et:
- Başlık Almanca mı?
- En az yaklaşık 11 anlamlı cümle var mı?
- Her görünen kelimenin Türkçe anlamı dolu ve bağlama uygun mu?
- Ayrılabilen fiillerin iki parçası aynı Lexeme kimliğinde mi?
- Dönüşlü ve sabit kelime grupları doğru parçaları birlikte vurguluyor mu?
- Fiil çekimleri doğru mu?
- İsim artikel/çoğul bilgileri doğru mu?
- Sınav hedeflerinde düşük değerli token var mı?

Bu kontroller tamamlanmadan yeni dersi hazır kabul etme.
