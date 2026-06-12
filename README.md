# Hops Ops

## Opis projektu

Aplikacja Hops Ops modeluje obsluge lokalnego sklepu sportowego, ktory prowadzi
sprzedaz detaliczna oraz czasowy wynajem sprzetu sportowego. System wspiera
pracownikow w rejestrowaniu klientow, zarzadzaniu oferta sprzetu, prowadzeniu
rezerwacji oraz obsludze transakcji sprzedazy i wypozyczen.

W modelu dziedziny centralnym pojeciem jest transakcja, poniewaz laczy klienta
z asortymentem i okresla, czy operacja dotyczy sprzedazy, czy wypozyczenia.
Oferta sprzedazy opisuje sprzet przeznaczony do zakupu, a oferta wypozyczenia
jest powiazana z konkretnymi egzemplarzami sprzetu. Egzemplarze maja stan oraz
status dostepnosci, dzieki czemu system moze blokowac wypozyczenie sprzetu,
ktory jest juz wypozyczony albo zarezerwowany.

Projekt zostal przygotowany w stylu Domain-Driven Design. Kod jest podzielony
na konteksty ograniczone reprezentowane przez pakiety Java, a przypadki uzycia
sa umieszczone w warstwie aplikacyjnej jako osobne serwisy transakcyjne.

## Dziedzina i konteksty

Glowny zakres systemu obejmuje:

- `customers` - kartoteka klientow, czyli dane identyfikacyjne i kontaktowe
  osob korzystajacych ze sklepu oraz wypozyczalni,
- `salesoffers` - oferty sprzedazy sprzetu, ceny sprzedazy i status aktywnosci
  oferty,
- `rentaloffers` - oferty wypozyczenia, egzemplarze sprzetu, rezerwacje,
  dostepnosc i status sprzetu,
- `transactions` - rejestracja sprzedazy, rejestracja wypozyczenia oraz
  zakonczenie wypozyczenia z naliczeniem ewentualnych oplat dodatkowych,
- `accounts` - model kont uzytkownikow systemu, rol i aktywnosci kont.

Aktualny backend udostepnia operacje REST dla klientow, ofert sprzedazy, ofert
wypozyczen, egzemplarzy sprzetu, rezerwacji oraz transakcji. Elementy logowania
i zakladania kont sa ujete w analizie oraz modelu domenowym, natomiast glowny
zaimplementowany zakres operacyjny dotyczy obslugi sprzedazy i wypozyczen.

## Uruchomienie

### Wymagania

- Java 21.
- Dostep do internetu przy pierwszym uruchomieniu Gradle/Bruno, jezeli
  zaleznosci nie sa jeszcze pobrane.
- Opcjonalnie Bruno CLI albo `npx`, jezeli chcesz uruchomic kolekcje REST z
  katalogu `testcase/`.

### Backend

1. Wczytaj projekt w IDE (IntelliJ: File / Open lub File / New / Project from
   Version Control...).
2. Poczekaj, az projekt zbuduje sie przez Gradle.
3. Testy mozna uruchomic z terminala:

```bash
sh ./gradlew test
```

4. Aplikacje mozna uruchomic z IDE albo poleceniem:

```bash
sh ./gradlew bootRun
```

Po starcie backend dziala lokalnie pod adresem `http://localhost:8080`.

## Konsola H2

W przegladarce otworz adres:

```text
http://localhost:8080/h2-console
```

Parametry polaczenia:

```text
JDBC URL: jdbc:h2:file:./data/hopsops
User Name: sa
Password:
```

## Swagger

Dokumentacja API jest dostepna pod adresem:

```text
http://localhost:8080/swagger-ui/index.html
```

## Testy i requesty REST

Projekt zawiera test ladowania kontekstu Spring Boot oraz testy przypadkow
uzycia transakcji sprzedazy i wypozyczenia. Katalog `testcase/` zawiera
kolekcje requestow API w formacie OpenCollection/Bruno.

Domyslne srodowisko kolekcji REST zaklada adres:

```text
http://localhost:8080/api
```

Kolekcja obejmuje 23 requesty dla klientow, ofert sprzedazy, ofert wypozyczen
i transakcji. Requesty zawieraja asercje statusow HTTP, a kroki tworzace dane
zapisuja zwrocone identyfikatory do zmiennych Bruno (`customer_id`,
`sales_offer_id`, `rental_offer_id`, `equipment_id`, `transaction_id`).

Przy uruchomionym backendzie kolekcje mozna sprawdzic poleceniem:

```bash
cd testcase
npx @usebruno/cli@latest run -r . --env hops-ops
```
