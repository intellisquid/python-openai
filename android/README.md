# Dashboard To-Do (Android prototype)

A single-activity Android app (Kotlin + Jetpack Compose) that works as a to-do dashboard with
permanent archival and backup built in from the start.

## Concepts

- **Active / Completed** — the day-to-day dashboard. Checking an item off marks it completed
  without removing it; it stays visible (struck through) until archived.
- **Archive** — archiving is the only way an item leaves the dashboard, and it's non-destructive.
  Archived items are listed on the Archive tab, where they can be restored or, as an explicit and
  separately confirmed action, deleted forever. Nothing is ever silently deleted.
- **Backup** — the Backup tab exports every item (active, completed, and archived) to a single
  JSON file at a location the user picks via the Storage Access Framework (device storage, an SD
  card, a synced Drive folder, etc.), and can restore from one. A restore always inserts items as
  new rows, so it can never overwrite or clobber what's already on the device. Independently, the
  app also opts in to Android's own automatic backup (`android:allowBackup`), so its database
  travels with the user's OS-level device backup too.

## Project layout

```
android/
  app/src/main/java/com/intellisquid/dashboard/todo/
    data/      Room entity, DAO, database, repository (archive/restore/purge logic)
    backup/    JSON backup format + import/export via a content:// Uri
    ui/        ViewModel + Compose screens (Dashboard, Archive, Backup)
    navigation/ Bottom-nav + NavHost wiring
  app/src/test/... unit tests for the repository and backup JSON mapping
```

## Building

This was written in an environment without the Android SDK or network access to
`dl.google.com` (where the Android Gradle Plugin and AndroidX artifacts are hosted), so it
hasn't been built end-to-end here. The pure-Kotlin business logic (`data/`, `backup/`,
`ui/DashboardViewModel.kt`) was compiled and unit-tested standalone against stubbed
Android/Room APIs to catch type errors; all tests pass. To build for real:

```
cd android
./gradlew assembleDebug
./gradlew test        # unit tests
```

Requires Android Studio (or the Android SDK + `ANDROID_HOME` set) with API 34 installed;
`minSdk` is 26.
