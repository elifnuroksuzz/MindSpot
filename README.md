# MindSpot

**MindSpot — Akıllı ve Güvenilir Duygu Takip Uygulaması**

![MindSpot Demo 1](images/1.jpg)
![MindSpot Demo 2](images/2.jpg)

> MindSpot, kullanıcıların günlük ruh hallerini kolaylıkla kaydetmelerine, duygusal eğilimlerini uzun vadede analiz etmelerine ve kendi psikolojik iyi oluşlarını daha yakından takip etmelerine imkân tanıyan modern bir Android uygulamasıdır. Minimalist tasarımı, güçlü altyapısı ve güvenli veri yönetimi ile kullanıcıya pratik ve profesyonel bir deneyim sunar.

---

## İçindekiler

* [Öne Çıkan Özellikler](#öne-çıkan-özellikler)
* [Uygulama Ekran Görselleri](#uygulama-ekran-görselleri)
* [Kurulum ve Çalıştırma](#kurulum-ve-çalıştırma)
* [Proje Yapısı](#proje-yapısı)
* [Mimari ve Teknolojiler](#mimari-ve-teknolojiler)
* [Lisans](#lisans)
* [İletişim](#iletişim)

---

## Öne Çıkan Özellikler

* **Anında duygu kaydı:** Kullanıcı tek tıkla ruh halini seçebilir.
* **Geçmiş takibi:** Kayıtlı duygu verileri gün ve haftalara göre listelenebilir.
* **Eğilim analizi:** Zaman içerisindeki duygu dalgalanmaları grafiklerle görselleştirilir.
* **Yerel veri güvenliği:** Tüm veriler cihazda saklanır, bulut zorunlu değildir.
* **Sade arayüz:** Kullanıcı dostu, hızlı erişim odaklı minimalist tasarım.

---

## Uygulama Ekran Görselleri

`MindSpot/images` klasöründe yer alan ekran görüntüleri:

* **Ana ekran** – `images/1.jpg`
* **Hızlı duygu girişi** – `images/2.jpg`
* **Geçmiş kayıtlar** – `images/3.jpg`
* **Duygu eğilim grafiği** – `images/4.jpg`

![MindSpot Demo 3](images/3.jpg)
![MindSpot Demo 4](images/4.jpg)

---

## Kurulum ve Çalıştırma

MindSpot’u kendi cihazınızda veya emülatörünüzde çalıştırmak için:

1. Depoyu klonlayın:

   ```bash
   git clone https://github.com/elifnuroksuzz/MindSpot.git
   cd MindSpot
   ```
2. Android Studio üzerinden projeyi açın.
3. Gradle senkronizasyonunun tamamlanmasını bekleyin.
4. Gerekirse SDK/Gradle sürüm güncellemelerini yapın.
5. Emülatör veya gerçek cihaz bağlayarak uygulamayı çalıştırın.

---

## Proje Yapısı

```
MindSpot/
├─ app/               # Ana Android uygulaması
│  ├─ src/main/java   # Kotlin kaynak kodları
│  ├─ src/main/res    # Layout, ikon, tema dosyaları
│  └─ AndroidManifest.xml
├─ images/            # Tanıtım görselleri
├─ README.md
└─ .gitignore
```

---

## Mimari ve Teknolojiler

* **Dil:** Kotlin
* **Mimari Öneri:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Veritabanı:** Room (SQLite tabanlı)
* **UI:** Material Design bileşenleri, Android Views veya Jetpack Compose (gelecek sürümde)
* **Grafikler:** MPAndroidChart veya eşdeğer kütüphaneler
* **Bağımlılık Yönetimi:** Gradle

Bu mimari sayesinde kod tabanı modüler, ölçeklenebilir ve test edilebilir hale gelir.

---

## Lisans

Projenin lisans bilgileri eklenecektir (`MIT`, `Apache-2.0` vb.). Lütfen kullanılacak lisans türünü belirleyin ve `LICENSE` dosyasını ekleyin.

---

## İletişim

* Geliştirici: **elifnuroksuzz** (GitHub)
* Sorular ve öneriler için: [GitHub Issues](https://github.com/elifnuroksuzz/MindSpot/issues)

---

**MindSpot ile kendi duygusal yolculuğunuzu keşfedin!**
