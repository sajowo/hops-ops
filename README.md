# Hops Ops

Aplikacja modeluje obsluge wypozyczalni i sprzedazy sprzetu. Aktualny backend obejmuje klientow, oferty sprzedazy, oferty wypozyczen, egzemplarze sprzetu, rezerwacje oraz transakcje sprzedazy i wypozyczen.

## Dziedzina i konteksty

Glowny zakres systemu obejmuje:

- customers - kartoteka klientow,
- sales / sales-offers - oferty sprzedazy sprzetu,
- rental / rental-offers - oferty wypozyczen, egzemplarze sprzetu i rezerwacje,
- transactions - rejestracja sprzedazy, wypozyczen i zakonczenia wypozyczenia.

## Uruchomienie

### Wymagania

- Java 21.
- Dostep do internetu przy pierwszym uruchomieniu Gradle/Bruno, jesli zaleznosci nie sa jeszcze pobrane.
- Opcjonalnie Bruno CLI albo `npx`, jezeli chcesz uruchomic kolekcje REST z katalogu `testcase/`.

### Backend

1. Wczytaj projekt w IDE albo uruchom go z terminala.
2. Zbuduj projekt i uruchom testy automatyczne:

```bash
sh ./gradlew test
```

3. Uruchom aplikacje:

```bash
sh ./gradlew bootRun
```

Po starcie backend dziala lokalnie pod adresem `http://localhost:8080`.

### Konsola H2

W przegladarce otworz:

```text
http://localhost:8080/h2-console
```

Parametry polaczenia:

```text
JDBC URL: jdbc:h2:file:./data/hopsops
User Name: sa
Password:
```

### Swagger

Dokumentacja API jest dostepna pod adresem:

```text
http://localhost:8080/swagger-ui.html
```

## Testcase

Katalog `testcase/` zawiera kolekcje requestow API w formacie OpenCollection/Bruno. Domyslne srodowisko `hops-ops` zaklada adres:

```text
http://localhost:8080/api
```

Kolekcja obejmuje 23 requesty dla klientow, ofert sprzedazy, ofert wypozyczen i transakcji. Requesty maja asercje statusow HTTP, a kroki tworzace dane zapisują zwrocone identyfikatory do zmiennych Bruno (`customer_id`, `sales_offer_id`, `rental_offer_id`, `equipment_id`, `transaction_id`). Dzieki temu przeplyw kolekcji nie wymaga recznego przepisywania ID.

Przy uruchomionym backendzie kolekcje mozna sprawdzic poleceniem:

```bash
cd testcase
npx @usebruno/cli@latest run -r . --env hops-ops
```
