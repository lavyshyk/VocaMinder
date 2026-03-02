# Android Dev Agent — VocaMinder

## Role
Expert Android/KMP developer assistant for the VocaMinder Kotlin Multiplatform project.
Follow all rules in `.claude/SYSTEM-PROMPT.md` (safety, code quality, risk assessment).

## Skills
Auto-load from `.claude/skills/` based on task keywords:

| Keyword in request | Skill to load |
|-|-|
| UI, screen, composable, Material 3 | `compose.skill.md` |
| navigate, routing, component, child | `decompose.skill.md` |
| lifecycle, ComponentContext | `essenty.skill.md` |
| Store, Intent, State, Event, MVI | `mvi.skill.md` |
| build, gradle, dependency, version | `gradle.skill.md` |
| test, MockK, unit test | `testing.skill.md` |
| network, API, Ktor, HTTP | `networking.skill.md` |
| database, Room, cache | `room.skill.md` |
| refactor, clean up, improve | `refactoring.skill.md` |
| plan, task, sprint, estimate | `work-planning.skill.md` |
| security, OWASP, encrypt | `security.skill.md` |

Multiple skills can load at once. Example: "Create a product list with API + caching" → compose + networking + room.

## Memory
- Project context:  `.claude/memory/project-context.json`
- User preferences: `.claude/memory/user-preferences.json`
- Domain knowledge: `.claude/memory/domain-knowledge.md`

## Project Architecture

**Type:** Kotlin Multiplatform (Android + iOS)
**Pattern:** MVI + Clean Architecture
**Navigation:** Decompose (ComponentContext-based)
**State:** MVIKotlin (Store + Intent + State + Label)
**Lifecycle:** Essenty

### Module Structure
```
VocaMinder/
├── androidApplication/   → Android entry point (MainActivity, previews)
├── composeApp/           → Shared UI layer (screens, theme, app root)
│   └── commonMain/kotlin/org/vocaminder/
│       ├── App.kt
│       ├── root/RootContent.kt
│       ├── home/HomeScreen.kt
│       ├── crudwordset/CRUDSWordSetScreen.kt
│       └── theme/
├── features/             → Feature components (business logic, Stores)
│   └── commonMain/kotlin/org/vocaminder/
│       ├── root/
│       ├── home/
│       ├── crudwordset/
│       └── settings/
└── core/                 → Shared utilities (cache, formatting, extensions)
    └── commonMain/kotlin/org/voceminder/core/
```

### Feature Module Pattern (api / impl)
```
features/src/commonMain/kotlin/org/vocaminder/<feature>/
├── api/
│   ├── <Feature>Component.kt       ← interface
│   ├── <Feature>ComponentState.kt  ← State/Intent/Event sealed classes
│   └── Preview<Feature>Component.kt
└── internal/
    ├── Default<Feature>Component.kt ← Decompose ComponentContext impl
    └── <Feature>Store.kt            ← MVIKotlin Store
```

## Tech Stack (from libs.versions.toml)
- **Kotlin:** 2.3.0
- **Compose Multiplatform:** 1.10.1
- **Material 3:** 1.10.0-alpha05 (KMP) / 1.3.1 (Android)
- **Decompose:** 3.3.0
- **Essenty:** 2.5.0
- **MVIKotlin:** 4.3.0
- **kotlinx-coroutines:** 1.10.2
- **kotlinx-serialization:** 1.8.1
- **minSdk:** 24 | **targetSdk/compileSdk:** 36
- **AGP:** 9.0.0

## Conventions
- Package: `org.vocaminder` (UI/features), `org.voceminder.core` (core)
- Naming: `DefaultXxxComponent`, `XxxStore`, `XxxState`, `XxxIntent`, `XxxLabel`
- No Koin/Hilt — dependencies passed via constructor
- Resources via `composeResources/` (fonts, drawables)
- Version catalog: `gradle/libs.versions.toml`

---

**Always follow `.claude/SYSTEM-PROMPT.md` safety rules. Load appropriate skills per task.**