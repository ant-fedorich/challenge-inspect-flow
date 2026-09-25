# InspectFlow

Native Android app for the coding challenge: fetch inspection JSON, show hierarchical Pages/Sections/Questions, offline cache, choice and image.

## Setup

- Android Studio with Android SDK 37
- JDK 25 (the Gradle daemon toolchain)
- A device or emulator on API 30 or higher
- Network access for the first load. Later launches show the last successful response from Room.

1. Clone the repository and open the project in Android Studio.
2. Sync Gradle. The wrapper uses Gradle 9.5.0.
3. Run the `app` configuration.

Command line:

```bash
./gradlew :app:assembleDebug
./gradlew testDebugUnitTest
```

On launch the app fetches the inspection JSON. Pull to refresh loads it again. If that request fails, the cached tree stays on screen.

## Functional requirements

1. User can see the visually structured content that comes from a backend: hierarchical Pages/Sections/Questions. Questions use one of three types: text, image, choice
2. User can answer choice questions. The app allows one or many selections depending on the question settings..
3. User can see a reduced-size preview of the image when an image question is presented.
4. User can open a full-screen view of an image question that shows the full-sized image and its title.

## Non-functional requirements

1. System must support nested sections with endless depth (sections containing sections, without a fixed level limit) for: parsed from API, saved in DB, rendered in Compose.
2. System must work in offline mode, i.e. save previously fetched data, using relational DB.
3. (optional) System must handle network failures with providing a fallback mechanism for poor connections

## Data flow

```
1. API
   ↓
  DTO
   ↓
2. Repository
   ↓
  Room Entity
   ↓
3. Room DB
   ↓
  Domain Model
   ↓
4. ViewModel
   ↓
  UI Model (flatten + depth)
   ↓
5. UI - LazyColumn
```

## Techstack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Networking:** Retrofit
- **Local storage:** Room
- **DI:** Koin

## Architecture

- **Modules:** single-module app (`:app` only)
- **Style:** Clean Architecture
  - **presentation** — Compose UI, ViewModel (MVI)
  - **domain** — use cases, models, repository interfaces
  - **data** — Retrofit API, Room, repository, implementations

## Testing

- 9 tests (unit, integration with test doubles, 1 Compose UI test) derived from the requirements above.
- All run on the JVM, no emulator: `./gradlew testDebugUnitTest`
- Strategy: [.files/test-strategy.md](.files/test-strategy.md)
- Scenarios: [.files/test-scenarios.md](.files/test-scenarios.md)

## Trade-offs

1. **static JSON URL** - Despite that the "../raw/lumiform-android-test.json" looks like just a file, and logically to use just moshi to parse it in RepoImpl, but in fact, it is the real endpoint like "../users", and it violates boundaries of architecture. So it is better to use with Retrofit as a usual endpoint.
2. **No fixed depth limit of nodes in the tree** - Chose one flat table with parentId (any depth). Not nested DB entities (only a fixed number of levels).
3. **Showing nested items in UI** - Chose flat items with a depth prop (long scrollable list) in LazyList.  Not nested LazyLists (recursive nested scroll problem), not recursive nested Compose without LazyLists (problem with long list).
4. **MVI vs pure MVVM** - Chose MVI: one `State`, one `Event`, one-shot `Effect`s for navigation and errors (not shown again after rotation). Not pure MVVM, where the screen watches several states and calls `ViewModel` methods directly.
5. **Reading** `items` + `response_sets` + `responses` - Chose `@Relation` (one snapshot, no duplicated rows, no `ForeignKey`). Not `Flow.combine()` (a new choice `id` can arrive before its `response_set`), not SQL `JOIN` (the choice row is repeated once per response).
6. **Refresh** - Chose clearing the three tables, then insert. Not `OnConflictStrategy.REPLACE` without a clear (the same `id` is overwritten, missing ids stay).

## Risks

1. Flatten items with depth prop (long scrollable list) for showing nested items in UI - harder to implement collapsing, paginating (depends on a real requirement, amount)

## Possible improvements

- WorkManager for Retry
- Collapsing Lists for nested items
- Observe Internet connection with message for a user
- Rich ErrorHangling Util class to parse exceptions
- Multi-module migration

