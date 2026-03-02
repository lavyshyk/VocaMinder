---
name: mvi
description: Model-View-Intent architecture pattern implementation. Triggers when user mentions MVI, state management, intent, unidirectional data flow, or wants to create features with proper architecture.
---

# MVI Architecture Skill

Model-View-Intent pattern for predictable, testable UI state management.

## Core Pattern

```
User Action → Intent → Store → State Update → UI Recomposition
     ↑                                              ↓
     └──────────────────────────────────────────────┘
```

## Components

### 1. State (Immutable Data Class)

```kotlin
data class ProductListState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val products: List<Product> = emptyList(),
    val selectedProductId: String? = null,
    val searchQuery: String = "",
    val error: String? = null,
    val filter: ProductFilter = ProductFilter.ALL
)

enum class ProductFilter {
    ALL, FAVORITES, RECENT
}
```

**Rules:**
- Always `data class`
- All properties are `val`
- Provide sensible defaults
- Keep flat when possible
- One state per screen/feature

### 2. Intent (User Actions)

```kotlin
sealed interface ProductListIntent {
    // Data loading
    data object LoadProducts : ProductListIntent
    data object Refresh : ProductListIntent
    data object RetryLoad : ProductListIntent
    
    // User interactions
    data class OnProductClick(val productId: String) : ProductListIntent
    data class OnSearchQueryChange(val query: String) : ProductListIntent
    data class OnFilterChange(val filter: ProductFilter) : ProductListIntent
    
    // Actions
    data class OnDeleteProduct(val productId: String) : ProductListIntent
    data class OnToggleFavorite(val productId: String) : ProductListIntent
    
    // Navigation
    data object OnBackClick : ProductListIntent
    data object OnSettingsClick : ProductListIntent
}
```

**Rules:**
- Use `sealed interface` (or `sealed class`)
- Use `data object` for intents without data
- Use `data class` for intents with parameters
- Name clearly: verb or action-based
- Group related intents

### 3. Event (One-Time Side Effects)

```kotlin
sealed interface ProductListEvent {
    // Navigation events
    data class NavigateToDetails(val productId: String) : ProductListEvent
    data object NavigateToSettings : ProductListEvent
    data object NavigateBack : ProductListEvent
    
    // UI feedback
    data class ShowToast(val message: String) : ProductListEvent
    data class ShowSnackbar(val message: String) : ProductListEvent
    data class ShowError(val error: Throwable) : ProductListEvent
    
    // Dialogs
    data class ShowDeleteConfirmation(val productId: String) : ProductListEvent
}
```

**When to use Events vs State:**
- **State:** Persistent UI state (data, flags, selections)
- **Event:** One-time actions (navigation, toasts, dialogs)

### 4. Store (State Manager)

