# Przypadki testowe

## Testy automatyczne Gradle

| ID | Obszar | Warunki wstepne | Kroki | Oczekiwany rezultat | Status |
| --- | --- | --- | --- | --- | --- |
| TC-A01 | Start aplikacji | Projekt z Java 21 | Uruchomic `sh ./gradlew test` | Kontekst Spring Boot startuje poprawnie | Passed lokalnie |
| TC-A02 | Transakcja sprzedazy | Dane tworzone w tescie | Zarejestrowac klienta, oferte i transakcje sprzedazy | Transakcja ma status `COMPLETED` i poprawna sume | Passed lokalnie |
| TC-A03 | Transakcja wypozyczenia | Dane tworzone w tescie | Zarejestrowac wypozyczenie i zakonczyc je z oplata dodatkowa | Status zmienia sie z `IN_PROGRESS` na `COMPLETED`, a sprzet wraca do dostepnosci | Passed lokalnie |

## Testy REST Bruno

| ID | Obszar | Request Bruno | Oczekiwany rezultat | Status |
| --- | --- | --- | --- | --- |
| TC-B01 | Klienci | `customers/Rejestracja klienta.yml` | HTTP 201 i zapis `customer_id` | Passed lokalnie |
| TC-B02 | Klienci | `customers/Modyfikacja klienta.yml` | HTTP 200 | Passed lokalnie |
| TC-B03 | Klienci | `customers/Pobranie klienta.yml` | HTTP 200 | Passed lokalnie |
| TC-B04 | Klienci | `customers/Pobranie listy klientow.yml` | HTTP 200 | Passed lokalnie |
| TC-B05 | Oferty sprzedazy | `sales/sales-offers/Rejestracja oferty sprzedazy.yml` | HTTP 201 i zapis `sales_offer_id` | Passed lokalnie |
| TC-B06 | Oferty sprzedazy | `sales/sales-offers/Modyfikacja oferty sprzedazy.yml` | HTTP 200 | Passed lokalnie |
| TC-B07 | Oferty sprzedazy | `sales/sales-offers/Dezaktywacja oferty sprzedazy.yml` | HTTP 200 | Passed lokalnie |
| TC-B08 | Oferty sprzedazy | `sales/sales-offers/Aktywacja oferty sprzedazy.yml` | HTTP 200 | Passed lokalnie |
| TC-B09 | Oferty sprzedazy | `sales/sales-offers/Pobranie oferty sprzedazy.yml` | HTTP 200 | Passed lokalnie |
| TC-B10 | Oferty sprzedazy | `sales/sales-offers/Pobranie listy ofert sprzedazy.yml` | HTTP 200 | Passed lokalnie |
| TC-B11 | Oferty wypozyczen | `rental/rental-offers/Rejestracja oferty wypozyczenia.yml` | HTTP 201 i zapis `rental_offer_id` | Passed lokalnie |
| TC-B12 | Oferty wypozyczen | `rental/rental-offers/Modyfikacja oferty wypozyczenia.yml` | HTTP 200 | Passed lokalnie |
| TC-B13 | Oferty wypozyczen | `rental/rental-offers/Pobranie oferty wypozyczenia.yml` | HTTP 200 | Passed lokalnie |
| TC-B14 | Oferty wypozyczen | `rental/rental-offers/Pobranie listy ofert wypozyczenia.yml` | HTTP 200 | Passed lokalnie |
| TC-B15 | Sprzet | `rental/rental-offers/Dodanie sprzetu.yml` | HTTP 201 i zapis `equipment_id` | Passed lokalnie |
| TC-B16 | Sprzet | `rental/rental-offers/Pobranie sprzetu oferty.yml` | HTTP 200 | Passed lokalnie |
| TC-B17 | Rezerwacje | `rental/rental-offers/Rezerwacja sprzetu.yml` | HTTP 200 | Passed lokalnie |
| TC-B18 | Rezerwacje | `rental/rental-offers/Pobranie rezerwacji.yml` | HTTP 200 | Passed lokalnie |
| TC-B19 | Transakcje | `transactions/Rejestracja sprzedazy.yml` | HTTP 200 | Passed lokalnie |
| TC-B20 | Transakcje | `transactions/Rejestracja wypozyczenia.yml` | HTTP 200 i zapis `transaction_id` | Passed lokalnie |
| TC-B21 | Transakcje | `transactions/Zakonczenie wypozyczenia.yml` | HTTP 200 | Passed lokalnie |
| TC-B22 | Transakcje | `transactions/Pobranie transakcji.yml` | HTTP 200 | Passed lokalnie |
| TC-B23 | Transakcje | `transactions/Pobranie listy transakcji.yml` | HTTP 200 | Passed lokalnie |

## Ostatnia lokalna weryfikacja

- `sh ./gradlew test` - sukces.
- Backend uruchomiony przez `sh ./gradlew bootRun`.
- Bruno: 23/23 requesty passed, 23/23 asercje passed.
- Swagger: `/v3/api-docs` zwraca HTTP 200, `/swagger-ui.html` przekierowuje do UI.
