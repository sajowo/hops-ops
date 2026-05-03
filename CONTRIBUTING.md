# Zasady wspolpracy

## Workflow

1. Zadanie tworzymy jako GitHub Issue.
2. Issue dostaje osobe odpowiedzialna i odpowiedni label.
3. Do zadania tworzymy osobny branch z `develop`.
4. Po zakonczeniu pracy tworzymy Pull Request do `develop`.
5. Pull Request powinien miec przynajmniej jedna osobe do review.
6. Po akceptacji i sprawdzeniu zmiana trafia do `develop`.
7. Stabilna wersja trafia z `develop` do `main` przez branch `release/*`.

## Nazewnictwo branchy

Przyklady:

```txt
feature/rejestracja
feature/panel-uzytkownika
bugfix/walidacja-formularza
docs/wymagania
design/makieta-logowania
test/scenariusze-logowania
release/v1.0
```

## Nazewnictwo commitow

Uzywamy prostych prefiksow:

```txt
feat: add login form
fix: handle empty password
docs: add requirements
design: add dashboard mockup
test: add login test cases
refactor: simplify auth service
```

## Pull Request

Kazdy Pull Request powinien zawierac:

- krotki opis zmiany,
- link do powiazanego Issue,
- informacje jak sprawdzic zmiane,
- screenshot, jesli zmiana dotyczy widoku.

## Ochrona branchy na GitHubie

Dla `main` warto ustawic:

- brak bezposredniego pushowania,
- wymagany Pull Request,
- minimum 1 approval,
- wymagane testy, jesli projekt ma CI.

Dla `develop` warto ustawic:

- Pull Request zamiast direct push,
- minimum 1 review,
- rozwiazywanie konfliktow przed mergem.