```kotlin
class ProductListStore(
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val scope: CoroutineScope
) {
    // State
    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state.asStateFlow()
    
    // Events (one-time)
    private val _events = MutableSharedFlow<ProductListEvent>()
    val events: SharedFlow<ProductListEvent> = _events.asSharedFlow()
    
    init {
        loadProducts()
    }
    
    // Intent handler
    fun accept(intent: ProductListIntent) {
        when (intent) {
            ProductListIntent.LoadProducts -> loadProducts()
            ProductListIntent.Refresh -> refresh()
            ProductListIntent.RetryLoad -> loadProducts()
            
            is ProductListIntent.OnProductClick -> 
                handleProductClick(intent.productId)
            is ProductListIntent.OnSearchQueryChange -> 
                updateSearchQuery(intent.query)
            is ProductListIntent.OnFilterChange -> 
                updateFilter(intent.filter)
            
            is ProductListIntent.OnDeleteProduct -> 
                deleteProduct(intent.productId)
            is ProductListIntent.OnToggleFavorite -> 
                toggleFavorite(intent.productId)
            
            ProductListIntent.OnBackClick -> 
                scope.launch { _events.emit(ProductListEvent.NavigateBack) }
            ProductListIntent.OnSettingsClick -> 
                scope.launch { _events.emit(ProductListEvent.NavigateToSettings) }
        }
    }
    
    // State updates
    private fun loadProducts() {
        scope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            
            getProductsUseCase()
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            products = products,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                    _events.emit(ProductListEvent.ShowError(error))
                }
        }
    }
    
    private fun refresh() {
        scope.launch {
            _state.update { it.copy(isRefreshing = true) }
            
            getProductsUseCase()
                .onSuccess { products ->
                    _state.update {
                        it.copy(
                            isRefreshing = false,
                            products = products
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isRefreshing = false) }
                    _events.emit(
                        ProductListEvent.ShowToast("Failed to refresh")
                    )
                }
        }
    }
    
    private fun handleProductClick(productId: String) {
        scope.launch {
            _events.emit(ProductListEvent.NavigateToDetails(productId))
        }
    }
    
    private fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
        // Trigger search with debounce if needed
    }
    
    private fun updateFilter(filter: ProductFilter) {
        _state.update { it.copy(filter = filter) }
    }
    
    private fun deleteProduct(productId: String) {
        scope.launch {
            deleteProductUseCase(DeleteProductUseCase.Params(productId))
                .onSuccess {
                    _state.update { state ->
                        state.copy(
                            products = state.products.filter { it.id != productId }
                        )
                    }
                    _events.emit(ProductListEvent.ShowToast("Product deleted"))
                }
                .onFailure { error ->
                    _events.emit(ProductListEvent.ShowError(error))
                }
        }
    }
    
    private fun toggleFavorite(productId: String) {
        scope.launch {
            toggleFavoriteUseCase(ToggleFavoriteUseCase.Params(productId))
                .onSuccess { updatedProduct ->
                    _state.update { state ->
                        state.copy(
                            products = state.products.map {
                                if (it.id == productId) updatedProduct else it
                            }
                        )
                    }
                }
        }
    }
}
```

## Component Integration (Decompose)

```kotlin
class ProductListComponent(
    componentContext: ComponentContext,
    private val getProductsUseCase: GetProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val onNavigateToDetails: (String) -> Unit,
    private val onNavigateToSettings: () -> Unit,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    private val scope = coroutineScope()
    
    private val store = ProductListStore(
        getProductsUseCase = getProductsUseCase,
        deleteProductUseCase = deleteProductUseCase,
        toggleFavoriteUseCase = toggleFavoriteUseCase,
        scope = scope
    )
    
    val state: StateFlow<ProductListState> = store.state
    
    init {
        // Handle events
        scope.launch {
            store.events.collect { event ->
                when (event) {
                    is ProductListEvent.NavigateToDetails -> 
                        onNavigateToDetails(event.productId)
                    ProductListEvent.NavigateToSettings -> 
                        onNavigateToSettings()
                    ProductListEvent.NavigateBack -> 
                        onNavigateBack()
                    is ProductListEvent.ShowToast,
                    is ProductListEvent.ShowSnackbar,
                    is ProductListEvent.ShowError,
                    is ProductListEvent.ShowDeleteConfirmation -> {
                        // Handled in UI
                    }
                }
            }
        }
    }
    
    fun onIntent(intent: ProductListIntent) {
        store.accept(intent)
    }
}
```

## UI Layer (Compose)

