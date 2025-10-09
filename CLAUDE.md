Rules for developing on Android projects:

Core Principles

- Follow functional Kotlin style: immutable data, pure functions, minimal side effects
- Prioritise clean and readable code. If a block of code is hard to understand at a glance, then its
  a sign we should refactor.
- Before adding new code, search for existing similar functionality
- Consolidate duplicate logic - one source of truth for formatting, parsing, validation, etc.
- When refactoring, ensure old and new approaches don't coexist
- **RED FLAG: If your plan adds new code but doesn't explicitly list what old code will be removed,
  stop and audit for architectural violations**
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
- Separate business logic from presentation logic:
  - Business logic (ViewModel): calculations, conversions, validation, state management
  - Presentation logic (UI layer): formatting (dates, numbers, currencies), colors, strings, layout
  - Example: Number parsing → ViewModel, Number formatting (commas, decimals) → UI layer
- Before following an existing pattern, verify it's architecturally correct
- If new code would duplicate existing logic, refactor the existing code instead of adding more
  duplication

Kotlin Language Use

- Prefer sealed class/sealed interface for representing finite states
- Leverage Kotlin collections API (map, filter, fold, etc.)
- Avoid null where possible. Its often an indication that we should refactor to more explicitly
  model states.

Compose

- ZERO business logic in composables - all logic goes in ViewModel where it's testable
  - Bad: `enabled = currencies.size > 1`
  - Good: `enabled = uiState.canRemoveCurrencies`
  - Composables are pure renderers - no ifs for business rules, no calculations
- Prefer stateless composables with ViewModel handling state
- Always provide `@Preview` functions for Compose screens/components
- Use Modifier parameters with default values
- Make composable parameters required by default - only add default values when there's a genuine
  optional use case
- Avoid nullable callback parameters - if a callback is always needed in actual usage, make it
  required
- Reserve `= null` and optional parameters for true configuration options, not core functionality
- Follow Material3 design guidelines
- Create custom preview annotations (e.g., created a `@FoldablePreview` for foldable screen sizes)
  to avoid
  repeating device specs
- Consider large screens and foldables: use max-width constraints (600-700dp) to prevent excessive
  stretching on wide displays
- When using `enableEdgeToEdge()`, always apply `windowInsetsPadding(WindowInsets.systemBars)` to
  handle both 3-button and gesture navigation
- ViewModels should be UI-framework agnostic - avoid presentation concerns like DecimalFormat,
  date formatting, etc.
- Use VisualTransformation, remember{}, derivedStateOf for presentation-only transformations

Testing

- Test-first for ALL new features and bug fixes - not just APIs
  - Write empty function → failing tests → implement → verify tests pass
  - Run tests after each step to confirm fail→pass cycle
  - ALWAYS write tests BEFORE implementation code in your plan
  - Plan structure: 1) Write failing tests, 2) Implement, 3) Run tests, 4) Iterate
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

Planning (Before Implementation)

- ALWAYS search for existing functionality first: Before proposing any new function, utility, or helper, use Grep/Glob to search the codebase for existing implementations. Search for relevant keywords related to what you're about to create (e.g., if creating formatting logic, search for "format"; if creating parsing logic, search for "parse").
- Read relevant existing code to understand current approach
- Identify code that will need refactoring to avoid duplication or where old and new approaches would coexist
- **Before proposing any solution, audit existing code for architectural violations:**
  - If adding formatting → check ViewModel for existing formatting to remove
  - If adding parsing → check UI layer for parsing logic to move
  - If adding validation → search for duplicate validation
  - Ask: "Will my solution create duplicate or coexisting approaches?" If yes, plan refactoring FIRST
- **When proposing a solution that touches a layer boundary (ViewModel ↔ UI):**
  - Explicitly check BOTH sides of the boundary
  - List what will be REMOVED from each layer
  - Ensure no logic leaks across the boundary in either direction
- Question existing architecture: If existing code violates separation of concerns, propose
  refactoring first
- Apply "one source of truth" rigorously: If you'd create duplicate
  formatting/parsing/validation, that's a signal to refactor
- When uncertain about layer boundaries, ask: "Could this logic exist without Android/Compose?"
  - Yes → ViewModel/domain
  - No → UI layer
- Structure plan as: failing tests → implementation → passing tests → refactor
- Explicitly call out what existing code will be removed/replaced in your plan