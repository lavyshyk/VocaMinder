---
name: compose
description: Jetpack Compose and Compose Multiplatform UI development. Triggers when user mentions Compose, composables, Material 3, UI components, screens, layouts, theming, or any Compose-specific features. Use for building user interfaces, custom components, animations, gestures, and Material Design implementation.
---

# Jetpack Compose Skill

Expert guidance for building modern Android UIs with Jetpack Compose and Compose Multiplatform.

## Version Info

**Compose:** 1.6.10  
**Material 3:** 1.2.1  
**Compose Compiler:** 1.5.14  

## Core Composable Patterns

### Basic Composable

```kotlin
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
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

### State Hoisting Pattern

```kotlin
// Stateless composable (receives state and events)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search...") }
    )
}

// Stateful parent
@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }
    
    SearchBar(
        query = query,
        onQueryChange = { query = it }
    )
}
```

## Material 3 Components

### Scaffold & App Bars

```kotlin
@Composable
fun FeatureScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Feature") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* action */ }) {
                        Icon(Icons.Default.MoreVert, "More")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* add */ }) {
                Icon(Icons.Default.Add, "Add")
            }
        }
    ) { paddingValues ->
        Content(modifier = Modifier.padding(paddingValues))
    }
}
```

### Lists (LazyColumn / LazyRow)

```kotlin
@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = products,
            key = { it.id } // Important for performance!
        ) { product ->
            ProductCard(
                product = product,
                onClick = { onProductClick(product.id) },
                modifier = Modifier.animateItemPlacement()
            )
        }
    }
}
```

### Grids

```kotlin
@Composable
fun ProductGrid(
    products: List<Product>,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products, key = { it.id }) { product ->
            ProductCard(product, onClick = { })
        }
    }
}
```

## State Management

### remember & mutableStateOf

```kotlin
@Composable
fun Counter() {
    // Survives recomposition, lost on config change
    var count by remember { mutableStateOf(0) }
    
    Button(onClick = { count++ }) {
        Text("Count: $count")
    }
}
```

### rememberSaveable (survives config changes)

```kotlin
@Composable
fun SearchInput() {
    var query by rememberSaveable { mutableStateOf("") }
    
    TextField(
        value = query,
        onValueChange = { query = it }
    )
}
```

### derivedStateOf (computed state)

```kotlin
@Composable
fun FilteredList(items: List<Item>, query: String) {
    // Only recomputes when items or query changes
    val filteredItems by remember(items, query) {
        derivedStateOf {
            if (query.isEmpty()) items
            else items.filter { it.name.contains(query, ignoreCase = true) }
        }
    }
    
    ItemList(filteredItems)
}
```

### StateFlow Integration

```kotlin
@Composable
fun FeatureScreen(component: FeatureComponent) {
    val state by component.state.collectAsState()
    
    when {
        state.isLoading -> LoadingView()
        state.error != null -> ErrorView(state.error!!)
        else -> ContentView(state.data)
    }
}
```

## Side Effects

### LaunchedEffect (coroutine scope)

```kotlin
@Composable
fun ProfileScreen(userId: String) {
    var profile by remember { mutableStateOf<Profile?>(null) }
    
    LaunchedEffect(userId) {
        // Cancels and restarts when userId changes
        profile = loadProfile(userId)
    }
}
```

### DisposableEffect (cleanup)

```kotlin
@Composable
fun EventListener(eventManager: EventManager) {
    DisposableEffect(eventManager) {
        val listener = eventManager.addListener { /* handle */ }
        
        onDispose {
            eventManager.removeListener(listener)
        }
    }
}
```

### SideEffect (non-suspend operations)

```kotlin
@Composable
fun AnalyticsScreen(screenName: String) {
    SideEffect {
        analytics.logScreenView(screenName)
    }
}
```

## Performance Optimization

### Use Stable Keys

```kotlin
// Good
LazyColumn {
    items(products, key = { it.id }) { product ->
        ProductCard(product)
    }
}

// Bad - will cause unnecessary recompositions
LazyColumn {
    items(products) { product ->
        ProductCard(product)
    }
}
```

### Avoid Creating New Lambdas

```kotlin
// Bad - creates new lambda on each recomposition
@Composable
fun ItemList(items: List<Item>, onClick: (String) -> Unit) {
    items.forEach { item ->
        ItemCard(
            item = item,
            onClick = { onClick(item.id) } // New lambda!
        )
    }
}

