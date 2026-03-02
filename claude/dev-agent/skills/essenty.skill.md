---
name: essenty
description: Essenty lifecycle management, state preservation, and back handling for Decompose components. Triggers when user mentions lifecycle, state keeping, back button, instance state, or component lifecycle management.
---

# Essenty Lifecycle Skill

Lifecycle-aware component management with Essenty.

## Version

**Essenty:** 2.0.0

## Core Components

### Lifecycle

```kotlin
class FeatureComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    init {
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onCreate() {
                    // Initialize resources
                }
                
                override fun onStart() {
                    // Start observations
                }
                
                override fun onResume() {
                    // Active state
                }
                
                override fun onPause() {
                    // Pause heavy operations
                }
                
                override fun onStop() {
                    // Stop observations
                }
                
                override fun onDestroy() {
                    // Cleanup resources
                }
            }
        )
    }
}
```

### Coroutine Scope (Lifecycle-aware)

```kotlin
class FeatureComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    // Automatically cancelled when component is destroyed
    private val scope = coroutineScope()
    
    fun loadData() {
        scope.launch {
            // This coroutine is tied to component lifecycle
            val data = repository.getData()
            // ...
        }
    }
}
```

### State Keeper (Save/Restore State)

```kotlin
@Serializable
data class FeatureState(
    val scrollPosition: Int = 0,
    val searchQuery: String = ""
)

class FeatureComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    // Restore saved state or use default
    private var state: FeatureState = stateKeeper.consume(
        key = "feature_state",
        strategy = FeatureState.serializer()
    ) ?: FeatureState()
    
    init {
        // Save state before destruction
        stateKeeper.register(
            key = "feature_state",
            strategy = FeatureState.serializer()
        ) { state }
    }
    
    fun updateScrollPosition(position: Int) {
        state = state.copy(scrollPosition = position)
    }
}
```

### Back Handler

```kotlin
class FeatureComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    private var hasUnsavedChanges = false
    
    private val backCallback = BackCallback(
        isEnabled = false,
        onBack = ::handleBack
    )
    
    init {
        backHandler.register(backCallback)
    }
    
    fun setHasUnsavedChanges(value: Boolean) {
        hasUnsavedChanges = value
        backCallback.isEnabled = value
    }
    
    private fun handleBack() {
        if (hasUnsavedChanges) {
            // Show confirmation dialog
            showUnsavedChangesDialog(
                onDiscard = {
                    hasUnsavedChanges = false
                    onNavigateBack()
                },
                onCancel = { /* Stay */ }
            )
        } else {
            onNavigateBack()
        }
    }
}
```

### Instance Keeper (Retain Across Config Changes)

```kotlin
class FeatureComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    // Survives configuration changes (rotation, etc.)
    private val retainedStore = instanceKeeper.getOrCreate {
        ProductListStore()
    }
}
```

## Complete Component Example

```kotlin
class ProductListComponent(
    componentContext: ComponentContext,
    private val getProductsUseCase: GetProductsUseCase,
    private val onNavigateToDetails: (String) -> Unit,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    // Lifecycle-aware coroutine scope
    private val scope = coroutineScope()
    
    // Retained across config changes
    private val store = instanceKeeper.getOrCreate {
        ProductListStore(
            getProductsUseCase = getProductsUseCase,
            scope = scope
        )
    }
    
    // State preservation
    @Serializable
    data class SavedState(
        val scrollPosition: Int = 0,
        val lastSearchQuery: String = ""
    )
    
    private var savedState = stateKeeper.consume(
        key = "saved_state",
        strategy = SavedState.serializer()
    ) ?: SavedState()
    
    // Back handling
    private val backCallback = BackCallback(
        isEnabled = true,
        onBack = onNavigateBack
    )
    
    init {
        // Register state saver
        stateKeeper.register(
            key = "saved_state",
            strategy = SavedState.serializer()
        ) { savedState }
        
        // Register back handler
        backHandler.register(backCallback)
        
        // Subscribe to lifecycle
        lifecycle.subscribe(
            object : Lifecycle.Callbacks {
                override fun onDestroy() {
                    // Cleanup if needed
                }
            }
        )
    }
    
    val state: StateFlow<ProductListState> = store.state
    
    fun onIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.OnProductClick ->
                onNavigateToDetails(intent.productId)
            else -> store.accept(intent)
        }
    }
    
    fun updateScrollPosition(position: Int) {
        savedState = savedState.copy(scrollPosition = position)
    }
}
```

## Lifecycle States

```
INITIALIZED → CREATED → STARTED → RESUMED
                ↓          ↓          ↓
            DESTROYED ← STOPPED ← PAUSED
```

## Best Practices

✅ **Do:**
- Use `coroutineScope()` for lifecycle-aware coroutines
- Save UI state in StateKeeper
- Use InstanceKeeper for expensive objects
- Register BackCallback when needed
- Clean up in lifecycle.onDestroy

❌ **Don't:**
- Use GlobalScope (always use component scope)
- Forget to unregister listeners
- Save large objects in StateKeeper
- Ignore lifecycle callbacks
- Keep references after destroy

## Common Patterns

### Save Scroll Position

```kotlin
fun saveScrollPosition(position: Int) {
    savedState = savedState.copy(scrollPosition = position)
}

fun restoreScrollPosition(): Int {
    return savedState.scrollPosition
}
```

### Handle Back With Confirmation

```kotlin
private var hasChanges = false

private val backCallback = BackCallback(
    isEnabled = hasChanges,
    onBack = {
        if (hasChanges) {
            showDialog(
                onConfirm = { onNavigateBack() },
                onCancel = { }
            )
        }
    }
)

fun setHasChanges(value: Boolean) {
    hasChanges = value
    backCallback.isEnabled = value
}
```

### Retain ViewModel-like Object

```kotlin
private val viewModel = instanceKeeper.getOrCreate {
    FeatureViewModel(repository)
}
```

---

**Remember:** Essenty manages lifecycle automatically - use it!
