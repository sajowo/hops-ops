# Wspolny projekt

Repozytorium zespolowe do pracy nad projektem.

## Szybki start

1. Sklonuj repozytorium.
2. Przelacz sie na branch `develop`.
3. Utworz branch do swojego zadania, np. `feature/logowanie`.
4. Po zakonczeniu pracy utworz Pull Request do `develop`.

## Glowne branche

- `main` - stabilna wersja projektu, gotowa do oddania lub prezentacji.
- `develop` - wspolna wersja robocza, do ktorej trafiaja zakonczone zadania.

## Typy branchy

- `feature/nazwa-zadania` - nowe funkcje.
- `bugfix/nazwa-bledu` - poprawki bledow.
- `docs/nazwa-dokumentu` - dokumentacja i analiza.
- `design/nazwa-makiety` - makiety, diagramy i materialy projektowe.
- `test/nazwa-testu` - scenariusze i automatyzacja testow.
- `release/v1.0` - przygotowanie wersji do oddania.

## Zespol i foldery odpowiedzialnosci

Kazda rola ma swoje glowne miejsce pracy w repozytorium. Dzieki temu latwiej sprawdzac zmiany i unikac konfliktow.

### Analitycy

Foldery i pliki:

- `docs/wymagania.md`
- `docs/user-stories.md`
- `docs/kryteria-akceptacji.md`

Odpowiedzialnosc:

- wymagania,
- user stories,
- kryteria akceptacji,
- zakres projektu.

### Projektanci

Foldery:

- `design/makiety/`
- `design/diagramy/`
- `assets/`

Odpowiedzialnosc:

- makiety,
- diagramy,
- materialy graficzne,
- opis wygladu aplikacji.

### Programisci

Foldery:

- `src/`

Odpowiedzialnosc:

- kod aplikacji,
- konfiguracja techniczna,
- integracja funkcji.

Jesli projekt zostanie podzielony na frontend i backend, mozna utworzyc `src/frontend/` oraz `src/backend/`.

### Testerzy

Foldery i pliki:

- `tests/`
- `docs/test-plan.md`
- `docs/test-cases.md`

Odpowiedzialnosc:

- plan testow,
- przypadki testowe,
- testowanie zmian,
- zglaszanie bledow.

## Zasady pracy w folderach

- Zmiany w cudzym obszarze warto uzgodnic w Pull Request albo Issue.
- Dokumentacja analityczna powinna byc aktualizowana przed rozpoczeciem wiekszych zadan programistycznych.
- Testy i przypadki testowe powinny byc dopisywane do funkcji, ktore trafiaja do `develop`.