// Good - stable reference
@Composable
fun ItemList(items: List<Item>, onClick: (String) -> Unit) {
    items.forEach { item ->
        val onItemClick = remember(item.id) {
            { onClick(item.id) }
        }
        ItemCard(item = item, onClick = onItemClick)
    }
}
```

### Mark Stable Data

```kotlin
@Immutable
data class Product(
    val id: String,
    val name: String,
    val price: Double
)

@Stable
class ProductViewModel {
    var products by mutableStateOf<List<Product>>(emptyList())
}
```

## Theming

### Material 3 Theme

```kotlin
// Theme.kt
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    background = Color.White,
    surface = Color(0xFFF5F5F5),
    error = Color(0xFFB00020)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    // ...
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
```

### Typography

```kotlin
val Typography = Typography(
    displayLarge = TextStyle(
        fontSize = 57.sp,
        lineHeight = 64.sp,
        fontWeight = FontWeight.Normal
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.SemiBold
    ),
    bodyLarge = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
)
```

## Animations

### Simple Animations

```kotlin
@Composable
fun AnimatedButton() {
    var expanded by remember { mutableStateOf(false) }
    
    val size by animateDpAsState(
        targetValue = if (expanded) 200.dp else 100.dp,
        label = "size"
    )
    
    Button(
        onClick = { expanded = !expanded },
        modifier = Modifier.size(size)
    ) {
        Text("Animate")
    }
}
```

### Visibility Animations

```kotlin
@Composable
fun AnimatedContent() {
    var visible by remember { mutableStateOf(false) }
    
    Column {
        Button(onClick = { visible = !visible }) {
            Text("Toggle")
        }
        
        AnimatedVisibility(visible) {
            Card {
                Text("I'm animated!")
            }
        }
    }
}
```

## Testing

### Compose Test

```kotlin
class ProductScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun productList_displaysProducts() {
        val products = listOf(
            Product("1", "Product 1", 10.0),
            Product("2", "Product 2", 20.0)
        )
        
        composeTestRule.setContent {
            ProductList(products, onProductClick = {})
        }
        
        composeTestRule
            .onNodeWithText("Product 1")
            .assertIsDisplayed()
        
        composeTestRule
            .onNodeWithText("Product 2")
            .assertIsDisplayed()
    }
    
    @Test
    fun productCard_clickTriggersCallback() {
        var clicked = false
        
        composeTestRule.setContent {
            ProductCard(
                product = Product("1", "Test", 10.0),
                onClick = { clicked = true }
            )
        }
        
        composeTestRule
            .onNodeWithText("Test")
            .performClick()
        
        assertTrue(clicked)
    }
}
```

## Common Patterns

### Loading States

```kotlin
@Composable
fun LoadingContent(
    isLoading: Boolean,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        content()
        
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
```

### Error Handling

```kotlin
@Composable
fun ErrorView(
    error: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.Error, null)
            Text("Error", style = MaterialTheme.typography.titleMedium)
            Text(error)
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}
```

### Empty States

```kotlin
@Composable
fun EmptyView(
    message: String,
    action: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Info,
            null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        action?.let {
            Spacer(Modifier.height(16.dp))
            it()
        }
    }
}
```

## Best Practices

✅ **Do:**
- Hoist state to the caller
- Use stable keys in lists
- Mark data classes with @Immutable
- Use remember for computed values
- Handle all states (loading, error, empty, success)
- Use Material 3 components
- Test composables

❌ **Don't:**
- Put business logic in composables
- Create new lambdas in list items
- Forget to handle loading/error states
- Skip keys in LazyColumn/LazyRow
- Use GlobalScope in composables
- Ignore accessibility

## Modifier Order

```kotlin
// Correct order
Modifier
    .fillMaxWidth()        // Size
    .padding(16.dp)        // Padding
    .border(1.dp, Color.Gray)  // Border
    .background(Color.White)   // Background
    .clickable { }         // Interaction
    .semantics { }         // Accessibility
```

---

**Remember:** Compose recomposes frequently. Write efficient, recomposition-friendly code!
