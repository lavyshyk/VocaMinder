---
name: decompose
description: Decompose navigation and component management for Compose Multiplatform. Triggers when user mentions navigation, routing, screens, back stack, deep links, or component lifecycle. Use for implementing type-safe navigation, managing navigation state, and creating lifecycle-aware components.
---

# Decompose Navigation Skill

Type-safe, lifecycle-aware navigation and component management.

## Version

**Decompose:** 3.0.0  
**Essenty:** 2.0.0

## Core Concepts

- **Component**: Business logic container with lifecycle
- **Configuration**: Type-safe screen representation
- **Child Stack**: Navigation stack management
- **Child Slot**: Tab/bottom navigation

## Basic Setup

### 1. Define Configurations

```kotlin
@Serializable
sealed interface RootConfig {
    @Serializable
    data object Home : RootConfig
    
    @Serializable
    data class ProductDetails(val productId: String) : RootConfig
    
    @Serializable
    data class Checkout(val cartId: String) : RootConfig
}
```

### 2. Create Root Component

```kotlin
class RootComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    private val navigation = StackNavigation<RootConfig>()
    
    val stack = childStack(
        source = navigation,
        serializer = RootConfig.serializer(),
        initialConfiguration = RootConfig.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )
    
    private fun createChild(
        config: RootConfig,
        componentContext: ComponentContext
    ): Child {
        return when (config) {
            RootConfig.Home -> Child.Home(
                HomeComponent(
                    componentContext = componentContext,
                    onNavigateToProduct = { id ->
                        navigation.push(RootConfig.ProductDetails(id))
                    }
                )
            )
            
            is RootConfig.ProductDetails -> Child.Details(
                ProductDetailsComponent(
                    componentContext = componentContext,
                    productId = config.productId,
                    onNavigateBack = navigation::pop
                )
            )
            
            is RootConfig.Checkout -> Child.Checkout(
                CheckoutComponent(
                    componentContext = componentContext,
                    cartId = config.cartId,
                    onNavigateBack = navigation::pop
                )
            )
        }
    }
    
    sealed class Child {
        data class Home(val component: HomeComponent) : Child()
        data class Details(val component: ProductDetailsComponent) : Child()
        data class Checkout(val component: CheckoutComponent) : Child()
    }
}
```

### 3. Feature Component

```kotlin
class ProductListComponent(
    componentContext: ComponentContext,
    private val getProductsUseCase: GetProductsUseCase,
    private val onNavigateToDetails: (String) -> Unit,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    private val scope = coroutineScope()
    
    private val store = ProductListStore(
        getProductsUseCase = getProductsUseCase,
        scope = scope
    )
    
    val state: StateFlow<ProductListState> = store.state
    
    fun onIntent(intent: ProductListIntent) {
        when (intent) {
            is ProductListIntent.OnProductClick ->
                onNavigateToDetails(intent.productId)
            ProductListIntent.OnBackClick ->
                onNavigateBack()
            else -> store.accept(intent)
        }
    }
}
```

## Navigation Operations

### Stack Operations

```kotlin
// Push new screen
navigation.push(RootConfig.ProductDetails("123"))

// Pop current screen
navigation.pop()

// Pop to index
navigation.popTo(0) // Back to root

// Replace current
navigation.replaceCurrent(RootConfig.Home)

// Replace entire stack
navigation.replaceAll(RootConfig.Home)

// Pop and push
navigation.popTo(0)
navigation.push(RootConfig.ProductDetails("123"))
```

## Bottom Navigation (Tabs)

```kotlin
@Serializable
sealed interface TabConfig {
    @Serializable data object Home : TabConfig
    @Serializable data object Search : TabConfig
    @Serializable data object Profile : TabConfig
}

class MainComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    private val navigation = SlotNavigation<TabConfig>()
    
    private val _selectedTab = MutableStateFlow<TabConfig>(TabConfig.Home)
    val selectedTab: StateFlow<TabConfig> = _selectedTab.asStateFlow()
    
    val slot = childSlot(
        source = navigation,
        serializer = TabConfig.serializer(),
        initialConfiguration = { TabConfig.Home },
        handleBackButton = false,
        childFactory = ::createChild
    )
    
    fun selectTab(config: TabConfig) {
        _selectedTab.value = config
        navigation.activate(config)
    }
    
    private fun createChild(
        config: TabConfig,
        componentContext: ComponentContext
    ): Child {
        return when (config) {
            TabConfig.Home -> Child.Home(
                HomeComponent(componentContext)
            )
            TabConfig.Search -> Child.Search(
                SearchComponent(componentContext)
            )
            TabConfig.Profile -> Child.Profile(
                ProfileComponent(componentContext)
            )
        }
    }
    
    sealed class Child {
        data class Home(val component: HomeComponent) : Child()
        data class Search(val component: SearchComponent) : Child()
        data class Profile(val component: ProfileComponent) : Child()
    }
}
```

