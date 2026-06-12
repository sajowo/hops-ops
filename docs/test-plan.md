# Plan testow

## Zakres testow

- Backend REST aplikacji Hops Ops.
- Komunikacja z baza H2 przez Spring Data JPA.
- Konteksty: klienci, oferty sprzedazy, oferty wypozyczen, sprzet, rezerwacje i transakcje.
- Swagger/OpenAPI jako dokumentacja endpointow.
- Kolekcja Bruno w katalogu `testcase/`.

## Typy testow

- testy jednostkowo-integracyjne Spring Boot uruchamiane przez Gradle,
- testy funkcjonalne REST uruchamiane przez Bruno,
- testy regresji glownego przeplywu danych,
- testy akceptacyjne endpointow przez Swagger/Bruno.

## Srodowisko testowe

- Java: 21.
- Backend: `http://localhost:8080`.
- API: `http://localhost:8080/api`.
- Swagger: `http://localhost:8080/swagger-ui.html`.
- Baza lokalna: H2 file database `jdbc:h2:file:./data/hopsops`.
- Dane testowe: tworzone przez requesty Bruno i testy automatyczne.

## Uruchamianie

```bash
sh ./gradlew test
sh ./gradlew bootRun
```

Po starcie backendu:

```bash
cd testcase
npx @usebruno/cli@latest run -r . --env hops-ops
```

Kolekcja Bruno zawiera 23 requesty i asercje statusow HTTP. Kroki tworzace dane zapisują identyfikatory z odpowiedzi do zmiennych srodowiskowych, dzieki czemu kolejne requesty korzystaja z aktualnych ID.

## Kryteria rozpoczecia testow

- funkcja jest dostepna na branchu `develop`,
- Pull Request zawiera opis zmian,
- wymagania lub kryteria akceptacji sa opisane.
- backend uruchamia sie lokalnie bez bledow startowych.

## Kryteria zakonczenia testow

- wszystkie krytyczne scenariusze przeszly,
- znalezione bledy sa opisane jako Issues,
- zespol zaakceptowal stan wersji.
- `sh ./gradlew test` konczy sie sukcesem,
- pelna kolekcja Bruno konczy sie bez nieudanych requestow i asercji,
- Swagger udostepnia specyfikacje OpenAPI pod `/v3/api-docs`.
