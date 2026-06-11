# Hops Ops

Aplikacja modeluje obsluge wypozyczalni i sprzedazy sprzetu. Aktualny backend obejmuje klientow, oferty sprzedazy, oferty wypozyczen, egzemplarze sprzetu, rezerwacje oraz transakcje sprzedazy i wypozyczen.

## Dziedzina i konteksty

Glowny zakres systemu obejmuje:

- customers - kartoteka klientow,
- sales / sales-offers - oferty sprzedazy sprzetu,
- rental / rental-offers - oferty wypozyczen, egzemplarze sprzetu i rezerwacje,
- transactions - rejestracja sprzedazy, wypozyczen i zakonczenia wypozyczenia.

## Uruchomienie

1. Wczytaj projekt w IDE albo uruchom go z terminala.
2. Zbuduj projekt poleceniem:

```bash
sh gradlew test
```

3. Uruchom aplikacje:

```bash
sh gradlew bootRun
```

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
http://localhost:8080/swagger-ui/index.html
```

## Testcase

Katalog `testcase/` zawiera kolekcje requestow API w formacie OpenCollection/Bruno. Domyslne srodowisko zaklada adres:

```text
http://localhost:8080/api
```
