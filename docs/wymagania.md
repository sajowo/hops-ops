# Wymagania

## Cel projektu

Celem projektu jest przygotowanie backendu REST dla wypozyczalni i sprzedazy sprzetu sportowego. System ma obslugiwac klientow, oferty sprzedazy, oferty wypozyczen, egzemplarze sprzetu, rezerwacje oraz transakcje sprzedazy i wypozyczen.

## Zakres

### W zakresie

- backend Java/Spring Boot,
- komunikacja z baza H2 przez JPA,
- REST API dla glownych agregatow domenowych,
- Swagger/OpenAPI dla testerow i integracji,
- kolekcja Bruno do testow manualnych/regresyjnych API.

### Poza zakresem

- frontend aplikacji,
- produkcyjny system logowania i autoryzacji,
- zewnetrzna baza produkcyjna,
- wdrozenie na serwerze.

## Wymagania funkcjonalne

| ID | Wymaganie | Priorytet | Kryterium akceptacji |
| --- | --- | --- | --- |
| WF-001 | System pozwala rejestrowac, modyfikowac i pobierac klientow. | Must have | KA-001 |
| WF-002 | System pozwala rejestrowac, modyfikowac, aktywowac, dezaktywowac i pobierac oferty sprzedazy. | Must have | KA-002 |
| WF-003 | System pozwala rejestrowac, modyfikowac i pobierac oferty wypozyczen. | Must have | KA-003 |
| WF-004 | System pozwala dodawac i pobierac egzemplarze sprzetu przypisane do oferty wypozyczenia. | Must have | KA-004 |
| WF-005 | System pozwala rezerwowac sprzet dla klienta. | Should have | KA-005 |
| WF-006 | System pozwala rejestrowac transakcje sprzedazy. | Must have | KA-006 |
| WF-007 | System pozwala rejestrowac i zakanczac transakcje wypozyczenia. | Must have | KA-007 |
| WF-008 | System pozwala pobierac liste transakcji i szczegoly transakcji. | Should have | KA-008 |
| WF-009 | API udostepnia dokumentacje Swagger/OpenAPI. | Must have | KA-009 |
| WF-010 | Kolekcja Bruno sprawdza glowne przeplywy REST z asercjami statusow HTTP. | Should have | KA-010 |

## Wymagania niefunkcjonalne

| ID | Wymaganie | Priorytet | Kryterium akceptacji |
| --- | --- | --- | --- |
| WNF-001 | Projekt uruchamia sie na Java 21. | Must have | `sh ./gradlew test` i `sh ./gradlew bootRun` dzialaja na JDK 21. |
| WNF-002 | Backend korzysta z lokalnej bazy H2. | Must have | Aplikacja laczy sie z `jdbc:h2:file:./data/hopsops`. |
| WNF-003 | Endpointy zwracaja czytelne statusy HTTP dla walidacji i konfliktow biznesowych. | Should have | Bledy danych wejściowych mapuja sie na 400, konflikty stanu na 409. |
| WNF-004 | Odpowiedzi stronicowane maja stabilny format JSON. | Should have | Spring Data Page jest serializowany przez DTO. |

## Ryzyka i zalozenia

- Testy lokalne wymagaja JDK 21; starsza wersja Javy moze nie uruchomic wrappera Gradle.
- Baza H2 w trybie plikowym utrzymuje dane pomiedzy uruchomieniami, wiec ponowne testy moga korzystac z istniejacych rekordow.
- Kolekcja Bruno zaklada, ze backend dziala pod `http://localhost:8080/api`.
