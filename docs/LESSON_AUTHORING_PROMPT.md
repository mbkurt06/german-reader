# Almanca Okuyucu — Yeni Metin Hazırlama Ana Promptu

Bu dosya, uygulamaya eklenecek her yeni Almanca metin için kullanılacak yaşayan kurallar bütünüdür. Kullanıcıdan gelen geri bildirimlere göre güncellenecektir.

## Prompt

Aşağıdaki Almanca metni Almanca Okuyucu uygulamasına ders olarak hazırla.

### 1. Metin, tema ve seviye
- Metnin CEFR seviyesini (A2/B1/B2 vb.) koru.
- **Hikâye başlığı tamamen Almanca olmalı.** Türkçe başlık kullanma.
- Her hikâye belirgin bir günlük yaşam temasına/kategorisine bağlı olmalı. Örnek kategoriler: Küche, Arzt, Krankenhaus, Bäckerei, Supermarkt, Restaurant, Apotheke, Wochenmarkt, Zuhause, Arbeit, Schule, Verkehr, Autowerkstatt, Reisen.
- Tema yalnız dekor olarak kullanılmamalı. Hikâyedeki hedef isimler, fiiller ve ifadeler aynı bağlamı gerçekten desteklemeli.
- Örneğin `Küche` hikâyesinde tencere, tava, bıçak, tabak gibi mutfak isimleriyle schneiden, kochen, rühren, braten gibi fiiller birlikte öğretilmeli.
- `Arzt/Krankenhaus` hikâyelerinde Termin, Wartezimmer, Untersuchung, Rezept, Schmerzen gibi isimlerle untersuchen, warten, verschreiben, sich fühlen gibi uygun fiiller kullanılmalı.
- `Bäckerei` hikâyesinde Brot, Brötchen, Kuchen, Teig, Ofen gibi isimlerle backen, bestellen, bezahlen, einpacken gibi uygun fiiller kullanılmalı.
- `Einkaufen/Supermarkt` hikâyesinde Einkaufswagen, Kasse, Angebot, Regal, Tüte gibi kelimelerle suchen, nehmen, wiegen, bezahlen, brauchen gibi fiiller kullanılmalı.
- Aynı temada birden fazla hikâye olabilir; ancak her yeni hikâye önceki hikâyeden farklı kelime ve ifade hedefleri getirmeli.
- Başlığın altında kısa Türkçe özet bulunabilir; başlık Almanca kalmalıdır.
- Metindeki her görünen kelime dokunulabilir olmalı.
- Kelimenin Türkçe anlamını yalnız bu cümledeki anlamına göre ver. Genel sözlük anlamlarını gereksiz yere sıralama.
- Her kelimenin gerçek kelime türünü ayrıca belirle: Fiil, İsim, Sıfat, Zarf, Edat, Bağlaç, Zamir, Artikel/Belirleyici, Parçacık, Özel isim vb.

### 2. Öğrenme değeri yüksek kelime ve ifadeler
- Metindeki öğrenilmesi faydalı fiilleri, isimleri, sıfatları, zarfları, bağlaçları ve özellikle sabit kelime gruplarını belirle.
- Her temada hem **isim grubu** hem **fiil grubu** bulunmalı; hikâye yalnız isim listesi gibi veya yalnız fiil listesi gibi yazılmamalı.
- Kişi isimleri, şehir isimleri, sıradan artikeller, zamirler ve öğrenme değeri düşük dolgu kelimelerini sınav hedefi yapma.
- Sınav için özellikle metnin seviyesine uygun önemli kelime, fiil, sabit ifade ve kelime gruplarını seç.

### 3. Kelime grupları
- Bir yapı birlikte öğrenilmesi gereken bir kalıpsa tek bir Lexeme olarak tanımla.
- Örnekler: `sich auf etwas vorbereiten`, `Angst vor etwas haben`, `sich um jemanden kümmern`, `an etwas teilnehmen`.
- Kullanıcı grubun herhangi bir parçasına dokunduğunda yalnız o gruba ait parçaların tamamı vurgulansın.
- Cümlede modal fiil (`müssen`, `können`, `sollen`, `wollen`, `dürfen`, `mögen`) bulunması, modal fiili otomatik olarak kelime grubuna dahil etmez.
- Modal fiil ancak öğrenilen yapının gerçekten zorunlu bir parçasıysa gruba dahil edilebilir.
- Ayrılabilen fiillerde fiilin iki parçası aynı Lexeme'e bağlanmalıdır.

