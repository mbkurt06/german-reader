# Almanca Okuyucu — Yeni Metin Hazırlama Ana Promptu

Bu dosya, uygulamaya eklenecek her yeni Almanca metin için kullanılacak yaşayan kurallar bütünüdür. Kullanıcıdan gelen geri bildirimlere göre güncellenecektir.

## Prompt

Aşağıdaki Almanca metni Almanca Okuyucu uygulamasına ders olarak hazırla.

### 1. Metin ve seviye
- Metnin CEFR seviyesini (A2/B1/B2 vb.) koru.
- Başlık ve kısa özet oluştur.
- Metindeki her görünen kelime dokunulabilir olmalı.
- Kelimenin Türkçe anlamını yalnız bu cümledeki anlamına göre ver. Genel sözlük anlamlarını gereksiz yere sıralama.

### 2. Öğrenme değeri yüksek kelime ve ifadeler
- Metindeki öğrenilmesi faydalı fiilleri, isimleri, sıfatları, zarfları, bağlaçları ve özellikle sabit kelime gruplarını belirle.
- Kişi isimleri, şehir isimleri, sıradan artikeller, zamirler ve öğrenme değeri düşük dolgu kelimelerini sınav hedefi yapma.
- Sınav için özellikle metnin seviyesine uygun önemli kelime, fiil, sabit ifade ve kelime gruplarını seç.

### 3. Kelime grupları
- Bir yapı birlikte öğrenilmesi gereken bir kalıpsa tek bir Lexeme olarak tanımla.
- Örnekler: `sich auf etwas vorbereiten`, `Angst vor etwas haben`, `sich um jemanden kümmern`, `an etwas teilnehmen`.
- Kullanıcı grubun herhangi bir parçasına dokunduğunda yalnız o gruba ait parçaların tamamı vurgulansın.
- Cümlede modal fiil (`müssen`, `können`, `sollen`, `wollen`, `dürfen`, `mögen`) bulunması, modal fiili otomatik olarak kelime grubuna dahil etmez.
- Modal fiil ancak öğrenilen yapının gerçekten zorunlu bir parçasıysa gruba dahil edilebilir.
- Örnek: `muss sich an seine neue Umgebung gewöhnen` cümlesinde `muss` seçilmemeli; grup `sich ... an ... gewöhnen` yapısının gerçek parçalarıyla sınırlı kalmalıdır.
- Ayrılabilen fiillerde fiilin iki parçası aynı Lexeme'e bağlanmalıdır.

### 4. Edatlar
- Tek başına kullanılan her önemli edat için genel/geçiştirici açıklama yazma; o cümlede tam olarak ne yaptığını açıkla.
- Açıklamada mümkünse şunlar bulunmalı:
  1. Cümledeki anlamı,
  2. Neyi neye bağladığı veya hangi ilişkiyi kurduğu,
  3. Yer, yön, zaman, araç/birliktelik veya sabit fiil/isim/sıfat yapısının parçası olup olmadığı,
  4. İstediği hâl (Akkusativ/Dativ) ve bunun nedeni.
- Örnek: `nach Stuttgart` için yalnız `-e/-a` yazma; `nach`ın artikelsiz şehir adına doğru hareket/hedef bildirdiğini açıkla.
- Sabit fiil + edat yapısındaki edatı yer/yön edatı gibi açıklama.

### 5. Bağlaçlar
- Bağlacın sözlük anlamının yanında o cümlede kurduğu mantıksal ilişkiyi açıkla: karşıtlık, neden, sonuç, ekleme vb.
- Gerekiyorsa kelime dizilişi bilgisini ekle (`obwohl/dass` → çekimli fiil sonda gibi).

### 6. Sınav
- Sınav soruları yalnız `quizEligible = true` olarak bilinçli biçimde işaretlenen öğrenme değeri yüksek Lexeme'lerden üretilmeli.
- Kişi adı, şehir adı, artikel, basit zamir veya anlamsız/düşük değerli token sınav sorusu olamaz.
- Sorular hikâyedeki önemli kelime ve kelime gruplarını öğretmeyi ölçmeli.
- Doğru cevap sürekli aynı şıkta bulunmamalı. Her soru için doğru cevap ve çeldiriciler karıştırılmalı.
- Çeldiriciler mümkün olduğunca gerçek ve öğrenme açısından anlamlı ifadelerden seçilmeli.

### 7. Kaydedilen kelimeler
- Her Lexeme kimliği ders bazında benzersiz olmalı.
- Kullanıcı yıldızladığında kelime/ifade kalıcı olarak kaydedilebilmeli.
- Kaydın hangi hikâyeye ait olduğu Lexeme kimliği üzerinden güvenilir biçimde bulunabilmeli.
- Hikâyenin `Kelimeler` sekmesi yalnız o hikâyeden yıldızlanan kelimeleri göstermeli.
- Ana `Kelimelerim` sayfası bütün hikâyelerden yıldızlananları birlikte göstermeli.

### 8. Kalite kontrol
Dersi eklemeden önce kontrol et:
- Aynı kelime grubunda yanlışlıkla modal fiil var mı?
- Grup dışında kalması gereken kelimeler aynı Lexeme kimliğini taşıyor mu?
- Edat açıklamaları gerçekten o cümleye özel mi?
- Sınav hedefleri arasında kişi/şehir adı, artikel veya anlamsız kelime var mı?
- Her önemli kelime/ifadenin Türkçe anlamı bağlama uygun mu?
- Sınavda doğru cevapların şık konumları karışıyor mu?
- Kaydedilen kelimeler doğru hikâyeyle eşleşebiliyor mu?

Bu kontroller tamamlanmadan yeni dersi hazır kabul etme.
