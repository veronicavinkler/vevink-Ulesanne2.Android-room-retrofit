# Android Trivia Quiz App
Androidi rakendus, mis võimaldab kasutajatel panna proovile oma teadmised erinevates valdkondades.
Äpp kasutab küsimuste hankimiseks OpenTDB (Open Trivia Database) API-t ning pakub täielikku offline-tuge tänu andmete puhverdamisele.

## Funktsioonid
1. Kategooriate valik:
- üldteadmised,
- ajalugu,
- arvutid,
- sport
2. Küsimused salvestatakse kohalikku Room andmebaasi. Kui internet puudub või API piirangud tulevad vastu, saab ikkagi mängida varem laaditud küsimustega.
3. Jälgi oma varasemaid sooritusi – salvestatakse kuupäev, kategooria ja skoor.
4. Näe oma parimaid tulemusi iga kategooria lõikes eraldi vaates.

## Tech Stack
- Keel: Kotlin
- UI raamistik: Jetpack Compose
- Võrgukiht: Retrofit & Gson API päringute lahendamiseks.
- Andmebaas: Room (SQLite abstraktsioonikiht andmete püsivaks salvestamiseks).
- Arhitektuur: MVVM (ViewModel, Repository pattern).
- Asünkroonsus: Kotlin Coroutines & Flow andmevoogude haldamiseks.
- Navigatsioon: Jetpack Navigation Compose.

  ## Paigaldamine
  
