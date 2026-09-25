# Testing Strategy
Requirement IDs (FRn / NFRn) refer to [README.md](../README.md).  
Test scenarios: `test-scenarios.md`.

## Approach
1. **Requirements-based.** Each test proves a behavior and refers to a requirement.
2. **Risk-driven.** Priority goes to logic that can break silently: recursion over nested sections, tree -> flat  -> tree, conversions, option selection, offline fallback.
3. **Local only.** JVM tests with test doubles, no emulator (including the UI tests).

## Requirements → Tests
- FR1
  - **Behavior:** Potentially unbounded nesting survives DTO -> Entity -> Domain
    - **Test:** T1
  - **Behavior:** Flattened rows with correct depth
    - **Test:** T2
- FR2
  - **Behavior:** Single selection replaces the previous option
    - **Test:** T5
  - **Behavior:** Multiple selection adds the tapped option
    - **Test:** T6
- FR4
  - **Behavior:** Opening an image emits one navigation effect
    - **Test:** T7
  - **Behavior:** Tapping an image sends OpenImage with title and source
    - **Test:** T8
- NFR1
  - **Behavior:** Potentially unbounded nesting survives DTO -> Entity -> Domain
    - **Test:** T1
- NFR2
  - **Behavior:** Successful refresh shows the new tree
    - **Test:** T3
  - **Behavior:** Network failure keeps the cached tree
    - **Test:** T4
- NFR3
  - **Behavior:** Network failure keeps the cached tree
    - **Test:** T4

## Test levels
- Unit: 2
- Integration (test doubles): 5
- UI: 1

## Tooling
- `kotlin.test` on JUnit 4 - test runner.
- Kotest assertions - `shouldBe` matchers (not the Kotest framework).
- `kotlinx-coroutines-test` + Turbine - coroutines and `Flow` emissions.
- Robolectric + Compose UI test - the UI test on the JVM.

Run all tests: `./gradlew testDebugUnitTest`.

## Test style
- One test class per subject: `<Subject>Test`.
- The test name is the scenario name from `test-scenarios.md`, e.g. ``T5 - title``.

```kotlin
class SubjectTest {

    @Test
    fun `T1 - subject should do something`() {
        val input = sampleInput()

        val result = subject(input)

        result shouldBe expected
    }
}
```

## Scope boundaries
- Font sizes and layout - visual only; `type` and `depth` are covered by mapper tests.
- Reduced-size image preview (FR3) - visual only; rendering is done by Coil.
- Use cases - pass-through to the repository; covered via ViewModel tests.
- Navigation graph - covered by the `NavigateToFullscreenImage` effect (T7).

