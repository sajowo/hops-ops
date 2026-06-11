# Kryteria akceptacji

## Format

Kazde kryterium powinno byc konkretne, mierzalne i mozliwe do sprawdzenia.

## Przyklad

```txt
Dane:
- uzytkownik znajduje sie na ekranie logowania

Kiedy:
- wpisze poprawny login i haslo
- kliknie przycisk logowania

Wtedy:
- system przenosi go do panelu uzytkownika
```

## Lista

| ID | Powiazanie | Kryterium | Status |
| --- | --- | --- | --- |
| KA-001 | WF-001 / US-001 / US-002 | Requesty Bruno dla klientow zwracaja oczekiwane statusy HTTP, a rejestracja klienta zapisuje `customer_id`. | Done |
| KA-002 | WF-002 / US-003 / US-004 | Oferta sprzedazy moze zostac utworzona, zmodyfikowana, dezaktywowana, ponownie aktywowana i pobrana przez API. | Done |
| KA-003 | WF-003 / US-005 | Oferta wypozyczenia moze zostac utworzona, zmodyfikowana i pobrana przez API. | Done |
| KA-004 | WF-004 / US-006 | Do oferty wypozyczenia mozna dodac egzemplarz sprzetu, a jego ID jest zapisywane jako `equipment_id`. | Done |
| KA-005 | WF-005 / US-007 | Sprzet mozna zarezerwowac dla klienta przy uzyciu aktualnych ID utworzonych w kolekcji Bruno. | Done |
| KA-006 | WF-006 / US-008 | Transakcja sprzedazy moze zostac zarejestrowana na aktywnej ofercie sprzedazy. | Done |
| KA-007 | WF-007 / US-009 / US-010 | Transakcja wypozyczenia moze zostac zarejestrowana i zakonczona, a `transaction_id` jest pobierane z odpowiedzi API. | Done |
| KA-008 | WF-008 / US-011 | API zwraca szczegoly transakcji oraz liste transakcji. | Done |
| KA-009 | WF-009 | `/v3/api-docs` zwraca specyfikacje OpenAPI, a `/swagger-ui.html` prowadzi do Swagger UI. | Done |
| KA-010 | WF-010 / US-012 | Pelna kolekcja Bruno przechodzi bez nieudanych requestow i asercji. | Done |
