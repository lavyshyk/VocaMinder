# Android Development Domain Knowledge

Core concepts and fundamentals that Claude should know.

## Android SDK Basics

### Activity Lifecycle
- onCreate → onStart → onResume → onPause → onStop → onDestroy
- Handle configuration changes properly
- Save state in onSaveInstanceState

### Fragments
- Use sparingly in modern apps (prefer Compose)
- Lifecycle tied to parent Activity
- Use ViewModel for data retention

### Services
- Foreground services require notification
- Background work limits (Android 8+)
- Use WorkManager for deferred work

## Kotlin Language Features

### Null Safety
- `?` nullable type
- `!!` non-null assertion (avoid!)
- `?.` safe call
- `?:` Elvis operator

### Coroutines
- `suspend` functions
- `launch` vs `async`
- Dispatchers: Main, IO, Default
- `Flow` for reactive streams

### Sealed Classes
- Exhaustive when expressions
- Perfect for state representation
- Type-safe alternatives

## Android Components

### Context
- Application Context: App lifetime
- Activity Context: Activity lifetime
- Avoid context leaks!

### Intents
- Explicit: Specific component
- Implicit: System chooses
- Intent filters in manifest

### Permissions
- Dangerous permissions require runtime request
- Check before use
- Handle denial gracefully

## Best Practices

### Memory Management
- Avoid static references to Context
- Use WeakReference when needed
- Clean up listeners/callbacks
- Cancel coroutines properly

### Performance
- Avoid work on main thread
- Use ProGuard/R8 for release
- Optimize images (WebP, proper sizing)
- Profile with Android Profiler

### Security
- Never hardcode secrets
- Use HTTPS always
- Validate all inputs
- Use Android Keystore

## Common Patterns

### Repository Pattern
```
UI → ViewModel → UseCase → Repository → DataSource
```

### MVI Pattern
```
Intent → Store → State → UI
```

### Dependency Injection
- Constructor injection preferred
- Use Koin/Hilt/Dagger
- Scopes match lifecycle

---

**This knowledge is always available - no need to load separately!**
