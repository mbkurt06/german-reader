# Genişletilebilir ama sade mimari

Uygulamanın ilk sürümü iki veri alanını ayırır:

- GitHub'daki `content/`: ChatGPT'nin hazırladığı ortak ders ve öğrenme birimleri.
- Telefon içi Room: yıldızlananlar, doğru/yanlışlar ve tekrar tarihleri.

## Değişmeden kalacak çekirdek

`Lesson`, metni ve `LearningUnit` listesini taşır. Bir öğrenme birimi birden fazla ve birbirinden uzak metin aralığına bağlanabilir. Böylece `steht ... auf`, `versammeln sich` ve `hängt von ... ab` aynı modelle gösterilir.

## Sonradan eklenebilecek özellikler

Yeni çalışma biçimleri `ActivityEngine` sözleşmesiyle eklenir. İçerikte yalnızca yeni bir `type` değeri ve ayarları bulunur. Örnekler:

- `gap_fill`: boşluk doldurma
- `multiple_choice`: çoktan seçmeli test
- `spaced_repetition`: aralıklı tekrar
- `listen_and_repeat`: sesli dinleme/tekrar
- `sentence_builder`: kelimelerden cümle kurma

Yeni tür eklemek eski JSON dosyalarını değiştirmeyi gerektirmez. Uygulama tanımadığı etkinlikleri atlar. `schemaVersion`, ileride veri yapısı gerçekten değişirse kontrollü dönüşüm yapmak içindir.

## İlk sürüm sınırı

Hesap, sunucu, Firebase ve uygulama içi yapay zekâ yoktur. GitHub içerik kaynağıdır; telefon kişisel ilerlemeyi yerel tutar.
