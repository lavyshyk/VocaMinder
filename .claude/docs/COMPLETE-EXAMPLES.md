# Complete Usage Examples

Real-world examples of using Android Dev Agent v2.

---

## Example 1: Creating a Complete Feature (E-Commerce Product List)

**User Request:**
```
Create a product list feature with:
- Fetch products from API
- Display in grid layout
- Pull-to-refresh
- Offline caching
- Navigate to product details
- Loading and error states
```

**Agent Response Process:**

### Step 1: Auto-loads Skills
```
Detected keywords:
- "API" → networking.skill.md
- "grid layout" → compose.skill.md
- "navigate" → decompose.skill.md
- "caching" → room.skill.md
```

### Step 2: Risk Assessment
```
Risk Level: 🟢 Low (new feature, no existing code modified)
No warnings needed.
```

### Step 3: Generates Complete Architecture

**Domain Layer:**
```kotlin
// domain/model/Product.kt
data class Product(
    val id: String,
    val name: String,
    val price: Double,
    val imageUrl: String,
    val category: String
)

// domain/repository/ProductRepository.kt
interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    fun observeProducts(): Flow<List<Product>>
}

// domain/usecase/GetProductsUseCase.kt
class GetProductsUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): Result<List<Product>> {
        return repository.getProducts()
    }
}

class ObserveProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.observeProducts()
    }
}
```

**Data Layer:**
```kotlin
// data/dto/ProductDto.kt
@Serializable
data class ProductDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("price") val price: Double,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("category") val category: String
)

// data/entity/ProductEntity.kt
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "image_url") val imageUrl: String,
    @ColumnInfo(name = "category") val category: String
)

// data/source/remote/ProductRemoteDataSource.kt
class ProductRemoteDataSource(private val client: HttpClient) {
    suspend fun fetchProducts(): Result<List<ProductDto>> = runCatching {
        client.get("https://api.example.com/products").body()
    }
}

// data/source/local/ProductLocalDataSource.kt
@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun observeAll(): Flow<List<ProductEntity>>
    
    @Query("SELECT * FROM products")
    suspend fun getAll(): List<ProductEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)
}

// data/repository/ProductRepositoryImpl.kt
class ProductRepositoryImpl(
    private val remoteDataSource: ProductRemoteDataSource,
    private val localDataSource: ProductDao,
    private val mapper: ProductMapper
) : ProductRepository {
    
    override suspend fun getProducts(): Result<List<Product>> = runCatching {
        // Fetch from API
        val dtos = remoteDataSource.fetchProducts().getOrThrow()
        
        // Cache locally
        val entities = dtos.map(mapper::toEntity)
        localDataSource.insertAll(entities)
        
        // Return domain models
        entities.map(mapper::toDomain)
    }
    
    override fun observeProducts(): Flow<List<Product>> {
        return localDataSource.observeAll()
            .map { entities -> entities.map(mapper::toDomain) }
    }
}
```

