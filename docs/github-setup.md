# Konfiguracja GitHuba

## Branche

W repozytorium powinny byc dwa stale branche:

- `main` - stabilna wersja projektu.
- `develop` - wspolna wersja robocza.

Nowe branche tworzymy z `develop`.

## Branch protection

### `main`

Rekomendowane ustawienia:

- Require a pull request before merging.
- Require approvals: `1`.
- Dismiss stale pull request approvals when new commits are pushed.
- Require conversation resolution before merging.
- Do not allow force pushes.
- Do not allow deletions.

### `develop`

Rekomendowane ustawienia:

- Require a pull request before merging.
- Require approvals: `1`.
- Require conversation resolution before merging.
- Do not allow force pushes.

## Labele

Proponowane labele:

| Label | Zastosowanie |
| --- | --- |
| `analysis` | wymagania, user stories, kryteria akceptacji |
| `design` | makiety, diagramy, UX/UI |
| `feature` | nowe funkcje |
| `bug` | bledy |
| `testing` | testy i scenariusze testowe |
| `documentation` | dokumentacja |
| `review` | zadania do sprawdzenia |
| `blocked` | zadania zablokowane |

## GitHub Project

Najprostszy widok tablicy:

```txt
Backlog -> To do -> In progress -> Review -> Testing -> Done
```

## Pierwsze wypchniecie repo

Po utworzeniu pustego repozytorium na GitHubie:

```bash
git remote add origin https://github.com/OWNER/REPO.git
git push -u origin main
git push -u origin develop
```

`OWNER` i `REPO` trzeba zastapic nazwa konta lub organizacji oraz nazwa repozytorium.