## Back Handling

### Automatic

```kotlin
val stack = childStack(
    source = navigation,
    // ...
    handleBackButton = true  // Automatic back handling
)
```

### Custom

```kotlin
class DetailsComponent(
    componentContext: ComponentContext,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    private var hasUnsavedChanges = false
    
    init {
        backHandler.register(
            BackCallback(
                isEnabled = hasUnsavedChanges,
                onBack = ::handleBack
            )
        )
    }
    
    private fun handleBack() {
        if (hasUnsavedChanges) {
            // Show dialog
            showUnsavedChangesDialog()
        } else {
            onNavigateBack()
        }
    }
}
```

## Nested Navigation

```kotlin
class HomeComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    private val navigation = StackNavigation<HomeConfig>()
    
    val stack = childStack(
        source = navigation,
        serializer = HomeConfig.serializer(),
        initialConfiguration = HomeConfig.ProductList,
        handleBackButton = true,
        childFactory = ::createChild
    )
    
    @Serializable
    sealed interface HomeConfig {
        @Serializable data object ProductList : HomeConfig
        @Serializable data class ProductDetails(val id: String) : HomeConfig
    }
}
```

## Deep Links

```kotlin
class RootComponent(
    componentContext: ComponentContext,
    deepLink: DeepLink? = null
) : ComponentContext by componentContext {
    
    private val navigation = StackNavigation<RootConfig>()
    
    init {
        deepLink?.let(::handleDeepLink)
    }
    
    private fun handleDeepLink(link: DeepLink) {
        when {
            link.path.startsWith("/product/") -> {
                val productId = link.path.substringAfter("/product/")
                navigation.push(RootConfig.ProductDetails(productId))
            }
            link.path == "/profile" -> {
                navigation.push(RootConfig.Profile)
            }
        }
    }
}
```

## Compose UI Integration

```kotlin
@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier
) {
    val stack by component.stack.subscribeAsState()
    
    Children(
        stack = stack,
        modifier = modifier,
        animation = stackAnimation(fade() + slide())
    ) { child ->
        when (val instance = child.instance) {
            is RootComponent.Child.Home ->
                HomeScreen(instance.component)
            
            is RootComponent.Child.Details ->
                ProductDetailsScreen(instance.component)
            
            is RootComponent.Child.Checkout ->
                CheckoutScreen(instance.component)
        }
    }
}
```

## Passing Data

### Via Configuration (Recommended)

```kotlin
@Serializable
data class ProductDetails(
    val productId: String,
    val source: String = "home"  // Optional analytics
) : RootConfig
```

### Via Callbacks

```kotlin
class ProductListComponent(
    componentContext: ComponentContext,
    private val onProductSelected: (Product) -> Unit
) : ComponentContext by componentContext
```

### Returning Results

```kotlin
class EditProductComponent(
    componentContext: ComponentContext,
    private val productId: String,
    private val onProductUpdated: (Product) -> Unit,
    private val onNavigateBack: () -> Unit
) : ComponentContext by componentContext {
    
    fun saveProduct() {
        scope.launch {
            val updated = repository.updateProduct(product)
            onProductUpdated(updated)
            onNavigateBack()
        }
    }
}
```

## State Preservation

```kotlin
class ProductListComponent(
    componentContext: ComponentContext
) : ComponentContext by componentContext {
    
    // Automatically saved/restored
    private val savedState = stateKeeper.consume(
        key = "product_list_state",
        strategy = ProductListState.serializer()
    ) ?: ProductListState()
    
    init {
        stateKeeper.register(
            key = "product_list_state",
            strategy = ProductListState.serializer()
        ) { state.value }
    }
}
```

## Testing

```kotlin
class RootComponentTest {
    
    @Test
    fun `initial state should be Home`() {
        val component = RootComponent(FakeComponentContext())
        
        val child = component.stack.value.active.instance
        assertTrue(child is RootComponent.Child.Home)
    }
    
    @Test
    fun `navigating to product should push ProductDetails`() {
        val component = RootComponent(FakeComponentContext())
        
        component.navigateToProduct("123")
        
        val child = component.stack.value.active.instance
        assertTrue(child is RootComponent.Child.Details)
        assertEquals("123", (child as RootComponent.Child.Details).component.productId)
    }
}
```

## Best Practices

✅ **Do:**
- Use `@Serializable` for configurations
- Pass minimal data in configs (IDs, not full objects)
- Use callbacks for navigation
- Handle back button explicitly
- Create helper navigation methods
- Use type-safe routing

❌ **Don't:**
- Pass large objects in configurations
- Navigate directly from UI
- Use string-based routes
- Ignore back button handling
- Create circular dependencies

---

**Remember:** Decompose provides lifecycle-aware, type-safe navigation!
