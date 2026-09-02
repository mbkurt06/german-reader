# Almanca Okuyucu

Almanca metinlerde tek tek sözlük anlamı yerine cümledeki gerçek öğrenme yapılarını gösteren kişisel Android uygulaması.

İlk hedef:

1. GitHub `content/index.json` dosyasından ders listesini almak.
2. Dersi indirip çevrimdışı saklamak.
3. `sich versammeln`, `warten auf + Akk.`, `steht ... auf` gibi dağınık parçaları tek öğrenme kartı olarak açmak.
4. Öğrenme birimini kaydetmek.
5. Kayıtlı yapılardan boşluk doldurma testi üretmek.

## Kurulum

`app/build.gradle.kts` içindeki `CONTENT_BASE_URL` değerinde `OWNER` yerine GitHub kullanıcı adını yazın. Projeyi Android Studio ile açın ve Gradle senkronizasyonunu çalıştırın.

## Yol haritası

- v0.1: içerik senkronizasyonu, okuyucu, öğrenme kartı, yerel kayıt
- v0.2: boşluk doldurma ve çoktan seçmeli test
- v0.3: aralıklı tekrar
- v0.4: Android metin okuma ile seslendirme

Ayrıntılı genişletme yaklaşımı: `docs/ARCHITECTURE.md`.