```kotlin
@Composable
fun ProductListScreen(
    component: ProductListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()
    
    // Handle events
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(Unit) {
        component.store.events.collect { event ->
            when (event) {
                is ProductListEvent.ShowToast -> {
                    // Show toast
                }
                is ProductListEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ProductListEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        message = event.error.message ?: "Error",
                        actionLabel = "Retry"
                    )
                }
                else -> {} // Navigation handled in component
            }
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ProductListTopBar(
                onSearchQueryChange = { 
                    component.onIntent(
                        ProductListIntent.OnSearchQueryChange(it)
                    )
                },
                onSettingsClick = {
                    component.onIntent(ProductListIntent.OnSettingsClick)
                }
            )
        }
    ) { padding ->
        ProductListContent(
            state = state,
            onIntent = component::onIntent,
            modifier = modifier.padding(padding)
        )
    }
}

@Composable
private fun ProductListContent(
    state: ProductListState,
    onIntent: (ProductListIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        state.isLoading -> LoadingView(modifier)
        state.error != null -> ErrorView(
            error = state.error,
            onRetry = { onIntent(ProductListIntent.RetryLoad) },
            modifier = modifier
        )
        state.products.isEmpty() -> EmptyView(
            message = "No products found",
            modifier = modifier
        )
        else -> ProductList(
            products = state.products,
            filter = state.filter,
            onProductClick = { 
                onIntent(ProductListIntent.OnProductClick(it))
            },
            onDeleteProduct = {
                onIntent(ProductListIntent.OnDeleteProduct(it))
            },
            modifier = modifier
        )
    }
}
```

## Testing

### Store Tests

```kotlin
class ProductListStoreTest {
    
    private lateinit var store: ProductListStore
    private lateinit var mockGetProductsUseCase: GetProductsUseCase
    
    @Before
    fun setup() {
        mockGetProductsUseCase = mockk()
        store = ProductListStore(
            getProductsUseCase = mockGetProductsUseCase,
            scope = TestScope()
        )
    }
    
    @Test
    fun `initial state should be loading`() {
        assertEquals(true, store.state.value.isLoading)
    }
    
    @Test
    fun `when products load successfully, should update state`() = runTest {
        val products = listOf(Product("1", "Test"))
        coEvery { mockGetProductsUseCase() } returns Result.success(products)
        
        store.accept(ProductListIntent.LoadProducts)
        advanceUntilIdle()
        
        assertEquals(false, store.state.value.isLoading)
        assertEquals(products, store.state.value.products)
        assertEquals(null, store.state.value.error)
    }
    
    @Test
    fun `when OnProductClick intent, should emit NavigateToDetails event`() = runTest {
        val events = mutableListOf<ProductListEvent>()
        val job = launch {
            store.events.collect { events.add(it) }
        }
        
        store.accept(ProductListIntent.OnProductClick("123"))
        advanceUntilIdle()
        
        assertTrue(
            events.any { 
                it is ProductListEvent.NavigateToDetails && 
                it.productId == "123" 
            }
        )
        
        job.cancel()
    }
}
```

## Best Practices

✅ **Do:**
- Keep state immutable (data class with val)
- Use sealed interfaces for Intents and Events
- Handle all states (loading, error, empty, success)
- Use StateFlow for state, SharedFlow for events
- Keep business logic in use cases, not stores
- Test state transitions
- Use update {} for thread-safe state changes

❌ **Don't:**
- Use var in state classes
- Put business logic in stores
- Forget to handle error states
- Use GlobalScope in stores
- Expose MutableStateFlow
- Create new lambdas in list items
- Skip event handling

## Common Patterns

### Optimistic Updates

```kotlin
private fun deleteProduct(productId: String) {
    // Update UI immediately
    val oldProducts = _state.value.products
    _state.update { state ->
        state.copy(products = state.products.filter { it.id != productId })
    }
    
    scope.launch {
        deleteProductUseCase(productId)
            .onFailure {
                // Revert on error
                _state.update { it.copy(products = oldProducts) }
                _events.emit(ProductListEvent.ShowError(it))
            }
    }
}
```

### Debounced Search

```kotlin
private var searchJob: Job? = null

private fun updateSearchQuery(query: String) {
    _state.update { it.copy(searchQuery = query) }
    
    searchJob?.cancel()
    searchJob = scope.launch {
        delay(300) // Debounce
        performSearch(query)
    }
}
```

---

**Remember:** MVI = Predictable, Testable, Maintainable!
