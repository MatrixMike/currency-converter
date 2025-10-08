Rules for developing on Android projects:

Core Principles

- Follow functional Kotlin style: immutable data, pure functions, minimal side effects
- When uncertain, ask for clarification and discuss trade-offs

Architecture

- MVVM with single StateFlow<ViewState> - unidirectional data flow only
- Keep ViewModels/domain free of Android dependencies (use `java.net.URI` not `android.net.Uri`)
- Use extension functions for Android ↔ domain conversions at boundaries
- Prefer higher-order functions over class injection for loose coupling and testability
- Use `Result<T>` or sealed classes for error handling, instead of returning null
- Model states explicitly with sealed classes - no nullable "loading" states, or "isLoading" booleans.

Kotlin Language Use 
 
- Prefer sealed class/sealed interface for representing finite states
- Leverage Kotlin collections API (map, filter, fold, etc.)
- Avoid null where possible.  Its often an indication that we should refactor to more explicitly model states.

Compose

- Prefer stateless composables with ViewModel handling state
- Always provide `@Preview` functions for Compose screens/components
- Use Modifier parameters with default values
- Follow Material3 design guidelines

Testing

- Test-first development when designing new APIs
- Test names with backticks describing behavior: `"should return X when Y"`
- Avoid mocking frameworks - use higher-order functions and fakes instead
- Structure: Arrange-Act-Assert with clear comments
- Use factory functions for test data creation
- Prefer state verification over interaction testing
- Use turbine for testing Flows