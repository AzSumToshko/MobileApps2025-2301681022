# AutoMarket

Мобилно приложение за публикуване и търсене на автомобилни обяви, разработено с Jetpack Compose и MVVM архитектура.

---

## Идея

AutoMarket е Android приложение, което позволява на потребителите да преглеждат, публикуват и управляват обяви за автомобили. Потребителите могат да търсят по марка, цена и вид гориво, да запазват любими и да споделят обяви с приятели.

---

## Как работи

Приложението стартира с кратък Splash екран и зарежда началния екран с всички обяви от локалната база данни. Потребителят може да търси и филтрира коли по различни критерии, да отвори детайлна страница с характеристики и да сподели обявата. За публикуване на нова обява се попълва 3-стъпков формуляр — марка и модел, технически детайли и снимки. Любимите обяви се запазват и са достъпни от отделен раздел. Всички данни се съхраняват локално чрез Room и оцеляват след рестарт на приложението.

---

## Архитектура

**MVVM + Repository Pattern**

```
UI Layer  (Jetpack Compose Screens)
ViewModel  (Hilt ViewModel)
Repository  (CarRepository)
Room Database  (CarDao → SQLite)
```

| Слой | Технология |
|---|---|
| UI |  Material3 |
| Navigation | Navigation Compose |
| ViewModel | AndroidX ViewModel and Coroutines |
| Repository | CarRepository - Singleton repo |
| Database | Room 2.6.1, SQLite |
| DI | Hilt 2.51.1 |
| Extra Feature | Share Intent |

---

## Потребителски поток

```
SplashScreen (2с)
    └── HomeScreen (начало, списък обяви)
            ├── CarDetailScreen (детайли, споделяне, изтриване, любими)
            ├── SearchScreen (филтри: марка, цена, гориво)
            ├── PostAdScreen (публикувай обява — 3 стъпки)
            ├── FavoritesScreen (запазени обяви)
            └── LoginScreen (вход / регистрация)
```

---

## Стъпки за стартиране

1. Клонирай репото:
   ```bash
   git clone https://github.com/SamuilDobrinski/MobileApps2025-2301681022
   ```
2. Отвори проекта в **Android Studio **
3. Изчакай **Gradle sync** да завърши
4. Стартирай на емулатор или устройство с **API 24+**
---

## Тестови акаунти

| Поле | Стойност |
|---|---|
| Email | test@automarket.bg |
| Парола | Test1234 |
---

## APK

Свали и инсталирай директно:

[apk/app-release.apk](apk/app-release.apk)

> Размер ≤ 60 MB · Min SDK 24 · Release build с R8
