Rules for developing on Android projects:

Core Principles

- Follow functional Kotlin style: immutable data, pure functions, minimal side effects
- Prioritise clean and readable code. If a block of code is hard to understand at a glance, then its
  a sign we should refactor.
- When uncertain, ask for clarification and discuss trade-offs
- Its okay to suggest cleaning up and refactoring, particularly at the beginning of a task, if it
  will make the task easier to complete.

Architecture

- MVVM with single StateFlow<ViewState> - unidirectional data flow only
- Keep ViewModels/domain free of Android dependencies (use `java.net.URI` not `android.net.Uri`)
- Use extension functions for Android ↔ domain conversions at boundaries
- Prefer higher-order functions over class injection for loose coupling and testability
- Use `Result<T>` or sealed classes for error handling, instead of returning null
- Model states explicitly with sealed classes - no nullable "loading" states, or "isLoading"
  booleans.

Kotlin Language Use

- Prefer sealed class/sealed interface for representing finite states
- Leverage Kotlin collections API (map, filter, fold, etc.)
- Avoid null where possible. Its often an indication that we should refactor to more explicitly
  model states.

Compose

- Prefer stateless composables with ViewModel handling state
- Avoid "business logic" inside the composable - instead shift this to the ViewModel where its
  testable. The composable should just be a straight renderer of the View Models state, and not
  contain ifs, build lists, etc
- Always provide `@Preview` functions for Compose screens/components
- Use Modifier parameters with default values
- Follow Material3 design guidelines

Testing

- Test-first development when designing new APIs
- When fixing bugs try and begin by writing a test that will reproduce the issue. ONLY AFTER we have
  reproduced the issue should we fix it.
- Test names with backticks describing behavior: `"should return X when Y"`
- Avoid mocking frameworks - use higher-order functions and fakes instead
- Structure: Arrange-Act-Assert with clear comments
- Use factory functions for test data creation
- Prefer state verification over interaction testing
- Use turbine for testing Flows
- Always question Thread.sleep() in tests!  Its often a sign that something is wrong somewhere else,
  particularly in our software architecture. It may indicate the need to refactor.