**Presentation Layer:**
```kotlin
// presentation/state/ProductListState.kt
data class ProductListState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)

// presentation/state/ProductListIntent.kt
sealed interface ProductListIntent {
    data object LoadProducts : ProductListIntent
    data object Refresh : ProductListIntent
    data class OnProductClick(val productId: String) : ProductListIntent
}

// presentation/state/ProductListEvent.kt
sealed interface ProductListEvent {
    data class NavigateToDetails(val productId: String) : ProductListEvent
    data class ShowError(val message: String) : ProductListEvent
}

// presentation/store/ProductListStore.kt
class ProductListStore(
    private val getProductsUseCase: GetProductsUseCase,
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state.asStateFlow()
    
    private val _events = MutableSharedFlow<ProductListEvent>()
    val events: SharedFlow<ProductListEvent> = _events.asSharedFlow()
    
    init {
        observeProducts()
    }
    
    fun accept(intent: ProductListIntent) {
        when (intent) {
            ProductListIntent.LoadProducts -> loadProducts()
            ProductListIntent.Refresh -> refresh()
            is ProductListIntent.OnProductClick -> handleProductClick(intent.productId)
        }
    }
    
    private fun observeProducts() {
        scope.launch {
            observeProductsUseCase().collect { products ->
                _state.update { it.copy(products = products) }
            }
        }
    }
    
    private fun loadProducts() {
        scope.launch {
            _state.update { it.copy(isLoading = true) }
            getProductsUseCase()
                .onSuccess {
                    _state.update { it.copy(isLoading = false, error = null) }
                }
                .onFailure { error ->
                    _state.update { 
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }
    
    private fun refresh() {
        scope.launch {
            _state.update { it.copy(isRefreshing = true) }
            getProductsUseCase()
                .onSuccess {
                    _state.update { it.copy(isRefreshing = false) }
                }
                .onFailure {
                    _state.update { it.copy(isRefreshing = false) }
                }
        }
    }
    
    private fun handleProductClick(productId: String) {
        scope.launch {
            _events.emit(ProductListEvent.NavigateToDetails(productId))
        }
    }
}

// presentation/component/ProductListComponent.kt
class ProductListComponent(
    componentContext: ComponentContext,
    private val getProductsUseCase: GetProductsUseCase,
    private val observeProductsUseCase: ObserveProductsUseCase,
    private val onNavigateToDetails: (String) -> Unit
) : ComponentContext by componentContext {
    
    private val scope = coroutineScope()
    
    private val store = ProductListStore(
        getProductsUseCase = getProductsUseCase,
        observeProductsUseCase = observeProductsUseCase,
        scope = scope
    )
    
    val state: StateFlow<ProductListState> = store.state
    
    init {
        scope.launch {
            store.events.collect { event ->
                when (event) {
                    is ProductListEvent.NavigateToDetails ->
                        onNavigateToDetails(event.productId)
                    is ProductListEvent.ShowError -> {
                        // Handle error
                    }
                }
            }
        }
        
        // Load initial data
        store.accept(ProductListIntent.LoadProducts)
    }
    
    fun onIntent(intent: ProductListIntent) {
        store.accept(intent)
    }
}

// presentation/ui/ProductListScreen.kt
@Composable
fun ProductListScreen(
    component: ProductListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()
    
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing,
        onRefresh = { component.onIntent(ProductListIntent.Refresh) }
    )
    
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Products") })
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            when {
                state.isLoading && state.products.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.error != null && state.products.isEmpty() -> {
                    ErrorView(
                        error = state.error!!,
                        onRetry = {
                            component.onIntent(ProductListIntent.LoadProducts)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(150.dp),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = state.products,
                            key = { it.id }
                        ) { product ->
                            ProductCard(
                                product = product,
                                onClick = {
                                    component.onIntent(
                                        ProductListIntent.OnProductClick(product.id)
                                    )
                                }
                            )
                        }
                    }
                }
            }
            
            PullRefreshIndicator(
                refreshing = state.isRefreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
```

### Step 4: Provides Integration Notes

**Dependencies needed:**
```kotlin
// build.gradle.kts
dependencies {
    // Networking
    implementation("io.ktor:ktor-client-android:2.3.11")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.11")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.11")
    
    // Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // Image loading
    implementation("io.coil-kt:coil-compose:2.6.0")
}
```

**File locations:**
```
feature-products/
├── domain/
│   ├── model/Product.kt
│   ├── repository/ProductRepository.kt
│   └── usecase/
│       ├── GetProductsUseCase.kt
│       └── ObserveProductsUseCase.kt
├── data/
│   ├── dto/ProductDto.kt
│   ├── entity/ProductEntity.kt
│   ├── mapper/ProductMapper.kt
│   ├── source/
│   │   ├── remote/ProductRemoteDataSource.kt
│   │   └── local/ProductDao.kt
│   └── repository/ProductRepositoryImpl.kt
└── presentation/
    ├── component/ProductListComponent.kt
    ├── store/ProductListStore.kt
    ├── state/
    │   ├── ProductListState.kt
    │   ├── ProductListIntent.kt
    │   └── ProductListEvent.kt
    └── ui/ProductListScreen.kt
```

