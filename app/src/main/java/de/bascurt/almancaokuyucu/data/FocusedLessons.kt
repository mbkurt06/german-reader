package de.bascurt.almancaokuyucu.data

import de.bascurt.almancaokuyucu.model.ReaderLesson

internal object FocusedLessons {
    private val F = ExtendedLessonFactory
    private val E = ContextLessonEnhancer

    val all: List<ReaderLesson> = listOf(
        E.apply(
            F.lesson("a2-neuer-anfang", "Ein neuer Anfang", "A2", "Arbeit • İlk iş günü ve otelde çalışma", listOf(
                "Elif beginnt heute ihre neue Arbeit im Hotel.",
                "Am Morgen begrüßt sie die Kollegen und bekommt eine Zimmerliste.",
                "Ihre Chefin zeigt ihr die Rezeption und erklärt die wichtigsten Aufgaben.",
                "Danach beantwortet Elif die ersten Fragen von den Gästen.",
                "Ein Gast fragt nach dem Frühstück und Elif erklärt ihm den Weg.",
                "Später kontrolliert sie die Zimmerliste noch einmal sorgfältig.",
                "Am Mittag macht sie eine kurze Pause mit den Kollegen.",
                "Nach der Pause hilft sie wieder an der Rezeption und sortiert Unterlagen.",
                "Eine Kollegin zeigt ihr noch einige wichtige Sachen am Arbeitsplatz.",
                "Elif hört aufmerksam zu und fragt bei Bedarf noch einmal nach.",
                "Am Abend spricht sie mit ihrer Chefin über den ersten Arbeitstag.",
                "Zum Schluss räumt sie ihren Platz auf und verabschiedet sich von den Kollegen."
            )),
            translations = listOf(
                "Elif bugün oteldeki yeni işine başlıyor.",
                "Sabah çalışma arkadaşlarını selamlıyor ve bir oda listesi alıyor.",
                "Şefi ona resepsiyonu gösteriyor ve en önemli görevleri açıklıyor.",
                "Daha sonra Elif misafirlerin ilk sorularını cevaplıyor.",
                "Bir misafir kahvaltıyı soruyor ve Elif ona yolu tarif ediyor.",
                "Daha sonra oda listesini bir kez daha dikkatlice kontrol ediyor.",
                "Öğlen çalışma arkadaşlarıyla kısa bir mola veriyor.",
                "Moladan sonra tekrar resepsiyonda yardım ediyor ve evrakları düzenliyor.",
                "Bir çalışma arkadaşı ona iş yerindeki birkaç önemli şeyi daha gösteriyor.",
                "Elif dikkatlice dinliyor ve gerektiğinde bir kez daha soruyor.",
                "Akşam şefiyle ilk iş günü hakkında konuşuyor.",
                "Son olarak yerini topluyor ve çalışma arkadaşlarıyla vedalaşıyor.",
                "Daha sonra görevlerini bir kez daha dikkatlice kontrol ediyor.",
                "Bir sonraki adım hakkında başka bir kişiyle kısaca konuşuyor.",
                "Son olarak yerini topluyor ve günü memnun bir şekilde bitiriyor."
            ),
            phrases = commonPhrases + listOf(
                phrase("am Morgen", "sabah", "Bu cümlede ‘am Morgen’ birlikte zaman bildirir; ‘am’ ve ‘Morgen’ ayrı ayrı çevrilmez."),
                phrase("an der Rezeption", "resepsiyonda", "Bu kullanım bulunduğu yeri anlatır: ‘an der Rezeption’ = resepsiyonda."),
                phrase("nach dem Frühstück", "kahvaltıyı / kahvaltı hakkında", "Buradaki ‘nach’ sormak fiiliyle birlikte ‘fragen nach’ yapısının parçasıdır; zaman anlamındaki ‘kahvaltıdan sonra’ değildir."),
                phrase("am Mittag", "öğlen", "‘am Mittag’ birlikte öğle vaktini anlatır."),
                phrase("nach der Pause", "moladan sonra", "Bu cümlede ‘nach’ zamansal kullanılır: moladan sonra."),
                phrase("am Abend", "akşam", "‘am Abend’ birlikte akşam vaktini anlatır."),
                phrase("ersten Arbeitstag", "ilk iş günü", "Bu cümlede ifade Elif'in işe başladığı ilk günü anlatır.")
            )
        ),
        E.apply(
            F.lesson("a2-kueche", "In der Küche", "A2", "Küche • Mutfak malzemeleri ve yemek hazırlama fiilleri", listOf(
                "Mina möchte heute eine Suppe und einen Salat vorbereiten.",
                "Zuerst stellt sie den Topf auf den Herd und nimmt ein Messer.",
                "Sie schneidet die Zwiebel und die Tomaten auf dem Brett in kleine Stücke.",
                "Danach gibt sie alles in den Topf und kocht die Suppe langsam.",
                "Während die Suppe kocht wäscht Mina den Salat und eine Gurke.",
                "Sie rührt die Suppe mit einem Löffel um und probiert sie vorsichtig.",
                "Dann würzt sie die Suppe mit Salz und Pfeffer.",
                "Anschließend bereitet sie den Salat vor und stellt ihn auf den Tisch.",
                "Zum Schluss legt sie Teller und Löffel auf den Tisch und serviert das Essen.",
                "Nach dem Essen räumt sie das Geschirr in die Spülmaschine ein.",
                "Sie wischt die Arbeitsfläche ab und räumt die benutzten Sachen weg.",
                "Jetzt ist die Küche wieder sauber und Mina macht eine kurze Pause."
            )),
            translations = listOf(
                "Mina bugün bir çorba ve salata hazırlamak istiyor.",
                "Önce tencereyi ocağın üzerine koyuyor ve bir bıçak alıyor.",
                "Soğanı ve domatesleri tahta üzerinde küçük parçalara doğruyor.",
                "Daha sonra hepsini tencereye koyuyor ve çorbayı yavaşça pişiriyor.",
                "Çorba pişerken Mina salatayı ve bir salatalığı yıkıyor.",
                "Çorbayı bir kaşıkla karıştırıyor ve dikkatlice tadına bakıyor.",
                "Sonra çorbaya tuz ve karabiberle tat veriyor.",
                "Ardından salatayı hazırlıyor ve masaya koyuyor.",
                "Son olarak tabakları ve kaşıkları masaya koyup yemeği servis ediyor.",
                "Yemekten sonra bulaşıkları bulaşık makinesine yerleştiriyor.",
                "Tezgâhı siliyor ve kullanılan eşyaları kaldırıyor.",
                "Şimdi mutfak yeniden temiz ve Mina kısa bir mola veriyor.",
                "Daha sonra kişi bütün önemli şeyleri bir kez daha kontrol ediyor.",
                "Çalışma yerini topluyor ve kullanılan eşyaları yerine geri koyuyor.",
                "Son olarak sonuç hakkında kısaca konuşuyor ve memnun oluyor."
            ),
            phrases = commonPhrases + listOf(
                phrase("auf den Herd", "ocağın üzerine", "Buradaki ‘auf’ ayrılabilir fiil eki değildir; tencerenin nereye konduğunu anlatan edattır."),
                phrase("auf dem Brett", "tahta üzerinde", "Buradaki ‘auf’ konum bildirir: tahta üzerinde."),
                phrase("in kleine Stücke", "küçük parçalara", "Doğrama sonucunu anlatan birlikte kullanılan ifadedir."),
                phrase("mit einem Löffel", "bir kaşıkla", "Buradaki ‘mit’ araç bildirir: kaşık kullanarak."),
                phrase("zum Schluss", "son olarak / en sonunda", "‘Zum Schluss’ bu cümlede tek bir zaman-sıralama ifadesidir. ‘Zum’ ve ‘Schluss’ ayrı ayrı çevrilmez."),
                phrase("nach dem Essen", "yemekten sonra", "Bu cümlede ‘nach dem Essen’ birlikte zamansal anlam taşır: yemekten sonra."),
                phrase("in die Spülmaschine", "bulaşık makinesinin içine", "Bu ifade hareketin yönünü anlatır; ‘einräumen’ fiilinin hedefidir.")
            )
        ),
        E.apply(
            F.lesson("a2-arzt", "Beim Arzt", "A2", "Arzt • Doktor randevusu, şikâyetler ve muayene", listOf(
                "Leyla hat seit gestern starke Halsschmerzen und fühlt sich müde.",
                "Am Morgen ruft sie beim Arzt an und bekommt einen Termin.",
                "Im Wartezimmer wartet sie ungefähr zwanzig Minuten.",
                "Dann ruft eine Mitarbeiterin Leyla auf und sie geht in das Behandlungszimmer.",
                "Der Arzt untersucht ihren Hals und misst ihre Temperatur.",
                "Er fragt wie lange die Beschwerden schon dauern.",
                "Leyla erzählt dass sie nachts schlecht geschlafen hat.",
                "Der Arzt erklärt dass sie viel trinken und sich ausruhen soll.",
                "Danach schreibt er ein Rezept und erklärt das Medikament.",
                "Leyla fragt noch einmal nach wie oft sie es einnehmen soll.",
                "Zum Schluss bedankt sie sich und geht mit dem Rezept nach Hause.",
                "Zu Hause trinkt sie Tee nimmt das Medikament ein und ruht sich auf dem Sofa aus."
            )),
            translations = listOf(
                "Leyla'nın dünden beri şiddetli boğaz ağrısı var ve kendini yorgun hissediyor.",
                "Sabah doktoru arıyor ve bir randevu alıyor.",
                "Bekleme odasında yaklaşık yirmi dakika bekliyor.",
                "Sonra bir çalışan Leyla'yı çağırıyor ve Leyla muayene odasına gidiyor.",
                "Doktor boğazını muayene ediyor ve ateşini ölçüyor.",
                "Şikâyetlerin ne kadar zamandır devam ettiğini soruyor.",
                "Leyla gece kötü uyuduğunu anlatıyor.",
                "Doktor çok sıvı içmesi ve dinlenmesi gerektiğini açıklıyor.",
                "Daha sonra bir reçete yazıyor ve ilacı açıklıyor.",
                "Leyla ilacı ne sıklıkta kullanması gerektiğini bir kez daha soruyor.",
                "Son olarak teşekkür ediyor ve reçeteyle eve gidiyor.",
                "Evde çay içiyor, ilacı alıyor ve kanepede dinleniyor.",
                "Daha sonra uzman sonraki adımları sakin bir şekilde açıklıyor.",
                "Kişi bir kez daha soruyor ve dikkatlice dinliyor.",
                "Son olarak önündeki günler için bir tavsiye alıyor."
            ),
            phrases = commonPhrases + listOf(
                phrase("seit gestern", "dünden beri", "‘seit gestern’ başlangıç noktası geçmişte olan ve hâlâ devam eden durumu anlatır."),
                phrase("beim Arzt", "doktorda / doktor muayenehanesinde", "‘beim’ burada ‘bei dem’ kısaltmasıdır ve bulunulan yeri anlatır."),
                phrase("wie lange", "ne kadar süredir / ne kadar uzun", "Bu soruda ‘wie lange’ süreyi soran tek bir soru ifadesidir."),
                phrase("noch einmal nach", "bir kez daha sorup netleştirmek", "Burada ‘nachfragen’ fiilinin ayrılan ‘nach’ parçası ile ‘noch einmal’ birlikte, yeniden açıklama istemeyi anlatır."),
                phrase("nach Hause", "eve", "‘nach Hause’ yön bildirir: eve doğru. ‘zu Hause’ ile karıştırılmamalıdır."),
                phrase("zu Hause", "evde", "‘zu Hause’ bulunulan yeri anlatır: evde. ‘nach Hause’ ise eve yönelmeyi anlatır."),
                phrase("auf dem Sofa", "kanepede / kanepe üzerinde", "Buradaki ‘auf’ normal edattır ve konum bildirir.")
            )
        ),
        E.apply(
            F.lesson("a2-baeckerei", "In der Bäckerei", "A2", "Bäckerei • Ekmek, hamur, fırın ve sipariş fiilleri", listOf(
                "Am frühen Morgen beginnt die Arbeit in der Bäckerei.",
                "Der Bäcker bereitet zuerst den Teig für das Brot vor.",
                "Danach formt er Brötchen und legt sie auf ein Blech.",
                "Er schiebt das Blech in den Ofen und kontrolliert die Zeit.",
                "Eine Kollegin schneidet Kuchen und füllt die Theke auf.",
                "Kurz danach kommen die ersten Kunden in die Bäckerei.",
                "Eine Kundin bestellt zwei Brötchen und ein Stück Kuchen.",
                "Sie fragt außerdem nach einem Brot ohne Nüsse.",
                "Der Bäcker zeigt ihr zwei verschiedene Brote und erklärt den Unterschied.",
                "Die Kundin entscheidet sich für ein Brot und der Bäcker packt alles ein.",
                "An der Kasse bezahlt sie mit ihrer Karte.",
                "Zum Abschied wünscht der Bäcker ihr einen schönen Tag."
            )),
            translations = listOf(
                "Sabah erken saatlerde fırında çalışma başlıyor.",
                "Fırıncı önce ekmek için hamuru hazırlıyor.",
                "Daha sonra küçük ekmekleri şekillendiriyor ve bir fırın tepsisine koyuyor.",
                "Tepsiyi fırına sürüyor ve süreyi kontrol ediyor.",
                "Bir çalışma arkadaşı pastayı kesiyor ve tezgâhı dolduruyor.",
                "Kısa süre sonra ilk müşteriler fırına geliyor.",
                "Bir müşteri iki küçük ekmek ve bir dilim pasta sipariş ediyor.",
                "Ayrıca kuruyemişsiz bir ekmek soruyor.",
                "Fırıncı ona iki farklı ekmek gösteriyor ve aralarındaki farkı açıklıyor.",
                "Müşteri bir ekmeği seçiyor ve fırıncı her şeyi paketliyor.",
                "Kasada kartıyla ödeme yapıyor.",
                "Vedalaşırken fırıncı ona güzel bir gün diliyor.",
                "Daha sonra kişi bütün önemli şeyleri bir kez daha kontrol ediyor.",
                "Çalışma yerini topluyor ve kullanılan eşyaları yerine geri koyuyor.",
                "Son olarak sonuç hakkında kısaca konuşuyor ve memnun oluyor."
            ),
            phrases = commonPhrases + listOf(
                phrase("am frühen Morgen", "sabah erken saatlerde", "Bu ifade birlikte günün erken sabah bölümünü anlatır."),
                phrase("für das Brot", "ekmek için", "‘für’ burada amaç bildirir: ekmek yapmak için kullanılacak hamur."),
                phrase("in den Ofen", "fırının içine", "Bu ifade hareketin yönünü, tepsinin nereye sürüldüğünü anlatır."),
                phrase("kurz danach", "kısa süre sonra", "İki olay arasındaki kısa zaman aralığını anlatan birlikte kullanılan ifadedir."),
                phrase("ein Stück Kuchen", "bir dilim / parça pasta", "Siparişte miktar bildiren kalıp olarak birlikte anlaşılır."),
                phrase("fragt außerdem nach", "ayrıca ... soruyor", "Burada ‘fragen nach’ bir şeyi sormak/istemek anlamındaki fiil-edat yapısıdır."),
                phrase("entscheidet sich für", "... seçiyor / ... karar veriyor", "‘sich entscheiden für’ birlikte öğrenilen dönüşlü fiil + edat yapısıdır."),
                phrase("an der Kasse", "kasada", "Bu cümlede ödeme yapılan yeri anlatır."),
                phrase("zum Abschied", "vedalaşırken / vedalaşma sırasında", "Bu ifade birlikte vedalaşma anını anlatır.")
            )
        ),
        E.apply(
            F.lesson("a2-supermarkt", "Im Supermarkt", "A2", "Einkaufen • Süpermarkette ürün bulma ve ödeme", listOf(
                "Emre geht nach der Arbeit in den Supermarkt.",
                "Am Eingang nimmt er einen Einkaufswagen und schaut auf seine Liste.",
                "Zuerst sucht er Milch Reis und frisches Gemüse.",
                "Die Milch findet er im Kühlregal aber der Reis ist im nächsten Gang.",
                "Emre sieht ein Angebot und vergleicht die Preise von zwei Säften.",
                "Danach legt er Äpfel Tomaten Brot und Eier in den Wagen.",
                "Er braucht auch Käse und fragt eine Mitarbeiterin nach dem richtigen Regal.",
                "Die Mitarbeiterin zeigt ihm den Weg und Emre bedankt sich.",
                "An der Kasse legt er alle Produkte auf das Band.",
                "Er bezahlt mit der Karte und packt die Einkäufe in seine Taschen.",
                "Bevor er geht kontrolliert er noch einmal den Kassenbon.",
                "Zum Schluss bringt er den Einkaufswagen zurück und fährt nach Hause."
            )),
            translations = listOf(
                "Emre işten sonra süpermarkete gidiyor.",
                "Girişte bir alışveriş arabası alıyor ve listesine bakıyor.",
                "Önce süt, pirinç ve taze sebze arıyor.",
                "Sütü soğutucu reyonda buluyor ama pirinç bir sonraki koridorda.",
                "Emre bir kampanya görüyor ve iki meyve suyunun fiyatlarını karşılaştırıyor.",
                "Daha sonra elma, domates, ekmek ve yumurtaları arabaya koyuyor.",
                "Peynire de ihtiyacı var ve bir çalışana doğru reyonu soruyor.",
                "Çalışan ona yolu gösteriyor ve Emre teşekkür ediyor.",
                "Kasada bütün ürünleri banda koyuyor.",
                "Kartla ödüyor ve alışverişlerini çantalarına yerleştiriyor.",
                "Gitmeden önce fişi bir kez daha kontrol ediyor.",
                "Son olarak alışveriş arabasını yerine götürüyor ve eve gidiyor.",
                "Daha sonra kişi iki kampanyayı daha birbiriyle karşılaştırıyor.",
                "Fiyatı kontrol ediyor ve uygun şeyleri çantaya koyuyor.",
                "Son olarak ödeme yapıyor ve fişi yanına alıyor."
            ),
            phrases = commonPhrases + listOf(
                phrase("nach der Arbeit", "işten sonra", "Buradaki ‘nach’ zamansal anlam taşır: iş bittikten sonra."),
                phrase("am Eingang", "girişte", "Bu ifade süpermarketin giriş bölümündeki konumu anlatır."),
                phrase("im Kühlregal", "soğutucu reyonda", "‘im’ = ‘in dem’; ürünün bulunduğu yeri anlatır."),
                phrase("im nächsten Gang", "bir sonraki koridorda", "Bu ifade ürünün bulunduğu koridoru anlatır."),
                phrase("fragt eine Mitarbeiterin nach", "bir çalışana ... soruyor", "‘jemanden nach etwas fragen’ yapısında kişi ve sorulan şey birlikte düşünülür."),
                phrase("an der Kasse", "kasada", "Bu cümlede ödeme yapılan yeri anlatır."),
                phrase("auf das Band", "bandın üzerine", "Buradaki ‘auf’ normal edattır; ürünlerin konduğu yeri/yönü anlatır."),
                phrase("mit der Karte", "kartla", "Ödeme aracını anlatan birlikte kullanılan ifadedir."),
                phrase("nach Hause", "eve", "Yön bildirir: eve doğru.")
            )
        )
    )

    private val commonPhrases = listOf(
        phrase("zum Schluss", "son olarak / en sonunda", "Bu cümlede ‘zum Schluss’ tek bir zaman-sıralama ifadesidir; kelimeler ayrı ayrı çevrilmez."),
        phrase("noch einmal", "bir kez daha / tekrar", "Bu iki kelime birlikte eylemin tekrarlandığını anlatır."),
        phrase("bei Bedarf", "gerektiğinde / ihtiyaç hâlinde", "‘bei Bedarf’ birlikte kullanılan kalıp bir ifadedir."),
        phrase("in Ruhe", "sakin bir şekilde / rahatça", "Bu kullanım eylemin acele etmeden, sakin biçimde yapılmasını anlatır.")
    )

    private fun phrase(text: String, meaning: String, explanation: String) = ContextPhrase(
        words = text.lowercase().split(" "),
        meaning = meaning,
        explanation = explanation
    )
}