### 4. Kelime çekim bilgileri
- Kullanıcı bir kelimeye dokunduğunda üst bilgi alanında kelime türü mutlaka görünmeli.
- Fiillerde mastar, Präsens 3. tekil şahıs, Präteritum ve Perfekt biçimleri hazırlanmalı.
- Örnek: `fahren → fährt → fuhr → ist gefahren`; `nehmen → nimmt → nahm → hat genommen`.
- Fiil bir kelime grubunun parçasıysa mümkün olduğunda grubun ana fiilinin çekimleri verilmeli.
- İsimlerde artikel ve çoğul biçimi mutlaka hazırlanmalı.
- Zayıf eril isim gibi Akkusativ Singular'da `-n/-en` alan isimlerde bu durum ayrıca açıkça belirtilmeli.
- Sıfatlarda yalın biçim, Komparativ ve Superlativ hazırlanmalı. Karşılaştırma biçimi doğal olarak kullanılmıyorsa boş bırakılabilir.
- Metinde çekimli görünen kelimenin üst başlığında mümkün olduğunca temel biçim gösterilmeli.

### 5. Edatlar
- Tek başına kullanılan her önemli edat için o cümlede tam olarak ne yaptığını açıkla.
- Açıklamada mümkünse cümledeki anlamı, kurduğu ilişki, yer/yön/zaman/araç veya sabit yapı olup olmadığı ve istediği hâl yer almalı.
- Sabit fiil + edat yapısındaki edatı yer/yön edatı gibi açıklama.

### 6. Bağlaçlar
- Bağlacın sözlük anlamının yanında o cümlede kurduğu mantıksal ilişkiyi açıkla: karşıtlık, neden, sonuç, ekleme vb.
- Gerekiyorsa kelime dizilişi bilgisini ekle (`obwohl/dass` → çekimli fiil sonda gibi).

### 7. Sınav
- Sınav soruları yalnız `quizEligible = true` olarak bilinçli biçimde işaretlenen öğrenme değeri yüksek Lexeme'lerden üretilmeli.
- Tematik hikâyelerde sınav hedefleri, o temanın çekirdek isim ve fiillerinden dengeli biçimde seçilmeli.
- Kişi adı, şehir adı, artikel, basit zamir veya anlamsız/düşük değerli token sınav sorusu olamaz.
- Doğru cevap ve çeldiriciler karıştırılmalı.

### 8. Kaydedilen kelimeler ve adaptif çalışma
- Her Lexeme kimliği ders bazında benzersiz olmalı.
- Kullanıcı yıldızladığında kelime/ifade kalıcı olarak kaydedilebilmeli.
- Kaydın hangi hikâyeye ait olduğu Lexeme kimliği üzerinden güvenilir biçimde bulunabilmeli.
- Hikâyenin `Kelimeler` sekmesi yalnız o hikâyeden yıldızlanan kelimeleri göstermeli.
- Ana `Kelimelerim` sayfası bütün hikâyelerden yıldızlananları birlikte göstermeli.
- Adaptif kelime çalışma havuzuna alınacak otomatik kelimeler yalnız öğrenme değeri yüksek `quizEligible` Lexeme'lerden gelmeli.
- Aynı tema içindeki kelimeler birbirini destekleyebileceği için otomatik 10 kelimelik çalışma setlerinde tematik çeşitlilik korunurken ilgili kelime kümelerinin tamamen parçalanmamasına dikkat edilmeli.

### 9. Kalite kontrol
Dersi eklemeden önce kontrol et:
- Başlık tamamen Almanca mı?
- Hikâye gerçekten belirgin bir günlük yaşam temasına mı ait?
- Tema ile isimler ve fiiller doğal biçimde uyumlu mu?
- Aynı kelime grubunda yanlışlıkla modal fiil var mı?
- Edat açıklamaları gerçekten o cümleye özel mi?
- Sınav hedefleri arasında kişi/şehir adı, artikel veya anlamsız kelime var mı?
- Her önemli kelime/ifadenin Türkçe anlamı bağlama uygun mu?
- Fiillerin mastar, 3. tekil, Präteritum ve Perfekt biçimleri doğru mu?
- İsimlerin artikel, çoğul ve gerekiyorsa Akkusativ bilgisi doğru mu?
- Sıfatların yalın, Komparativ ve Superlativ biçimleri doğru mu?
- Kaydedilen kelimeler doğru hikâyeyle eşleşebiliyor mu?

Bu kontroller tamamlanmadan yeni dersi hazır kabul etme.
