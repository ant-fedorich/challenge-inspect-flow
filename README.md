# InspectFlow
Native Android app for the coding challenge: fetch inspection JSON, show hierarchical Pages/Sections/Questions, offline cache, choice and image.

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
- 8 tests (unit, integration with test doubles, 1 Compose UI test) derived from the requirements above.
- All run on the JVM, no emulator: `./gradlew testDebugUnitTest`
- Strategy: [.files/test-strategy.md](.files/test-strategy.md)
- Scenarios: [.files/test-scenarios.md](.files/test-scenarios.md)

## Trade-offs
1. **static JSON URL** — Despite that the "../raw/lumiform-android-test.json" looks like just a file, and logically to use just moshi to parse it in RepoImpl, but in fact, it is the real endpoint like "../users", and it violates boundaries of architecture. So it is better to use with Retrofit as a usual endpoint
2. Endless depth of nodes in the tree - flatten one table in DB (can handle endless nodes depth) vs nested DB entites like domain models (does not handle endless nodes depth)
3. (option?) Multimodule - single module + sctucture similar to multimodule (easier to migrate to multi-module) vs multi-module (overengineered in this task)
4. Showing nested items in UI - Flatten items with depth prop (long scrollable list) in LazyList vs nested LazyLists (recursive nested scoll problem) vs recursive nested Compose (problem with long list) without LazyLists
5. MVI vs pure MVVM
6. Do not cash option selection VS cash option selection
7. ?Conbine table with Flow conbine() VS not via SQL JOIN, because JOIN would duplicate rows for choice responses i.e. returns dublicated objects to Kotlin.

## Risks
1. Flatten items with depth prop (long scrollable list) for showing nested items in UI - harder to implement collapsing, paginating (depends on a real requirement, amount)

## TODO
- WorkManager for Retry
- Collapsing Lists for nested items
- Observe Internet connection with message
- ErrorHangling Util to parse exceptions

