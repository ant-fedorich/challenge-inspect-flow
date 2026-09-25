# Test Scenarios
Strategy and scope: `test-strategy.md`.
Requirement IDs (FRn / NFRn) refer to [README.md](../README.md).
One numbered item = one test. Bullets are Given/When/Then (+ And).

Source for generating tests. Each test name must match the scenario title.

## Scenarios
##### FR1 / NFR1 - Hierarchy and potentially unbounded nesting (mappers)
1. **T1 - deep nesting should keep its shape from DTO to domain**
  - Given a page -> nested sections -> text question with nesting depth 10
  - When mapped DTO -> Entity -> Domain
  - Then the domain tree has the same shape and ids at every level

##### FR1 - Flatten for UI (`toUIModel`)
1. **T2 - flattened rows should carry depth per level**
  - Given page -> section -> nested section -> question
  - When flattened
  - Then rows are `[page(depth 0), section(depth 1), section(depth 2), question(depth 3)]`

##### NFR2 / NFR3 - Offline and network failure (repository)
1. **T3 - successful refresh should emit Loading then Success and show the new tree**
  - Given the API returns data
  - When refreshing
  - Then emissions are `Loading` -> `Success`
  - And `observeInspection()` emits the new tree
2. **T4 - network failure should emit Loading then Failure and keep the cached tree**
  - Given cached data
  - And the API throws (e.g. `IOException`)
  - When refreshing
  - Then emissions are `Loading` -> `Failure`
  - And `observeInspection()` still emits the cached tree

##### FR2 - Choice selection (ViewModel)
1. **T5 - single selection should replace the previous option**
  - Given a single-selection question with `{1}` selected
  - When option 2 is tapped
  - Then selection is `{2}`
2. **T6 - multiple selection should add the tapped option**
  - Given a multiple-selection question with `{1}` selected
  - When option 2 is tapped
  - Then selection is `{1, 2}`

##### FR4 - Full-screen image (ViewModel)
1. **T7 - opening an image should emit exactly one navigation effect**
  - Given the list screen is shown
  - When `OpenImage(title, imageSrc)` is sent
  - Then exactly one `NavigateToFullscreenImage` effect is emitted with the same `title` and `imageSrc`

##### FR4 - UI
1. **T8 - tapping an image should send OpenImage with its title and source**
  - Given `state` with one image question (`title`, `imageSrc`)
  - When the user taps the image
  - Then `onEvent` receives `OpenImage(title, imageSrc)`