**Next steps:**
1. Add DI configuration (Koin modules)
2. Create Database class
3. Wire up navigation in RootComponent
4. Add tests

---

## Example 2: Refactoring Legacy Code

**User Request:**
```
I have this old ViewModel code using LiveData and imperative style.
Refactor it to use MVI pattern with StateFlow.

[pastes old code]
```

**Agent Response Process:**

### Step 1: Auto-loads Skills
```
- refactoring.skill.md
- mvi.skill.md
```

### Step 2: Risk Assessment
```
Risk Level: 🟡 Medium (modifying existing code)

Warnings:
⚠️ This will change the state management pattern
⚠️ UI code will need updates to collect StateFlow
⚠️ Ensure all tests pass after refactoring

Recommendation: Create git commit before proceeding
```

### Step 3: Analyzes Existing Code
```
Identified:
- 3 LiveData properties
- 5 imperative update methods
- No clear state model
- Mixed UI logic with business logic
```

### Step 4: Provides Refactored Code
```
[Complete MVI implementation with State, Intent, Event, Store]
```

### Step 5: Migration Guide
```
**How to update UI code:**

Before:
viewModel.products.observe(this) { products ->
    // update UI
}

After:
lifecycleScope.launch {
    component.state.collect { state ->
        // update UI with state.products
    }
}
```

---

## Example 3: Debugging Issue

**User Request:**
```
My app crashes with:
java.lang.IllegalStateException: StateFlow.value is not set
```

**Agent Process:**

### Step 1: Asks for Context
```
Could you share:
1. The code where the crash occurs
2. Full stack trace
3. When does it crash (on launch, specific action, etc.)
```

### Step 2: Identifies Issue
```
The crash occurs because:
- StateFlow is created without initial value
- Accessing .value before setting it

Root cause: Missing initial state in Store constructor
```

### Step 3: Provides Fix
```kotlin
// Before (WRONG)
private val _state = MutableStateFlow<MyState>()  // No initial value!

// After (CORRECT)
private val _state = MutableStateFlow(MyState())  // Has initial value
```

### Step 4: Explains Prevention
```
**How to prevent:**
1. Always provide initial value to MutableStateFlow
2. Use data class with default values for state
3. Consider using `lateinit var` only if truly necessary
```

---

## Example 4: Adding Tests

**User Request:**
```
Write tests for my ProductListStore
```

**Agent Process:**

### Step 1: Auto-loads Skill
```
- testing.skill.md
```

### Step 2: Generates Tests
```kotlin
class ProductListStoreTest {
    private lateinit var store: ProductListStore
    private lateinit var mockGetProductsUseCase: GetProductsUseCase
    private lateinit var mockObserveProductsUseCase: ObserveProductsUseCase
    
    @Before
    fun setup() {
        mockGetProductsUseCase = mockk()
        mockObserveProductsUseCase = mockk()
        store = ProductListStore(
            getProductsUseCase = mockGetProductsUseCase,
            observeProductsUseCase = mockObserveProductsUseCase,
            scope = TestScope()
        )
    }
    
    @Test
    fun `when LoadProducts intent, should update state to loading`() = runTest {
        // Arrange
        val products = listOf(Product("1", "Test", 10.0, "", ""))
        coEvery { mockGetProductsUseCase() } returns Result.success(products)
        
        // Act
        store.accept(ProductListIntent.LoadProducts)
        
        // Assert
        assertTrue(store.state.value.isLoading)
    }
    
    @Test
    fun `when products loaded successfully, should update state`() = runTest {
        // Arrange
        val products = listOf(Product("1", "Test", 10.0, "", ""))
        coEvery { mockGetProductsUseCase() } returns Result.success(products)
        
        // Act
        store.accept(ProductListIntent.LoadProducts)
        advanceUntilIdle()
        
        // Assert
        assertEquals(products, store.state.value.products)
        assertFalse(store.state.value.isLoading)
    }
}
```

---

**These examples show the complete workflow from request to working code!** 🚀
